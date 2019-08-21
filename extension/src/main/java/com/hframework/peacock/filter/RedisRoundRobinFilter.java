package com.hframework.peacock.filter;

import com.hframework.common.springext.properties.PropertyConfigurerUtils;
import com.hframework.peacock.parser.ExpressionConfigurationParser;
import com.hframework.smartsql.client2.RedisClient;
import com.hframework.smartweb.bean.SmartFilter;
import com.hframework.smartweb.bean.SmartObject;
import com.hframework.strategy.rule.ExpressInvoker;
import com.hframework.peacock.parser.ExpressionConfigurationParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

/**
 * Created by zhangquanhong on 2017/2/24.
 */
public class RedisRoundRobinFilter extends ExpressionConfigurationParser implements SmartFilter{
    private static final Logger logger = LoggerFactory.getLogger(RedisRoundRobinFilter.class);
    private static final String PEACOCK_REDIS_KEY = "peacock";
    private static final String UNKNOWN_FLAG = "unknown";
    private Set<Integer> resultIndexSet = null;
    public RedisRoundRobinFilter() throws Exception {
        super.config.ok();
        RedisClient.registerRedis(PEACOCK_REDIS_KEY,
                PropertyConfigurerUtils.getProperty("redis.host"),
                Integer.valueOf(PropertyConfigurerUtils.getProperty("redis.port")),
                PropertyConfigurerUtils.getProperty("redis.password"), 9);
    }


    @Override
    public boolean filter(String path, String version, String key, List<Map<String, Object>> returnObjects, Object attrObject, String[] when, Map<String, String> parameters) throws Exception {

        ExpressInvoker expressInvoker = super.getExpressInvoker(path, version, key, parameters, null);
        List<String> tags = expressInvoker.thenExpresses();
        setResultIndexSetIfNecessary(expressInvoker);


        String tmpPath = path.replaceAll("\\\\", "_") + "_" + key;
        String redisKeyPrefix = tmpPath;

        Object retVal = null;
        Map<String, String> tmpMap = new LinkedHashMap<>();
        if(resultIndexSet.size() > 0 && returnObjects != null && returnObjects.size() == 1 && SmartObject.isAssignableFrom(returnObjects.get(0))) {
            retVal = SmartObject.getObject(returnObjects.get(0));
            if(retVal instanceof String) {
                String[] retVals = ((String) retVal).split(",");
                for (Integer index : resultIndexSet) {
                    String value = index < retVals.length ? retVals[index]: UNKNOWN_FLAG;
                    tmpMap.put("result." + index, value);
                    redisKeyPrefix += ("_" + value);
                }
            }
        }

        //返回结果为空或者没有需要的标签，则直接返回null
        if(tmpMap.values().contains(UNKNOWN_FLAG) && new HashSet<>(tmpMap.values()).size() == 1) {
            logger.info("[path={}, version={}, key={}, pass={} ] id = {}, result = {}, group = {} ",
                    path, version, key, Arrays.toString(when).substring(1, Arrays.toString(when).length() - 1), attrObject, retVal, null);
            return false;
        }
        //AB测试达到上限，针对于同一组输出无需再进行处理，直接返回为null
        if(RedisClient.sIsMember(PEACOCK_REDIS_KEY, tmpPath + "_Exit", redisKeyPrefix.substring(tmpPath.length() + 1))){
            logger.info("[path={}, version={}, key={}, pass={} ] id = {}, result = {}, group = {} ",
                    path, version, key, Arrays.toString(when).substring(1, Arrays.toString(when).length() - 1), attrObject, retVal, null);
            return false;
        }

        parameters.putAll(tmpMap);
        String curTag = null;
        Set<String> tagsSet = new HashSet(tags);
        for (String tag : tagsSet) {
            if(RedisClient.sIsMember(PEACOCK_REDIS_KEY, redisKeyPrefix + "_" + tag, String.valueOf(attrObject))) {
                curTag = tag;
                break;
            }
        }

        if(curTag == null) {
            Long sequence = RedisClient.increase(PEACOCK_REDIS_KEY, redisKeyPrefix + "_Index", 0L, -1);
            parameters.put("sequence", String.valueOf(sequence));
            String newTag =  super.parse(path, version, key, parameters, null);
            if("X".equals(newTag)) {//表明截流退出AB测试
                RedisClient.sAdd(PEACOCK_REDIS_KEY, tmpPath + "_Exit" , redisKeyPrefix.substring(tmpPath.length()+1));
                logger.info("[path={}, version={}, key={}, pass={} ] id = {}, result = {}, group = {} ",
                        path, version, key, Arrays.toString(when).substring(1, Arrays.toString(when).length() - 1), attrObject, retVal, "X");
                return false;
            }
            RedisClient.sAdd(PEACOCK_REDIS_KEY, redisKeyPrefix + "_" + newTag, String.valueOf(attrObject));
            curTag = newTag;
        }
        logger.info("[path={}, version={}, key={}, pass={} ] id = {}, result = {}, group = {} ",
                path, version, key, Arrays.toString(when).substring(1, Arrays.toString(when).length() - 1), attrObject, retVal, curTag);
        return Arrays.asList(when).contains(curTag);
    }

    private void setResultIndexSetIfNecessary(ExpressInvoker expressInvoker) {
        if(resultIndexSet != null) return;
        resultIndexSet = new HashSet<>();
        String[] vars = expressInvoker.getVars();
        for (String var : vars) {
            if(var.matches("\\$result\\.\\d")) {
                resultIndexSet.add(Integer.valueOf(var.substring("$result.".length())));
            }
        }
    }
}
