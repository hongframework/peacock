package com.hframework.springext.returnval;

import com.hframework.smartweb.*;
import com.hframework.smartweb.annotation.*;
import com.hframework.smartweb.bean.*;
import com.hframework.smartweb.bean.handler.HandlerHelper;
import com.hframework.smartweb.bean.handler.ResultHelper;
import com.hframework.smartweb.exception.SmartHandlerException;
import com.hframework.common.frame.ServiceFactory;
import com.hframework.common.util.collect.CollectionUtils;
import com.hframework.common.util.collect.bean.Fetcher;
import com.hframework.springext.argument.SmartParameterResolver;
import com.hframework.peacock.controller.base.dc.DC;
import com.hframework.peacock.controller.base.dc.DCUtils;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.core.MethodParameter;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.web.accept.ContentNegotiationManager;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.support.ModelAndViewContainer;
import org.springframework.web.servlet.mvc.method.annotation.AbstractMessageConverterMethodProcessor;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.*;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

/**
 * Controller方法返回对象转换扩展类
 * 针对于返回对象的类型（单个、多个）进行转换、扩展处理。
 * 转换：无论返回对象是单个还是多个，都是进行单个按照注解规则进行转化（别名，格式化）
 * 扩展：
 *      1.当在进入方法执行前会进行预判，当满足扩展处理条件会开启异步线程进行扩展处处理,见{@link SmartParameterResolver}
 *        并将Future对象存入webRequest对象中
 *      2.如果是单个返回对象，判断步骤1是否存在对应的扩展Future对象，如果存在直接获取结果拼装，否则分别开启异步线程进行扩展类{@link SmartHandler}处理,
 *        然后再将异步结果拼装到返回map中
 *      3.如果是多个返回对象，但是多个返回对象对应的扩展属性值相同，判断步骤1是否存在对应的扩展Future对象，
 *        如果存在直接获取结果拼装到所有结果对象中，否则分别开启异步线程进行扩展类{@link SmartHandler}批量数据处理,然后再将异步结果桉顺序拼装到返回map中
 * Created by zhangquanhong on 2017/2/23.
 */
public class SmartReturnValueHandler extends AbstractMessageConverterMethodProcessor {


    private ExecutorService executorService = SmartThreadPoolExecutorFactory.getExecutorServiceObject();

    protected SmartReturnValueHandler(List<HttpMessageConverter<?>> messageConverters) {
        super(messageConverters);
    }

    protected SmartReturnValueHandler(List<HttpMessageConverter<?>> messageConverters,
                                      ContentNegotiationManager manager) {
        super(messageConverters, manager);
    }

    protected SmartReturnValueHandler(List<HttpMessageConverter<?>> messageConverters,
                                      ContentNegotiationManager manager, List<Object> responseBodyAdvice) {
        super(messageConverters, manager, responseBodyAdvice);
    }

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return false;
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
                                  NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
        return null;
    }

    @Override
    public boolean supportsReturnType(MethodParameter returnType) {
        return returnType.getMethodAnnotation(SmartResult.class) != null;
    }

    @Override
    public void handleReturnValue(Object returnValue, MethodParameter returnType, ModelAndViewContainer mavContainer,
                                  NativeWebRequest webRequest) throws Exception {
        mavContainer.setRequestHandled(true);
        LogHelper.mainEnd(LogHelper.Type.Statistics);
        //执行结果过滤与扩展操作
        Object returnObject = doSmartFilter(returnValue, returnType, webRequest);

        writeWithMessageConverters(returnObject, returnType, webRequest);
    }

    /**
     * 结果过滤与扩展
     * @param returnValue 返回对象
     * @param returnType 返回类型
     * @param webRequest request请求对象
     * @return
     * @throws IllegalAccessException
     * @throws InstantiationException
     * @throws ExecutionException
     * @throws InterruptedException
     */
    private Object doSmartFilter(Object returnValue, MethodParameter returnType, NativeWebRequest webRequest)
            throws Exception {

        if(returnValue == null || BeanUtils.isSimpleProperty(returnValue.getClass())) {
            return returnValue;
        }

        //将Bean对象转化为Map对象
        List<Map<String, Object>> returnObjects = Object2MapHelper.transformAllToMapStruts(returnType, returnValue);
        SmartResult result = returnType.getMethodAnnotation(SmartResult.class);
        if(returnObjects.size() >= 1) {
            doSmartBatchFilter(returnObjects, result, returnType, webRequest);
        }

        Object data = null;
        if(Object2MapHelper.getCollectionParameterType(returnType) != null || Object2MapHelper.getArrayParameterType(returnType) != null){
            data = returnObjects != null ? returnObjects : new ArrayList<>();
        }else if(returnObjects != null && returnObjects.size() > 0 && returnObjects.get(0)  != null){
            if(SmartObject.isAssignableFrom(returnObjects.get(0))) {
                data = SmartObject.getObject(returnObjects.get(0));
            }else {
                data = returnObjects.get(0);
            }
        }else {
            data = null;
        }

//        logger.info("{}|{}|{}|{}|{}|{}", DateUtils.getCurrentDateYYYYMMDDHHMMSS(), smartApiInfo[0], smartApiInfo[1], smartApiInfo[2], holder, parameter);

        if(data != null && data instanceof SmartMessage) {
            return DynResultVO.success(((SmartMessage) data).getObject());
        }

        return DynResultVO.success(data);
    }





    /**
     * 批量结果过滤与扩展
     * @param returnObjects
     * @param result
     * @param webRequest   @throws InstantiationException
     * @throws IllegalAccessException
     * @throws ExecutionException
     * @throws InterruptedException
     */
    private void doSmartBatchFilter(List<Map<String, Object>> returnObjects, SmartResult result, MethodParameter returnType, NativeWebRequest webRequest)
            throws Exception {


        Set<String> newAttrSet = new HashSet<>();
        if(result.expand().length > 0) {//批量扩展
            doSmartBatchExpand(returnObjects, result.expand(), newAttrSet, webRequest);
        }
        //对象循环进行过滤处理
        for (Map<String, Object> objectMap : returnObjects) {
            if(result.value().length > 0) doSmartResult(objectMap, result.value(), newAttrSet, returnType, webRequest);
            if(result.include().length > 0) doSmartExtract(objectMap, result.include(),newAttrSet);
            if(result.exclude().length > 0) doSmartIgnore(objectMap, result.exclude());
        }

        for (Filter filter : result.filter()) {
            SmartFilter smartFilter = SmartParameterInvoker.getSmartHelperBean(filter.filter());
            SmartApi smartApi = returnType.getMethod().getAnnotation(SmartApi.class);
            String path = ((HttpServletRequest)webRequest.getNativeRequest()).getRequestURI();
            Object attrObject = filter.attr().length > 0 ? webRequest.getParameter(filter.attr()[0]) : null;  // TODO
            Map<String, String> parameters = new LinkedHashMap<>();  // TODO
            try {
                boolean filterResult = smartFilter.filter(path, smartApi != null ? smartApi.version() : null, filter.holder(), returnObjects, attrObject, filter.when(), parameters);
                if(!filterResult) {
                    returnObjects.clear();
                    break;
                }
            } catch (Exception e) {
                logger.error(ExceptionUtils.getFullStackTrace(e));
                throw new SmartHandlerException(APIErrorType.SERVER_ERROR, "result filter exception !");
            }
        }
    }

//    /**
//     * 单个结果对象Map数据过滤与扩展
//     * @param objectMap
//     * @param webRequest
//     * @throws InstantiationException
//     * @throws IllegalAccessException
//     * @throws ExecutionException
//     * @throws InterruptedException
//     */
//    private void doSmartFilter(Map<String, Object> objectMap, SmartResult result, Class invokeClass, NativeWebRequest webRequest)
//            throws InstantiationException, IllegalAccessException, ExecutionException, InterruptedException, NoSuchMethodException {
//
//
//        if(result != null) {
//            Set<String> newAttrSet = new HashSet<>();
//            if(result.expand().length > 0) doSmartExpand(objectMap, result.expand(), newAttrSet, webRequest);
//            if(result.value().length > 0) doSmartResult(objectMap, result.value(), newAttrSet, invokeClass, webRequest);
//            if(result.include().length > 0) doSmartExtract(objectMap, result.include(),newAttrSet);
//            if(result.exclude().length > 0) doSmartIgnore(objectMap, result.exclude());
//
//        }
//    }

    /**
     * 批量扩展
     * @param objectMapList
     * @param value
     * @param newAttrSet
     * @param webRequest
     * @throws IllegalAccessException
     * @throws InstantiationException
     * @throws ExecutionException
     * @throws InterruptedException
     */
    private void doSmartBatchExpand(final List<Map<String, Object>> objectMapList, Expand[] value, Set<String> newAttrSet,
                               NativeWebRequest webRequest) throws IllegalAccessException, InstantiationException,
            ExecutionException, InterruptedException {

        //如果入参中的参数作为结果expand的条件，这么需要默认添加，以保证handler可以进行获取这些参数，同时处理结束后，需要删除这些入参值
        Object attribute = webRequest.getAttribute(SmartParameterResolver.SMART_WEB_PARAMETER_PREFIX, WebRequest.SCOPE_REQUEST);
        if(attribute != null) {
            for (Map<String, Object> map : objectMapList) {
                map.putAll((Map<String, Object>) attribute);
            }
        }

        //计算扩展的相互依赖关系
        Map<Expand, Set<Expand>> expandDependRelation = calcExpandDependRelation(value);
        Map<Future, Expand> executingFutureExpandRelation = new LinkedHashMap<>();
        handlerInvokeIfNoDepend(expandDependRelation, executingFutureExpandRelation, objectMapList, webRequest);

        while(true) {//循环删除该队列中数据，直到没有数据为止，是否更应该用queue?
            Iterator<Map.Entry<Future, Expand>> iterator = executingFutureExpandRelation.entrySet().iterator();
            if (iterator.hasNext()) {
                Map.Entry<Future, Expand> next = iterator.next();
                Object result = next.getKey().get();
                //合并结果数据
                mergeResult(result, objectMapList, newAttrSet);

                //依赖数据已经合并,删除依赖关系
                Iterator<Map.Entry<Expand, Set<Expand>>> iterator2 = expandDependRelation.entrySet().iterator();
                while (iterator2.hasNext()) {
                    Map.Entry<Expand, Set<Expand>> next1 = iterator2.next();
                    next1.getValue().remove(next.getValue());
                }
                iterator.remove();
                handlerInvokeIfNoDepend(expandDependRelation, executingFutureExpandRelation, objectMapList, webRequest);
            }else{
                break;
            }
        }

        if(attribute != null) {
            for (Map<String, Object> map : objectMapList) {
                for (String key : ((Map<String, Object>) attribute).keySet()) {
                    map.remove(key);
                }
            }
        }

    }

    private void handlerInvokeIfNoDepend(Map<Expand, Set<Expand>> expandDependRelation, Map<Future, Expand>
            executingFutureExpandRelation, List<Map<String, Object>> objectMapList, NativeWebRequest webRequest)
            throws InstantiationException, IllegalAccessException {
        Iterator<Map.Entry<Expand, Set<Expand>>> iterator = expandDependRelation.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<Expand, Set<Expand>> dependExpand = iterator.next();
            if(dependExpand.getValue().size() == 0) {//不存在依赖关系，直接执行
                Future future = handlerInvoke(dependExpand.getKey(), objectMapList, webRequest);
                executingFutureExpandRelation.put(future, dependExpand.getKey());
                iterator.remove();
            }
        }

    }

    private void mergeResult(Object result, List<Map<String, Object>> objectMapList, Set<String> newAttrSet) {
        if(result != null) {
              //只有主线程修改objectMapList，如果子线程不迭代读取map既不会报错
//            synchronized (objectMapList) {//在合并结果时，需要加锁，因为子线程可能正读取objectMapList，则会出现java.util.ConcurrentModificationException
                if(result instanceof Map) {//如果扩展中存在单个数据扩展，则将所有结果对象都增加该单个数据扩展内容
                    Map<String, Object> handleResult = (Map<String, Object>) result;
                    for (int i = 0; i < objectMapList.size(); i++) {
                        objectMapList.get(i).putAll(handleResult);
                        newAttrSet.addAll(handleResult.keySet());
                    }
                }else if(result instanceof List){//如果是走批量数据扩展，按照顺序进行对象扩展（注意：这里要求批量数据扩展需要严格按照源数据顺序返回）
                    List<Map<String, Object>> handleResult = (List<Map<String, Object>>) result;
                    if(handleResult.size() != objectMapList.size()) {
                        throw new SmartHandlerException(APIErrorType.SERVER_ERROR, "extend number not matches !");
                    }
                    for (int i = 0; i < objectMapList.size(); i++) {
                        objectMapList.get(i).putAll(handleResult.get(i));
                        newAttrSet.addAll(handleResult.get(i).keySet());
                    }
                }
//            }
        }

    }

    private Future handlerInvoke(Expand expand, final List<Map<String, Object>> objectMapList,
                                                      NativeWebRequest webRequest) throws IllegalAccessException,
            InstantiationException {
        final String[] from = expand.attr();
        //获取批量扩展的源数据集合
        final boolean[] noKey = {true};
        List<String[]> fromValues = CollectionUtils.fetch(objectMapList, new Fetcher<Map<String, Object>, String[]>() {
            @Override
            public String[] fetch(Map<String, Object> objectMap) {
                List<String> tmpList = new ArrayList<String>();
                for (String frm : from) {
                    if(!objectMap.containsKey(frm) ){
                        tmpList.add(null);
                    }else if(objectMap.get(frm) == null) {
                        tmpList.add(null);
                        noKey[0] = false;
                    }else {
                        tmpList.add(String.valueOf(objectMap.get(frm)));
                        noKey[0] = false;
                    }
                }
                return tmpList.toArray(new String[0]);
            }
        });
        List<String[]> distinctFromValues = distinctFromValues(fromValues);

        //如果仅有一个源数据对象，且在SmartParameterResolver已经进行了异步获取不再重新发起异步获取
        Future<Map<String, Object>> future = null;
        if(distinctFromValues.size() == 1) {
            if(noKey[0]) {//返回无此字段
                future = (Future<Map<String, Object>>)webRequest.getAttribute(
                        Expand.Print.toString(expand), RequestAttributes.SCOPE_REQUEST);
            }else {//判断expand是否在SmartParameterResolver已经开始异步获取,如果是直接获取future对象，否则开始异步请求
                future = (Future<Map<String, Object>>)webRequest.getAttribute(
                        Expand.Print.toString(expand, distinctFromValues.get(0)), RequestAttributes.SCOPE_REQUEST);
            }
        }


        if(future != null) {
            return future;
        }else {//调用handler进行批量数据扩展处理
            final String[] to =expand.newAttr();
            final String patternString = expand.patternString();
            final Class<? extends SmartHandler> handler = expand.handler();
            final SmartHandler smartHandler = ServiceFactory.getService(handler);
            final Method method = SmartHandlerFactory.getHandler(handler)[0].getMethod();
            final Method bathMethod = SmartHandlerFactory.getHandler(handler)[1].getMethod();
            final Thread mainThread = Thread.currentThread();
            //在主线程中完成序列化工作，否则子线程需要加锁处理，因为在主线程合并结果时，子线程iterate objectMapList，则会出现java.util.ConcurrentModificationException
            final String objectMapListSerialize = objectMapList.toString();
            return executorService.submit(new Callable<List<Map<String, Object>>>() {
                @Override
                public List<Map<String, Object>> call() throws Exception {
                    try {
                        LogHelper.parallelStart(LogHelper.Type.Statistics, mainThread, smartHandler.getClass(), objectMapListSerialize);
                        DC dc = HandlerHelper.batchHandle(executorService, smartHandler, method, bathMethod, from, to, patternString, DCUtils.warpDC(objectMapList), null, null);
                        return DCUtils.getList(dc);
                    }finally {
                        LogHelper.parallelEnd(LogHelper.Type.Statistics, smartHandler.getClass());
                    }

                }
            });

        }
    }

    private Map<Expand, Set<Expand>> calcExpandDependRelation(Expand[] depands) {
        Map<Expand, Set<Expand>> expandDependRelation = new HashMap<>();
        Map<String, Expand> newAttrExpandRelation = new HashMap<>();
        for (Expand expand : depands) {
            expandDependRelation.put(expand, new HashSet<Expand>());
            String[] attrs = expand.attr();
            for (String attr : attrs) {
                if(newAttrExpandRelation.containsKey(attr)){
                    expandDependRelation.get(expand).add(newAttrExpandRelation.get(attr));
                }
            }
            String[] newAttrs = expand.newAttr();
            for (String newAttr : newAttrs) {
                newAttrExpandRelation.put(newAttr, expand);
            }
        }
        return expandDependRelation;
    }

    protected List<String[]> distinctFromValues(List<String[]> fromValues){
        List<String[]> result = new ArrayList<>();
        for (String[] fromValue : fromValues) {
            boolean isContain = false;
            for (Object[] objects : result) {
                if(isSameElement(fromValue, objects)){
                    isContain = true;
                    break;
                };
            }
            if(!isContain){
                result.add(fromValue);
            }

        }
        return result;

    }

    protected  boolean isSameElement(Object[] fromValue, Object[] toValues){
        if(fromValue.length != toValues.length) return false;

        for (int i = 0; i < fromValue.length; i++) {
            if(fromValue[i] == null && toValues[i] == null) {
                return true;
            }
            if((fromValue[i] == null && toValues[i] != null) ||(fromValue[i] != null && toValues[i] == null) || !fromValue[i].equals(toValues[i])) {
                return false;
            }
        }
        return true;
    }


//    /**
//     * 进行扩展处理
//     * 判断expand是否在{@link SmartParameterResolver}已经开始异步获取,如果是直接获取future对象，否则再开始异步请求
//     * @param objectMap
//     * @param value
//     * @param newAttrSet
//     * @param webRequest
//     * @throws IllegalAccessException
//     * @throws InstantiationException
//     * @throws ExecutionException
//     * @throws InterruptedException
//     */
//    private void doSmartExpand(final Map<String, Object> objectMap, Expand[] value, Set<String> newAttrSet,
//                               NativeWebRequest webRequest) throws IllegalAccessException, InstantiationException,
//            ExecutionException, InterruptedException {
//
//        List<Future<Map<String, Object>>> futures = new ArrayList<>();
//        for (Expand expand : value) {
//            String[] from = expand.attr();
//            String[] to =expand.newAttr();
//            String patternString = expand.patternString();
//            Class<? extends SmartHandler> handler = expand.handler();
//            final SmartHandler smartHandler = handler.newInstance();
//            ServiceFactory.autowireBeanProperties(smartHandler,
//                    AutowireCapableBeanFactory.AUTOWIRE_BY_NAME, false);
//            if(from.length > 0) smartHandler.setFromProperties(from);
//            if(to != null && to.length > 0) smartHandler.setToProperties(to);
//            if(StringUtils.isNoneBlank(patternString)) smartHandler.setPatternString(patternString);
//
//            //判断expand是否在SmartParameterResolver已经开始异步获取,如果是直接获取future对象，否则开始异步请求
//            Future<Map<String, Object>> future = null;
//            if(objectMap.containsKey(from)) {//如果返回存在from字段，这完全匹配，如果返回没有from字段，看是否请求参数中含有该扩展
//                List<String> fromValue = new ArrayList<String>();
//                for (String frm : from) {
//                    fromValue.add(String.valueOf(objectMap.get(frm)));
//                }
//                future = (Future<Map<String, Object>>)webRequest.getAttribute(
//                        Expand.Print.toString(expand, fromValue.toArray(new String[0])), RequestAttributes.SCOPE_REQUEST);
//            }else {
//                future = (Future<Map<String, Object>>)
//                        webRequest.getAttribute(Expand.Print.toString(expand), RequestAttributes.SCOPE_REQUEST);
//            }
//
//            if(future != null) {
//                futures.add(future);
//            }else {
//                futures.add(executorService.submit(new Callable<Map<String, Object>>() {
//                    @Override
//                    public Map<String, Object> call() throws Exception {
//                        return smartHandler.handle(objectMap);
//                    }
//                }));
//            }
//        }
//
//        //扩展数据合并
//        for (Future<Map<String, Object>> future : futures) {
//            Map<String, Object> handleResult = future.get();
//            if(handleResult != null){
//                objectMap.putAll(handleResult);
//                newAttrSet.addAll(handleResult.keySet());
//            }
//        }
//    }

    /**
     * 单个对象的格式化与别名处理
     * @param objectMap
     * @param results
     * @param newAttrSet
     *@param webRequest  @throws IllegalAccessException
     * @throws InstantiationException
     */
    private void doSmartResult(Map<String, Object> objectMap, Result[] results, Set<String> newAttrSet, MethodParameter returnType, NativeWebRequest webRequest)
            throws Exception {


        //获得需要引用处理的注解所在的类与方法，并按照维度合并
        Map<Class, Map<Method, List<String>>> mergeMap = mergeDependSmartResultMap(results, returnType.getMethod().getDeclaringClass());
        for (Class dependClass : mergeMap.keySet()) {
            Map<Method, List<String>> methodListMap = mergeMap.get(dependClass);
            for (Method dependMethod : methodListMap.keySet()) {
                List<String> attrs = methodListMap.get(dependMethod);
                List<Map<String, Object>> subDataAll = new ArrayList<>();
                for (String attr : attrs) {
                    List<Map<String, Object>> subData = Object2MapHelper.transformAllToMapStruts(objectMap.get(attr));
                    if(subData != null && subData.size() > 0) {
                        if(objectMap.get(attr) instanceof List || objectMap.get(attr).getClass().isArray()) {
                            objectMap.put(attr, subData);
                        }else {
                            objectMap.put(attr, subData.get(0));
                        }
                        subDataAll.addAll(subData);
                    }
                }
                SmartResult annotation = dependMethod.getAnnotation(SmartResult.class);
                if(annotation != null) {
                    if(subDataAll.size() >= 1) {
                        doSmartBatchFilter(subDataAll,annotation,returnType,webRequest);
                    }
                }
            }
        }


        for (Result result : results) {
            ResultHelper.doSmartAliasAndFormat(objectMap, result.attr(), result.alias(), result.keepAttr(), result.defaultValue(), result.formatter(), result.pattern().getPattern(), newAttrSet);
        }

        for (Result result : results) {
            if(result.expand().length == 0 && result.values().length == 0
                    && result.include().length == 0 && result.exclude().length == 0) {
                continue;
            }
            //将Bean对象转化为Map对象
            List<Map<String, Object>> subData = Object2MapHelper.transformAllToMapStruts(objectMap.get(result.attr()));
            if(subData != null && subData.size() > 0) {
                if(objectMap.get(result.attr()) instanceof List || objectMap.get(result.attr()).getClass().isArray()) {
                    objectMap.put(result.attr(), subData);
                }else {
                    objectMap.put(result.attr(), subData.get(0));
                }

                if(result.expand().length > 0) doSmartBatchExpand(subData, result.expand(), newAttrSet, webRequest);

                for (Map<String, Object> map : subData) {
                    if(result.values().length > 0) {
                        for (SubResult subResult : result.values()) {
                            ResultHelper.doSmartAliasAndFormat(map, subResult.attr(), subResult.alias(), subResult.keepAttr(), subResult.defaultValue(), subResult.formatter(),
                                    subResult.pattern().getPattern(), new HashSet<String>());
                        }
                    }
                    if(result.include().length > 0) doSmartExtract(map, result.include(), newAttrSet);
                    if(result.exclude().length > 0){
                        doSmartIgnore(map, result.exclude());
                    }

                }
            }
        }
    }

    private Map<Class, Map<Method, List<String>>> mergeDependSmartResultMap(Result[] results, Class invokeClass) throws NoSuchMethodException {
        Map<Class, Map<Method, List<String>>> mergeMap = new HashMap<>();

        for (Result result : results) {
            if(StringUtils.isNotBlank(result.dependMethod())) {//引用注解方法进行子关系依赖
                Class dependClass = result.dependClass();
                if(result.dependClass().equals(Object.class)) {
                    dependClass = invokeClass;
                }
                Method dependMethod = dependClass.getDeclaredMethod(result.dependMethod());
                SmartResult annotation = dependMethod.getAnnotation(SmartResult.class);
                if(annotation != null) {
                    if(!mergeMap.containsKey(dependClass)) {
                        mergeMap.put(dependClass, new HashMap<Method, List<String>>());
                    }
                    if(!mergeMap.get(dependClass).containsKey(dependMethod)){
                        mergeMap.get(dependClass).put(dependMethod, new ArrayList<String>());
                    }
                    mergeMap.get(dependClass).get(dependMethod).add(result.attr());
                }
            }
        }
        return mergeMap;
    }




    /**
     * 多余属性过滤处理
     * @param objectMap
     * @param deleteKeys
     */
    private void doSmartIgnore(Map<String, Object> objectMap, String[] deleteKeys) {
        for (String removeKey : deleteKeys) {
            objectMap.remove(removeKey);
        }
    }

    /**
     * 额外保留数据处理
     * @param objectMap
     * @param keys
     * @param newAttrSet
     */
    private void doSmartExtract(Map<String, Object> objectMap, String[] keys, Set<String> newAttrSet) {
        Set<String> removeKeys = new HashSet<>(objectMap.keySet());
        removeKeys.removeAll(Arrays.asList(keys));
        removeKeys.removeAll(newAttrSet);
        for (String removeKey : removeKeys) {
            objectMap.remove(removeKey);
        }
    }


}
