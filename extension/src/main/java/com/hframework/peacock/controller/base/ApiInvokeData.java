package com.hframework.peacock.controller.base;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.hframework.common.frame.ServiceFactory;
import com.hframework.common.util.RegexUtils;
import com.hframework.common.util.message.JsonUtils;
import com.hframework.peacock.controller.base.descriptor.HandlerDescriptor;
import com.hframework.peacock.controller.base.descriptor.ResultDescriptor;
import com.hframework.peacock.controller.base.descriptor.ResultTreeDescriptor;
import com.hframework.smartweb.LogHelper;
import com.hframework.smartweb.SmartHandlerFactory;
import com.hframework.smartweb.SmartThreadPoolExecutorFactory;
import com.hframework.smartweb.ThreadLocalCenter;
import com.hframework.smartweb.bean.SmartHandler;
import com.hframework.smartweb.bean.apiconf.Precheck;
import com.hframework.smartweb.bean.handler.HandlerHelper;
import com.hframework.smartweb.exception.SmartHandlerException;
import com.hframework.strategy.rule.ExpressInvoker;
import com.hframework.strategy.rule.data.EDataSet;
import com.hframework.tracer.PeacockSpan;
import com.hframework.tracer.TracerFactory;
import com.hframework.peacock.controller.base.dc.BreakDC;
import com.hframework.peacock.controller.base.dc.DC;
import com.hframework.peacock.controller.base.dc.DCUtils;
import com.hframework.peacock.controller.base.dc.NullDC;
import com.hframework.peacock.controller.base.descriptor.HandlersDescriptor;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.MethodParameter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

/**
 * Created by zhangquanhong on 2017/11/16.
 */
public class ApiInvokeData{

    private static final Logger logger = LoggerFactory.getLogger(ApiInvokeData.class);
    private DC requestDC = null;
    private HttpServletRequest request;
    private HttpServletResponse response;
    private ApiExecutor apiExecutor;

    private Map<String, Object> parameterKVPair = new HashMap<>();
    private HandlersData preHandlersData;
    private HandlersData handlersData;

    private ResultTreeDescriptor resultStruct;

    private PeacockSpan rootSpan;

    public ApiInvokeData(ApiExecutor apiExecutor, HttpServletRequest request, HttpServletResponse response, DC requestDC) {
        this.requestDC = requestDC;
        this.request = request;
        this.response = response;
        this.apiExecutor = apiExecutor;
        resultStruct = apiExecutor.getResultStruct();
    }

    public static ApiInvokeData getInstance(ApiExecutor apiExecutor, HttpServletRequest request, HttpServletResponse response) {
        return getInstance(apiExecutor, request, response, NullDC.SINGLETON);
    }

    public static ApiInvokeData getInstance(ApiExecutor apiExecutor, HttpServletRequest request, HttpServletResponse response, DC requestDC) {
        return new ApiInvokeData(apiExecutor, request, response, requestDC);
    }

    public ApiInvokeData setRootSpan(PeacockSpan rootSpan) {
        this.rootSpan = rootSpan;
        return this;
    }

    public ApiInvokeData parseParameter(boolean isApi) throws IllegalAccessException, InstantiationException, NoSuchMethodException, InvocationTargetException {
        parameterKVPair = apiExecutor.parseParameter(request, requestDC);
        if(logger.isInfoEnabled()) {
            StringBuffer sb = new StringBuffer();
            for (String name : parameterKVPair.keySet()) {
                Object value = parameterKVPair.get(name);
                if(value.getClass().isArray()) {
                    try {
                        value = JsonUtils.writeValueAsString(value);
                    } catch (IOException e) {}
                }
                sb.append(name).append(" = ").append(value).append("; ");
            }

            logger.info("[{}]REQUEST-PARAMETERS => {}", this.apiExecutor.getApiPath(), sb.toString());
        }
        TracerFactory.addTag(rootSpan, TracerFactory.TagKey.REQUEST, parameterKVPair, isApi ? rootSpan.hasApiRequestTag() : rootSpan.hasHandlerRequestTag());
        TracerFactory.addSpanAnnotation(rootSpan, TracerFactory.Annotation.PARAMETER_PARSE);
        return this;
    }

    public ApiInvokeData preHandle() throws ExecutionException, InterruptedException, ClassNotFoundException {
        HandlersDescriptor preHandlers = apiExecutor.getPreHandlers();
        if(preHandlers == null || preHandlers.getHandlers() == null || preHandlers.getHandlers().isEmpty()) return this;
        preHandlersData = new HandlersData(this, preHandlers);
        preHandlersData.handle();
        TracerFactory.addSpanAnnotation(rootSpan, TracerFactory.Annotation.PRE_HANDLER_INVOKE);
        return this;
    }

    public ApiInvokeData preCheck() {
        TracerFactory.addSpanAnnotation(rootSpan, TracerFactory.Annotation.PRE_CHECK);
        return this;
    }

    public ApiInvokeData handle() throws ExecutionException, InterruptedException, ClassNotFoundException {
        HandlersDescriptor handlers = apiExecutor.getHandlers();
        handlersData = new HandlersData(this, handlers, preHandlersData);
        handlersData.handle();
        TracerFactory.addSpanAnnotation(rootSpan, TracerFactory.Annotation.HANDLER_INVOKE);
        return this;
    }

    public String snapshot(){
        StringBuffer sb = new StringBuffer();
        Map<HandlerDescriptor, HandleSwapData> resultData = handlersData.getResultData();
        for (HandlerDescriptor handlerDescriptor : resultData.keySet()) {
            HandleSwapData handleSwapData = resultData.get(handlerDescriptor);
            sb.append(handlerDescriptor.getHandlerMark()).append(" => ").append(handleSwapData.getResponseData()).append("\n");
        }
        logger.debug("snapshot : " + sb.toString());
        return sb.toString();
    }

    public DC resultMergeAndFormat() throws Exception {
        ThreadLocalCenter.programIdTL.set(apiExecutor.getApiProgram());
        Map<HandlerDescriptor, HandleSwapData> resultData = handlersData.getActivateResultData();

        for (HandlerDescriptor handlerDescriptor : resultData.keySet()) {
            HandleSwapData handleSwapData = resultData.get(handlerDescriptor);
            handlerDescriptor.formatResult(handleSwapData.getResponseData());
            handlerDescriptor.removeUnusedField(handleSwapData.getResponseData());
        }

        this.snapshot();

        DC packagedDC = packageResult(resultData);

//        List<Map<String, Object>> handleResult = new ArrayList<>();
//        //TODO
//        mergeResult(handleResult, resultData);

        ResultDescriptor resultDescriptor = apiExecutor.getResultDescriptor();
        resultDescriptor.formatResult(packagedDC);

        //如果RequestDC是单个请求，但是返回结果为GroupDC，表明中间存在一个ListDC与一到多个GroupDC合并为GroupDC引起，在最后我们需要还原为ListDC
        if(DCUtils.isGroupDC(packagedDC) && !DCUtils.isMultiDC(requestDC)) {
            packagedDC = DCUtils.groupDC2ListDC(packagedDC);
        }
        TracerFactory.addTag(rootSpan, TracerFactory.TagKey.RESPONSE, packagedDC, rootSpan.hasHandlerResponseTag());
        TracerFactory.addSpanAnnotation(rootSpan, TracerFactory.Annotation.RESULT_PACKAGE);
        logger.info("[{}]RESPONSE-DC => {}", this.apiExecutor.getApiPath(), packagedDC);
        //TODO
        return packagedDC;
    }

    public Object result() throws Exception {
        ThreadLocalCenter.programIdTL.set(apiExecutor.getApiProgram());
        Map<HandlerDescriptor, HandleSwapData> resultData = handlersData.getActivateResultData();
        for (HandlerDescriptor handlerDescriptor : resultData.keySet()) {
            HandleSwapData handleSwapData = resultData.get(handlerDescriptor);
            handlerDescriptor.formatResult(handleSwapData.getResponseData());
        }

        this.snapshot();
        ResultDescriptor resultDescriptor = apiExecutor.getResultDescriptor();
        DC packageResult = packageResult(resultData);
        resultDescriptor.formatResult(packageResult);
        logger.info("[{}]RESPONSE-DC => {}", this.apiExecutor.getApiPath(), packageResult);
        TracerFactory.addSpanAnnotation(rootSpan, TracerFactory.Annotation.RESULT_PACKAGE);
        return DCUtils.getValues(packageResult);

//        List<Map<String, Object>> handleResult = new ArrayList<>();
//        //TODO
//        boolean isManyResult = mergeResult(handleResult, resultData);
//
//
//        resultDescriptor.formatResult(handleResult);
//        //TODO
//        return returnResult(handleResult, isManyResult);
    }

    private Object returnResult(List<Map<String, Object>> handleResult, boolean isManyResult) {
        if(isManyResult) {
            return handleResult;
        }else {
            return handleResult.get(0);
        }

    }

    private DC packageResult(Map<HandlerDescriptor, HandleSwapData> handleResult) {
        return resultStruct.packaging(handleResult);
    }

//    private boolean mergeResult(List<Map<String, Object>> result, Map<HandlerDescriptor, HandleSwapData> handleResult) {
//        return resultStruct.packaging(result, handleResult);
//    }

    public String getJson() {
        return null;
    }

    public static class HandlersData{
        private HandlersData relHandlersData;

        private ExecutorService executorService = SmartThreadPoolExecutorFactory.getExecutorServiceObject();
        private Set<HandlerDescriptor> undoHandlers;
        private Set<HandlerDescriptor> waitingHandlers;

        private ApiInvokeData apiData;

//        private HandlersDescriptor handlersDescriptor;

        private Map<HandlerDescriptor, HandleSwapData> resultData = new HashMap<>();

        Map<Future<DC>, HandlerDescriptor> executingHandler = new HashMap<>();

        public HandlersData(ApiInvokeData apiData, HandlersDescriptor handlersDescriptor){
            this(apiData, handlersDescriptor, null);
        }

        public HandlersData(ApiInvokeData apiData, HandlersDescriptor handlersDescriptor, HandlersData relHandlersData){
            this.apiData = apiData;
//            this.handlersDescriptor = handlersDescriptor;
            undoHandlers = Sets.newHashSet(handlersDescriptor.getHandlers());
            if(handlersDescriptor.getMissHandlers() == null) {
                waitingHandlers = Sets.newHashSet();
            }else {
                waitingHandlers = Sets.newHashSet(handlersDescriptor.getMissHandlers());
            }
            for (HandlerDescriptor handlerDescriptor : undoHandlers) {
                HandleSwapData handleData = HandleSwapData.getInstance();
                handleData.setTmpMissParameters(Sets.newHashSet(handlerDescriptor.getMissParameters()));
                handleData.setReturnType(handlerDescriptor.getReturnType());
                handleData.setHandlerDescriptor(handlerDescriptor);
                resultData.put(handlerDescriptor, handleData);
            }

            setRelHandlersData(relHandlersData);
        }


        public void handle() throws ExecutionException, InterruptedException, ClassNotFoundException {
            handleInternal();
            if(relHandlersData != null) {
                for (HandlerDescriptor handlerDescriptor : relHandlersData.getResultData().keySet()) {
                    resultData.remove(handlerDescriptor);
                }
            }
        }

        public void handleInternal() throws ExecutionException, InterruptedException, ClassNotFoundException {

            int firstExecute = tryExecuteHandlers();
            if(firstExecute == 0) {
                logger.error("any handler can execute, {}", undoHandlers);
                throw new SmartHandlerException("handlers都不能进行第一次执行！");
            }
            while (true) {//循环删除该队列中数据，直到没有数据为止，是否更应该用queue?
                Iterator<Map.Entry<Future<DC>, HandlerDescriptor>> iterator = executingHandler.entrySet().iterator();
                if (iterator.hasNext()) {
                    Map.Entry<Future<DC>, HandlerDescriptor> entry = iterator.next();
                    HandlerDescriptor resultHandler = entry.getValue();
                    Future<DC> future = entry.getKey();

                    //合并结果数据
                    HandleSwapData handleSwapData = resultData.get(resultHandler);
                    handleSwapData.setResponseData(future.get());

                    handleSwapData.setExecuteStageFinish();

                    //如果是breakDC，有可能是多个case中的一个分支，因此会由别的分支进行执行，这里个不处理，
                    // TODO 或者判断是否存在多个分支，如果多个分支最后一个breakDC或者DataDc存在就trigger
                    if(!(handleSwapData.getResponseData() instanceof BreakDC)) {
                        triggerWaitingHandlers(resultHandler);
                    }



                    tryExecuteHandlers();
                    synchronized (iterator) {
                        executingHandler.remove(future);
//                        iterator.remove();//Concurrent Exception Exists !
                    }

                } else {
                    break;
                }
            }
        }

        private void triggerWaitingHandlers(HandlerDescriptor resultHandler) {
            if(resultHandler.getTriggerTo() == null) return;
            Iterator<HandlerDescriptor> retryHandlers = resultHandler.getTriggerTo().iterator();
            while (retryHandlers.hasNext()) {
                HandlerDescriptor retryHandler = retryHandlers.next();
                //当prehandler中的handler触发handlers中的handler，这里undoHandlers.contains(retryHandler) == false
                if (!undoHandlers.contains(retryHandler) || resultData.get(retryHandler).isExecuted()) {
                    continue;
                }
                for (Map.Entry<String, Set<HandlerDescriptor>> dependToInfo : retryHandler.getDependTo().entrySet()) {
                    if (dependToInfo.getValue().contains(resultHandler)) {
                        resultData.get(retryHandler).removeTmpMissParameter(dependToInfo.getKey());
                    }
                }
                if (resultData.get(retryHandler).canExecutable()) {
                    waitingHandlers.remove(retryHandler);
                }
            }
        }

        public int tryExecuteHandlers() throws ClassNotFoundException {
            int executeCount = 0;
            Iterator<HandlerDescriptor> undoIterator = undoHandlers.iterator();
            while (undoIterator.hasNext()) {
                HandlerDescriptor handlerDescriptor = undoIterator.next();
                if (!waitingHandlers.contains(handlerDescriptor) && resultData.get(handlerDescriptor).isUnExecute() && resultData.get(handlerDescriptor).canExecutable()) {
                    HandleSwapData handleSwapData = resultData.get(handlerDescriptor);
                    handleSwapData.setExecuteStageExecuting(apiData.rootSpan);
//                    try (CurrentTraceContext.Scope scope = TracerFactory.maybeScope(handleSwapData.span)){
                        Future<DC> future = handle(handlerDescriptor, handleSwapData);
                        if(future != null) {
                            executingHandler.put(future, handlerDescriptor);
                            executeCount ++ ;
                            undoIterator.remove();
                        }
//                    }
                }
            }
            return executeCount;
        }

        private Future<DC> handle(HandlerDescriptor handlerDescriptor, HandleSwapData handleData) {
            Map<String, Class> allParameters = handlerDescriptor.getAllParameters();
            Map<String, String> inputParameters = handlerDescriptor.getInputParameters();
            Map<String, Object> defaultValues = handlerDescriptor.getDefaultValues();
            Set<String> requires = handlerDescriptor.getRequires();
            Map<String, String> dependAttr = handlerDescriptor.getDependParameters();
            Map<String, Set<HandlerDescriptor>> dependTo = handlerDescriptor.getDependTo();

            Set<String> precheckVars = handlerDescriptor.getPrecheckVars();
            if(precheckVars != null) {
                allParameters = Maps.newHashMap(allParameters);
                for (String precheckVar : precheckVars) {
                    if(!allParameters.containsKey(precheckVar)) {
                        allParameters.put(precheckVar, null);
                    }
                }
            }
            try {
                Map<String, List<Object>> parameterValues = getParameterValues(handlerDescriptor,
                        allParameters, inputParameters, defaultValues, requires, dependAttr, dependTo);
                handleData.setRequestData(parameterValues);
                Future<DC> future = handlerInvoke(handlerDescriptor, handleData);
                return future;
            }catch (BreakException e) {
                handleData.setRequestData(new HashMap<String, List<Object>>());
                handleData.setResponseData(NullDC.SINGLETON);
                return executorService.submit(new Callable<DC>() {
                    @Override
                    public DC call() throws Exception {
                        return NullDC.SINGLETON;
                    }
                });
            }
        }

        private Future<DC> handlerInvoke(final HandlerDescriptor handlerDescriptor, HandleSwapData handleData){
            final Thread mainThread = Thread.currentThread();
            SmartHandlerFactory.HandlerInfo single = handlerDescriptor.getSingle();
            SmartHandlerFactory.HandlerInfo batch = handlerDescriptor.getBatch();

            Map<String, Precheck> precheckMap = handlerDescriptor.getPrecheckMap();
            if(precheckMap != null) {
                boolean pass =true;
                for (String express : precheckMap.keySet()) {
                    ExpressInvoker expressInvoker = handlerDescriptor.getRules().get(express);
                    EDataSet result = expressInvoker.invoke(new EDataSet(handleData.requestData.getRuleData()));
//                    logger.info("[path={}, version={}, key={}, config={} ] parameters = {}, label = {} ",
//                            path, version, key, config, parameters, result.getLabel()[0]);
                    pass &= Boolean.parseBoolean(result.getLabel()[0]);
                    TracerFactory.addTag(handleData.span, TracerFactory.TagKey.PRE_CHECK, express + "=>" + express);
                    if(!pass) {
                        return executorService.submit(new Callable<DC>() {
                            @Override
                            public DC call() throws Exception {
                                return BreakDC.SINGLETON;
                            }
                        });
                    }
                }
            }
            final PeacockSpan span = handleData.span;
            Future<DC> future = null;
            if(single != null && single.getMethod() == null) {//如果是配置的Handler
                final DC requestDC = handleData.requestData;
                final String path = handlerDescriptor.getHandler().getPath();
                final String program = handlerDescriptor.getProgram();
                final String version = handlerDescriptor.getHandler().getVersion();
                //在主线程中完成序列化工作，否则子线程需要加锁处理，因为在主线程合并结果时，子线程iterate objectMapList，则会出现java.util.ConcurrentModificationException
                final String objectMapListSerialize = requestDC.toString();
                future = executorService.submit(TracerFactory.wrap(new Callable<DC>() {
                    @Override
                    public DC call() throws Exception {
                        try {
                            LogHelper.parallelStart(LogHelper.Type.Statistics, mainThread, path, objectMapListSerialize);
                            return ApiManager.invokeHandler(program, path, version, requestDC, null, null, span);
                        }finally {
                            LogHelper.parallelEnd(LogHelper.Type.Statistics, path);
                        }
                    }
                }));
            }else {
                final Method method = single != null ? single.getMethod() : null;
                final Method batchMethod = batch != null ? batch.getMethod() : null;
                final Class<? extends SmartHandler> handler = (Class<? extends SmartHandler>) method.getDeclaringClass();
                final SmartHandler smartHandler = ServiceFactory.getService(handler);
                final String[] from = handlerDescriptor.getAllParameters().keySet().toArray(new String[0]);
                final String[] to = handlerDescriptor.getResults().toArray(new String[0]);
                final DC requestMapList = handleData.requestData;
                //在主线程中完成序列化工作，否则子线程需要加锁处理，因为在主线程合并结果时，子线程iterate objectMapList，则会出现java.util.ConcurrentModificationException
                final String objectMapListSerialize = requestMapList.toString();
                future = executorService.submit(TracerFactory.wrap(new Callable<DC>() {
                    @Override
                    public DC call() throws Exception {
                        try {//TODO
                            LogHelper.parallelStart(LogHelper.Type.Statistics, mainThread, smartHandler.getClass(), objectMapListSerialize);
                            DC dc = HandlerHelper.batchHandle(executorService, smartHandler,
                                    method, batchMethod, from, to, null, requestMapList, Lists.newArrayList(apiData.parameterKVPair.keySet()).toArray(new String[0]), span);
                            TracerFactory.addTag(span, TracerFactory.TagKey.RESPONSE, dc, span.hasIOResponseTag());//如果是调用的本地类执行，那么需要手动添加response与result_package
                            TracerFactory.addSpanAnnotation(span, TracerFactory.Annotation.RESULT_PACKAGE);

                            return dc;
                        }finally {
                            LogHelper.parallelEnd(LogHelper.Type.Statistics, smartHandler.getClass());
                        }
                    }
                }));
            }

            return future;
        }

        class BreakException extends RuntimeException{
            public BreakException(String message) {
                super(message);
            }

            @Override
            public String getMessage() {
                return super.getMessage();
            }
        };

        private Map<String, List<Object>> getParameterValues(final HandlerDescriptor handlerDescriptor, Map<String, Class> allParameters, Map<String, String> inputParameters,
                                                             Map<String, Object> defaultValues, Set<String> requires, Map<String, String> dependAttr,
                                                             Map<String, Set<HandlerDescriptor>> dependTo) {
            Map<String, List<Object>> parameterValues = new HashMap<>();

            boolean dependIsNull = false;
            for (Map.Entry<String, Class> parameterPair : allParameters.entrySet()) {
                String parameterName = parameterPair.getKey();
                if(dependAttr != null && dependAttr.containsKey(parameterName.trim())) {// 依赖别的输出
                    String dependResult = dependAttr.get(parameterName.trim());
                    if(dependTo == null) {
                        throw new SmartHandlerException(parameterName.trim() + " depend to " + dependResult + " not exists !");
                    }
                    for (HandlerDescriptor dependHandler : dependTo.get(parameterName.trim())) {
                        if(resultData.containsKey(dependHandler)) {//依赖的DependHandler已经处理结束
                            if(!this.resultData.get(dependHandler).executeStage.isFinish()) {
                                //TODO 虽然建立了依赖关系，但是由于parameterName被配置为可选，导致这里报错,这里应该调整一下逻辑，存在依赖关系，那么tmpMissParameters就应该建立等待
                                logger.warn("虽然建立了依赖关系，但是由于" + parameterName.trim() + "被配置为可选，导致这里报错");
                            }

                            List<Object> values = this.resultData.get(dependHandler).fetch(dependResult);
                            dependIsNull |= values.isEmpty();
                            if(values.isEmpty() || (values.size() == 1 && StringUtils.isBlank(String.valueOf(values.get(0))))) {
                                Object defaultValue = defaultValues.get(parameterName.trim());
                                //处理默认值问题，比如：${PARAMETERS.PAGE.NO:1}，${PARAMETERS.PAGE.Size:20}
                                if(defaultValue != null && defaultValue instanceof String && ((String) defaultValue).matches("\\$\\{[a-zA-Z.]+:\\d+\\}")){
                                    defaultValue = RegexUtils.find((String) defaultValue, "(?!(\\$\\{[a-zA-Z.]+:))\\d+(?=\\})")[0];
                                }
                                if(defaultValue != null) {
                                    parameterValues.put(parameterName.trim(), Lists.newArrayList(defaultValue));
                                }else if(requires != null && requires.contains(parameterName.trim())){
                                    throw new BreakException("handler parameter [" + parameterName.trim()+ "] is required, but depend handler return empty !");
                                }else{
                                    parameterValues.put(parameterName.trim(), Lists.newArrayList());
                                }

                            }else {
                                parameterValues.put(parameterName.trim(), values);
                            }
                            break;
                        }
                    }
                }else if(inputParameters != null && inputParameters.containsKey(parameterName) && !parameterValues.containsKey(parameterName)){//依赖于入参
//                    parameterValues.put(parameterName, Lists.newArrayList(apiData.parameterKVPair.get(parameterName)));//应该取别名的值，不应该是自己的变量名
                    parameterValues.put(parameterName, Lists.newArrayList(apiData.parameterKVPair.get(inputParameters.get(parameterName))));
                }else if(defaultValues != null && defaultValues.containsKey(parameterName) && !parameterValues.containsKey(parameterName)) {
                    if("${PARAMETERS.VALUES.ARRAY}".equals(defaultValues.get(parameterName))) {
                        parameterValues.put(parameterName, new ArrayList<Object>(){{add(apiData.parameterKVPair.values().toArray(new Object[0]));}});
                    }else if("${HANDLER.CONFIG.PARAMETERS}".equals(defaultValues.get(parameterName))) {
                        parameterValues.put(parameterName, new ArrayList<Object>(){{add((apiData.parameterKVPair).keySet().toArray(new String[0]));}});
                    }else if("${RUNTIME.DEPEND.LIST.A}".equals(defaultValues.get(parameterName))) {
                        parameterValues.put(parameterName, new ArrayList<Object>(){{add((apiData.parameterKVPair).keySet().toArray(new String[0]));}});
                    }else if("${RUNTIME.DEPEND.LIST.B}".equals(defaultValues.get(parameterName))) {
                        parameterValues.put(parameterName, new ArrayList<Object>(){{add((apiData.parameterKVPair).keySet().toArray(new String[0]));}});
                    }else if("${RUNTIME.DEPEND.OFFSET.A}".equals(defaultValues.get(parameterName))) {
                        parameterValues.put(parameterName, new ArrayList<Object>(){{add((apiData.parameterKVPair).keySet().toArray(new String[0]));}});
                    }else if("${RUNTIME.DEPEND.OFFSET.B}".equals(defaultValues.get(parameterName))) {
                        parameterValues.put(parameterName, new ArrayList<Object>(){{add((apiData.parameterKVPair).keySet().toArray(new String[0]));}});
                    }else if("${HANDLER.CONFIG.RESULTS}".equals(defaultValues.get(parameterName))) {
                        parameterValues.put(parameterName, new ArrayList<Object>(){{add(handlerDescriptor.getDbQueryFields().toArray(new String[0]));}});
                    }else {
                        Object defaultValue = defaultValues.get(parameterName);
                        //处理默认值问题，比如：${PARAMETERS.PAGE.NO:1}，${PARAMETERS.PAGE.Size:20}
                        if(defaultValue != null && defaultValue instanceof String && ((String) defaultValue).matches("\\$\\{[a-zA-Z.]+:\\d+\\}")){
                            defaultValue = RegexUtils.find((String) defaultValue, "(?!(\\$\\{[a-zA-Z.]+:))\\d+(?=\\})")[0];
                        }
                        parameterValues.put(parameterName, Lists.newArrayList(defaultValue));
                    }

                }else if(requires != null && !requires.contains(parameterName) && !parameterValues.containsKey(parameterName)) {
                    parameterValues.put(parameterName, Lists.newArrayList());
                }else {
                    //TODO
                    //没有找对对应的值
                }
            }
            return parameterValues;
        }

        public void setRelHandlersData(HandlersData relHandlersData) {
            if(relHandlersData == null) return;
            this.relHandlersData = relHandlersData;
            Map<HandlerDescriptor, HandleSwapData> relResultData = relHandlersData.getResultData();
            for (Map.Entry<HandlerDescriptor, HandleSwapData> entry : relResultData.entrySet()) {
                resultData.put(entry.getKey(), entry.getValue());
                triggerWaitingHandlers(entry.getKey());
            }
        }

        public Map<HandlerDescriptor, HandleSwapData> getResultData() {
            return resultData;
        }
        public Map<HandlerDescriptor, HandleSwapData> getActivateResultData() {
            Map<HandlerDescriptor, HandleSwapData> tmp = new HashMap<>();
            for (HandlerDescriptor handlerDescriptor : resultData.keySet()) {
                String parentPath = handlerDescriptor.getHandler().getParentPath();
                if(StringUtils.isNotBlank(parentPath) && parentPath.startsWith("/tmp/")) {
                    continue;//临时节点数据不拼接报文
                }else {
                    HandleSwapData handleSwapData = resultData.get(handlerDescriptor);
                    tmp.put(handlerDescriptor, handleSwapData);
                }
            }
            return tmp;
        }
    }

    public static class HandleSwapData {
        private HandlerDescriptor handlerDescriptor;
        private DC requestData;
//        private List<Map<String, Object>> requestData = new ArrayList<>();
//        private List<Map<String, Object>> responseData;
        private DC responseData;
        private HashSet<String> tmpMissParameters;

        private ExecuteStage executeStage = ExecuteStage.waiting;
        private MethodParameter returnType;

        private PeacockSpan span;

        public List<Object> fetch(final String resultName){
            if(resultName.startsWith("HANDLER:")){
                List<Object> result = new ArrayList<>();
                result.add(DCUtils.getValues(responseData));
                return result;
            }else {
                if(responseData == null) {
                    System.out.println(11);
                }
                Object fetch = responseData.fetch(resultName);
                if(DCUtils.isNullDC(responseData)) {
                    return new ArrayList<>();
                }else if(DCUtils.isMapDC(responseData)) {
                    return Lists.newArrayList(fetch);
                }else {
                    Object[] fetchResult = (Object[]) fetch;
                    return Lists.newArrayList(fetchResult);
//                    return (List<Object>) fetch;
                }
//                return CollectionUtils.fetch(responseData, new Fetcher<Map<String, Object>, Object>() {
//                    @Override
//                    public Object fetch(Map<String, Object> objectMap) {
//                        if (objectMap.get(resultName) == null) {
//                            return null;
//                        }else {
//                            return objectMap.get(resultName);
//                        }
//                    }
//                });
            }
        }

//        private void addRequest(Map<String, Object> one) {
//            requestData.add(one);
//        }

        public void setExecuteStageExecuting(PeacockSpan rootSpan) throws ClassNotFoundException {

//            ApiDescriptor apiDescriptor = ApiManager.findApiDescriptorByPath(handlerDescriptor.getProgram(),
//                    handlerDescriptor.getHandler().getPath(), handlerDescriptor.getHandler().getVersion());
            SmartHandlerFactory.HandlerInfo[] handler;
            if(StringUtils.isNotBlank(handlerDescriptor.getHandler().getClazz())){
                handler = SmartHandlerFactory.getHandler(Class.forName(handlerDescriptor.getHandler().getClazz()));
            }else {
                handler = SmartHandlerFactory.getHandler(handlerDescriptor.getProgram(), handlerDescriptor.getHandler().getPath());
            }

            span = TracerFactory.addChildSpan(handlerDescriptor.getProgram(), rootSpan,
                    handlerDescriptor.getHandler().getPath(), handler[0].getTitle(), handlerDescriptor.getHandler().getPath());
            this.executeStage = ExecuteStage.executing;
        }
        public void setExecuteStageFinish(){
            TracerFactory.finishCurrentSpan(span);
            this.executeStage = ExecuteStage.finish;
        }

        public boolean isUnExecute(){
            return executeStage.isWaiting();
        }

        public boolean isExecuted(){
            return !executeStage.isWaiting();
        }

//        public static HandleSwapData getInstance(Map<String, List<Object>> parameterValues) {
//            HandleSwapData resultData = new HandleSwapData();
//            int length = 0;
//            for (List<Object> objects : parameterValues.values()) {
//                length = objects.size() > length ? objects.size() : length;
//            }
//            for (int i = 0; i < length; i++) {
//                Map<String, Object> one = new HashMap<>();
//                for (String propertyName : parameterValues.keySet()) {
//                    List<Object> objects = parameterValues.get(propertyName);
//                    one.put(propertyName, objects.size() > i ? objects.get(i) : objects.get(0));
//                }
//                resultData.addRequest(one);
//            }
//            return resultData;
//        }

        public static HandleSwapData getInstance() {
            return  new HandleSwapData();
        }

        public void setRequestData(Map<String, List<Object>> parameterValues) {
            List<Map<String, Object>> parameterList = parseColumnsToRows(parameterValues);
            this.requestData = DCUtils.warpDC(parameterList);
            TracerFactory.addTag(span, TracerFactory.TagKey.REQUEST, DCUtils.toPlantString(this.requestData),
                    StringUtils.isNotBlank(handlerDescriptor.getHandler().getClazz()) ? span.hasIORequestTag() : span.hasHandlerRequestTag());
//
//            for (Map<String, Object> map : parameterList) {
//                this.addRequest(map);
//            }
        }

        public static List<Map<String, Object>> parseColumnsToRows(Map<String, List<Object>> columns) {
            List<Map<String, Object>> result = new ArrayList<>();

            int length = 0;
            for (List<Object> objects : columns.values()) {
                if(objects == null){
                    System.out.println("test_data => " + columns);
                }
                length = objects.size() > length ? objects.size() : length;
            }
            for (int i = 0; i < length; i++) {
                Map<String, Object> one = new HashMap<>();
                for (String propertyName : columns.keySet()) {
                    List<Object> objects = columns.get(propertyName);
                    one.put(propertyName, objects.size() > i ? objects.get(i) : objects.isEmpty() ? null : objects.get(0));
                }
                result.add(one);
            }
            return result;
        }

        public void setTmpMissParameters(HashSet<String> tmpMissParameters) {
            this.tmpMissParameters = tmpMissParameters;
        }

        public void removeTmpMissParameter(String name) {
            if(tmpMissParameters != null) {
                tmpMissParameters.remove(name);
            }
        }

        public boolean canExecutable(){
            return tmpMissParameters == null || tmpMissParameters.size() == 0;
        }

        public void setResponseData(DC returnValue) {
            this.responseData = returnValue;
//            //将Bean对象转化为Map对象
//            responseData = Object2MapHelper.transformAllToMapStruts(returnValue);
        }

        public void setReturnType(MethodParameter returnType) {
            this.returnType = returnType;
        }

        public void setHandlerDescriptor(HandlerDescriptor handlerDescriptor) {
            this.handlerDescriptor = handlerDescriptor;
        }

        public static enum ExecuteStage{
            waiting, executing, finish;

            public boolean isWaiting(){
                return this == waiting;
            }

            public boolean isFinish(){
                return this == finish;
            }
        }

        public DC getResponseData() {
            return responseData;
        }
    }

}