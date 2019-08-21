package com.hframework.client.yar;

import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
import com.hframework.client.yar.bean.RepayCalendarsYearVO;
import com.hframework.client.yar.bean.VipAO;
import org.apache.commons.collections.map.HashedMap;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class YarClient {

    public static List<Map> getViplist(String ids) {
        Map<String, Object> params = new HashedMap();
        params.put("userIds", ids);
        try {
            String request = YARBaseClient.request(YarConfig.VIP_INFOLSIT, params);
            APIResultAO resp1 = JSONObject.parseObject(request, APIResultAO.class);
            Map<String, Map> list = JSONObject.parseObject(JSONObject.parseObject(resp1.getData()).get("list").toString(), HashedMap.class);
            for (Map.Entry<String, Map> stringMapEntry : list.entrySet()) {
                stringMapEntry.getValue().put("userId", stringMapEntry.getKey());
            }
            return Lists.newArrayList(list.values());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static VipAO getVipInfo(Integer userId) {
        Map<String, Object> params = new HashedMap();
        params.put("userId",userId);
        try {
            String  request = YARBaseClient.request(YarConfig.VIP_INFO, params);
            APIResultAO resp1 = JSONObject.parseObject(request, APIResultAO.class);
            return JSONObject.parseObject(JSONObject.parseObject(resp1.getData()).toString(), VipAO.class);
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    public static Long queryTicketAmount(Integer userId, Integer type, Integer consumeType) {
        Map<String, Object> params =new HashMap();
        params.put("userId", userId);
        params.put("consumeType", consumeType);
        params.put("type", type);

        try {
            String request = YARBaseClient.requestForO2O(YarConfig.TICKET_AMOUNT_UNUSED, params);
            APIResultAO resp1 = JSONObject.parseObject(request, APIResultAO.class);
            if(resp1.getErrorCode()==0){
                return Long.valueOf(String.valueOf(JSONObject.parseObject(resp1.getData()).get("data")));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0L;
    }

    public static RepayCalendarsYearVO getRepayCalendarYear(int p2pId){
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("cfpId", p2pId);
        //以下为借口必传但没意义的参数
        params.put("repayType", "toRepay");
        params.put("date", "2011-11-01");
        params.put("offset", "0");
        params.put("count", "20");
        try {
            String request = YARBaseClient.request(YarConfig.REPAY_CALENDAR_YEAR, params);
            APIResultAO resp1 = JSONObject.parseObject(request, APIResultAO.class);
            return  JSONObject.parseObject(resp1.getData(), RepayCalendarsYearVO.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }



    public static class APIResultAO {

        private int errorCode;
        private String errorMsg;
        private String data;

        public boolean isSuccess() {

            return 0 == errorCode;
        }

        public int getErrorCode() {
            return errorCode;
        }

        public void setErrorCode(int errorCode) {
            this.errorCode = errorCode;
        }

        public String getErrorMsg() {
            return errorMsg;
        }

        public void setErrorMsg(String errorMsg) {
            this.errorMsg = errorMsg;
        }

        public String getData() {
            return data;
        }

        public void setData(String data) {
            this.data = data;
        }

    }
}
