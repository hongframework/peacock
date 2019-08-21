package com.hframework.smartapi.handler;

import com.hframework.smartapi.CommonSuite;
import com.hframework.smartweb.bean.ApiConf;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;
import com.hframework.peacock.controller.base.ApiConfigureRegistry;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.apache.commons.lang3.StringUtils;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * Created by zhangquanhong on 2017/11/28.
 */
public class HandlerConfigureRegistrySuite extends CommonSuite {

    @BeforeClass
    public static void init_context() throws Exception {
        new SmartHandlerFactorySuite().init_classpath_handler();
    }

    @Test
    public void initHandlerConfigureRegistry() {
        ApiConfigureRegistry handlerRegistry = ApiConfigureRegistry.getHandlerInstance();
        handlerRegistry.snapshot();
    }

    @Test
    public void test(){
        String content = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                "\n" +
                "<api>\n" +
                "  <parameters>\n" +
                "    <parameter name=\"custIds\" description=\"客户ID数组\" type=\"int[]\" required=\"true\"/>\n" +
                "    <parameter name=\"pageNo\" description=\"页编号\" type=\"int\" min=\"1\" default=\"1\"/>\n" +
                "    <parameter name=\"pageSize\" description=\"页大小\" type=\"int\" min=\"1\" max=\"1000\" default=\"20\"/>\n" +
                "  </parameters>\n" +
                "  <prehandler/>\n" +
                "  <handlers>\n" +
                "    <handler class=\"com.hframework.peacock.handler.base.database.SqlQueryMoreHandler\" path=\"/base/sql_query_list\" version=\"1.0.0\" returnType=\"Array\">\n" +
                "      <parameter name=\"dbKey\" description=\"数据库\" scope=\"parameter\" default=\"${DATASOURCE.DBS}\">\n" +
                "        <value>firstb2b</value>\n" +
                "      </parameter>\n" +
                "      <parameter name=\"sql\" description=\"SQL语句\" scope=\"parameter\">\n" +
                "        <value>SELECT a.loan_user_id AS userId, c.deal_type AS dealType, a.deal_id, a.deal_loan_id AS dealLoadId, SUM(a.money) AS totalRepayMoney, SUM(CASE WHEN a.type IN (1,3,8) THEN a.money ELSE 0 END) AS principalRepayMoney, SUM(CASE WHEN a.type NOT IN (1,3,8) THEN a.money ELSE 0 END) AS profitRepayMoney, COUNT(DISTINCT a.deal_loan_id) AS totalRepayCnt, a.time AS repayTime, c.name AS dealName, c.rate AS rate, c.loantype AS loanType, dlt.name AS dealLoanType, c.repay_time AS dealRepayTime,GROUP_CONCAT(DISTINCT dtag.tag_id ORDER BY dtag.tag_id SEPARATOR ',') tagIds FROM firstb2b_deal_loan_repay AS a LEFT JOIN firstb2b_deal_load AS b ON a.deal_loan_id = b.id LEFT JOIN firstb2b_deal AS c ON a.deal_id = c.id LEFT JOIN firstb2b_deal_loan_type AS dlt ON dlt.id = c.type_id LEFT JOIN firstb2b_deal_tag dtag ON a.deal_id = dtag.deal_id WHERE a.status = 1 AND a.loan_user_id IN(${custIds}) AND a.time &gt; 0 AND a.type IN(1,2,3,4,7,8,9) GROUP BY a.loan_user_id, a.deal_id, a.time ORDER BY a.real_time desc, a.time LIMIT ${(pageNo-1)*pageSize}, ${pageSize}</value>\n" +
                "      </parameter>\n" +
                "      <parameter name=\"queryFields\" description=\"查询字段\" default=\"${HANDLER.CONFIG.PARAMETERS}\">\n" +
                "        <value>${HANDLER.CONFIG.PARAMETERS}</value>\n" +
                "      </parameter>\n" +
                "      <parameter name=\"parameterValues\" description=\"查询值\" default=\"${PARAMETERS.VALUES.ARRAY}\">\n" +
                "        <value>${PARAMETERS.VALUES.ARRAY}</value>\n" +
                "      </parameter>\n" +
                "      <result name=\"userId\" description=\"投标用户id\" type=\"int\"/>\n" +
                "      <result name=\"dealType\" description=\"标的类型\" type=\"int\"/>\n" +
                "      <result name=\"deal_id\" description=\"订单id\" type=\"int\"/>\n" +
                "      <result name=\"dealLoadId\" description=\"投资ID\" type=\"int\"/>\n" +
                "      <result name=\"totalRepayMoney\" description=\"回款金额\" type=\"float\"/>\n" +
                "      <result name=\"principalRepayMoney\" description=\"本金\" type=\"float\"/>\n" +
                "      <result name=\"profitRepayMoney\" description=\"利息\" type=\"float\"/>\n" +
                "      <result name=\"totalRepayCnt\" description=\"回款数\" type=\"int\"/>\n" +
                "      <result name=\"repayTime\" description=\"回款时间\" type=\"int\"/>\n" +
                "      <result name=\"dealName\" description=\"回款标的\" type=\"string\"/>\n" +
                "      <result name=\"rate\" description=\"借款利率\" type=\"float\"/>\n" +
                "      <result name=\"loanType\" description=\" 还款方式\" type=\"int\"/>\n" +
                "      <result name=\"dealLoanType\" description=\"借款类型\" type=\"string\"/>\n" +
                "      <result name=\"dealRepayTime\" description=\"标的回款时间\" type=\"int\"/>\n" +
                "      <result name=\"tagIds\" description=\"标的TagIds\" type=\"string\" formatter=\"com.hframework.smartweb.bean.formatter.DictionaryContainFormatter\" pattern=\"70:随鑫约;71:随鑫约;72:智多鑫;null:专享\"/>\n" +
                "    </handler>\n" +
                "  </handlers>\n" +
                "  <result-view>\n" +
                "  </result-view>\n" +
                "</api>\n";
        ApiConf apiConf = readValue(content, ApiConf.class);
        System.out.println(apiConf);
    }

    public <T> T readValue(String content, Class<T> valueType) {
        XStream xstream = new XStream(new DomDriver());
        xstream.processAnnotations(valueType);
        xstream.aliasSystemAttribute("BEAN_CLASS", "class");
//        System.out.println("====>" + content);
        return (T) xstream.fromXML(content);
    }
}
