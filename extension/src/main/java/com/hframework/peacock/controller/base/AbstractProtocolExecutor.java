package com.hframework.peacock.controller.base;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.hframework.beans.exceptions.BusinessException;
import com.hframework.common.util.message.Dom4jUtils;
import com.hframework.peacock.controller.base.descriptor.ThirdApiDescriptor;
import org.dom4j.dom.DOMElement;
import org.w3c.dom.NodeList;

import java.util.HashMap;
import java.util.Map;

public abstract class AbstractProtocolExecutor {

    protected ThirdApiDescriptor apiDescriptor;
    protected ThirdApiConfigureRegistry registry;
    protected String domain;

    public AbstractProtocolExecutor(ThirdApiDescriptor apiDescriptor, ThirdApiConfigureRegistry registry) {
        this.apiDescriptor = apiDescriptor;
        this.registry = registry;
        domain = registry.getDomain(getDomainId());
    }


    /**
     * 协议按照descriptor进行API调用
     * @param parameters
     * @param nodes
     * @return
     */
    public abstract String execute(Map<String, Object> parameters, Map<String, Object> nodes) throws Exception;

    public Long getDomainId(){
        return apiDescriptor.getDomainId();
    }

    public static String getFinalUrl(String patternUrl, Map<String, String> parameterMap) {
        String finalUrl = patternUrl;
        if(finalUrl.contains("?")) {
            finalUrl = finalUrl.substring(0,finalUrl.indexOf("?")) + "?";
        }else {
            finalUrl = finalUrl + "?";
        }
        for (Map.Entry<String, String> entry : parameterMap.entrySet()) {
            finalUrl+= entry.getKey() + "=" + entry.getValue() + "&";
        }

        if(finalUrl.endsWith("&")) {
            finalUrl = finalUrl.substring(0,finalUrl.length() - 1);
        }
        return finalUrl;
    }

    protected String getJson(Map<String, Object> httpInvokeBodyNodes) {
        JSONObject result = new JSONObject();
        for (String path : httpInvokeBodyNodes.keySet()) {
            Object value = httpInvokeBodyNodes.get(path);
            JSONObject tempObject = result;
            String[] items = (path.startsWith("/")? path.substring(1):path).split("/");
            for (int i = 0; i < items.length; i++) {
                String item = items[i];
                String itemCode = item.replaceAll("\\[\\d*\\]|\\{\\d*\\}", "");
                int arrayIndex = getArrayIndex(item.substring(itemCode.length()));

                if(arrayIndex == -1) {//值为单例
                    if(i == items.length - 1) {//叶子节点
                        tempObject.put(itemCode, value);
                    }else{//非叶子节点
                        if(!tempObject.containsKey(itemCode)){
                            tempObject.put(itemCode, new JSONObject());
                        }
                        tempObject = tempObject.getJSONObject(itemCode);
                    }
                }else {//值为数组
                    if(!tempObject.containsKey(itemCode)) {
                        tempObject.put(itemCode,  new JSONArray());
                    }
                    JSONArray array = tempObject.getJSONArray(itemCode);
                    if(i == items.length - 1) {//叶子节点
                        array.add(value);
                    }else {
                        if(array.size() == arrayIndex) {
                            array.add(new JSONObject());
                        }else if(array.size() < arrayIndex) {
                            throw new BusinessException("KV to JSON array index not continuous (miss:" + array.size() + ")!");
                        }
                        tempObject = array.getJSONObject(arrayIndex);
                    }
                }
            }
        }
        return result.toJSONString();
    }

    protected String getXml(Map<String, Object> httpInvokeBodyNodes) {
        Map blankMap = new HashMap();
        DOMElement result = Dom4jUtils.createElement("xml", blankMap);
        for (String path : httpInvokeBodyNodes.keySet()) {
            Object value = httpInvokeBodyNodes.get(path);
            DOMElement tempObject = result;
            String[] items = (path.startsWith("/")? path.substring(1):path).split("/");
            for (int i = 0; i < items.length; i++) {
                String item = items[i];
                String itemCode = item.replaceAll("\\[\\d*\\]|\\{\\d*\\}", "");
                int arrayIndex = getArrayIndex(item.substring(itemCode.length()));
                if(arrayIndex == -1) arrayIndex = 0;

                if(i == items.length - 1) {//如果是叶子节点，直接在上级append该节点即可
                    DOMElement itemElement = Dom4jUtils.createElement(itemCode, blankMap);
                    itemElement.addText(String.valueOf(value));
                    tempObject.add(itemElement);
                }else {//如果非叶子节点
                    NodeList childNodes = tempObject.getElementsByTagName(itemCode);
                    if(childNodes.getLength() == arrayIndex) {
                        DOMElement newChild = Dom4jUtils.createElement(itemCode, blankMap);
                        tempObject.add(newChild);
                        tempObject = newChild;
                    }else if(childNodes.getLength() <  arrayIndex) {
                        throw new BusinessException("KV to XML array index not continuous (miss:" + childNodes.getLength() + ")!");
                    }else {
                        tempObject = (DOMElement) childNodes.item(arrayIndex);
                    }
                }
            }
        }
        return result.asXML();
    }


    private int getArrayIndex(String info) {
        if(info.matches("\\[\\d+\\]")) {
            return Integer.parseInt(info.substring(1, info.length() - 1));
        }else if(info.matches("\\[\\]")) {
            return 0;
        }else {
            return -1;
        }
    }

    public static Map<String, String> getStringMapByObjMap(Map<String, Object> parameters){
        Map<String, String> stringParameters = new HashMap<>();
        for (String key : parameters.keySet()) {
            stringParameters.put(key, parameters.get(key) != null ? String.valueOf(parameters.get(key)) : null);
        }
        return stringParameters;
    }

    public static Map<String, Object> getObjMapByStringMap(Map<String, String> parameters){
        Map<String, Object> stringParameters = new HashMap<>();
        for (String key : parameters.keySet()) {
            stringParameters.put(key, parameters.get(key));
        }
        return stringParameters;
    }
}
