package com.hframework.strategy;

import com.hframework.common.client.hbase.HBaseClient;
import com.hframework.strategy.index.repository.converter.SHA256RowKeyConverter;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by zhangquanhong on 2017/9/19.
 */
public class HbaseClientSuite {

    @Test
    public void test_insert() throws Exception {
        HashMap<String, Object> hashMap = new HashMap() {{
            put("userId", 6090);
            put("total_bid_count", 10); //总投资次数
            put("user_balance_loan_repay", 0); //在投金额
            put("total_bid_annual_money", 10); //累计年化投资额
            put("user_repay_last_time", System.currentTimeMillis() / 1000 - Math.round(Math.random() * 14 * 24 * 60 * 60 * 2 / 1)); //最后一次回款时间
            put("last_bid_time", System.currentTimeMillis() / 1000 - (Math.round(Math.random()) == 1 ? 13 * 24 * 60 * 60 : 62 * 24 * 60 * 60)); //最后一次投资时间
            put("user_payment_last_time", System.currentTimeMillis() / 1000 - Math.round(Math.random() * 14 * 24 * 60 * 60 * 2)); //最后一次充值时间
            put("user_withdraw_last_time", 1205040201); //最后一次提现时间
            put("user_attribute_money",  0); //账户余额
            put("user_attribute_lock_money",  0); //冻结金额
            put("lastLevel", "\"活跃用户\""); //上次用户评级
            put("potentialValue", Math.round(Math.random() * 100000)); //潜力值
            put("maxDayInvestingAmountOfSixMonth", 0); //近6月最高日在途
            put("planingRefundAmount", Math.round(Math.random() * 2)); //计划回款
        }};
        HBaseClient.PutContainer putContainer = HBaseClient.getInstance("zqh,wzk,zzy", "2181").table("hbase_data_profile_user").rowKey(new SHA256RowKeyConverter().convert(6090));
        for (Map.Entry<String, Object> stringObjectEntry : hashMap.entrySet()) {
                putContainer.column("data", stringObjectEntry.getKey(), stringObjectEntry.getValue());
            }
        putContainer.put();
    }


}
