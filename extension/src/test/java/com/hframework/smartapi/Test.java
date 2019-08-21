package com.hframework.smartapi;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.hframework.beans.class0.XmlNode;
import com.hframework.common.util.message.Dom4jUtils;
import com.hframework.generator.BeanGeneratorUtil;
import com.hframework.generator.bean.DefaultGenerateDescriptor;
import org.dom4j.Element;
import org.dom4j.Node;
import org.springframework.beans.BeanUtils;

import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class Test {

    private String json = "{\n" +
            "  \"error_code\" : 0,\n" +
            "  \"error_msg\" : \"\",\n" +
            "  \"data\" : {\n" +
            "    \"n\" : \"3560\",\n" +
            "    \"id\" : \"4000060\",\n" +
            "    \"name\" : \"山楂\",\n" +
            "    \"system\" : \"S\",\n" +
            "    \"company\" : \"c1543994131\",\n" +
            "    \"subcompany\" : \"s1543994538\",\n" +
            "    \"dept\" : \"d1550823401\",\n" +
            "    \"team\" : \"t1550823418\",\n" +
            "    \"usera\" : \"\",\n" +
            "    \"org\" : \"\",\n" +
            "    \"userb\" : \"\",\n" +
            "    \"level\" : \"usera\",\n" +
            "    \"upper_id\" : \"t1550823418\",\n" +
            "    \"childs\" : [ \"4000060v\" ],\n" +
            "    \"code\" : \"CJQ29S\",\n" +
            "    \"groupid\" : \"263\",\n" +
            "    \"row_prefix\" : \"36\",\n" +
            "    \"short_alias\" : \"\",\n" +
            "    \"weight\" : \"1\",\n" +
            "    \"remark\" : \"\",\n" +
            "    \"ctime\" : \"1550823564\",\n" +
            "    \"mtime\" : \"2019-02-28 11:37:29\",\n" +
            "    \"parents\" : [ {\n" +
            "      \"id\" : \"S\",\n" +
            "      \"level\" : \"system\",\n" +
            "      \"name\" : \"system\"\n" +
            "    }, {\n" +
            "      \"id\" : \"c1543994131\",\n" +
            "      \"level\" : \"company\",\n" +
            "      \"name\" : \"一片新气象总公司\"\n" +
            "    }, {\n" +
            "      \"id\" : \"s1543994538\",\n" +
            "      \"level\" : \"subcompany\",\n" +
            "      \"name\" : \"分公司一\"\n" +
            "    }, {\n" +
            "      \"id\" : \"d1550823401\",\n" +
            "      \"level\" : \"dept\",\n" +
            "      \"name\" : \"222\"\n" +
            "    }, {\n" +
            "      \"id\" : \"t1550823418\",\n" +
            "      \"level\" : \"team\",\n" +
            "      \"name\" : \"山楂\"\n" +
            "    }, {\n" +
            "      \"id\" : \"4000060\",\n" +
            "      \"level\" : \"usera\",\n" +
            "      \"name\" : \"山楂\"\n" +
            "    } ],\n" +
            "    \"secondids\" : [ ],\n" +
            "    \"manager\" : [ ],\n" +
            "    \"secondid_list\" : { },\n" +
            "    \"app_role\" : \"usera\",\n" +
            "    \"auth\" : [ \"customer_avatar\", \"customer_name\", \"customer_phone\", \"customer_search\" ],\n" +
            "    \"path\" : \"/system/一片新气象总公司/分公司一/222/山楂/山楂\"\n" +
            "  }\n" +
            "}";

    @org.junit.Test
    public void test(){
        String xml = "<xml><user>张三</user><age>19</age><children><a>1</a></children></xml>";
        Map<String, Object> result = new LinkedHashMap<>();
        Element rootElement = Dom4jUtils.getDocumentByContent(xml).getRootElement();
        for (int i = 0; i < rootElement.nodeCount(); i++) {
            Node node = rootElement.node(i);
            String text = node.getText();
            result.put(node.getName(), text);


        }
        System.out.println(result);
    }

    @org.junit.Test
    public void test_rule_data() throws IOException {
        Map<String, Object> flatMap = BeanGeneratorUtil.getFlatMap(BeanGeneratorUtil.getXmlNodeByJson(new DefaultGenerateDescriptor(), json), "");
        System.out.println(flatMap);
//        System.out.println(parseNode(JSONObject.parseObject(json), "/"));


    }



}
