package com.hframework.strategy.rule.fetch;

import com.hframework.common.client.hbase.HBaseClient;
import com.hframework.strategy.index.repository.converter.SHA256RowKeyConverter;

import java.util.*;

/**
 * Created by zhangquanhong on 2017/6/27.
 */
public class DemoFetcher implements Fetcher<Integer> {

    public FetchData fetch(Set<Integer> userIds) throws Exception {
        List<Map> list = new ArrayList<Map>();
        final float weight = 0.7f;
        for (final Integer userId : userIds) {
            HashMap<String, Object> hashMap = new HashMap() {{
                put("userId", userId);
                put("total_bid_count", Math.round(Math.random() * 9 / weight)); //总投资次数
                put("user_balance_loan_repay", Math.round(Math.random()/* * weight */) == 1 ? 1 : 0); //在投金额
                put("total_bid_annual_money", Math.round(Math.random() * 1000 / weight)); //累计年化投资额
                put("user_repay_last_time", System.currentTimeMillis() / 1000 - Math.round(Math.random() * 14 * 24 * 60 * 60 * 2 / weight)); //最后一次回款时间
                put("last_bid_time", System.currentTimeMillis() / 1000 - (Math.round(Math.random()) == 1 ? 13 * 24 * 60 * 60 : 62 * 24 * 60 * 60)); //最后一次投资时间
                put("user_payment_last_time", System.currentTimeMillis() / 1000 - Math.round(Math.random() * 14 * 24 * 60 * 60 * 2)); //最后一次充值时间
                put("user_withdraw_last_time", System.currentTimeMillis() / 1000 - (Math.round(Math.random()) == 1 ? 70 * 24 * 60 * 60 : 10 * 24 * 60 * 60)); //最后一次提现时间
                put("user_attribute_money", Math.round(Math.random()) == 1 ? 11000 : 0); //账户余额
                put("user_attribute_lock_money", Math.round(Math.random()) == 1 ? 1100 : 0); //冻结金额
                put("lastLevel", "\"活跃用户\""); //上次用户评级
                put("potentialValue", Math.round(Math.random() * 100000)); //潜力值
                put("maxDayInvestingAmountOfSixMonth", Math.round(Math.random() * 1000 * 2 / weight)); //近6月最高日在途
                put("planingRefundAmount", Math.round(Math.random() * 2)); //计划回款
            }};
            list.add(hashMap);
//            HBaseClient.PutContainer putContainer = HBaseClient.getInstance("zqh,wzk,zzy", "2181").table("hbase_data_profile_user").rowKey(new SHA256RowKeyConverter().convert(userId));
//            for (Map.Entry<String, Object> stringObjectEntry : hashMap.entrySet()) {
//                putContainer.column("data", stringObjectEntry.getKey(), stringObjectEntry.getValue());
//            }
//            putContainer.put();
        }



        return FetchData.swap(list, new FetchData.KeyMap<Map, Integer>() {
            public Integer map(Map t) {
                return (Integer) t.get("userId");
            }
        });
    }
}
