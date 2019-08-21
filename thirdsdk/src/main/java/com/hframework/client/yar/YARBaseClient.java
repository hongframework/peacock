package com.hframework.client.yar;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.yarclient.client.YarClient;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class YARBaseClient {

    public static final Logger logger = LoggerFactory.getLogger(YARBaseClient.class);

    private static YarClient yarClient = null;
    private static String[] items = {"account", "password", "mail", "realName", "userName", "mobile", "bankNo"};

    private static String yarPortalUrl = YarConfig.getInstance().getP2pDomain();

    private static String yarPortalUrlForO2O = YarConfig.getInstance().getO2oDomain();

    private static String yarPortalUrlForBonus = YarConfig.getInstance().getBonusDomain();

    static {
        if (StringUtils.isBlank(yarPortalUrl)) {
            throw new MissingResourceException("Missing yarPortalUrl property.", "YarService", "yarPortalUrl");
        }
        yarClient = new YarClient();
    }

    private static String getYarPortalUrl(String url){
        return yarPortalUrl;
    }

    /**
     * yar  for  O2O
     * @param url
     * @param params
     * @return
     * @throws Exception
     */
    @SuppressWarnings("unchecked")
    public String requestForBonus(String url, @SuppressWarnings("rawtypes") Map params) throws Exception {
        params = new HashMap<String, Object>((Map<String, Object>)params);
        YarMapping mapping = YarMapping.getMapping(url);

        Map<String, Object> requestParams = buildRequestParamsForO2O(mapping, params);

        long beginTime = System.currentTimeMillis();
        try {
            logger.info("YarRequetForBonus: "+url+"|"+yarPortalUrlForBonus +"|"+ trimLogInfo(JSON.toJSONString(requestParams), items));
            String jsonStr = yarClient.clientRequestToJson(yarPortalUrlForBonus, "callByObject", requestParams, null);
            logger.info(" time:" + (System.currentTimeMillis() - beginTime)+ "ms staus:success " + trimLogInfo(JSON.toJSONString(requestParams) + " resultForBonus: " + jsonStr, items));
            return addHeader(url, jsonStr);
        } catch (Exception e) {
            logger.error("yar-requestForBonus:" + url+" time:"+ (System.currentTimeMillis() - beginTime) + "ms staus:error " + trimLogInfo(JSON.toJSONString(requestParams), items),e);
            return buildErrorMsg(e.getMessage());
        }
    }
    @SuppressWarnings("unchecked")
    public static String requestForO2O(String url, @SuppressWarnings("rawtypes") Map params) throws Exception {
        params = new HashMap<String, Object>((Map<String, Object>)params);
        YarMapping mapping = YarMapping.getMapping(url);

        Map<String, Object> requestParams = buildRequestParamsForO2O(mapping, params);

        long beginTime = System.currentTimeMillis();
        try {
            logger.info("YarRequetForO2O: "+url+"|"+yarPortalUrlForO2O +"|"+ trimLogInfo(JSON.toJSONString(requestParams), items));
            String jsonStr = yarClient.clientRequestToJson(yarPortalUrlForO2O, "callByObject", requestParams, null);
            logger.info(" time:" + (System.currentTimeMillis() - beginTime)+ "ms staus:success " + trimLogInfo(JSON.toJSONString(requestParams) + " resultForO2O: " + jsonStr, items));
            return addHeader(url, jsonStr);
        } catch (Exception e) {
            logger.error("yar-requestForO2O:" + url+" time:"+ (System.currentTimeMillis() - beginTime) + "ms staus:error " + trimLogInfo(JSON.toJSONString(requestParams), items),e);
            return buildErrorMsg(e.getMessage());
        }
    }


    @SuppressWarnings("unchecked")
    public static String request(String url, @SuppressWarnings("rawtypes") Map params) throws Exception {
        params = new HashMap<String, Object>((Map<String, Object>)params);

        String userId = String.valueOf (params.containsKey("userId") ? params.get("userId") : params.get("p2pId"));
        params.remove("token");

        YarMapping mapping = YarMapping.getMapping(url);
        Map<String, Object> requestParams = buildRequestParams(mapping, params);
        long beginTime = System.currentTimeMillis();
        try {

            String  finalUrl= getYarPortalUrl(url);

            logger.info("YarRequet: "+url+"|"+finalUrl +"|"+ trimLogInfo(JSON.toJSONString(requestParams), items));

            String jsonStr = yarClient.clientRequestToJson(finalUrl, "callByObject", requestParams, null);

            if(StringUtils.isEmpty(userId) && StringUtils.isNotEmpty(jsonStr)){
                Matcher matcher = Pattern.compile(".+\"userId\":(\\d+?),").matcher(jsonStr);
                if(matcher.find()){
                    userId = matcher.group(1);
                }
            }

            logger.info(" uid:" + userId + " time:" + (System.currentTimeMillis() - beginTime)
                    + "ms staus:success " + trimLogInfo(JSON.toJSONString(requestParams) + " result: " + jsonStr, items));
            // "yar-request:" + url +
            return addHeader(url, jsonStr);
        } catch (Exception e) {
            logger.error("yar-request:" + url + " uid:" + userId + " time:"
                            + (System.currentTimeMillis() - beginTime) + "ms staus:error " + trimLogInfo(JSON.toJSONString(requestParams), items),
                    e);

            return buildErrorMsg(e.getMessage());
        }
    }

    private static String trimLogInfo(String log, String[] items) {
        for (String item : items) {
            log = trimLogItem(log, item);
        }
        return log;
    }

    private static String trimLogItem(String log, String item) {
        if (log == null) {
            return null;
        }
        if (log.indexOf(item) >= 0) {
            log = log.replaceAll("\"" + item + "\":\"[^\"]+\"", "\"" + item + "\":\"------\"");
        }
        return log;
    }

    @SuppressWarnings("unchecked")
    private static Map<String, Object> buildRequestParams(YarMapping mapping, @SuppressWarnings("rawtypes") Map params) {
//		params.put("proto", mapping.proto);
        Map<String, Object> pageParam = buildPageParam(params);
        if (pageParam != null) {
            params.put("pageable", pageParam);
        }

        Map<String, Object> requestParams = new HashMap<String, Object>();
        requestParams.put("service", mapping.service);
        requestParams.put("method", mapping.method);
        requestParams.put("client", "fundgate_api");
        requestParams.put("args", params);
//		requestParams.put("sign", "be3s4rtestyu4rfwt4e2d7ce9348cb27ed904951");
        String sign = upperMd5(params);
        System.out.println(params);
//		requestParams.put("sign", sign);
        params.put("sign", sign);
        return requestParams;
    }

    @SuppressWarnings("unchecked")
    private static Map<String, Object> buildRequestParamsForO2O(YarMapping mapping, @SuppressWarnings("rawtypes") Map params) {

        Map<String, Object> requestParams = new HashMap<String, Object>();
        requestParams.put("service", mapping.service);
        requestParams.put("method", mapping.method);
        requestParams.put("client", "fundgate_api");
        requestParams.put("args", params);
//		requestParams.put("sign", "be3s4rtestyu4rfwt4e2d7ce9348cb27ed904951");
        String sign = upperMd5(params);
        System.out.println(params);
//		requestParams.put("sign", sign);
        params.put("sign", sign);
        return requestParams;
    }
    private static String addHeader(String url, String json) {
        if (StringUtils.isEmpty(json)) {
            return json;
        }

        JSONObject parseObject = JSON.parseObject(json);
        JSONObject res = new JSONObject();

        if (YarConfig.FP_LOGIN.equals(url)) {
            res.put("errorMsg", parseObject.get("errorMsg"));
            if (parseObject.get("errorCode") == null) {
                res.put("errorCode", 0);
            } else {
                res.put("errorCode", parseObject.get("errorCode"));
                parseObject.remove("errorCode");
            }
            res.put("data", parseObject);
            parseObject.remove("errorMsg");
        } else {
            res.put("errorMsg", null);
            res.put("errorCode", 0);
            res.put("data", parseObject);
        }
        return res.toJSONString();
    }

    private static String buildErrorMsg(String message) {
        if (StringUtils.isBlank(message)) {
            return null;
        }

        Map<String, Object> errMsg = new HashMap<String, Object>();
        Pattern pattern = Pattern.compile("responseCode=(\\d+)");
        Matcher match = pattern.matcher(message);
        if (match.find()) {
            errMsg.put("errorCode", match.group(1));
            errMsg.put("errorMsg", "server error");
            message = message.replaceAll("responseCode=\\d+[\\s,]*", "");
        }

        pattern = Pattern.compile("message=(.*?)(, |\\s*\\}|\\s*$)");
        match = pattern.matcher(message);
        if (match.find()) {
            errMsg.put("errorMsg", match.group(1));
        } else {
            errMsg.put("errorMsg", message);
        }

        pattern = Pattern.compile("code=(.*?)(, |\\s*\\})");
        match = pattern.matcher(message);
        if (match.find()) {
            errMsg.put("errorCode", match.group(1));
        }

        return JSON.toJSONString(errMsg);
    }

    private static Map<String, Object> buildPageParam(Map<String, Object> param) {
        Map<String, Object> res = null;
        if (param.containsKey("pageNo")) {
            res = new HashMap<String, Object>();
            res.put("pageNo", param.get("pageNo"));
            param.remove("pageNo");
        }

        if (param.containsKey("pageSize")) {
            res = res == null ? new HashMap<String, Object>() : res;
            res.put("pageSize", param.get("pageSize"));
            param.remove("pageSize");
        }
        return res;
    }

    public static void main(String[] args) {
        System.out
                .println(buildErrorMsg("responseCode=222,message=hello world"));
    }

    @Deprecated
    public static String upperMd5(Map<String, Object> param) {
        String result = "";

        List<Map.Entry<String, Object>> infoIds = new ArrayList<Map.Entry<String, Object>>(param.entrySet());
        Collections.sort(infoIds,
                new Comparator<Map.Entry<String, Object>>() {
                    public int compare(Map.Entry<String, Object> o1,
                                       Map.Entry<String, Object> o2) {
                        return (o1.getKey()).toString().compareTo(
                                o2.getKey());
                    }
                });

        for (Map.Entry<String, Object> entry : infoIds) {
            String key = entry.getKey();
            Object o = entry.getValue();
            if (o instanceof Float) {
                Float aFloat = (Float) o;
                result += key + aFloat.intValue();
            }else {
                String val = String.valueOf(param.get(key));
                result += key + val;
            }
        }

        result = YarConfig.RPC_SECRET + result + YarConfig.RPC_SECRET;
        System.out.println("-->" + result);
        return DigestUtils.md5Hex(result).toUpperCase();
    }
}
