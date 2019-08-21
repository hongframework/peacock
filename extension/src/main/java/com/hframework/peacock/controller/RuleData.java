package com.hframework.peacock.controller;

import com.alibaba.fastjson.JSONObject;
import com.google.common.base.Joiner;
import com.hframework.common.util.StringUtils;
import com.hframework.common.util.collect.CollectionUtils;
import com.hframework.common.util.collect.bean.Fetcher;
import com.hframework.common.util.message.Dom4jUtils;
import com.hframework.common.util.security.MD5Util;
import com.hframework.generator.BeanGeneratorUtil;
import com.hframework.generator.bean.DefaultGenerateDescriptor;
import org.dom4j.Element;
import org.dom4j.Node;
import org.dom4j.tree.DefaultElement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;

import java.io.IOException;
import java.util.*;

public class RuleData {
    private static final Logger logger = LoggerFactory.getLogger(RuleData.class);

    private Object data;

    public String getString(){
        return String.valueOf(data);
    }
    public Map<String, Object> getMap(){
        return (Map<String, Object>) data;
    }

    public static RuleData load(Object data){
        RuleData resultData = new RuleData();
        resultData.data = data;
        return resultData;
    }

    public static RuleData loadBody(String body){
        if(body.startsWith("<") && body.endsWith(">")) {
            return loadXml(body, "");
        }else if(body.startsWith("{") && body.endsWith("}")) {
            return loadJson(body);
        }else {
            return null;
        }
    }

    public static Map<String, Object> loadBodyToMap(String body, String kvContainersStr) throws IOException {
        if(body.startsWith("<") && body.endsWith(">")) {
            return BeanGeneratorUtil.getFlatMap(BeanGeneratorUtil.getXmlNodeByXml(new DefaultGenerateDescriptor(), body), kvContainersStr);
        }else if(body.startsWith("{") && body.endsWith("}")) {
            return BeanGeneratorUtil.getFlatMap(BeanGeneratorUtil.getXmlNodeByJson(new DefaultGenerateDescriptor(), body), kvContainersStr);
        }else {
            return null;
        }
    }

    public static RuleData loadJson(String json) {
        RuleData resultData = new RuleData();
        resultData.data = JSONObject.parse(json);
        return resultData;
    }

    public static RuleData loadXml(String body, String filterCode) {
        Map<String, Object> result = new LinkedHashMap<>();
        Element rootElement = Dom4jUtils.getDocumentByContent(body.trim()).getRootElement();
        for (int i = 0; i < rootElement.nodeCount(); i++) {
            Node node = rootElement.node(i);
            if(node instanceof DefaultElement){
                String text = node.getText();
                if(!node.getName().equals(filterCode)) {
                    result.put(node.getName(), text);
                }
            }
        }
        RuleData resultData = new RuleData();
        resultData.data = result;
        return resultData;
    }

    public void filterValue(String parameter) {
        if("isDefined".equals(parameter.trim())){
            Map<String, Object> map = getMap();
            Iterator<Map.Entry<String, Object>> iterator = map.entrySet().iterator();
            while (iterator.hasNext()) {
                Map.Entry<String, Object> next = iterator.next();
                if(next.getValue() !=  null){
                    if(next.getValue() instanceof String && StringUtils.isBlank((String)next.getValue())) {
                        iterator.remove();
                    }
                }else iterator.remove();
            }
        }
    }

    public void sortByKey() {
        Map<String, Object> sortData = new LinkedHashMap<>();
        Map<String, Object> data = getMap();
        ArrayList<String> keyList = new ArrayList<>(data.keySet());
        Collections.sort(keyList);
        for (String key : keyList) {
            sortData.put(key, data.get(key));
        }
        this.data = sortData;

    }

    public void entryJoin(final String parameter) {
        Map<String, Object> map = getMap();
        List<String> result = CollectionUtils.fetch(new ArrayList<>(map.entrySet()), new Fetcher<Map.Entry<String, Object>, String>() {
            @Override
            public String fetch(Map.Entry<String, Object> stringObjectEntry) {
                return stringObjectEntry.getKey() + parameter + stringObjectEntry.getValue();
            }
        });
        this.data = result;
    }

    public void join(String parameter) {
        this.data = Joiner.on(parameter).join((List)data);
    }

    public void concat(String parameter) {
        this.data = getString().concat(parameter);
    }

    public void md5() {
        String before = String.valueOf(data);
        data = MD5Util.encrypt(before);
        logger.info("md5 origin : " + before + "; target : " + data);
    }

    public void upperCase(){
        data = String.valueOf(data).toUpperCase();
    }

    public Object getVal() {
        if(BeanUtils.isSimpleProperty(data.getClass())){
            return data;
        }else {
            return String.valueOf(data);
        }
    }
}
