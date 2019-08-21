package com.hframework.peacock.controller.base;

import com.alibaba.fastjson.JSON;
import com.hframework.client.yar.YarConfig;
import com.hframework.peacock.controller.base.descriptor.ThirdApiDescriptor;
import com.yarclient.client.YarClient;
import org.apache.commons.codec.digest.DigestUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

public class YarRpcProtocolExecutor extends AbstractProtocolExecutor {
    private static String[] items = {};

    private static final Logger logger = LoggerFactory.getLogger(YarRpcProtocolExecutor.class);

    private static YarClient yarClient =  new YarClient();
    private String service;
    private String method;


    public YarRpcProtocolExecutor(ThirdApiDescriptor apiDescriptor, ThirdApiConfigureRegistry registry) {
        super(apiDescriptor, registry);
        String path = apiDescriptor.getPath();
        String[] splits = path.split(":+");
        service = splits[0];
        method = splits[1];
    }

    /**
     * 协议按照descriptor进行API调用
     *
     * @param parameters
     * @param nodes
     * @return
     */
    @Override
    public String execute(Map<String, Object> parameters, Map<String, Object> nodes) throws Exception {
        Map<String, Object> requestParams = buildRequestParams(parameters);
        long beginTime = System.currentTimeMillis();

//        logger.info("YarRequest: "+domain +"|"+ trimLogInfo(JSON.toJSONString(requestParams), items));
        logger.info("YarRequest: {}|{}", domain, JSON.toJSONString(requestParams));
        String jsonStr = yarClient.clientRequestToJson(domain, "callByObject", requestParams, null);
        logger.info("YarResponse: {}|{}|{}({}ms)", domain, JSON.toJSONString(requestParams), jsonStr,
                (System.currentTimeMillis() - beginTime));
//        logger.info("YarResponse: " + (System.currentTimeMillis() - beginTime)
//                + "ms status:success " + trimLogInfo(JSON.toJSONString(requestParams) + " result: " + jsonStr, items));
        return jsonStr;
    }

    private  Map<String, Object> buildRequestParams( @SuppressWarnings("rawtypes") Map params) {
//		params.put("proto", mapping.proto);
        Map<String, Object> pageParam = buildPageParam(params);
        if (pageParam != null) {
            params.put("pageable", pageParam);
        }

        Map<String, Object> requestParams = new HashMap<String, Object>();
        requestParams.put("service", service);
        requestParams.put("method", method);
        requestParams.put("client", "fundgate_api");
        requestParams.put("args", params);
        String sign = upperMd5(params);
        params.put("sign", sign);
        return requestParams;
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

        String md5String = DigestUtils.md5Hex(result).toUpperCase();
        logger.info("YarMD5: {}|{}", result, md5String);
//        System.out.println("-->" + result);
        return md5String;
    }
}
