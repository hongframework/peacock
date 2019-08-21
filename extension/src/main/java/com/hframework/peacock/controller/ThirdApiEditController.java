package com.hframework.peacock.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.parser.Feature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hframework.beans.controller.ResultData;
import com.hframework.common.client.http.HttpClient;
import com.hframework.common.util.RegexUtils;
import com.hframework.common.util.UrlHelper;
import com.hframework.common.util.collect.CollectionUtils;
import com.hframework.common.util.collect.bean.Mapper;
import com.hframework.common.util.message.Dom4jUtils;
import com.hframework.peacock.controller.base.ThirdApiConfigureRegistry;
import com.hframework.peacock.controller.base.ThirdApiExecutor;
import com.hframework.peacock.controller.base.YarRpcProtocolExecutor;
import com.hframework.peacock.controller.bean.apiconf.*;
import com.hframework.peacock.controller.xstream.ext.XStreamUtil;
import com.hframework.peacock.config.domain.model.*;
import com.hframework.peacock.config.service.interfaces.*;
import com.hframework.smartweb.APIErrorType;
import com.hframework.smartweb.exception.SmartHandlerException;
import com.hframework.web.ControllerHelper;
import com.hframework.web.controller.DefaultController;
import com.hframework.peacock.controller.base.ThirdApiConfigureRegistry;
import com.hframework.peacock.controller.base.ThirdApiExecutor;
import com.hframework.peacock.controller.base.YarRpcProtocolExecutor;
import com.hframework.peacock.controller.base.descriptor.ThirdApiDescriptor;
import com.hframework.peacock.controller.bean.apiconf.*;
import com.hframework.peacock.controller.xstream.ext.XStreamUtil;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.apache.commons.lang3.StringUtils;
import org.dom4j.Document;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.XMLWriter;
import org.dom4j.tree.DefaultElement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.StringWriter;
import java.util.*;

@Controller
@RequestMapping("/extend")
public class ThirdApiEditController {
    private static final Logger logger = LoggerFactory.getLogger(ThirdApiEditController.class);

    @Resource
    private IThirdDomainSV thirdDomainSV;

    @Resource
    private IThirdPubRequestSV pubRequestSV;

    @Resource
    private IThirdPubResponseSV pubResponseSV;

    @Resource
    private IThirdDomainParameterSV domainParameterSV;

    @Resource
    private IThirdPublicRuleSV ruleSV;

    @Resource
    private IThirdApiSV thirdApiSV;

    @Resource
    private IThirdCommonParameterSV thirdCommonParameterSV;


    @RequestMapping("/invokeThirdApi.json")
    @ResponseBody
    public String invokeThirdApi(Long domainId, String method, String url, String parameter, String parameterType, String body){
        try {
            ThirdDomain thirdDomain = thirdDomainSV.getThirdDomainByPK(domainId);
            String domain = thirdDomain.getUrl();
            String protocol = thirdDomain.getProtocol();
            String path = url.contains(domain)? url.substring(url.indexOf(domain) + domain.length()): url.substring(url.indexOf("/", 10));
            path = path.replaceAll("/+", "/");
            String ret;
            if("yar".equals(protocol)) {
                ThirdApiDescriptor thirdApiDescriptor = new ThirdApiDescriptor();
                thirdApiDescriptor.setDomainId(domainId);
                thirdApiDescriptor.setPath(path);
                Map<String, Object> urlObjectParameters = getUrlParameterObjects("?" + parameter);
                Map<String, String> urlParameterTypes = getUrlParameters("?" + parameterType);
                for (String key : urlParameterTypes.keySet()) {
                    String value = urlParameterTypes.get(key);
                    if("integer".equals(value)) {
                        urlObjectParameters.put(key, Integer.valueOf((String)urlObjectParameters.get(key)));
                    }
                }

                ret = new YarRpcProtocolExecutor(thirdApiDescriptor, ThirdApiConfigureRegistry.getDefaultInstance()).execute(urlObjectParameters, null);
            }else {
                if("get".equals(method)) {
                    ret = HttpClient.doGet(url, UrlHelper.getUrlParameters("?" + parameter, false));
                }else if(StringUtils.isNotBlank(body)){
                    String finalUrl = StringUtils.isNoneBlank(parameter) && !url.contains("?")? url + "?" + parameter : url;
                    ret = HttpClient.doJsonPost(finalUrl, body);
                }else {
                    ret = HttpClient.doPost(url, UrlHelper.getUrlParameters("?" + parameter, false));
                }
            }

            String[] strings = RegexUtils.find(ret, "(?<=(\\{|,))\\d+[ ]*(?=\\:)");
            for (String string : strings) {
                ret = ret.replace(string, "\"" + string + "\"");
            }

            ObjectMapper mapper = new ObjectMapper();
            try{
                Object json = mapper.readValue(ret, Object.class);
                return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(json);
            }catch (Exception e) {
                return ret;
            }
        }catch (Exception e) {
            logger.error("YarResponse: {}", ExceptionUtils.getFullStackTrace(e));
            return e.getMessage();
        }
    }



    @RequestMapping("/configMergeSubmit.json")
    @ResponseBody
    public ResultData configMergeSubmit(HttpServletRequest request,
                                        HttpServletResponse response) throws Exception {
        String dataJson = DefaultController.getRequestPostStr(request);
        JSONObject jsonObject = JSONObject.parseObject(dataJson, Feature.OrderedField);
        JSONObject apiInfo = jsonObject.getJSONArray("api-config-table|0").getJSONObject(0);
        String apiPath = apiInfo.getString("request-path");
        Long domainId = apiInfo.getLong("api-domainId");
        ThirdApi_Example apiExample = new ThirdApi_Example();
        apiExample.createCriteria().andDomainIdEqualTo(domainId).andPathEqualTo(apiPath);
        List<ThirdApi> thirdApiListByExample = thirdApiSV.getThirdApiListByExample(apiExample);

        ThirdApi api = thirdApiListByExample.isEmpty() ? new ThirdApi() : thirdApiListByExample.get(0);
        ControllerHelper.setDefaultValue(api, "id");

        api.setDomainId(domainId);
        api.setPath(apiPath);
        api.setMethod(apiInfo.getString("request-method"));
        api.setName(apiInfo.getString("api-name"));
        api.setApiType(apiInfo.getByte("api-type"));
        api.setRequestType(apiInfo.getString("request-data"));
        api.setResponseType(apiInfo.getString("response-data"));



        Api apiXml = new Api();
        apiXml.setRequest(new Request());
        apiXml.setResponse(new Response());

        JSONArray parameterInfo = jsonObject.getJSONArray("request-parameter-table|0");

        if(parameterInfo != null && parameterInfo.size() > 0) {
            apiXml.getRequest().setParameters(new Parameters());
            apiXml.getRequest().getParameters().setParameterList(new ArrayList<Parameter>());
            for (int i = 0; i < parameterInfo.size(); i++) {
                JSONObject row = parameterInfo.getJSONObject(i);
                Parameter parameter = new Parameter();
                parameter.setCode(row.getString("path"));
                parameter.setType(row.getString("type"));
                parameter.setName(row.getString("name"));
                parameter.setValue(row.getString("value"));
                parameter.setBatchHelper(row.getString("helper"));

                if(row.getBoolean("is_pub")){
                    parameter.setPublic(row.getBoolean("is_pub"));
                }

                apiXml.getRequest().getParameters().getParameterList().add(parameter);
            }
        }


        JSONArray requestInfo = jsonObject.getJSONArray("request-body-table|0");
        if(requestInfo != null && requestInfo.size() > 0) {
            apiXml.getRequest().setNode(new Node());
            apiXml.getRequest().getNode().setNodeList(new ArrayList<Node>());
            for (int i = 0; i < requestInfo.size(); i++) {
                JSONObject row = requestInfo.getJSONObject(i);
                Node node = new Node();
                node.setPath(row.getString("path"));
                node.setName(row.getString("name"));
                node.setType(row.getString("type"));
                node.setValue(row.getString("value"));
                if(row.getBoolean("is_pub")){
                    node.setPublic(row.getBoolean("is_pub"));
                }
                apiXml.getRequest().getNode().getNodeList().add(node);
            }
        }

        JSONArray responseInfo = jsonObject.getJSONArray("response-config-table|0");
        if(responseInfo.size() > 0) {
            apiXml.getResponse().setNode(new Node());
            apiXml.getResponse().getNode().setNodeList(new ArrayList<Node>());
            for (int i = 0; i < responseInfo.size(); i++) {
                JSONObject row = responseInfo.getJSONObject(i);
                Node node = new Node();
                node.setPath(row.getString("path"));
                node.setName(row.getString("name"));
                node.setType(row.getString("type"));
                node.setValue(row.getString("value"));
                node.setBatchHelper(row.getString("helper"));
                if(row.getBoolean("is_pub")){
                    node.setPublic(row.getBoolean("is_pub"));
                }
                apiXml.getResponse().getNode().getNodeList().add(node);
            }
        }

        JSONArray parameterExample = jsonObject.getJSONArray("request-parameter-example|0");
        Map<String, String> parameterExampleMap = new LinkedHashMap<>();
        if(parameterExample != null) {
            for (Object o : parameterExample) {
                JSONObject jo = (JSONObject)o;
                parameterExampleMap.put(jo.getString("name"), jo.getString("value"));
            }
        }

        apiXml.setExample(new Example());
        apiXml.getExample().setParameters(UrlHelper.getUrlQueryString(parameterExampleMap));
        if(jsonObject.getJSONArray("request-body-example|0") != null){
            apiXml.getExample().setRequest(jsonObject.getJSONArray("request-body-example|0").getJSONObject(0).getString("request-message-body"));
        }

        apiXml.getExample().setResponse(jsonObject.getJSONArray("response-body-example|0").getJSONObject(0).getString("response-message-body"));

        String content = XStreamUtil.writeValueAsString(apiXml);
        api.setContent(content);
        if(thirdApiListByExample.isEmpty()) {
            thirdApiSV.create(api);
        }else {
            thirdApiSV.update(api);
        }

        return ResultData.success();
    }

    public static Map<String, String> getUrlParameters(String url) {
        Map<String, String> map = new LinkedHashMap<>();
        if(url.contains("?")) {
            url = url.substring(url.indexOf("?") + 1);
            String[] params = url.split("&");
            if(params == null) {
                return map;
            }
            for (String param : params) {
                if(param.contains("=")) {
                    map.put(param.substring(0, param.indexOf("=")), param.substring(param.indexOf("=") + 1).trim());
                }
            }
        }
        return map;
    }
    public static Map<String, Object> getUrlParameterObjects(String url) {
        Map<String, Object> map = new LinkedHashMap<>();
        if(url.contains("?")) {
            url = url.substring(url.indexOf("?") + 1);
            String[] params = url.split("&");
            if(params == null) {
                return map;
            }
            for (String param : params) {
                if(param.contains("=")) {
                    map.put(param.substring(0, param.indexOf("=")), param.substring(param.indexOf("=") + 1).trim());
                }
            }
        }
        return map;
    }

    @RequestMapping("/configMergeReview.json")
    @ResponseBody
    public ResultData configMergeReview(Long domainId, String method, String url, String parameter, String parameterType,
                                        String body, @ModelAttribute("response") String responseBody, String kvContainers){
        try {

            String domain = thirdDomainSV.getThirdDomainByPK(domainId).getUrl();

            String path = url.contains(domain)? url.substring(url.indexOf(domain) + domain.length()): url.substring(url.indexOf("/", 10));
            path = path.replaceAll("/+", "/");

            ThirdApi_Example apiExample = new ThirdApi_Example();
            apiExample.createCriteria().andDomainIdEqualTo(domainId).andPathEqualTo(path);
            List<ThirdApi> thirdApiListByExample = thirdApiSV.getThirdApiListByExample(apiExample);
            final ThirdApi thirdApi;
            boolean isNew = true;
            if(!thirdApiListByExample.isEmpty()){
                thirdApi = thirdApiListByExample.get(0);
                isNew = false;

            }else {
                thirdApi = new ThirdApi();
                thirdApi.setPath(path);
                thirdApi.setDomainId(domainId);
            }
            thirdApi.setMethod(method);

            Boolean hasParameter = StringUtils.isNoneBlank(parameter);
            Boolean isXmlRequestBody = StringUtils.isNoneBlank(body) && body.startsWith("<") && body.endsWith(">");
            Boolean isJsonRequestBody = StringUtils.isNoneBlank(body) && body.startsWith("{") && body.endsWith("}");
            Boolean isTextRequestBody = StringUtils.isNoneBlank(body) && !isXmlRequestBody && !isJsonRequestBody;

            if(hasParameter && isXmlRequestBody){
                thirdApi.setRequestType("kv,xml");
            }else if(hasParameter && isJsonRequestBody){
                thirdApi.setRequestType("kv,json");
            }else if(hasParameter && isTextRequestBody){
                thirdApi.setRequestType("kv,txt");
            }else if(!hasParameter && isXmlRequestBody){
                thirdApi.setRequestType("xml");
            }else if(!hasParameter && isJsonRequestBody){
                thirdApi.setRequestType("json");
            }else if(!hasParameter && isTextRequestBody){
                thirdApi.setRequestType("kv,txt");
            }else {
                thirdApi.setRequestType("kv");
            }

            Boolean isXmlResponseBody = StringUtils.isNoneBlank(responseBody) && responseBody.startsWith("<") && responseBody.endsWith(">");
            Boolean isJsonResponseBody = StringUtils.isNoneBlank(responseBody) && responseBody.startsWith("{") && responseBody.endsWith("}");
            Boolean isTextResponseBody = StringUtils.isNoneBlank(responseBody) && !isXmlRequestBody && !isJsonRequestBody;
            if(isXmlResponseBody){
                thirdApi.setResponseType("xml");
            }else if(isJsonResponseBody){
                thirdApi.setResponseType("json");
            }else if(isTextResponseBody){
                thirdApi.setResponseType("txt");
            }

            ThirdPubRequest_Example example = new ThirdPubRequest_Example();
            example.createCriteria().andDomainIdEqualTo(domainId);
            List<ThirdPubRequest> pubRequestList = pubRequestSV.getThirdPubRequestListByExample(example);
            Map<String, ThirdPubRequest> pubRequestMap = CollectionUtils.convert(pubRequestList, new Mapper<String, ThirdPubRequest>() {
                @Override
                public <K> K getKey(ThirdPubRequest thirdPubRequest) {
                    return (K) thirdPubRequest.getPath();
                }
            });

            List<ThirdCommonParameter> commonParameters = thirdCommonParameterSV.getThirdCommonParameterAll();
            Map<String, ThirdCommonParameter> commonParameterMap = CollectionUtils.convert(commonParameters, new Mapper<String, ThirdCommonParameter>() {
                @Override
                public <K> K getKey(ThirdCommonParameter thirdCommonParameter) {
                    return (K) thirdCommonParameter.getPath();
                }
            });

            final Map<String, Parameter> parameters = new LinkedHashMap<>();


            Map<String, String> requestParameters = getUrlParameters("?" + parameter);
            Map<String, String> requestParameterTypes = getUrlParameters("?" + parameterType);
            for (Map.Entry<String, String> entry : requestParameters.entrySet()) {
                String code = entry.getKey();
                String value = entry.getValue();
                parameters.put(code, new Parameter());
                parameters.get(code).setCode(code);
                parameters.get(code).setName(pubRequestMap.containsKey(code) ?
                        pubRequestMap.get(code).getName() :
                        commonParameterMap.containsKey(code) ?
                                commonParameterMap.get(code).getName() :"");
                parameters.get(code).setValue(pubRequestMap.containsKey(code) ? pubRequestMap.get(code).getValue() : value);
                parameters.get(code).setPublic(pubRequestMap.containsKey(code));
                parameters.get(code).setCommon(pubRequestMap.containsKey(code) || commonParameterMap.containsKey(code));
                parameters.get(code).setType(pubRequestMap.containsKey(code) ?
                        String.valueOf(pubRequestMap.get(code).getType()) :
                        commonParameterMap.containsKey(code) ?
                            String.valueOf(commonParameterMap.get(code).getType()) :
                            "integer".equals(requestParameterTypes.get(code)) ? "2" : "1");
                parameters.get(code).setOper("1");

            }

            final Map<String, Node> request = new LinkedHashMap<>();

            if(StringUtils.isNotBlank(body.trim())){
                Map<String, Object> fieldMap = RuleData.loadBodyToMap(body.trim(), "");
                for (Map.Entry<String, Object> entry : fieldMap.entrySet()) {
                    String code = entry.getKey();
                    Object value = entry.getValue();
                    request.put(code, new Node());
                    request.get(code).setPath(code);
                    request.get(code).setName(pubRequestMap.containsKey(code) ? pubRequestMap.get(code).getName() : "");
                    request.get(code).setValue(pubRequestMap.containsKey(code) ? pubRequestMap.get(code).getValue() : String.valueOf(value));
                    request.get(code).setPublic(pubRequestMap.containsKey(code));
                    request.get(code).setType(pubRequestMap.containsKey(code) ? String.valueOf(pubRequestMap.get(code).getType()) : "1");
                    request.get(code).setCommon(pubRequestMap.containsKey(code) || commonParameterMap.containsKey(code));
                    request.get(code).setOper("1");
                }
            }

            ThirdPubResponse_Example responseExample = new ThirdPubResponse_Example();
            responseExample.createCriteria().andDomainIdEqualTo(domainId);
            List<ThirdPubResponse> pubResponseList = pubResponseSV.getThirdPubResponseListByExample(responseExample);
            Map<String, ThirdPubResponse> pubResponseMap = CollectionUtils.convert(pubResponseList, new Mapper<String, ThirdPubResponse>() {
                @Override
                public <K> K getKey(ThirdPubResponse thirdPubRequest) {
                    return (K) thirdPubRequest.getPath();
                }
            });

            String dataContainer = "";//暂不定义dataContainer，全权交给Handler通过Schema
//            for (ThirdPubResponse thirdPubResponse : pubResponseList) {
//                if(thirdPubResponse.getType() == 8){
//                    dataContainer = thirdPubResponse.getPath();
//                }
//            }

            final Map<String, Node> response = new LinkedHashMap<>();
            if(StringUtils.isNotBlank(responseBody.trim())){
                Map<String, Object> fieldMap = RuleData.loadBodyToMap(responseBody.trim(), kvContainers);
                for (Map.Entry<String, Object> entry : fieldMap.entrySet()) {
                    String code = entry.getKey().startsWith(dataContainer) && !entry.getKey().equals(dataContainer)?
                            entry.getKey().substring(dataContainer.length()) : entry.getKey();
                    Object value = entry.getValue();
                    response.put(code, new Node());
                    response.get(code).setPath(code);
                    response.get(code).setName(pubResponseMap.containsKey(code) ? pubResponseMap.get(code).getName() : "");
                    response.get(code).setValue(pubResponseMap.containsKey(code) ? pubResponseMap.get(code).getValue() : String.valueOf(value));
                    response.get(code).setPublic(pubResponseMap.containsKey(code));
                    response.get(code).setType(pubResponseMap.containsKey(code) ? String.valueOf(pubResponseMap.get(code).getType()) : "1");
                    response.get(code).setCommon(pubResponseMap.containsKey(code) || commonParameterMap.containsKey(code));
                    response.get(code).setOper("1");
                    response.get(code).setTempPath(entry.getKey());
                    if(code.endsWith("{}")) {
                        response.get(code).setType(String.valueOf(9));
                    }
                }
            }

            String apiXml = thirdApi.getContent();
            if(StringUtils.isNotBlank(apiXml)) {
                Api api = XStreamUtil.readValue(apiXml, Api.class);
                if(api.getRequest().getParameters() != null) {
                    List<Parameter> parameterList = api.getRequest().getParameters().getParameterList();
                    for (Parameter histParameter : parameterList) {
                        if(parameters.containsKey(histParameter.getCode())) {
                            Parameter inputParameter = parameters.get(histParameter.getCode());
                            inputParameter.mergeHist(histParameter);
                        }else {
                            parameters.put(histParameter.getCode(), histParameter);
                            histParameter.setOper("4");
                        }
                    }
                }

                if(api.getRequest().getNode() != null) {
                    List<Node> nodeList = api.getRequest().getNode().getNodeList();
                    for (Node histNode : nodeList) {
                        if(request.containsKey(histNode.getPath())) {
                            Node inputNode = request.get(histNode.getPath());
                            inputNode.mergeHist(histNode);
                        }else {
                            request.put(histNode.getPath(), histNode);
                            histNode.setOper("4");
                        }
                    }
                }

                if(api.getResponse().getNode() != null) {
                    List<Node> nodeList = api.getResponse().getNode().getNodeList();
                    for (Node histNode : nodeList) {
                        if(response.containsKey(histNode.getPath())) {
                            Node inputNode = response.get(histNode.getPath());
                            inputNode.mergeHist(histNode);
                        }else if(histNode.getPath().endsWith("{}")) {
                            String origPath = histNode.getPath().substring(0, histNode.getPath().length() - 2);
                            for (String tempKey : new HashSet<>(response.keySet())) {
                                if(tempKey.startsWith(origPath + "/") || tempKey.equals(origPath)){
                                    response.remove(tempKey);
                                }
                            }
                        }else{
                            response.put(histNode.getPath(), histNode);
                            histNode.setOper("4");
                        }
                    }
                }
            }

            return ResultData.success(new HashMap<String, Object>(){{
                put("api", thirdApi);
                put("parameter", parameters.values());
                put("request", request.values());
                put("response", response.values());
            }});
        }catch (Exception e) {
            e.printStackTrace();
            throw new SmartHandlerException(APIErrorType.SERVER_ERROR);
        }
    }


    @RequestMapping("/initApiTesting.json")
    @ResponseBody
    public ResultData initApiTesting(Long domainId, final Long apiId){
        try {
            ThirdDomain thirdDomain = thirdDomainSV.getThirdDomainByPK(domainId);
            final ThirdApi thirdApi = thirdApiSV.getThirdApiByPK(apiId);
            String url = "", request = "", response = "", kvContainers = "";
            if(thirdApi != null && StringUtils.isNotBlank(thirdApi.getContent())) {
                Api api = XStreamUtil.readValue(thirdApi.getContent(), Api.class);
                url = thirdDomain.getUrl() + "/" + thirdApi.getPath() + "?" +
                        (api.getExample() == null || api.getExample().getParameters() == null ? "" : api.getExample().getParameters().trim());
                request = api.getExample() == null? "" : api.getExample().getRequest();
                response = api.getExample() == null? "" :  api.getExample().getResponse();
//                if(api.getResponse().getNode() != null) {
//                    List<Node> nodeList = api.getResponse().getNode().getNodeList();
//                    for (Node node : nodeList) {
//                        if(node.getPath().endsWith("{}")){
//                            kvContainers += (node.getTempPath() + ",");
//                        }
//                    }
//                }
            }else {
                url = thirdDomain.getUrl() + "/" + thirdApi.getPath();
            }

            final String finalUrl = url.replace("/\\", "\\");
            final String finalRequest = request != null ? request.trim(): null;
            final String finalResponse = response != null ? response.trim(): null;
//            final String finalKvContainers = kvContainers;
            return ResultData.success(new HashMap<String, Object>(){{
                put("url", finalUrl);
                put("method", thirdApi.getMethod());
                put("request", finalRequest);
                put("response", finalResponse);
//                put("kvContainers", finalKvContainers);
            }});
        }catch (Exception e) {
            e.printStackTrace();
            throw new SmartHandlerException(APIErrorType.SERVER_ERROR);
        }
    }

    @RequestMapping("/saveCommonParameter.json")
    @ResponseBody
    public ResultData saveCommonParameter(String type, String path, String name, Long domainId){
        try {
            ThirdCommonParameter parameter = new ThirdCommonParameter();
            parameter.setDomainId(domainId);
            parameter.setType(Byte.valueOf(type));
            parameter.setPath(path);
            parameter.setName(name);
            parameter.setDomainId(domainId);
            ControllerHelper.setDefaultValue(parameter, "id");
            thirdCommonParameterSV.create(parameter);
            return ResultData.success();
        }catch (Exception e) {
            e.printStackTrace();
            throw new SmartHandlerException(APIErrorType.SERVER_ERROR);
        }
    }


    @RequestMapping("/addPubParameter.json")
    @ResponseBody
    public ResultData addPubParameter(Long domainId, final String parameter, String body){
        try {

            ThirdPubRequest_Example example = new ThirdPubRequest_Example();
            example.createCriteria().andDomainIdEqualTo(domainId);
            List<ThirdPubRequest> pubRequestList = pubRequestSV.getThirdPubRequestListByExample(example);

            ThirdDomainParameter_Example domainParameterExample = new ThirdDomainParameter_Example();
            domainParameterExample.createCriteria().andDomainIdEqualTo(domainId);
            List<ThirdDomainParameter> domainParameterList = domainParameterSV.getThirdDomainParameterListByExample(domainParameterExample);

            Map<String, String> allFixVal = new HashMap<>();
            for (ThirdDomainParameter domainParameter : domainParameterList) {
                allFixVal.put(domainParameter.getCode(), domainParameter.getValue());
            }

            Map<String, Object> parameterMap = getUrlParameterObjects("?" + parameter);

            final List<Map<String, String>> parameterList = new ArrayList<>();
            for (ThirdPubRequest thirdPubRequest : pubRequestList) {
                String code = thirdPubRequest.getPath();
                String name = thirdPubRequest.getName();
                String value = thirdPubRequest.getValue();
                if(code.startsWith("/")){
                    body = addBody(body, code.substring(1), parseVal(code, value, allFixVal, parameterMap, body, domainId));
                }else {
                    Map row = new HashMap();
                    row.put("code", code);
                    row.put("name", name);
                    row.put("value", parseVal(code, value, allFixVal, parameterMap, body, domainId));
                    parameterList.add(row);
                    parameterMap.put(code, String.valueOf(row.get("value")));
                }
            }
            final String finalBody = body;
            return ResultData.success(new HashMap<String, Object>(){{
                put("parameters", parameterList);
                put("body", finalBody);
            }});
        }catch (Exception e) {
            e.printStackTrace();
            throw new SmartHandlerException(APIErrorType.SERVER_ERROR);
        }
    }

    private String addBody(String body, String code, Object value) throws IOException {
        if(body.startsWith("<") && body.endsWith(">")) {
            Document document = Dom4jUtils.getDocumentByContent(body);
            if(document.getRootElement().element(code) != null){
                document.getRootElement().element(code).setText(String.valueOf(value));
            }else {
                DefaultElement newElement = new DefaultElement(code);
                newElement.setText(String.valueOf(value));
                document.getRootElement().add(newElement);
            }


            OutputFormat formater = OutputFormat.createPrettyPrint();
            formater.setEncoding("UTF-8");
            StringWriter out=new StringWriter();
            XMLWriter writer=new XMLWriter(out,formater);
            writer.write(document.getRootElement());
            writer.close();

            return out.toString().trim();
        }else if(body.startsWith("{") && body.endsWith("}")) {
            JSONObject jsonObject = JSONObject.parseObject(body);
            jsonObject.put(code, value);
            return jsonObject.toJSONString();
        }
        return body + code + "|" + value;
    }

    public Object parseVal(String code, String valueExp, Map<String, String> allFixVal,
                           Map<String, Object> parameterMap, String body, Long domainId) throws Exception {
        if(valueExp.matches("#val\\{[a-zA-Z_0-9\\-]+\\}")){
            String parameterName = valueExp.substring(5, valueExp.length() -1).trim();
            return allFixVal.get(parameterName);
        }else if(valueExp.matches("#rule\\{[a-zA-Z_0-9\\-]+\\}")){

            String ruleName = valueExp.substring(6, valueExp.length() -1).trim();
            ThirdPublicRule_Example ruleExample = new ThirdPublicRule_Example();
            ruleExample.createCriteria().andCodeEqualTo(ruleName).andDomainIdEqualTo(domainId);
            List<ThirdPublicRule> ruleList = ruleSV.getThirdPublicRuleListByExample(ruleExample);
            String ruleExpr = ruleList.get(0).getExpression();

            return ThirdApiExecutor.executeRule(code, ruleExpr, parameterMap, null, body, allFixVal);
        }else if(StringUtils.isNotBlank(valueExp)){
            throw new RuntimeException("unsupport config [" + valueExp + "] !");
        }else {
            return "";
        }
    }

}
