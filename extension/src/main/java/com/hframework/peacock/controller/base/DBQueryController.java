package com.hframework.peacock.controller.base;

import com.alibaba.fastjson.JSONObject;
import com.hframework.beans.exceptions.BusinessException;
import com.hframework.common.frame.cache.CacheFactory;
import com.hframework.common.util.DateUtils;
import com.hframework.common.util.message.PropertyReader;
import com.hframework.peacock.parser.ConfigurationParser;
import com.hframework.peacock.parser.ExpressionConfigurationParser;
import com.hframework.smartsql.client2.DBClient;
import com.hframework.smartweb.annotation.SmartApi;
import com.hframework.smartweb.annotation.SmartHolder;
import com.hframework.smartweb.annotation.SmartParameter;
import com.hframework.smartweb.annotation.SmartResult;
import com.hframework.smartweb.bean.SmartMap;
import com.hframework.smartweb.bean.checker.GenericAuthChecker;
import com.hframework.peacock.parser.ConfigurationParser;
import com.hframework.peacock.parser.ExpressionConfigurationParser;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;

import java.util.Calendar;
import java.util.Map;

/**
 * Created by zhangquanhong on 2017/8/10.
 */
@Controller
@SmartApi("/base")
public class DBQueryController {
    private static final Logger logger = LoggerFactory.getLogger(DBQueryController.class);

    private static final String BAIZE_LOG = "baize.log";
    private static final String BAIZE_LOG_JDBC_URL = "baize.log.jdbc.url";
    private static final String BAIZE_LOG_JDBC_USER = "baize.log.jdbc.user";
    private static final String BAIZE_LOG_JDBC_PASSWORD = "baize.log.jdbc.password";

    private static final String PEACOCK_DATA = "peacock.data";
    private static final String PEACOCK_DATA_JDBC_URL = "peacock.data.jdbc.url";
    private static final String PEACOCK_DATA_JDBC_USER = "peacock.data.jdbc.user";
    private static final String PEACOCK_DATA_JDBC_PASSWORD = "peacock.data.jdbc.password";

    private static final String DEFAULT_JDBC_URL = "jdbc.url";
    private static final String DEFAULT_JDBC_USER = "jdbc.user";
    private static final String DEFAULT_JDBC_PASSWORD = "jdbc.password";

    private static final String BAIZE_REAL_TIME = "baize.realTime";
    private static final String BAIZE_REAL_TIME_JDBC_URL = "baize.realTime.jdbc.url";
    private static final String BAIZE_REAL_TIME_JDBC_USER = "baize.realTime.jdbc.user";
    private static final String BAIZE_REAL_TIME_JDBC_PASSWORD = "baize.realTime.jdbc.password";

    private static PropertyReader propertyReader =
            PropertyReader.read("properties/dataSource.properties");
    static {
        DBClient.registerDatabase(PEACOCK_DATA, propertyReader.get(PEACOCK_DATA_JDBC_URL),
                propertyReader.get(PEACOCK_DATA_JDBC_USER), propertyReader.get(PEACOCK_DATA_JDBC_PASSWORD));
        DBClient.registerDatabase(BAIZE_LOG, propertyReader.get(BAIZE_LOG_JDBC_URL),
                propertyReader.get(BAIZE_LOG_JDBC_USER), propertyReader.get(BAIZE_LOG_JDBC_PASSWORD));
        DBClient.registerDatabase("default", propertyReader.get(DEFAULT_JDBC_URL),
                propertyReader.get(DEFAULT_JDBC_USER), propertyReader.get(DEFAULT_JDBC_PASSWORD));

        DBClient.registerDatabase(BAIZE_REAL_TIME, propertyReader.get(BAIZE_REAL_TIME_JDBC_URL),
                propertyReader.get(BAIZE_REAL_TIME_JDBC_USER), propertyReader.get(BAIZE_REAL_TIME_JDBC_PASSWORD));
        DBClient.setCurrentDatabaseKey("default");
    }


    //http://localhost:8190/api/base/baize/user_preference_recommend?clientId=50003&timestamp=1461139244&sign=8E625E9C7B4CA5644201DDD6190B2E70&userId=10&strategy=long
    @SmartApi(path = "/baize/user_preference_recommend", version = "1.0.0", name = "用户投资偏好推荐查询", description = "根据用户ID查询对应策略下投资偏好的期限与起投金额",checker = GenericAuthChecker.class, owners = "2")
    @SmartResult
    public SmartMap userPreferenceRecommendQuery(
            @SmartParameter(required = true, description = "用户ID") Long userId,
            @SmartParameter(required = true, description = "策略类型", optionJson = "{'balance':'余额策略'," +
                    "'long':'长期策略','short':'短期策略','high':'最高策略','low':'最低策略','best':'最优策略'}") String strategy,
            @SmartHolder(name = "sql", required = true, parser = ConfigurationParser.class) String sql,
            @SmartHolder(name = "transform", required = true, parser = ConfigurationParser.class) String mapperJson,
            @SmartHolder(name = "abtest", required = true, parser = ExpressionConfigurationParser.class) String passed,
            @SmartHolder(name = "out-abtest", required = true, parser = ExpressionConfigurationParser.class) String outPassed){


        if(!Boolean.valueOf(passed)) {
            throw new BusinessException("100001","数据不满足AB测试要求！");
        }

        DBClient.setCurrentDatabaseKey(PEACOCK_DATA);
        Map<String, Object> map = DBClient.executeQueryMap(sql, new Object[]{userId});
        if(map == null) {
            throw new BusinessException("100023","对应用户推荐信息不存在！");
        }
        logger.debug(mapperJson);
        JSONObject jsonObject = JSONObject.parseObject(mapperJson);
        JSONObject mappers = jsonObject.getJSONObject(strategy);


        SmartMap smartMap = SmartMap.hashMap();
        smartMap.put("outer-ab", StringUtils.isNotBlank(outPassed) && !Boolean.valueOf(outPassed) ? "B":"A");
        for (Map.Entry<String, Object> mapper : mappers.entrySet()) {
            smartMap.put(mapper.getValue(), map.get(mapper.getKey()));
        }

        return smartMap;
    }


    @SmartApi(path = "/baize/query_daily_invest_summary", version = "1.0.0", name = "获取每日投资概览接口", description = "获取平台的每日投资概览,这里只有交易额"
            /*,checker = GenericAuthChecker.class*/, owners = "2")
    @SmartResult(value ={
//        @Result(attr = "dailyInvestMoney", alias = "dailyInvestMoney", formatter = MoneyDisplayFormatter.class)
    })
    public SmartMap queryDailyInvestSummary(@SmartParameter(required = false, description = "查询日期") String qryDate){
        if(StringUtils.isBlank(qryDate)) {
            qryDate = DateUtils.getDateYYYYMMDD(Calendar.getInstance().getTime());
        }
        Map<String, Object> result = (Map<String, Object>) CacheFactory.get(DBQueryController.class, qryDate);

        if(result == null) {
            result = DBClient.executeQueryMap(BAIZE_REAL_TIME, "SELECT CONCAT(IFNULL(SUM(money),0)) AS dailyInvestMoney FROM(\n" +
                    "    SELECT ROUND(SUM(money),2) AS money FROM canal_hour_deal_money  WHERE FROM_UNIXTIME(logdate,'%Y-%m-%d')= ? \n" +
                    "    UNION\n" +
                    "    SELECT money FROM canal_gold_money WHERE  FROM_UNIXTIME(logdate,'%Y-%m-%d')= ? \n" +
                    "    UNION\n" +
                    "    SELECT money FROM canal_gold_crt_money  WHERE  FROM_UNIXTIME(logdate,'%Y-%m-%d')= ? \n" +
                    "    ) AS a", new Object[]{qryDate, qryDate, qryDate});

            CacheFactory.put(DBQueryController.class, qryDate, result);
        }

        return SmartMap.build(result);
    }
}
