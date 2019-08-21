package com.hframework.smartweb;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.hframework.common.frame.ServiceFactory;
import com.hframework.common.util.UrlHelper;
import com.hframework.common.util.message.XmlUtils;
import com.hframework.smartweb.annotation.Handler;
import com.hframework.smartweb.annotation.SmartParameter;
import com.hframework.smartweb.bean.ApiConf;
import com.hframework.smartweb.bean.CombineResult;
import com.hframework.smartweb.bean.Object2MapHelper;
import com.hframework.smartweb.bean.SmartHandler;
import com.hframework.smartweb.bean.apiconf.*;
import com.hframework.smartweb.bean.handler.HandlerHelper;
import com.hframework.smartweb.exception.SmartHandlerException;
import com.hframework.web.config.bean.dataset.HelperItem;
import com.hframework.web.config.bean.dataset.HelperLabel;
import com.hframework.peacock.controller.base.HelperRegistry;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.LocalVariableTableParameterNameDiscoverer;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.annotation.ValueConstants;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.*;

/**
 * Created by zhangquanhong on 2016/9/22.
 */
public class SmartHandlerFactory {

    private static final Logger logger = LoggerFactory.getLogger(SmartHandlerFactory.class);

    private static final LocalVariableTableParameterNameDiscoverer parameterNameDiscoverer = new LocalVariableTableParameterNameDiscoverer();

    //Map<项目,Map<PATH, TreeMap<版本, [单个&批量]处理器信息>>>
    private static Map<String, Map<String, TreeMap<String, HandlerInfo[]>>> handlerInfo = new LinkedHashMap<>();
    private static Map<Class, TreeMap<String, HandlerInfo[]>> handlerClassInfo = new HashMap<Class, TreeMap<String, HandlerInfo[]>>();

    private static Set<String> handlerClassNotOnlySet = new HashSet<>();

    private static final String DEFAULT_PROGRAM = "frame";

    /**
     * 获取方法所有参数名
     * @param method
     * @return
     */
    public static String[] getParameterNames(Method method) {
        return parameterNameDiscoverer.getParameterNames(method);
    }


    /**
     * 添加handler
     * @param handlerClass
     * @param handlerMethod
     * @param classAnnotation
     * @param methodAnnotation
     * @param <T>
     * @param <V>
     */
    public static <T, V extends Annotation> void addHandler(Class handlerClass, Method handlerMethod,
                                                            Handler classAnnotation, Handler methodAnnotation) {
        String path = "/" + classAnnotation.path().trim() + "/" + methodAnnotation.path().trim();
        String[] owners = classAnnotation.owners();
        if(owners == null || owners.length == 0) {
            owners = methodAnnotation.owners();
        }
        if(owners == null || owners.length == 0) {
            owners = new String[]{DEFAULT_PROGRAM};
        }

        path = path.replaceAll("[/]+", "/");
        if(path.endsWith("/")) {
            path = path.substring(0, path.length() - 1);
        }

        String version = StringUtils.isNotBlank(methodAnnotation.version()) ? methodAnnotation.version() : classAnnotation.version();
        int index = methodAnnotation.batch() ? 1 : 0;
        for (String owner : owners) {
            addHandlerInternal(owner, path, version, index, new HandlerInfo(handlerMethod, methodAnnotation.description(), path, version));
        }

        if(!handlerClassInfo.containsKey(handlerClass)) {
            handlerClassInfo.put(handlerClass, new TreeMap<String, HandlerInfo[]>());
        }
        if(!handlerClassInfo.get(handlerClass).containsKey(version)) {
            handlerClassInfo.get(handlerClass).put(version, new HandlerInfo[2]);
        }

        if(handlerClassInfo.get(handlerClass).get(version)[index] != null) {
            handlerClassInfo.get(handlerClass).get(version)[index] = null;
            handlerClassNotOnlySet.add(handlerClass.getName() + ":" + version);
        }else if(!handlerClassNotOnlySet.contains(handlerClass.getName() + ":" + version)){
            handlerClassInfo.get(handlerClass).get(version)[index] = new HandlerInfo(handlerMethod, methodAnnotation.description(), path, version);
        }
    }

    private static void addHandlerInternal(String program, String path, String version, int index, HandlerInfo handlerInfo1) {
        if(!handlerInfo.containsKey(program)) {
            handlerInfo.put(program, new LinkedHashMap<String, TreeMap<String, HandlerInfo[]>>());
        }

        Map<String, TreeMap<String, HandlerInfo[]>> programHandlerInfo = handlerInfo.get(program);

        if(!programHandlerInfo.containsKey(path)) {
            programHandlerInfo.put(path, new TreeMap<String, HandlerInfo[]>());
        }
        if(!programHandlerInfo.get(path).containsKey(version)) {
            programHandlerInfo.get(path).put(version, new HandlerInfo[2]);
        }
        if(programHandlerInfo.get(path).get(version)[index] != null) {
            throw new SmartHandlerException("same path handler exists : "
                    + programHandlerInfo.get(path).get(version)[index] + "; " + handlerInfo1);
        }

        programHandlerInfo.get(path).get(version)[index] = handlerInfo1;

    }


    public static <T, V extends Annotation> void addHandler(String program, String module,
                                                            String version, String path, String title, String xmlContext, Byte state, boolean batch) {
        int index = batch ? 1 : 0;
        path = path.substring(program.length()+1);
        addHandlerInternal(program, path, version, index, new HandlerInfo(program, module, version, path, title, xmlContext, state));
    }

    public static <T, V extends Annotation> void modifyHandler(String program, String module, String version, String path, String title, String xmlContext, Byte state, boolean batch) {
        int index = batch ? 1 : 0;
        path = path.substring(program.length()+1);
        if(!handlerInfo.containsKey(program)) {
            throw new SmartHandlerException("handler modify exception, origin handler exists : " + path + "; "  + version + "; "  + index + "; " + title);
        }
        Map<String, TreeMap<String, HandlerInfo[]>> programHandlerInfo = handlerInfo.get(program);
        if(!programHandlerInfo.containsKey(path) || !programHandlerInfo.get(path).containsKey(version) || programHandlerInfo.get(path).get(version)[index] == null) {
            throw new SmartHandlerException("handler modify exception, origin handler exists : " + path + "; "  + version + "; "  + index + "; " + title);
        }

        programHandlerInfo.get(path).get(version)[index] = new HandlerInfo(program, module, version, path, title, xmlContext, state);
    }

    public static <T, V extends Annotation> void deleteHandler(String program, String path, String version, String title, boolean batch) {
        int index = batch ? 1 : 0;
        path = path.substring(program.length()+1);
        if(!handlerInfo.containsKey(program)) {
            throw new SmartHandlerException("handler modify exception, origin handler exists : " + path + "; "  + version + "; "  + index + "; " + title);
        }
        Map<String, TreeMap<String, HandlerInfo[]>> programHandlerInfo = handlerInfo.get(program);
        if(!programHandlerInfo.containsKey(path) || !programHandlerInfo.get(path).containsKey(version) || programHandlerInfo.get(path).get(version)[index] == null) {
            throw new SmartHandlerException("handler delete exception, origin handler exists : " + path + "; "  + version + "; "  + index + "; " + title);
        }

        programHandlerInfo.get(path).get(version)[index] = null;
        if(programHandlerInfo.get(path).get(version)[0] == null && programHandlerInfo.get(path).get(version)[1] == null) {
            handlerInfo.get(path).remove(version);
        }
        if(handlerInfo.get(path).isEmpty()){
            handlerInfo.remove(path);
        }
    }


    /**
     * 获取handler
     * @param handlerClass
     * @return
     */
    public static HandlerInfo[] getHandler(Class handlerClass) {
        if(!handlerClassInfo.containsKey(handlerClass)) throw new RuntimeException("handler[" + handlerClass + "]: not exists !");
        return handlerClassInfo.get(handlerClass).lastEntry().getValue();
    }

    public static String snapshot(){
        JSONObject images = new JSONObject(true);
        for (Class clazz : handlerClassInfo.keySet()) {
            TreeMap<String, HandlerInfo[]> versionAndHandler = handlerClassInfo.get(clazz);
            addToImages(images, clazz.getName(), versionAndHandler);
        }
        for (String program : handlerInfo.keySet()) {
            Map<String, TreeMap<String, HandlerInfo[]>> programHandlerInfo = handlerInfo.get(program);
            for (String path : programHandlerInfo.keySet()) {
                addToImages(images, "/" + program + path, programHandlerInfo.get(path));
            }
        }

        String json = images.toJSONString();
        logger.info("smart handlers snapshot => " + json);
        return json;
    }

    public static void addToImages(JSONObject images, String key, TreeMap<String, HandlerInfo[]> versionAndHandler) {
        JSONObject jsonObject = new JSONObject();
        images.put(key, jsonObject);
        for (String version : versionAndHandler.keySet()) {
            HandlerInfo[] handlerInfos = versionAndHandler.get(version);
            JSONArray handlers = new JSONArray();
            for (HandlerInfo info : handlerInfos) {
                if(info != null ) {
                    handlers.add(info.getMethod() != null ? info.getMethod().toGenericString(): info.getXmlContent());
                }
            }
            jsonObject.put(version, handlers);
        }
    }

    /**
     * 获取handler
     * @param path
     * @return
     */
    public static HandlerInfo[] getHandler(String program, String path) {
        if(!handlerInfo.containsKey(program) || !handlerInfo.get(program).containsKey(path)) {
            throw new RuntimeException("handler[" + program + ":" + path + "] not found !");
        }
        return handlerInfo.get(program).get(path).lastEntry().getValue();
    }

    /**
     * 获取handler
     * @param path
     * @param version
     * @return
     */
    public static HandlerInfo[] getHandler(String program, String path, String version) {
        if(!handlerInfo.containsKey(program) || !handlerInfo.get(program).containsKey(path) || !handlerInfo.get(program).get(path).containsKey(version)) {
            throw new RuntimeException("handler[" + program + ":" + path + ":" + version + "] not found !");
        }
        return handlerInfo.get(program).get(path).get(version);
    }

    /**
     * 获取handler
     * @param handlerClass
     * @return
     */
    public static boolean contain(Class handlerClass) {
        return handlerClassInfo.containsKey(handlerClass);
    }

    public static class ResultInfo{
        private String description;
        private String formatter;
        private String pattern;
        private String defaultValue;
        private String type;

        public ResultInfo() {
        }
        public ResultInfo(String description) {
            this.description = description;
        }

        public ResultInfo(String description, String formatter, String pattern, String defaultValue, String type) {
            this.description = description;
            this.formatter = formatter;
            this.pattern = pattern;
            this.defaultValue = defaultValue;
            this.type = type;
        }

        public String getFormatter() {
            return formatter;
        }

        public void setFormatter(String formatter) {
            this.formatter = formatter;
        }

        public String getPattern() {
            return pattern;
        }

        public void setPattern(String pattern) {
            this.pattern = pattern;
        }

        public String getDefaultValue() {
            return defaultValue;
        }

        public void setDefaultValue(String defaultValue) {
            this.defaultValue = defaultValue;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }
    }

    public static class HandlerInfo {
        private String program;
        private String path;
        private String module;
        private String title;
        private String version;
        private Method method;
        private Class handlerClass;
        private String returnType;


        private Map<String, Class> parameters = new LinkedHashMap<>();
        private Map<String, String> parameterCodeAndName = new LinkedHashMap<>();
        private Set<String> requiredParameters = new HashSet<>();
        private Map<String, Object> parameterDefaultValues = new HashMap<>();

        private Set<String> results;
        private Map<String, ResultInfo> resultInfo = new HashMap<>();//<result-code, result-name, result-type, result

        private String xmlContext = null;
        private HelperItem helperItem;

        public HandlerInfo(String program, String module, String version, String path, String title, String xmlContext, Byte state) {
            this.program = program;
            this.module = module;
            this.title = title;
            this.path = path;
            this.version = version;
            this.xmlContext = xmlContext;

            ApiConf apiConf = XmlUtils.readValue(xmlContext, ApiConf.class);
            if(apiConf.getParameters() != null && apiConf.getParameters().getParameterList() != null) {
                List<Parameter> parameterList = apiConf.getParameters().getParameterList();
                for (Parameter parameter : parameterList) {
                    parameters.put(parameter.getName(), HelperRegistry.getType(parameter.getType()));
                    parameterCodeAndName.put(parameter.getName(), parameter.getDescription());
                    if("true".equals(parameter.getRequired())){
                        requiredParameters.add(parameter.getName());
                    }
                    if(StringUtils.isNotBlank(parameter.getDefaultValue())){
                        parameterDefaultValues.put(parameter.getName(), parameter.getDefaultValue());
                    }
                }
            }

            this.returnType = "UNKNOWN";
            Handlers handlers = apiConf.getHandlers();
            if(apiConf.getResults() != null && apiConf.getResults().getResultList() != null) {
                results = new LinkedHashSet<>();
                for (Result result : apiConf.getResults().getResultList()) {
                    String resultName = StringUtils.isNoneBlank(result.getAlias()) ? result.getAlias() : result.getName();
                    results.add(resultName);
                    resultInfo.put(resultName, new ResultInfo(result.getDescription(), result.getFormatter(),
                            result.getPattern(), result.getDefaultValue(), result.getType()));
                }
            }else if(apiConf.getHandlers() != null && apiConf.getHandlers().getHandlerList() != null) {
                results = new LinkedHashSet<>();
//                this.returnType = apiConf.getHandlers().getHandlerList().get(0).getReturnType();
//                if(path.contains("get_repay_calender_day_custs")) {
//                    System.out.println(1);
//                }
                boolean isMulti = false;
                int flag = 0;
                for (com.hframework.smartweb.bean.apiconf.Handler handler : apiConf.getHandlers().getHandlerList()) {
                    if(handler.getResultList() == null || (handler.getParentPath() != null && handler.getParentPath().startsWith("/tmp/")))
                        continue;
                    boolean allMoveLeft = true;
                    for (Result result : handler.getResultList()) {
                        allMoveLeft &= result.getName().endsWith("[]");
                        String resultName = StringUtils.isNoneBlank(result.getAlias()) ? result.getAlias() : result.getName();
                        results.add(resultName);
                        resultInfo.put(resultName, new ResultInfo(result.getDescription(), result.getFormatter(),
                                result.getPattern(), result.getDefaultValue(), result.getType()));
                    }

                    isMulti |= "Array".equals(allMoveLeft? "Object" : handler.getReturnType());
                }
                this.returnType = (flag == 0 && isMulti) ? "Array" : "Object";
            }
        }

        public HandlerInfo(Method method, String title, String path, String version) {
            this.path = path;
            this.version = version;
            this.module = path.substring(1, path.substring(1).indexOf("/")+1);
            this.method = method;
            this.title = title;
            this.handlerClass = method.getDeclaringClass();
            this.returnType = method.getReturnType().isArray() || List.class.isAssignableFrom(method.getReturnType())
                    || CombineResult.class.isAssignableFrom(method.getReturnType())? "Array" : "Object";
            parseParameter();
            parseResult();
        }

        private HelperItem setHelperItem() {
            HelperItem helperItem = new HelperItem();
            helperItem.setName(title);

            com.hframework.smartweb.bean.apiconf.Handler handler = new com.hframework.smartweb.bean.apiconf.Handler();
            if(handlerClass != null) handler.setClazz(handlerClass.getName());
            handler.setPath(path);
            handler.setVersion(version);
            handler.setReturnType(returnType);
            handler.setParameterList(new ArrayList<Parameter>());

            for (String parameterName : parameters.keySet()) {
                Parameter parameter = new Parameter();
                parameter.setName(parameterName);
                parameter.setDescription(parameterCodeAndName.get(parameterName));
                parameter.setValue(parameterDefaultValues.containsKey(parameterName) ? String.valueOf(parameterDefaultValues.get(parameterName)) : null);
                parameter.setDefaultValue(parameterDefaultValues.containsKey(parameterName) ? String.valueOf(parameterDefaultValues.get(parameterName)) : null);
                parameter.setRef("");
                parameter.setScope("parameter");
                handler.getParameterList().add(parameter);
            }
            handler.setResultList(new ArrayList<Result>());
            if(this.getResults() != null && this.getResults().size() > 0) {
                for (String resultName : this.getResults()) {
                    Result result1 = new Result();
                    result1.setName(resultName);
                    result1.setDescription(!resultInfo.containsKey(resultName) ? "" : resultInfo.get(resultName).getDescription());
//                    result1.setType(resultInfo.get(resultName).getType());
                    result1.setType(!resultInfo.containsKey(resultName) ? "" : SmartResultHelper.mergeType(resultInfo.get(resultName).getType(),
                            resultInfo.get(resultName).getFormatter(), resultInfo.get(resultName).getPattern()));
                    handler.getResultList().add(result1);
                }
            }else if(results instanceof Object2MapHelper.RuntimeSet){
                Result result1 = new Result();
                result1.setName("${RUNTIME.RESULT}");
                result1.setDescription("动态返回结果");
                handler.getResultList().add(result1);
            }else {
                if(handlerClass == null) {
                    handler.getResultList().addAll(getResultsByHandlesXML());
                }else {//ThirdApiInvokeHandler.java 中OBJECT返回，页面刷新不出来
                    Result result1 = new Result();
                    result1.setName("${RUNTIME.RESULT}");
                    result1.setDescription("动态返回结果");
                    handler.getResultList().add(result1);
                }
            }
//            helperItem.setText("");
            try {
                helperItem.setText(XmlUtils.writeValueAsString(handler));
            } catch (IOException e) {
                e.printStackTrace();
            }
            return helperItem;
        }

        public List<Result> getResultsByHandlesXML(){
            ApiConf apiConf = XmlUtils.readValue(xmlContext, ApiConf.class);
            List<com.hframework.smartweb.bean.apiconf.Handler> handlerList = apiConf.getHandlers().getHandlerList();
            for (com.hframework.smartweb.bean.apiconf.Handler aHandler : handlerList) {
                String clazz = aHandler.getClazz();
                List<Result> resultList = aHandler.getResultList();
                if(resultList != null && resultList.size() == 1 && "${RUNTIME.RESULT}".equals(resultList.get(0).getName())){
                    List<Parameter> parameterList = aHandler.getParameterList();
                    List<Object> parameterValues = new ArrayList<>();
                    for (Parameter parameter : parameterList) {
                        if("sql".equals(parameter.getName()) && StringUtils.isNoneBlank(parameter.getValue())) {
                            String sql = calcDemoSql(parameter.getValue());
                            parameterValues.add(sql);
                        }else if ("${PARAMETERS.VALUES.ARRAY}".equals(parameter.getValue())){
                            parameterValues.add(null);
                        }else if(StringUtils.isNoneBlank(parameter.getValue())) {
                            parameterValues.add(parameter.getValue());
                        }else {
                            parameterValues.add(null);
                        }
                    }


                    try {
                        final SmartHandler smartHandler = (SmartHandler) ServiceFactory.getService(Class.forName(clazz));
                        Method method = SmartHandlerFactory.getHandler(Class.forName(clazz))[0].getMethod();
                        Object returnObject = HandlerHelper.handle(smartHandler, method, parameterValues.toArray(new Object[0]));
                        List<Map<String, Object>> maps = Object2MapHelper.transformAllToMapStruts(returnObject);
                        if(maps.size() > 0) {
                            Map<String, Object> propertiesMap = maps.get(0);
                            List<Result> results= new ArrayList<>();
                            for (String property : propertiesMap.keySet()) {
                                Result result1 = new Result();
                                result1.setName(property);
                                results.add(result1);
                            }
                            return results;
                        }
                    }catch (Exception e) {
                        e.printStackTrace();
                        throw new SmartHandlerException(APIErrorType.SERVER_ERROR, "invoke handler method error ! handler = " + method.getDeclaringClass().getSimpleName() + "; method = " + method.getName() + "; errorInfo = " + e.getMessage() );
                    }
                }
            }
            return new ArrayList<>();
        }

        public static String calcDemoSql(String sql){
            sql = sql.toLowerCase().trim();
            sql = sql.replaceAll("((and)|(or)) [^\\?]*\\?", "");
            sql = sql.replaceAll("(where)[^\\?]*\\?[ ]*((and)|(or ))", "where ");
            sql = sql.replaceAll("(where)[^\\?]*\\?[ ]*", "");
//                System.out.println(sql);
//                System.out.println(sql.matches(".*where.*=.*"));
            if(!sql.matches(".*where.*=.*") && sql.contains("where")){
                sql = sql.replaceAll("where", " ");
            }
            sql = sql.replaceAll("limit[ ]* [,0-9 ]+", "limit 1");
            if(!sql.matches(".*limit 1")) {
                sql += " limit 1";
            }
            return sql;
        }

        private void parseResult() {
            results = Object2MapHelper.allResults(resultInfo, new MethodParameter(method, -1));

        }

        private void parseParameter() {
            String[] parameterNames = getParameterNames(method);
            for (int i=0;i<parameterNames.length;i++){
                SmartParameter ann = null;
                Annotation[] anns = method.getParameterAnnotations()[i];
                if(anns != null) {
                    for (Annotation annotation : anns) {
                        if(annotation.annotationType().equals(SmartParameter.class)) {
                            ann = (SmartParameter) annotation;
                            break;
                        }
                    }
                }

                String parameterName = parameterNames[i];
                if(ann != null && StringUtils.isNotBlank(ann.name())) {
                    parameterName  = ann.name();
                }

                parameters.put(parameterName, method.getParameterTypes()[i]);

                if(ann != null) {
                    if(StringUtils.isNotBlank(ann.description())) {
                        parameterCodeAndName.put(parameterName, ann.description());
                    }
                    if(ann.required()) {
                        requiredParameters.add(parameterName);
                    }
                    if(StringUtils.isNotBlank(ann.defaultValue()) && !ValueConstants.DEFAULT_NONE.equals(ann.defaultValue())) {
                        parameterDefaultValues.put(parameterName, ann.defaultValue());
                    }
                }
            }
        }

        public String getXmlContent(){

            if(StringUtils.isBlank(xmlContext)){
                synchronized (this){
                    if(StringUtils.isBlank(xmlContext)) {
                        xmlContext = setXmlContent();
                    }
                }
            }
            return xmlContext;
        }
        public String setXmlContent(){
            ApiConf apiConf = new ApiConf();
            apiConf.setParameters(new Parameters());
            if(apiConf.getParameters() == null) {
                apiConf.setParameters(new Parameters());
            }
            apiConf.getParameters().setParameterList(new ArrayList<Parameter>());
            for (String parameterName : parameters.keySet()) {
                Parameter parameter = new Parameter();
                parameter.setName(parameterName);
                parameter.setType(parameters.get(parameterName).getSimpleName().toLowerCase());
                parameter.setRequired(String.valueOf(requiredParameters.contains(parameterName)));
                parameter.setDefaultValue(parameterDefaultValues.containsKey(parameterName) ? String.valueOf(parameterDefaultValues.get(parameterName)) : null);
                apiConf.getParameters().getParameterList().add(parameter);
            }
            apiConf.setResults(new Results());
            apiConf.getResults().setResultList(new ArrayList<Result>());
            if(this.getResults() != null && this.getResults().size() > 0) {
                for (String resultName : this.getResults()) {
                    Result result1 = new Result();
                    result1.setName(resultName);
                    apiConf.getResults().getResultList().add(result1);
                }
            }
            try {
                return XmlUtils.writeValueAsString(apiConf);
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        }

        public Map<String, String> getParameterCodeAndName() {
            return parameterCodeAndName;
        }

        public Method getMethod() {
            return method;
        }

        public Map<String, Class> getParameters() {
            return parameters;
        }

        public Set<String> getRequiredParameters() {
            return requiredParameters;
        }

        public Map<String, Object> getParameterDefaultValues() {
            return parameterDefaultValues;
        }

        public Set<String> getResults() {
            return results;
        }

        public String getTitle() {
            return title;
        }

        public String getModule() {
            return module;
        }

        public void setModule(String module) {
            this.module = module;
        }

        public String getProgram() {
            return program;
        }

        public HelperItem getHelperItem() {
//            if(helperItem == null) {
//                synchronized (this){
//                    if(helperItem == null) {
//                        helperItem = setHelperItem();
//                    }
//                }
//            }
            return setHelperItem();
        }

        public String getReturnType() {
            return returnType;
        }

        public String getPath() {
            return path;
        }
    }

    public static Map<String, TreeMap<String, HandlerInfo[]>> getHandlerInfo(String program) {
        LinkedHashMap<String, TreeMap<String, HandlerInfo[]>> programHandlerInfo = new LinkedHashMap<>();
        programHandlerInfo.putAll(handlerInfo.get("frame"));
        programHandlerInfo.putAll(handlerInfo.get(program));
        return programHandlerInfo;
    }

    public static String getPreHandlersXML(HttpServletRequest request){
       String result = getHandlersXML(request);
        return result.replaceAll("<handler","<prehandler").replaceAll("</handler>", "</prehandler>").
                replaceAll("&lt;handler","&lt;prehandler").replaceAll("&lt;/handler&gt;", "&lt;/prehandler&gt;");
    }

    public static String getHandlersXML(HttpServletRequest request){
        String programId = request.getParameter("programId");
        if(StringUtils.isBlank(programId)) {
            Map<String, String> referParameters = UrlHelper.getUrlParameters(request.getHeader("Referer"), false);
            if(referParameters.containsKey("programId")) {
                programId = referParameters.get("programId");
            }
        }
        if(StringUtils.isBlank(programId)) {
            throw new RuntimeException("项目ID[ " + programId + "]不存在,请检查URL合法性！");
        }

        Map<String, TreeMap<String, HandlerInfo[]>> programHandlerInfo = new LinkedHashMap<>();
        programHandlerInfo.putAll(handlerInfo.get("frame"));
        if(handlerInfo.containsKey(programId)) {
            programHandlerInfo.putAll(handlerInfo.get(programId));
        }

        Map<String, List<HelperItem>> result = new LinkedHashMap<>();
        for (String path : programHandlerInfo.keySet()) {
            TreeMap<String, HandlerInfo[]> stringTreeMap =  programHandlerInfo.get(path);
            HandlerInfo single = stringTreeMap.lastEntry().getValue()[0];
            if(single == null) {
                logger.error(path + " has not any handler !");
            }
            String module = single.getModule();
            if(!result.containsKey(module)) result.put(module, new ArrayList<HelperItem>());
            result.get(module).add(single.getHelperItem());
        }
        try {
            String xml = "";
            for (String module : result.keySet()) {
                HelperLabel helperLabel = new HelperLabel();
                helperLabel.setName(module);
                helperLabel.setHelpItems(result.get(module));
                xml += (XmlUtils.writeValueAsString(helperLabel) + "\n");
            }
            return xml;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
