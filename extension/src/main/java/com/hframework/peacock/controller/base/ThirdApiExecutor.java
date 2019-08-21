package com.hframework.peacock.controller.base;

import com.hframework.beans.exceptions.BusinessException;
import com.hframework.common.util.DateUtils;
import com.hframework.common.util.RegexUtils;
import com.hframework.peacock.controller.bean.apiconf.Node;
import com.hframework.smartweb.SmartParameterInvoker;
import com.hframework.smartweb.bean.SmartParser;
import com.hframework.smartweb.bean.handler.HandlerHelper;
import com.hframework.peacock.controller.RuleData;
import com.hframework.peacock.controller.base.descriptor.*;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Array;
import java.util.*;

/**
 * Created by zhangquanhong on 2017/11/15.
 */
public class ThirdApiExecutor {
    private static final Logger logger = LoggerFactory.getLogger(ThirdApiExecutor.class);

    private ThirdApiDescriptor apiDescriptor;
    private ThirdApiConfigureRegistry registry;
    private AbstractProtocolExecutor protocolExecutor;

    public ThirdApiExecutor(ThirdApiConfigureRegistry registry, ThirdApiDescriptor apiDescriptor){
        this.apiDescriptor = apiDescriptor;
        this.registry = registry;

        String protocol = registry.getProtocol(getDomainId());
        if("yar".equals(protocol)) {
            protocolExecutor = new YarRpcProtocolExecutor(apiDescriptor, registry);
        }else {
            protocolExecutor = new HttpProtocolExecutor(apiDescriptor, registry);
        }
        initialize();
    }

    private void initialize() {
//        List<Parameter> parameterList = apiDescriptor.getApiConf().getParameters().getParameterList();
//        for (Parameter parameter : parameterList) {
//            parameterDescriptors.add(new ParameterDescriptor(parameter));
//        }
    }


    public Object execute(List<Object> parameterValues, String schema, HttpServletRequest request,
                            HttpServletResponse response)  {

        Map<String, ThirdParameterDescriptor> inputParameterDescriptors = apiDescriptor.getInputParameterDescriptors();
        Map<String, ThirdParameterDescriptor> ruleParameterDescriptors = apiDescriptor.getRuleParameterDescriptors();
        Map<String, ThirdNodeDescriptor> inputNodeDescriptors = apiDescriptor.getInputNodeDescriptors();
        Map<String, ThirdNodeDescriptor> ruleNodeDescriptors = apiDescriptor.getRuleNodeDescriptors();

        if(parameterValues.size() != inputParameterDescriptors.size() + inputNodeDescriptors.size()) {
            String message = "参数数量不匹配(input: " + parameterValues.size() + "; require: "
                    + inputParameterDescriptors.size() + " + " + inputNodeDescriptors.size() + ")!";
            logger.error(message);
            throw new BusinessException(message);
        }

        Map<String, Object> httpInvokeParameters = new HashMap<>();
        Map<String, Object> httpInvokeBodyNodes = new HashMap<>();
        int i = 0;
        for (String code : inputParameterDescriptors.keySet()) {
            //类型转换放在executeSingle内部处理，parameterDescriptor.getParameter().getBatchHelper()里面的每个元素也需要处理
//            ThirdParameterDescriptor parameterDescriptor = inputParameterDescriptors.get(code);
//            String type = parameterDescriptor.getParameter().getType();
//            if("1".equals(type) && StringUtils.isBlank(parameterDescriptor.getParameter().getBatchHelper())) {
//                httpInvokeParameters.put(code, String.valueOf(parameterValues.get(i++)));
//            }else {
//                httpInvokeParameters.put(code, parameterValues.get(i++));
//            }
            httpInvokeParameters.put(code, parameterValues.get(i++));

        }

        boolean isBatch = false;
        boolean executeSingle = true;
        Map<String, Object> tmpInvokeParameters = new HashMap<>();
        for (String key : httpInvokeParameters.keySet()) {
            Object value = httpInvokeParameters.get(key);
            String batchHelper = inputParameterDescriptors.get(key).getParameter().getBatchHelper();
            isBatch |= HandlerHelper.isArray(value);
            Object o = HandlerHelper.arrayToString(value, batchHelper);
            executeSingle &= HandlerHelper.isSingle(o);
            tmpInvokeParameters.put(key, o);
        }

        if(isBatch && executeSingle) {//批量请求时，但接口支持批量
            NodeData retData = executeSingle(ruleParameterDescriptors, inputNodeDescriptors,
                    ruleNodeDescriptors, tmpInvokeParameters, httpInvokeBodyNodes, parameterValues, inputParameterDescriptors);
            Set<Node> responseRuleNodes = apiDescriptor.getResponseRuleNodes();

            if(!responseRuleNodes.isEmpty()) {
                Node ruleNode = responseRuleNodes.iterator().next();
                String path = ruleNode.getPath();
                String helper = ruleNode.getBatchHelper();
                if("batch_join_by_commas".equals(helper)) {
                    NodeData ruleData = retData.getPathCache().get(path);
                    String[] values = ((String) ruleData.getValue()).split(",");
                    List result = new ArrayList();
                    for (String value : values) {
                        ruleData.setValue(value);
                        result.add(retData.getSchema(schema));

//                if(cnt ++ > 0) {
//                    NodeData newRuleData =  new NodeData(ruleData.getCode(), value);
//                    newRuleData.setRuntime(true);
//                    ruleData.getParent().addChildren(newRuleData);
//                }else {
//                    ruleData.setValue(value);
//                    ruleData.setRuntime(true);
//                    result.add(retData.getSchema(schema));
//                }
                    }
                    return result;
                }else{
                    throw new RuntimeException("unsupported !");
                }
            }else {
                return retData.getSchema(schema);
            }

        }else if(isBatch && !executeSingle) {//批量请求时，接口不支持批量
            List result = new ArrayList();
            for (int j = 0; j < HandlerHelper.getArrayLength(httpInvokeParameters.values()); j++) {
                tmpInvokeParameters = new HashMap<>();
                for (String key : httpInvokeParameters.keySet()) {
                    tmpInvokeParameters.put(key, Array.get(httpInvokeParameters.get(key), j));
                }
                NodeData retData = executeSingle(ruleParameterDescriptors, inputNodeDescriptors,
                        ruleNodeDescriptors, tmpInvokeParameters, httpInvokeBodyNodes, parameterValues, inputParameterDescriptors);
                result.add(retData.getSchema(schema));
            }
            return result;
        }else {//单个请求
            NodeData retData = executeSingle(ruleParameterDescriptors,inputNodeDescriptors,
                    ruleNodeDescriptors,httpInvokeParameters,httpInvokeBodyNodes,parameterValues, inputParameterDescriptors);
            return retData.getSchema(schema);
        }




    }

    public NodeData executeSingle(Map<String, ThirdParameterDescriptor> ruleParameterDescriptors,
                                  Map<String, ThirdNodeDescriptor> inputNodeDescriptors,
                                  Map<String, ThirdNodeDescriptor> ruleNodeDescriptors,
                                  Map<String, Object> httpInvokeParameters,
                                  Map<String, Object> httpInvokeBodyNodes,
                                  List<Object> parameterValues, Map<String, ThirdParameterDescriptor> inputParameterDescriptors) {

        for (String code : inputParameterDescriptors.keySet()) {
            ThirdParameterDescriptor parameterDescriptor = inputParameterDescriptors.get(code);
            if("1".equals(parameterDescriptor.getParameter().getType())) {
                httpInvokeParameters.put(code, String.valueOf(httpInvokeParameters.get(code)));
            }
        }

        int i = 0;
        for (String path : inputNodeDescriptors.keySet()) {
            String code = path.substring(1).trim();
            httpInvokeBodyNodes.put(code, parameterValues.get(i++));
        }


        for (String code : ruleParameterDescriptors.keySet()) {
            ThirdParameterDescriptor descriptor = ruleParameterDescriptors.get(code);
            if(descriptor.isFixValue()) {
                httpInvokeParameters.put(code, registry.getFixVal(getDomainId(), descriptor.getValueCode()));
            }else if(descriptor.isRule()) {
                httpInvokeParameters.put(code, String.valueOf(executeRule(code, registry.getRulExpr(getDomainId(), descriptor.getRuleCode()),
                        httpInvokeParameters, httpInvokeBodyNodes, null, registry.getAllFixVal(getDomainId()))));
            }

        }

        for (String code : ruleNodeDescriptors.keySet()) {
            ThirdNodeDescriptor descriptor = ruleNodeDescriptors.get(code);
            if(descriptor.isFixValue()) {
                httpInvokeBodyNodes.put(code, registry.getFixVal(getDomainId(), code));
            }else if(descriptor.isFixValue()) {
                httpInvokeBodyNodes.put(code, executeRule(code, registry.getRulExpr(getDomainId(), code),
                        httpInvokeParameters, httpInvokeBodyNodes, null, registry.getAllFixVal(getDomainId())));
            }
        }
        try {

            String result = protocolExecutor.execute(httpInvokeParameters, httpInvokeBodyNodes);
            if(StringUtils.isBlank(result)) {
                String finalUrl = AbstractProtocolExecutor.getFinalUrl(registry.getDomain(getDomainId()) + apiDescriptor.getPath(),
                        AbstractProtocolExecutor.getStringMapByObjMap(httpInvokeParameters));
                logger.warn("third api invoke return null => {},{}", finalUrl, result);
            }

            ThirdApiDescriptor.ResponseType responseType = apiDescriptor.getResponseType();
            NodeData retData;
            switch (responseType) {
                case XML_BODY:
                    retData = NodeData.parseXml(result);
                    break;
                case JSON_BODY:
                    retData = NodeData.parseJson(result);
                    break;
                case TXT_BODY:
                default:
                    throw new BusinessException(responseType + "is not support !");
            }

            ResponseBodyDescriptor responseDescriptor = apiDescriptor.getResponseBodyDescriptor();
            Map<String, ThirdNodeDescriptor> respRuleDescriptor = responseDescriptor.getRuleNodeDescriptors();
            for (String path : respRuleDescriptor.keySet()) {
                //TODO 针对于response进行结果内部处理
            }

            retData.build(responseDescriptor.getMeta());
            return retData;
        } catch (Exception e) {
            logger.error("Third Api Exception: {}", ExceptionUtils.getFullStackTrace(e));
            throw new BusinessException("yar exception occur !(" + e.getMessage()+ ")");
        }
    }

    public static Object executeRule(String code, String ruleExpr, Map<String, Object> parameterMap,
                              Map<String, Object> nodeMap, String body, Map<String, String> allFixVal){

        String[] vars = RegexUtils.find(ruleExpr, "\\$[a-zA-Z0-9_]+");
        for (String var : vars) {
            if(allFixVal.containsKey(var.substring(1))) {
                ruleExpr = ruleExpr.replace(var, allFixVal.get(var.substring(1)));
            }else if(parameterMap.containsKey(var.substring(1))) {
                ruleExpr = ruleExpr.replace(var, String.valueOf(parameterMap.get(var.substring(1))));
            }else if(!"$parameters".equals(var) && !"$xml".equals(var) && !"$json".equals(var)){
                throw new RuntimeException(var.substring(1) + " not defined, con't use by " + ruleExpr + " !");
            }
        }

        String[] ruleSegments = ruleExpr.split("\\.");
        RuleData resultData = null;
        for (String ruleSegment : ruleSegments) {
            String function;
            String parameter;
            if(ruleSegment.matches("[\\$a-zA-Z0-9]+\\([^\\)]*\\)")){
                function = ruleSegment.substring(0, ruleSegment.indexOf("("));
                parameter = ruleSegment.substring(ruleSegment.indexOf("(") + 1, ruleSegment.indexOf(")"));
            }else {
                function = ruleSegment;
                parameter = "";
            }

            if(parameter.matches("'[^']+'") || parameter.matches("\"[^\"]+\"")){
                parameter = parameter.substring(1, parameter.length() - 1);
            }

            if("$xml".equals(function)) {
                if(nodeMap != null && !nodeMap.isEmpty()) {
                    nodeMap.remove(code);
                    resultData = RuleData.load(nodeMap);
                }else {
                    resultData = RuleData.loadXml(body, code);
                }
            }else if("random".equals(function)) {
                resultData = RuleData.load(String.valueOf(Math.round(Math.random() * Long.valueOf(parameter))));
            }else if("timestamp".equals(function)) {
                resultData = RuleData.load(String.valueOf(System.currentTimeMillis() / 1000));
            }else if("datetime".equals(function)) {
                resultData = RuleData.load(DateUtils.getCurrentDateYYYYMMDDHHMMSS());
            }else if("$".equals(function)) {
                resultData = RuleData.load(parameter);
            }else if("$parameters".equals(function)) {
                parameterMap.remove(code);
                resultData = RuleData.load(parameterMap);
            }else if("filterValue".equals(function)) {
                resultData.filterValue(parameter);
            }else if("sortByKey".equals(function)) {
                resultData.sortByKey();
            }else if("entryJoin".equals(function)) {
                resultData.entryJoin(parameter);
            }else if("join".equals(function)) {
                resultData.join(parameter);
            }else if("concat".equals(function)) {
                resultData.concat(parameter);
            }else if("md5".equals(function)) {
                resultData.md5();
            }else if("upperCase".equals(function)) {
                resultData.upperCase();
            }else {
                throw new RuntimeException("unsupport " + function + " !");
            }
        }
        return resultData.getVal();
    }

//    public Object parseVal(String code, String valueExp, Map<String, ThirdDomainParameter>  domainParameterMap,
//                           Map<String, String> parameterMap, String body, Long domainId) throws Exception {
//        if(valueExp.matches("#val\\{[a-zA-Z_0-9]+\\}")){
//            String parameterName = valueExp.substring(5, valueExp.length() -1).trim();
//            return domainParameterMap.get(parameterName).getValue();
//        }else if(valueExp.matches("#rule\\{[a-zA-Z_0-9]+\\}")){
//            String ruleName = valueExp.substring(6, valueExp.length() -1).trim();
//            ThirdPublicRule_Example ruleExample = new ThirdPublicRule_Example();
//            ruleExample.createCriteria().andCodeEqualTo(ruleName).andDomainIdEqualTo(domainId);
//            List<ThirdPublicRule> ruleList = ruleSV.getThirdPublicRuleListByExample(ruleExample);
//            String[] ruleSegments = ruleList.get(0).getExpression().split("\\.");
//            RuleData resultData = null;
//            for (String ruleSegment : ruleSegments) {
//                String function;
//                String parameter;
//                if(ruleSegment.matches("[a-zA-Z0-9]+\\([^\\)]*\\)")){
//                    function = ruleSegment.substring(0, ruleSegment.indexOf("("));
//                    parameter = ruleSegment.substring(ruleSegment.indexOf("(") + 1, ruleSegment.indexOf(")"));
//                }else {
//                    function = ruleSegment;
//                    parameter = "";
//                }
//
//                if(parameter.matches("'[^']+'") || parameter.matches("\"[^\"]+\"")){
//                    parameter = parameter.substring(1, parameter.length() - 1);
//                }
//
//                String[] vars = RegexUtils.find(parameter, "\\$[a-zA-Z0-9_]+");
//                for (String var : vars) {
//                    parameter = parameter.replace(var, domainParameterMap.get(var.substring(1)).getValue());
//                }
//
//                if("random".equals(function)) {
//                    resultData = RuleData.load(String.valueOf(Math.round(Math.random() * Long.valueOf(parameter))));
//                }else if("timestamp".equals(function)) {
//                    resultData = RuleData.load(String.valueOf(System.currentTimeMillis() / 1000));
//                }else if("$xml".equals(function)) {
//                    if(StringUtils.isBlank(body)) body = "<xml></xml>";
//                    resultData = RuleData.loadXml(body, code);
//                }else if("$parameters".equals(function)) {
//                    parameterMap.remove(code);
//                    resultData = RuleData.load(parameterMap);
//                }else if("filterValue".equals(function)) {
//                    resultData.filterValue(parameter);
//                }else if("sortByKey".equals(function)) {
//                    resultData.sortByKey();
//                }else if("entryJoin".equals(function)) {
//                    resultData.entryJoin(parameter);
//                }else if("join".equals(function)) {
//                    resultData.join(parameter);
//                }else if("concat".equals(function)) {
//                    resultData.concat(parameter);
//                }else if("md5".equals(function)) {
//                    resultData.md5();
//                }else if("upperCase".equals(function)) {
//                    resultData.upperCase();
//                }else {
//                    throw new RuntimeException("unsupport " + function + " !");
//                }
//            }
//            return resultData.getVal();
//        }else if(StringUtils.isNotBlank(valueExp)){
//            throw new RuntimeException("unsupport config [" + valueExp + "] !");
//        }else {
//            return "";
//        }
//    }
//
//
//    public List<ParameterDescriptor> getParameterDescriptors() {
//
//        return apiDescriptor.getParameterDescriptors();
//    }
//
//    public ResultTreeDescriptor getResultStruct() {
//
//        return apiDescriptor.getResultStruct();
//    }
//
//    public ResultDescriptor getResultDescriptor(){
//        return apiDescriptor.getResult();
//    }
//
//    public HandlersDescriptor getPreHandlers() {
//        return apiDescriptor.getPreHandlers();
//    }
//
//    public HandlersDescriptor getHandlers() {
//        return apiDescriptor.getHandlers();
//    }


//    public Map<String, Object> parseParameter(HttpServletRequest request, List<Map<String, Object>> requestMapList) throws IllegalAccessException, InstantiationException, NoSuchMethodException, InvocationTargetException {
//        Map<String, Object> parameterKVPair = new LinkedHashMap<>();
//        for (ParameterDescriptor parameter : getParameterDescriptors()) {
//            Object parameterValue = getParameterValue(request, requestMapList, parameter.getName());
//            parameterValue = setDefaultValueIfNull(parameterValue, parameter.getDefaultValue());
//            parameterValue = parseValueIfTypeMissMatch(parameter.getName(), parameterValue, parameter.getType(), parameter.getParser(), parameter.getPattern());
//
//            if(parameterValue == null || parameterValue instanceof String) {
//                Class parameterType = parameter.getType();
//                String parameterName = parameter.getName();
//                //参数校验
//                //必选
//                SmartParameterInvoker.checkRequiredPass((String) parameterValue, parameter.isRequired(), parameterName);
//                SmartParameterInvoker.checkTypePass(parameterType, (String) parameterValue, parameterName);
//                SmartParameterInvoker.checkEnumsPass((String) parameterValue, parameter.getEnums(), parameterName);
//                SmartParameterInvoker.checkOptionsPass((String) parameterValue, parameter.getOptions(), parameterName);
//                SmartParameterInvoker.checkMinMaxPass((String) parameterValue, parameter.getMin(),
//                        parameter.getMax(), parameterName);
//                SmartParameterInvoker.checkRegexPass((String) parameterValue, parameter.getRegex(), parameterName);
//                if(parameter.getChecker() != null) {
//                    for (Map.Entry<Class<? extends SmartChecker>, String> checkerInfo : parameter.getChecker().entrySet()) {
//                        SmartParameterInvoker.checker(parameterName, (String)parameterValue, checkerInfo.getKey(), checkerInfo.getValue());
//                    }
//                }
//            }
//            parameterKVPair.put(parameter.getName(), parameterValue);
//        }
//        return parameterKVPair;
//    }
    private Object parseValueIfTypeMissMatch(String parameterName, Object parameterValue, Class type, Class<? extends SmartParser> parser, String pattern) throws InstantiationException, IllegalAccessException {
        if(parameterValue == null || !(parameterValue instanceof String)) return parameterValue;
        return SmartParameterInvoker.parser(type, (String) parameterValue, parser, pattern, parameterName);
    }

    private Object setDefaultValueIfNull(Object parameterValue, String defaultValue) {
        if(parameterValue != null) {
            return parameterValue;
        }
        return defaultValue;
    }

    public static Object getParameterValue(HttpServletRequest request, List<Map<String, Object>> requestMapList, String parameterName){

        if(requestMapList != null && requestMapList.size() > 0) {
            List<Object> parameterValues = new ArrayList<>();
            for (Map<String, Object> map : requestMapList) {
                parameterValues.add(map.get(parameterName));
            }
            return parameterValues.size() == 1 ? parameterValues.get(0) : parameterValues.toArray(new Object[0]);
        }else {
            //获取请求参数
            String[] paramValues = request.getParameterValues(parameterName);
            if (paramValues != null) {
                return paramValues.length == 1 ? paramValues[0] : paramValues;
            }
        }

        return null;
    }

    public Long getDomainId(){
        return apiDescriptor.getDomainId();
    }
}
