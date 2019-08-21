package com.hframework.smartapi;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.clearspring.analytics.util.Lists;
import com.hframework.peacock.controller.base.NodeData;
import com.hframework.peacock.controller.base.NodeMeta;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class NodeDataNodeMetaSuite {

    @Test
    public void test_build_node_meta(){
        NodeMeta nodeMeta = build_node_meta();
        for (String path : nodeMeta.getPathCache().keySet()) {
            System.out.println(path);
        }
    }

    @Test
    public void test_tree_value(){
        NodeData nodeData = NodeData.parseXml(xml);
        nodeData.build(build_node_meta());
        System.out.println(new JSONObject((Map<String, Object>) nodeData.getSchema("/")).toJSONString());
        System.out.println(new JSONObject((Map<String, Object>) nodeData.getSchema("/data")).toJSONString());
        System.out.println(new JSONObject((Map<String, Object>) nodeData.getSchema("/data/info")).toJSONString());
        System.out.println(new JSONArray((List<Object>) nodeData.getSchema("/data/info/parents[]")).toJSONString());
        System.out.println(new JSONArray(Lists.newArrayList((Iterable<Object>) nodeData.getSchema("/data/groups{}"))).toJSONString());
    }

    @Test
    public void build_node_data_by_platMap(){
        NodeData nodeData = NodeData.parseJson(json);
        nodeData.build(build_node_meta());
        Map<String, Object> map = nodeData.toMap();
        nodeData = NodeData.parseByPathMap(map);
        nodeData.build(build_node_meta());
        for (String path : nodeData.getPathCache().keySet()) {
            System.out.println(path);
        }
        System.out.println(nodeData.toJson().toJSONString());
        System.out.println(nodeData.toXml().asXML());
        System.out.println(new JSONObject(nodeData.toMap()).toJSONString());
    }

    @Test
    public void build_node_data_by_xml(){
        NodeData nodeData = NodeData.parseXml(xml);
        nodeData.build(build_node_meta());
        for (String path : nodeData.getPathCache().keySet()) {
            System.out.println(path);
        }
        System.out.println(nodeData.toJson().toJSONString());
        System.out.println(nodeData.toXml().asXML());
        System.out.println(new JSONObject(nodeData.toMap()).toJSONString());
    }

    @Test
    public void build_node_data_by_json(){
        NodeData nodeData = NodeData.parseJson(json);
        nodeData.build(build_node_meta());
        for (String path : nodeData.getPathCache().keySet()) {
            System.out.println(path);
        }
        System.out.println(nodeData.toJson().toJSONString());
        System.out.println(nodeData.toXml().asXML());
        System.out.println(new JSONObject(nodeData.toMap()).toJSONString());
    }

    public NodeMeta build_node_meta(){
        return NodeMeta.getInstanceByPaths(Arrays.asList(template));
    }



    public static String[] template = new String[]{
            "/error_code",
            "/error_msg",
            "/data",
            "/data/info",
            "/data/info/n",
            "/data/info/id",
            "/data/info/name",
            "/data/info/system",
            "/data/info/company",
            "/data/info/subcompany",
            "/data/info/dept",
            "/data/info/team",
            "/data/info/usera",
            "/data/info/org",
            "/data/info/userb",
            "/data/info/level",
            "/data/info/upper_id",
            "/data/info/childs[]",
            "/data/info/code",
            "/data/info/groupid",
            "/data/info/row_prefix",
            "/data/info/short_alias",
            "/data/info/weight",
            "/data/info/remark",
            "/data/info/ctime",
            "/data/info/mtime",
            "/data/info/parents[]",
            "/data/info/parents[]/id",
            "/data/info/parents[]/level",
            "/data/info/parents[]/name",
            "/data/info/secondids[]",
            "/data/info/manager[]",
            "/data/info/secondid_list_chart",
            "/data/info/userb_num",
            "/data/info/usera_num",
            "/data/info/reg_num",
            "/data/info/invest_num",
            "/data/info/current_invest_num",
            "/data/info/invest_money",
            "/data/info/invest_year_money",
            "/data/info/invest_count",
            "/data/info/firstinvest_num",
            "/data/info/firstinvest_money",
            "/data/info/firstinvest_year_money",
            "/data/info/referer_rebate_money",
            "/data/info/pay_money",
            "/data/info/carry_money",
            "/data/info/loan_repay_money",
            "/data/info/real_repay_money",
            "/data/info/gold_amount",
            "/data/info/gold_invest_money",
            "/data/info/gold_invest_year_money",
            "/data/info/goldc_amount",
            "/data/info/goldc_invest_money",
            "/data/info/goldc_invest_year_money",
            "/data/info/gold_invest_num",
            "/data/info/gold_loan_amount",
            "/data/info/gold_repay_amount",
            "/data/info/gold_cash_amount",
            "/data/info/xinbao_partake",
            "/data/info/xinli",
            "/data/info/xinbao",
            "/data/info/carry_num",
            "/data/info/invest_money_format",
            "/data/info/invest_money_unit",
            "/data/info/invest_year_money_format",
            "/data/info/invest_year_money_unit",
            "/data/info/offer_total",
            "/data/info/is_leaf",
            "/data/info/rowkey",
            "/data/info/date",
            "/data/info/next_updatetime",
            "/data/info/total_invest_num",
            "/data/info/total_current_invest_num",
            "/data/info/total_gold_invest_num",
            "/data/info/total_xinbao_partake",
            "/data/info/custom_num",
            "/data/info/app_role",
            "/data/info/auth[]",
            "/data/info/secondid_list",
            "/data/invest",
            "/data/invest/reg_num",
            "/data/invest/invest_num",
            "/data/invest/current_invest_num",
            "/data/invest/invest_money",
            "/data/invest/invest_year_money",
            "/data/invest/invest_count",
            "/data/invest/firstinvest_num",
            "/data/invest/firstinvest_money",
            "/data/invest/firstinvest_year_money",
            "/data/invest/referer_rebate_money",
            "/data/invest/pay_money",
            "/data/invest/carry_money",
            "/data/invest/loan_repay_money",
            "/data/invest/real_repay_money",
            "/data/invest/gold_amount",
            "/data/invest/gold_invest_money",
            "/data/invest/gold_invest_year_money",
            "/data/invest/goldc_amount",
            "/data/invest/goldc_invest_money",
            "/data/invest/goldc_invest_year_money",
            "/data/invest/gold_invest_num",
            "/data/invest/gold_loan_amount",
            "/data/invest/gold_repay_amount",
            "/data/invest/gold_cash_amount",
            "/data/invest/xinbao_partake",
            "/data/invest/xinli",
            "/data/invest/xinbao",
            "/data/invest/carry_num",
            "/data/invest/invest_money_format",
            "/data/invest/invest_money_unit",
            "/data/invest/invest_year_money_format",
            "/data/invest/invest_year_money_unit",
            "/data/invest/offer_total",
            "/data/invest/is_leaf",
            "/data/invest/id",
            "/data/invest/rowkey",
            "/data/invest/name",
            "/data/invest/date",
            "/data/invest/mtime",
            "/data/invest/next_updatetime",
            "/data/invest/ctime",
            "/data/invest/total_invest_num",
            "/data/invest/total_current_invest_num",
            "/data/invest/total_gold_invest_num",
            "/data/invest/total_xinbao_partake",
            "/data/invest/custom_num",
            "/data/groups{}",
            "/data/groups{}/_key_",
            "/data/groups{}/start_day",
            "/data/groups{}/end_day",
            "/data/groups{}/is_leaf",
            "/data/groups{}/reg_num",
            "/data/groups{}/ctime",
            "/data/groups{}/invest_num",
            "/data/groups{}/current_invest_num",
            "/data/groups{}/invest_money",
            "/data/groups{}/invest_year_money",
            "/data/groups{}/invest_count",
            "/data/groups{}/firstinvest_num",
            "/data/groups{}/firstinvest_money",
            "/data/groups{}/firstinvest_year_money",
            "/data/groups{}/referer_rebate_money",
            "/data/groups{}/pay_money",
            "/data/groups{}/carry_money",
            "/data/groups{}/loan_repay_money",
            "/data/groups{}/real_repay_money",
            "/data/groups{}/gold_amount",
            "/data/groups{}/gold_invest_money",
            "/data/groups{}/gold_invest_year_money",
            "/data/groups{}/goldc_amount",
            "/data/groups{}/goldc_invest_money",
            "/data/groups{}/goldc_invest_year_money",
            "/data/groups{}/gold_invest_num",
            "/data/groups{}/gold_loan_amount",
            "/data/groups{}/gold_repay_amount",
            "/data/groups{}/gold_cash_amount",
            "/data/groups{}/xinbao_partake",
            "/data/groups{}/xinli",
            "/data/groups{}/xinbao",
            "/data/groups{}/carry_num",
            "/data/groups{}/user_status",
            "/data/groups{}/offer_date[]",
            "/data/groups{}/name",
            "/data/groups{}/id",
            "/data/groups{}/code",
            "/data/groups{}/level",
            "/data/groups{}/groupid",
            "/data/groups{}/company",
            "/data/groups{}/weight",
            "/data/groups{}/userb_num",
            "/data/groups{}/custom_num",
            "/data/groups{}/invest_money_format",
            "/data/groups{}/invest_money_unit",
            "/data/groups{}/invest_year_money_format",
            "/data/groups{}/invest_year_money_unit"
    };

    public static String xml = "<xml>\n" +
            "    <error_code>0</error_code>\n" +
            "    <error_msg>111</error_msg>\n" +
            "    <data>\n" +
            "        <info>\n" +
            "            <n>3560</n>\n" +
            "            <id>4000060</id>\n" +
            "            <name>山楂</name>\n" +
            "            <system>S</system>\n" +
            "            <company>c1543994131</company>\n" +
            "            <subcompany>s1543994538</subcompany>\n" +
            "            <dept>d1550823401</dept>\n" +
            "            <team>t1550823418</team>\n" +
            "            <usera></usera>\n" +
            "            <org></org>\n" +
            "            <userb></userb>\n" +
            "            <level>usera</level>\n" +
            "            <upper_id>t1550823418</upper_id>\n" +
            "            <childs>4000060v</childs>\n" +
            "            <code>CJQ29S</code>\n" +
            "            <groupid>263</groupid>\n" +
            "            <row_prefix>36</row_prefix>\n" +
            "            <short_alias></short_alias>\n" +
            "            <weight>1</weight>\n" +
            "            <remark></remark>\n" +
            "            <ctime>1550823564</ctime>\n" +
            "            <mtime>2019-02-28 11:37:29</mtime>\n" +
            "            <parents>\n" +
            "                <id>S</id>\n" +
            "                <level>system</level>\n" +
            "                <name>system</name>\n" +
            "            </parents>\n" +
            "            <parents>\n" +
            "                <id>c1543994131</id>\n" +
            "                <level>company</level>\n" +
            "                <name>一片新气象总公司</name>\n" +
            "            </parents>\n" +
            "            <parents>\n" +
            "                <id>s1543994538</id>\n" +
            "                <level>subcompany</level>\n" +
            "                <name>分公司一</name>\n" +
            "            </parents>\n" +
            "            <parents>\n" +
            "                <id>d1550823401</id>\n" +
            "                <level>dept</level>\n" +
            "                <name>222</name>\n" +
            "            </parents>\n" +
            "            <parents>\n" +
            "                <id>t1550823418</id>\n" +
            "                <level>team</level>\n" +
            "                <name>山楂</name>\n" +
            "            </parents>\n" +
            "            <parents>\n" +
            "                <id>4000060</id>\n" +
            "                <level>usera</level>\n" +
            "                <name>山楂</name>\n" +
            "            </parents>\n" +
            "            <secondid_list_chart>4000060</secondid_list_chart>\n" +
            "            <userb_num>0</userb_num>\n" +
            "            <usera_num>1</usera_num>\n" +
            "            <reg_num>0</reg_num>\n" +
            "            <invest_num>0</invest_num>\n" +
            "            <current_invest_num>0</current_invest_num>\n" +
            "            <invest_money>0</invest_money>\n" +
            "            <invest_year_money>0</invest_year_money>\n" +
            "            <invest_count/>\n" +
            "            <firstinvest_num>0</firstinvest_num>\n" +
            "            <firstinvest_money>0</firstinvest_money>\n" +
            "            <firstinvest_year_money>0</firstinvest_year_money>\n" +
            "            <referer_rebate_money>0</referer_rebate_money>\n" +
            "            <pay_money>0</pay_money>\n" +
            "            <carry_money>0</carry_money>\n" +
            "            <loan_repay_money>0</loan_repay_money>\n" +
            "            <real_repay_money>0</real_repay_money>\n" +
            "            <gold_amount>0.000</gold_amount>\n" +
            "            <gold_invest_money>0</gold_invest_money>\n" +
            "            <gold_invest_year_money>0</gold_invest_year_money>\n" +
            "            <goldc_amount>0.000</goldc_amount>\n" +
            "            <goldc_invest_money>0</goldc_invest_money>\n" +
            "            <goldc_invest_year_money>0</goldc_invest_year_money>\n" +
            "            <gold_invest_num>0</gold_invest_num>\n" +
            "            <gold_loan_amount>0.000</gold_loan_amount>\n" +
            "            <gold_repay_amount>0.000</gold_repay_amount>\n" +
            "            <gold_cash_amount>0.000</gold_cash_amount>\n" +
            "            <xinbao_partake>0</xinbao_partake>\n" +
            "            <xinli>0</xinli>\n" +
            "            <xinbao>0.000</xinbao>\n" +
            "            <carry_num>0</carry_num>\n" +
            "            <invest_money_format>0.00</invest_money_format>\n" +
            "            <invest_money_unit>元</invest_money_unit>\n" +
            "            <invest_year_money_format>0.00</invest_year_money_format>\n" +
            "            <invest_year_money_unit>元</invest_year_money_unit>\n" +
            "            <offer_total>1</offer_total>\n" +
            "            <is_leaf>0</is_leaf>\n" +
            "            <rowkey></rowkey>\n" +
            "            <date></date>\n" +
            "            <next_updatetime>1551696376</next_updatetime>\n" +
            "            <total_invest_num>0</total_invest_num>\n" +
            "            <total_current_invest_num>0</total_current_invest_num>\n" +
            "            <total_gold_invest_num>0</total_gold_invest_num>\n" +
            "            <total_xinbao_partake>0</total_xinbao_partake>\n" +
            "            <custom_num>2</custom_num>\n" +
            "            <app_role>usera</app_role>\n" +
            "            <auth>customer_avatar</auth>\n" +
            "            <auth>customer_name</auth>\n" +
            "            <auth>customer_phone</auth>\n" +
            "            <auth>customer_search</auth>\n" +
            "            <secondid_list/>\n" +
            "        </info>\n" +
            "        <invest>\n" +
            "            <reg_num>0</reg_num>\n" +
            "            <invest_num>0</invest_num>\n" +
            "            <current_invest_num>0</current_invest_num>\n" +
            "            <invest_money>0</invest_money>\n" +
            "            <invest_year_money>0</invest_year_money>\n" +
            "            <invest_count/>\n" +
            "            <firstinvest_num>0</firstinvest_num>\n" +
            "            <firstinvest_money>0</firstinvest_money>\n" +
            "            <firstinvest_year_money>0</firstinvest_year_money>\n" +
            "            <referer_rebate_money>0</referer_rebate_money>\n" +
            "            <pay_money>0</pay_money>\n" +
            "            <carry_money>0</carry_money>\n" +
            "            <loan_repay_money>0</loan_repay_money>\n" +
            "            <real_repay_money>0</real_repay_money>\n" +
            "            <gold_amount>0.000</gold_amount>\n" +
            "            <gold_invest_money>0</gold_invest_money>\n" +
            "            <gold_invest_year_money>0</gold_invest_year_money>\n" +
            "            <goldc_amount>0.000</goldc_amount>\n" +
            "            <goldc_invest_money>0</goldc_invest_money>\n" +
            "            <goldc_invest_year_money>0</goldc_invest_year_money>\n" +
            "            <gold_invest_num>0</gold_invest_num>\n" +
            "            <gold_loan_amount>0.000</gold_loan_amount>\n" +
            "            <gold_repay_amount>0.000</gold_repay_amount>\n" +
            "            <gold_cash_amount>0.000</gold_cash_amount>\n" +
            "            <xinbao_partake>0</xinbao_partake>\n" +
            "            <xinli>0</xinli>\n" +
            "            <xinbao>0.000</xinbao>\n" +
            "            <carry_num>0</carry_num>\n" +
            "            <invest_money_format>0.00</invest_money_format>\n" +
            "            <invest_money_unit>元</invest_money_unit>\n" +
            "            <invest_year_money_format>0.00</invest_year_money_format>\n" +
            "            <invest_year_money_unit>元</invest_year_money_unit>\n" +
            "            <offer_total>1</offer_total>\n" +
            "            <is_leaf>0</is_leaf>\n" +
            "            <id>4000060</id>\n" +
            "            <rowkey></rowkey>\n" +
            "            <name>山楂</name>\n" +
            "            <date></date>\n" +
            "            <mtime>2019-02-28 11:37:29</mtime>\n" +
            "            <next_updatetime>1551696376</next_updatetime>\n" +
            "            <ctime>1551692776</ctime>\n" +
            "            <total_invest_num>0</total_invest_num>\n" +
            "            <total_current_invest_num>0</total_current_invest_num>\n" +
            "            <total_gold_invest_num>0</total_gold_invest_num>\n" +
            "            <total_xinbao_partake>0</total_xinbao_partake>\n" +
            "            <custom_num>2</custom_num>\n" +
            "        </invest>\n" +
            "        <groups>\n" +
            "            <a4000060v>\n" +
            "                <start_day>2019-03-05</start_day>\n" +
            "                <end_day>2019-03-05</end_day>\n" +
            "                <is_leaf>0</is_leaf>\n" +
            "                <reg_num>0</reg_num>\n" +
            "                <ctime>0</ctime>\n" +
            "                <invest_num>0</invest_num>\n" +
            "                <current_invest_num>0</current_invest_num>\n" +
            "                <invest_money>0</invest_money>\n" +
            "                <invest_year_money>0</invest_year_money>\n" +
            "                <invest_count>0</invest_count>\n" +
            "                <firstinvest_num>0</firstinvest_num>\n" +
            "                <firstinvest_money>0.00</firstinvest_money>\n" +
            "                <firstinvest_year_money>0.00</firstinvest_year_money>\n" +
            "                <referer_rebate_money>0</referer_rebate_money>\n" +
            "                <pay_money>0</pay_money>\n" +
            "                <carry_money>0</carry_money>\n" +
            "                <loan_repay_money>0</loan_repay_money>\n" +
            "                <real_repay_money>0</real_repay_money>\n" +
            "                <gold_amount>0.000</gold_amount>\n" +
            "                <gold_invest_money>0</gold_invest_money>\n" +
            "                <gold_invest_year_money>0</gold_invest_year_money>\n" +
            "                <goldc_amount>0.000</goldc_amount>\n" +
            "                <goldc_invest_money>0</goldc_invest_money>\n" +
            "                <goldc_invest_year_money>0</goldc_invest_year_money>\n" +
            "                <gold_invest_num>0</gold_invest_num>\n" +
            "                <gold_loan_amount>0.000</gold_loan_amount>\n" +
            "                <gold_repay_amount>0.000</gold_repay_amount>\n" +
            "                <gold_cash_amount>0.000</gold_cash_amount>\n" +
            "                <xinbao_partake>0</xinbao_partake>\n" +
            "                <xinli>0</xinli>\n" +
            "                <xinbao>0.000</xinbao>\n" +
            "                <carry_num>0</carry_num>\n" +
            "                <user_status>EXIST</user_status>\n" +
            "                <name>兼职理财师组</name>\n" +
            "                <id>4000060v</id>\n" +
            "                <code>4000060v</code>\n" +
            "                <level>org</level>\n" +
            "                <groupid>0</groupid>\n" +
            "                <company>c1543994131</company>\n" +
            "                <weight>1</weight>\n" +
            "                <userb_num>0</userb_num>\n" +
            "                <custom_num>0</custom_num>\n" +
            "                <invest_money_format>0.00</invest_money_format>\n" +
            "                <invest_money_unit>元</invest_money_unit>\n" +
            "                <invest_year_money_format>0.00</invest_year_money_format>\n" +
            "                <invest_year_money_unit>元</invest_year_money_unit>\n" +
            "            </a4000060v>\n" +
            "            <userc>\n" +
            "                <start_day>2019-03-05</start_day>\n" +
            "                <end_day>2019-03-05</end_day>\n" +
            "                <is_leaf>1</is_leaf>\n" +
            "                <reg_num>0</reg_num>\n" +
            "                <ctime>0</ctime>\n" +
            "                <invest_num>0</invest_num>\n" +
            "                <current_invest_num>0</current_invest_num>\n" +
            "                <invest_money>0</invest_money>\n" +
            "                <invest_year_money>0</invest_year_money>\n" +
            "                <invest_count/>\n" +
            "                <firstinvest_num>0</firstinvest_num>\n" +
            "                <firstinvest_money>0</firstinvest_money>\n" +
            "                <firstinvest_year_money>0</firstinvest_year_money>\n" +
            "                <referer_rebate_money>0</referer_rebate_money>\n" +
            "                <pay_money>0</pay_money>\n" +
            "                <carry_money>0</carry_money>\n" +
            "                <loan_repay_money>0</loan_repay_money>\n" +
            "                <real_repay_money>0</real_repay_money>\n" +
            "                <gold_amount>0.000</gold_amount>\n" +
            "                <gold_invest_money>0</gold_invest_money>\n" +
            "                <gold_invest_year_money>0</gold_invest_year_money>\n" +
            "                <goldc_amount>0.000</goldc_amount>\n" +
            "                <goldc_invest_money>0</goldc_invest_money>\n" +
            "                <goldc_invest_year_money>0</goldc_invest_year_money>\n" +
            "                <gold_invest_num>0</gold_invest_num>\n" +
            "                <gold_loan_amount>0.000</gold_loan_amount>\n" +
            "                <gold_repay_amount>0.000</gold_repay_amount>\n" +
            "                <gold_cash_amount>0.000</gold_cash_amount>\n" +
            "                <xinbao_partake>0</xinbao_partake>\n" +
            "                <xinli>0</xinli>\n" +
            "                <xinbao>0.000</xinbao>\n" +
            "                <carry_num>0</carry_num>\n" +
            "                <user_status>EXIST</user_status>\n" +
            "                <name>山楂</name>\n" +
            "                <id>userc</id>\n" +
            "                <code>4000060</code>\n" +
            "                <level>userc</level>\n" +
            "                <groupid/>\n" +
            "                <company/>\n" +
            "                <weight>1</weight>\n" +
            "                <custom_num>2</custom_num>\n" +
            "                <invest_money_format>0.00</invest_money_format>\n" +
            "                <invest_money_unit>元</invest_money_unit>\n" +
            "                <invest_year_money_format>0.00</invest_year_money_format>\n" +
            "                <invest_year_money_unit>元</invest_year_money_unit>\n" +
            "            </userc>\n" +
            "        </groups>\n" +
            "    </data>\n" +
            "</xml>\n";

    public static String json = "{\n" +
            "  \"error_code\" : 0,\n" +
            "  \"error_msg\" : \"1111\",\n" +
            "  \"data\" : {\n" +
            "    \"info\" : {\n" +
            "      \"n\" : \"3560\",\n" +
            "      \"id\" : \"4000060\",\n" +
            "      \"name\" : \"山楂\",\n" +
            "      \"system\" : \"S\",\n" +
            "      \"company\" : \"c1543994131\",\n" +
            "      \"subcompany\" : \"s1543994538\",\n" +
            "      \"dept\" : \"d1550823401\",\n" +
            "      \"team\" : \"t1550823418\",\n" +
            "      \"usera\" : \"\",\n" +
            "      \"org\" : \"\",\n" +
            "      \"userb\" : \"\",\n" +
            "      \"level\" : \"usera\",\n" +
            "      \"upper_id\" : \"t1550823418\",\n" +
            "      \"childs\" : [ \"4000060v\" ],\n" +
            "      \"code\" : \"CJQ29S\",\n" +
            "      \"groupid\" : \"263\",\n" +
            "      \"row_prefix\" : \"36\",\n" +
            "      \"short_alias\" : \"\",\n" +
            "      \"weight\" : \"1\",\n" +
            "      \"remark\" : \"\",\n" +
            "      \"ctime\" : \"1550823564\",\n" +
            "      \"mtime\" : \"2019-02-28 11:37:29\",\n" +
            "      \"parents\" : [ {\n" +
            "        \"id\" : \"S\",\n" +
            "        \"level\" : \"system\",\n" +
            "        \"name\" : \"system\"\n" +
            "      }, {\n" +
            "        \"id\" : \"c1543994131\",\n" +
            "        \"level\" : \"company\",\n" +
            "        \"name\" : \"一片新气象总公司\"\n" +
            "      }, {\n" +
            "        \"id\" : \"s1543994538\",\n" +
            "        \"level\" : \"subcompany\",\n" +
            "        \"name\" : \"分公司一\"\n" +
            "      }, {\n" +
            "        \"id\" : \"d1550823401\",\n" +
            "        \"level\" : \"dept\",\n" +
            "        \"name\" : \"222\"\n" +
            "      }, {\n" +
            "        \"id\" : \"t1550823418\",\n" +
            "        \"level\" : \"team\",\n" +
            "        \"name\" : \"山楂\"\n" +
            "      }, {\n" +
            "        \"id\" : \"4000060\",\n" +
            "        \"level\" : \"usera\",\n" +
            "        \"name\" : \"山楂\"\n" +
            "      } ],\n" +
            "      \"secondids\" : [ ],\n" +
            "      \"manager\" : [ ],\n" +
            "      \"secondid_list_chart\" : \"4000060\",\n" +
            "      \"userb_num\" : 0,\n" +
            "      \"usera_num\" : 1,\n" +
            "      \"reg_num\" : 0,\n" +
            "      \"invest_num\" : 0,\n" +
            "      \"current_invest_num\" : 0,\n" +
            "      \"invest_money\" : 0,\n" +
            "      \"invest_year_money\" : 0,\n" +
            "      \"invest_count\" : null,\n" +
            "      \"firstinvest_num\" : 0,\n" +
            "      \"firstinvest_money\" : 0,\n" +
            "      \"firstinvest_year_money\" : 0,\n" +
            "      \"referer_rebate_money\" : 0,\n" +
            "      \"pay_money\" : 0,\n" +
            "      \"carry_money\" : 0,\n" +
            "      \"loan_repay_money\" : 0,\n" +
            "      \"real_repay_money\" : 0,\n" +
            "      \"gold_amount\" : \"0.000\",\n" +
            "      \"gold_invest_money\" : 0,\n" +
            "      \"gold_invest_year_money\" : 0,\n" +
            "      \"goldc_amount\" : \"0.000\",\n" +
            "      \"goldc_invest_money\" : 0,\n" +
            "      \"goldc_invest_year_money\" : 0,\n" +
            "      \"gold_invest_num\" : 0,\n" +
            "      \"gold_loan_amount\" : \"0.000\",\n" +
            "      \"gold_repay_amount\" : \"0.000\",\n" +
            "      \"gold_cash_amount\" : \"0.000\",\n" +
            "      \"xinbao_partake\" : 0,\n" +
            "      \"xinli\" : 0,\n" +
            "      \"xinbao\" : \"0.000\",\n" +
            "      \"carry_num\" : 0,\n" +
            "      \"invest_money_format\" : \"0.00\",\n" +
            "      \"invest_money_unit\" : \"元\",\n" +
            "      \"invest_year_money_format\" : \"0.00\",\n" +
            "      \"invest_year_money_unit\" : \"元\",\n" +
            "      \"offer_total\" : \"1\",\n" +
            "      \"is_leaf\" : 0,\n" +
            "      \"rowkey\" : \"\",\n" +
            "      \"date\" : \"\",\n" +
            "      \"next_updatetime\" : 1551696376,\n" +
            "      \"total_invest_num\" : 0,\n" +
            "      \"total_current_invest_num\" : 0,\n" +
            "      \"total_gold_invest_num\" : 0,\n" +
            "      \"total_xinbao_partake\" : 0,\n" +
            "      \"custom_num\" : 2,\n" +
            "      \"app_role\" : \"usera\",\n" +
            "      \"auth\" : [ \"customer_avatar\", \"customer_name\", \"customer_phone\", \"customer_search\" ],\n" +
            "      \"secondid_list\" : { }\n" +
            "    },\n" +
            "    \"invest\" : {\n" +
            "      \"reg_num\" : 0,\n" +
            "      \"invest_num\" : 0,\n" +
            "      \"current_invest_num\" : 0,\n" +
            "      \"invest_money\" : 0,\n" +
            "      \"invest_year_money\" : 0,\n" +
            "      \"invest_count\" : null,\n" +
            "      \"firstinvest_num\" : 0,\n" +
            "      \"firstinvest_money\" : 0,\n" +
            "      \"firstinvest_year_money\" : 0,\n" +
            "      \"referer_rebate_money\" : 0,\n" +
            "      \"pay_money\" : 0,\n" +
            "      \"carry_money\" : 0,\n" +
            "      \"loan_repay_money\" : 0,\n" +
            "      \"real_repay_money\" : 0,\n" +
            "      \"gold_amount\" : \"0.000\",\n" +
            "      \"gold_invest_money\" : 0,\n" +
            "      \"gold_invest_year_money\" : 0,\n" +
            "      \"goldc_amount\" : \"0.000\",\n" +
            "      \"goldc_invest_money\" : 0,\n" +
            "      \"goldc_invest_year_money\" : 0,\n" +
            "      \"gold_invest_num\" : 0,\n" +
            "      \"gold_loan_amount\" : \"0.000\",\n" +
            "      \"gold_repay_amount\" : \"0.000\",\n" +
            "      \"gold_cash_amount\" : \"0.000\",\n" +
            "      \"xinbao_partake\" : 0,\n" +
            "      \"xinli\" : 0,\n" +
            "      \"xinbao\" : \"0.000\",\n" +
            "      \"carry_num\" : 0,\n" +
            "      \"invest_money_format\" : \"0.00\",\n" +
            "      \"invest_money_unit\" : \"元\",\n" +
            "      \"invest_year_money_format\" : \"0.00\",\n" +
            "      \"invest_year_money_unit\" : \"元\",\n" +
            "      \"offer_total\" : \"1\",\n" +
            "      \"is_leaf\" : 0,\n" +
            "      \"id\" : \"4000060\",\n" +
            "      \"rowkey\" : \"\",\n" +
            "      \"name\" : \"山楂\",\n" +
            "      \"date\" : \"\",\n" +
            "      \"mtime\" : \"2019-02-28 11:37:29\",\n" +
            "      \"next_updatetime\" : 1551696376,\n" +
            "      \"ctime\" : 1551692776,\n" +
            "      \"total_invest_num\" : 0,\n" +
            "      \"total_current_invest_num\" : 0,\n" +
            "      \"total_gold_invest_num\" : 0,\n" +
            "      \"total_xinbao_partake\" : 0,\n" +
            "      \"custom_num\" : 2\n" +
            "    },\n" +
            "    \"groups\" : {\n" +
            "      \"4000060v\" : {\n" +
            "        \"start_day\" : \"2019-03-05\",\n" +
            "        \"end_day\" : \"2019-03-05\",\n" +
            "        \"is_leaf\" : 0,\n" +
            "        \"reg_num\" : 0,\n" +
            "        \"ctime\" : 0,\n" +
            "        \"invest_num\" : 0,\n" +
            "        \"current_invest_num\" : 0,\n" +
            "        \"invest_money\" : 0,\n" +
            "        \"invest_year_money\" : 0,\n" +
            "        \"invest_count\" : \"0\",\n" +
            "        \"firstinvest_num\" : 0,\n" +
            "        \"firstinvest_money\" : \"0.00\",\n" +
            "        \"firstinvest_year_money\" : \"0.00\",\n" +
            "        \"referer_rebate_money\" : 0,\n" +
            "        \"pay_money\" : 0,\n" +
            "        \"carry_money\" : 0,\n" +
            "        \"loan_repay_money\" : 0,\n" +
            "        \"real_repay_money\" : 0,\n" +
            "        \"gold_amount\" : \"0.000\",\n" +
            "        \"gold_invest_money\" : 0,\n" +
            "        \"gold_invest_year_money\" : 0,\n" +
            "        \"goldc_amount\" : \"0.000\",\n" +
            "        \"goldc_invest_money\" : 0,\n" +
            "        \"goldc_invest_year_money\" : 0,\n" +
            "        \"gold_invest_num\" : 0,\n" +
            "        \"gold_loan_amount\" : \"0.000\",\n" +
            "        \"gold_repay_amount\" : \"0.000\",\n" +
            "        \"gold_cash_amount\" : \"0.000\",\n" +
            "        \"xinbao_partake\" : 0,\n" +
            "        \"xinli\" : 0,\n" +
            "        \"xinbao\" : \"0.000\",\n" +
            "        \"carry_num\" : 0,\n" +
            "        \"user_status\" : \"EXIST\",\n" +
            "        \"offer_date\" : [ ],\n" +
            "        \"name\" : \"兼职理财师组\",\n" +
            "        \"id\" : \"4000060v\",\n" +
            "        \"code\" : \"4000060v\",\n" +
            "        \"level\" : \"org\",\n" +
            "        \"groupid\" : \"0\",\n" +
            "        \"company\" : \"c1543994131\",\n" +
            "        \"weight\" : \"1\",\n" +
            "        \"userb_num\" : 0,\n" +
            "        \"custom_num\" : 0,\n" +
            "        \"invest_money_format\" : \"0.00\",\n" +
            "        \"invest_money_unit\" : \"元\",\n" +
            "        \"invest_year_money_format\" : \"0.00\",\n" +
            "        \"invest_year_money_unit\" : \"元\"\n" +
            "      },\n" +
            "      \"userc\" : {\n" +
            "        \"start_day\" : \"2019-03-05\",\n" +
            "        \"end_day\" : \"2019-03-05\",\n" +
            "        \"is_leaf\" : 1,\n" +
            "        \"reg_num\" : 0,\n" +
            "        \"ctime\" : 0,\n" +
            "        \"invest_num\" : 0,\n" +
            "        \"current_invest_num\" : 0,\n" +
            "        \"invest_money\" : 0,\n" +
            "        \"invest_year_money\" : 0,\n" +
            "        \"invest_count\" : null,\n" +
            "        \"firstinvest_num\" : 0,\n" +
            "        \"firstinvest_money\" : 0,\n" +
            "        \"firstinvest_year_money\" : 0,\n" +
            "        \"referer_rebate_money\" : 0,\n" +
            "        \"pay_money\" : 0,\n" +
            "        \"carry_money\" : 0,\n" +
            "        \"loan_repay_money\" : 0,\n" +
            "        \"real_repay_money\" : 0,\n" +
            "        \"gold_amount\" : \"0.000\",\n" +
            "        \"gold_invest_money\" : 0,\n" +
            "        \"gold_invest_year_money\" : 0,\n" +
            "        \"goldc_amount\" : \"0.000\",\n" +
            "        \"goldc_invest_money\" : 0,\n" +
            "        \"goldc_invest_year_money\" : 0,\n" +
            "        \"gold_invest_num\" : 0,\n" +
            "        \"gold_loan_amount\" : \"0.000\",\n" +
            "        \"gold_repay_amount\" : \"0.000\",\n" +
            "        \"gold_cash_amount\" : \"0.000\",\n" +
            "        \"xinbao_partake\" : 0,\n" +
            "        \"xinli\" : 0,\n" +
            "        \"xinbao\" : \"0.000\",\n" +
            "        \"carry_num\" : 0,\n" +
            "        \"user_status\" : \"EXIST\",\n" +
            "        \"offer_date\" : [ ],\n" +
            "        \"name\" : \"山楂\",\n" +
            "        \"id\" : \"userc\",\n" +
            "        \"code\" : \"4000060\",\n" +
            "        \"level\" : \"userc\",\n" +
            "        \"groupid\" : null,\n" +
            "        \"company\" : null,\n" +
            "        \"weight\" : 1,\n" +
            "        \"custom_num\" : 2,\n" +
            "        \"invest_money_format\" : \"0.00\",\n" +
            "        \"invest_money_unit\" : \"元\",\n" +
            "        \"invest_year_money_format\" : \"0.00\",\n" +
            "        \"invest_year_money_unit\" : \"元\"\n" +
            "      }\n" +
            "    }\n" +
            "  }\n" +
            "}";

}
