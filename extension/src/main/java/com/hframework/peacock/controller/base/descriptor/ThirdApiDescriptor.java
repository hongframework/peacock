package com.hframework.peacock.controller.base.descriptor;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.hframework.peacock.controller.bean.apiconf.Api;
import com.hframework.peacock.controller.bean.apiconf.Node;
import com.hframework.peacock.controller.bean.apiconf.Parameters;
import com.hframework.smartweb.bean.ApiConf;
import com.hframework.smartweb.bean.apiconf.Handler;
import com.hframework.smartweb.bean.apiconf.Parameter;
import com.hframework.smartweb.bean.apiconf.Result;
import com.hframework.peacock.controller.base.NodeMeta;
import com.hframework.peacock.controller.bean.apiconf.Api;
import com.hframework.peacock.controller.bean.apiconf.Node;
import com.hframework.peacock.controller.bean.apiconf.Parameters;
import org.apache.commons.lang3.StringUtils;

import java.util.*;

/**
 * Created by zhangquanhong on 2017/11/15.
 */
public class ThirdApiDescriptor {

    private static final RequestBodyDescriptor DEFAULT_EMPTY_REQUEST_DESCRIPTOR  = new RequestBodyDescriptor(new ArrayList<Node>());
    private static final ResponseBodyDescriptor DEFAULT_EMPTY_RESPONSE_DESCRIPTOR  = new ResponseBodyDescriptor(new ArrayList<Node>());


    private Long domainId;
    private Long apiId;
    private Long apiType;
    private String name ;
    private String path;
    private String method;
    private String tags;
    private String content;

    private Api apiConf;

    private RequestType requestType;
    private ResponseType responseType;

    private Map<String, ThirdParameterDescriptor> inputParameterDescriptors = new LinkedHashMap<>();
    private Map<String, ThirdParameterDescriptor> ruleParameterDescriptors = new LinkedHashMap<>();
    private RequestBodyDescriptor requestBodyDescriptor = DEFAULT_EMPTY_REQUEST_DESCRIPTOR;
    private ResponseBodyDescriptor responseBodyDescriptor = DEFAULT_EMPTY_RESPONSE_DESCRIPTOR;

    private Set<Node> responseRuleNodes = new HashSet<>();

    public ThirdApiDescriptor() {
    }

    public ThirdApiDescriptor(Long domainId, Long apiId, Long apiType, String name, String path, String method,
                              String tags, String content, Api apiConf, RequestType requestType, ResponseType responseType) {
        this.domainId = domainId;
        this.apiId = apiId;
        this.apiType = apiType;
        this.name = name;
        this.path = path;
        this.method = method;
        this.tags = tags;
        this.content = content;
        this.apiConf = apiConf;
        this.requestType = requestType;
        this.responseType = responseType;
    }

    public void initialize() throws ClassNotFoundException {

        Parameters parameters = apiConf.getRequest().getParameters();
        if(parameters != null && parameters.getParameterList() != null) {
            for (com.hframework.peacock.controller.bean.apiconf.Parameter parameter : parameters.getParameterList()) {
                ThirdParameterDescriptor parameterDescriptor = new ThirdParameterDescriptor(parameter);
                if(parameterDescriptor.isInput()){
                    inputParameterDescriptors.put(parameter.getCode(), parameterDescriptor);
                }else {
                    ruleParameterDescriptors.put(parameter.getCode(), parameterDescriptor);
                }
            }
        }
        Node requestNode = apiConf.getRequest().getNode();
        if(requestNode != null && requestNode.getNodeList() != null && requestNode.getNodeList().size() > 0) {
            requestBodyDescriptor = new RequestBodyDescriptor(requestNode.getNodeList());
        }

        Node responseNode = apiConf.getResponse().getNode();
        if(responseNode != null && responseNode.getNodeList() != null && responseNode.getNodeList().size() > 0) {
            responseBodyDescriptor = new ResponseBodyDescriptor(responseNode.getNodeList());
            for (Node node : responseNode.getNodeList()) {
                if(StringUtils.isNotBlank(node.getBatchHelper())) {
                    responseRuleNodes.add(node);
                }
            }
        }

    }

    public Long getDomainId() {
        return domainId;
    }

    public void setDomainId(Long domainId) {
        this.domainId = domainId;
    }

    public Long getApiId() {
        return apiId;
    }

    public void setApiId(Long apiId) {
        this.apiId = apiId;
    }

    public Long getApiType() {
        return apiType;
    }

    public void setApiType(Long apiType) {
        this.apiType = apiType;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getTags() {
        return tags;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Api getApiConf() {
        return apiConf;
    }

    public void setApiConf(Api apiConf) {
        this.apiConf = apiConf;
    }

    public RequestType getRequestType() {
        return requestType;
    }

    public void setRequestType(RequestType requestType) {
        this.requestType = requestType;
    }

    public ResponseType getResponseType() {
        return responseType;
    }

    public void setResponseType(ResponseType responseType) {
        this.responseType = responseType;
    }

    public static enum RequestType{
        PARAMETERS, //仅参数
        XML_BODY, //XML报文体
        JSON_BODY, //JSON报文体
        TXT_BODY, //TXT报文体
        PARAMETERS_XML_BODY, //参数 & XML报文体
        PARAMETERS_JSON_BODY, //参数 & JSON报文体
        PARAMETERS_TXT_BODY; //参数 & TXT报文体
        public static RequestType parseFrom(String typeStr){
            Set<String> typeItems = new HashSet<>(Arrays.asList(typeStr.split(",")));
            boolean hasParameter = typeItems.contains("kv");
            boolean hasXmlBody = typeItems.contains("xml");
            boolean hasJsonBody = typeItems.contains("json");
            boolean hasTxtBody = typeItems.contains("txt");
            if(hasParameter && hasXmlBody) return PARAMETERS_XML_BODY;
            else if(hasParameter && hasJsonBody) return PARAMETERS_JSON_BODY;
            else if(hasParameter && hasTxtBody) return PARAMETERS_TXT_BODY;
            else if(hasXmlBody) return XML_BODY;
            else if(hasJsonBody) return JSON_BODY;
            else if(hasTxtBody) return TXT_BODY;
            else if(hasParameter) return PARAMETERS;
            else throw new RuntimeException("parse third api request type [" + typeStr + "] failed !");
        }

        public boolean hasXmlBody(){
            return this.equals(XML_BODY) || this.equals(PARAMETERS_XML_BODY);
        }
        public boolean hasJsonBody(){
            return this.equals(JSON_BODY) || this.equals(PARAMETERS_JSON_BODY);
        }
        public boolean hasTxtBody(){
            return this.equals(TXT_BODY) || this.equals(PARAMETERS_TXT_BODY);
        }
    }

    public static enum ResponseType{
        XML_BODY, //XML报文体
        JSON_BODY, //JSON报文体
        TXT_BODY; //TXT报文体
        public static ResponseType parseFrom(String typeStr){
            Set<String> typeItems = new HashSet<>(Arrays.asList(typeStr.split(",")));
            boolean hasXmlBody = typeItems.contains("xml");
            boolean hasJsonBody = typeItems.contains("json");
            boolean hasTxtBody = typeItems.contains("txt");
            if(hasXmlBody) return XML_BODY;
            else if(hasJsonBody) return JSON_BODY;
            else if(hasTxtBody) return TXT_BODY;
            else throw new RuntimeException("parse third api response type [" + typeStr + "] failed !");
        }
    }

    public Map<String, ThirdParameterDescriptor> getInputParameterDescriptors() {
        return inputParameterDescriptors;
    }

    public Map<String, ThirdParameterDescriptor> getRuleParameterDescriptors() {
        return ruleParameterDescriptors;
    }

    public Map<String, ThirdNodeDescriptor> getInputNodeDescriptors(){
        return requestBodyDescriptor.getInputNodeDescriptors();
    }

    public Map<String, ThirdNodeDescriptor> getRuleNodeDescriptors(){
        return requestBodyDescriptor.getRuleNodeDescriptors();
    }

    public RequestBodyDescriptor getRequestBodyDescriptor() {
        return requestBodyDescriptor;
    }

    public ResponseBodyDescriptor getResponseBodyDescriptor() {
        return responseBodyDescriptor;
    }

    public boolean isGetMethod(){
        return "get".equals(this.method);
    }

    public Set<Node> getResponseRuleNodes() {
        return responseRuleNodes;
    }
}




