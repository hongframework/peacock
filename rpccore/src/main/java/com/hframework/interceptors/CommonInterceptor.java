package com.hframework.interceptors;

import com.alibaba.fastjson.JSON;
;
import com.hframework.common.client.redis.bean.JedisPoolFactory;
import com.hframework.smartweb.APIErrorType;
import com.hframework.smartweb.DynResultVO;
import com.hframework.smartweb.LogHelper;
import org.apache.commons.collections.map.HashedMap;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.PathMatcher;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;

/**
 * Created by zqh on 2016/4/11.
 */
public class CommonInterceptor implements HandlerInterceptor {

    private static final Logger logger = LoggerFactory.getLogger(CommonInterceptor.class);

    private static final String LOGIN_CHECK = "visit_";

    //过滤掉的sql关键字，可以手动添加
    private static final String[] sqlInjectionKeywords = (
            "'|and|exec|execute|insert|create|drop|table|from|grant|use|group_concat|column_name" +
            "|information_schema.columns|table_schema|union|where|select|delete|update|order" +
            "|by|count|*|chr|mid|master|truncate|char|declare|or|;|-|--|+|,|like|//|/|%|#").split("\\|");

    private  Map<String, String> urlVisitRuleMapping = new LinkedHashMap<>();

    private static final Map<String, List<Handler>> visitRuleHandlerMapperMapping = new HashedMap();


    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler1) throws Exception {
        LogHelper.start(LogHelper.Type.Statistics, request);
        try {
            String url = request.getRequestURI();
            logger.debug("request:{}?{}", url, getParamString(request));

            if(checkSqlInjectionNoPass(request)){
                DynResultVO resultVO = DynResultVO.error(APIErrorType.ILLEGAL_INPUT);
                response.setHeader("content-type", "text/html;charset=UTF-8");
                response.setCharacterEncoding("UTF-8");
                response.getWriter().print(JSON.toJSONString(resultVO));
                response.getWriter().close();
                return false;
            }

            for (Map.Entry<String, String> urlVisitRule : urlVisitRuleMapping.entrySet()) {
                String urlPattern = urlVisitRule.getKey();
                PathMatcher pathMatcher = new AntPathMatcher();
                if(pathMatcher.match(urlPattern, url)) {
                    List<Handler> handlers = getHandlers(urlVisitRule.getValue().trim());
                    for (Handler handler : handlers) {
                        boolean result = handler.handle(request, response, handler1);
                        if(!result) {
                            logger.warn("pre handle check not pass.|{}|{}|{}|{}", url,
                            request.getQueryString(), urlPattern, urlVisitRule.getValue().trim());
                            DynResultVO resultVO = DynResultVO.error(APIErrorType.ACCOUNT_TRY_TOO_MUCH);
                            response.setHeader("content-type", "text/html;charset=UTF-8");
                            response.setCharacterEncoding("UTF-8");
                            response.getWriter().print(JSON.toJSONString(resultVO));
                            response.getWriter().close();
                            return false;
                        }
                    }
                    return true;
                }
            }
        }catch (Exception e) {
            logger.error("pre handle check error.|{}|{}|{}", request.getRequestURI(),
                    request.getQueryString(),e);
        }

        return true;
    }

    private boolean checkSqlInjectionNoPass(HttpServletRequest request) {
        boolean result = false;
        String parameter = request.getParameter("skey");
        if(StringUtils.isNotBlank(parameter)) {
            result |= checkSqlInjectionNoPass(parameter);
        }
        String discountId = request.getParameter("discountid");
        if(StringUtils.isNotBlank(parameter)) {
            result |= checkSqlInjectionNoPass(parameter);
        }
        return result;
    }
    private boolean checkSqlInjectionNoPass(String parameter) {
        for (String keyword : sqlInjectionKeywords) {
            if(parameter.contains(keyword)){
                return true;
            }
        }
        return false;
    }

    private Object getParamString(HttpServletRequest request) {

        String queryString = request.getQueryString();
        if(StringUtils.isNotBlank(queryString)) {
            return queryString;
        }else {
            queryString +="POST : ";
            Map parameters = request.getParameterMap();//保存request请求参数的临时变量
            Iterator paiter = parameters.keySet().iterator();
            while (paiter.hasNext()) {
                String key = paiter.next().toString();
                String[] values = (String[])parameters.get(key);
                queryString += (key+"="+values[0] + "&");
            }
        }
        return queryString;
    }

    private List<Handler> getHandlers(String visitRule) {

        if(StringUtils.isBlank(visitRule)) {
            return null;
        }

        if(!visitRuleHandlerMapperMapping.containsKey(visitRule)) {
            String[] visitRules = visitRule.split(";");
            if(visitRules == null) {
                return null;
            }
            List<Handler> handlers = new ArrayList<>();
            for (String rule : visitRules) {
                String[] ruleInfo = rule.split(":");
                if(ruleInfo == null || ruleInfo.length !=2) {
                    logger.warn(" get handlers error.|{}|{}", visitRule, rule);
                    continue;
                }

                int limitCount = Integer.valueOf(ruleInfo[1]);

                if(TimeUnit.MINUTE.getName().equals(ruleInfo[0])) {
                    handlers.add(new MinuteCheckHandler(limitCount));
                }else if(TimeUnit.HOUR.getName().equals(ruleInfo[0])) {
                    handlers.add(new HourCheckHandler(limitCount));
                }else if(TimeUnit.DATE.getName().equals(ruleInfo[0])) {
                    handlers.add(new DateCheckHandler(limitCount));
                }
            }
            visitRuleHandlerMapperMapping.put(visitRule, handlers);
        }
        return visitRuleHandlerMapperMapping.get(visitRule);
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        LogHelper.end(LogHelper.Type.Statistics);
    }

    public Map<String, String> getUrlVisitRuleMapping() {
        return urlVisitRuleMapping;
    }

    public void setUrlVisitRuleMapping(Map<String, String> urlVisitRuleMapping) {
        this.urlVisitRuleMapping = urlVisitRuleMapping;
    }

    public  interface Handler{
        public boolean handle(HttpServletRequest request, HttpServletResponse response, Object handler);
    }

    public enum TimeUnit{
        MINUTE("Minute"),
        HOUR("Hour"),
        DATE("Date");

        private String name;

        TimeUnit(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }

    public static boolean check(TimeUnit timeUnit, int limitCount, HttpServletRequest request) {
        String username = request.getParameter("account");
        if(StringUtils.isBlank(username)) {
            username = request.getParameter("token");
        }

        boolean result = checkInternal(username, timeUnit, limitCount, request);
        if(!result) return false;

        return checkInternal(getRemoteIp(request), timeUnit, limitCount, request);
    }

    private static String getRemoteIp(HttpServletRequest request) {
        String ip = request.getHeader("x-forwarded-for");
        logger.debug("x-forwarded-for = {}", ip);
        if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
            logger.debug("Proxy-Client-IP = {}", ip);
        }
        if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
            logger.debug("WL-Proxy-Client-IP = {}", ip);
        }
        if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
            logger.debug("RemoteAddr-IP = {}", ip);
        }
        if(StringUtils.isNotBlank(ip)) {
            ip = ip.split(",")[0];
        }
        return ip;
    }

    public static boolean checkInternal(String accessKey, TimeUnit timeUnit, int limitCount, HttpServletRequest request) {
        logger.debug("redis check.|{}|{}|{}",accessKey, timeUnit, limitCount);
        JedisPool jedisPool = JedisPoolFactory.getJedisPool();
        String key = LOGIN_CHECK + timeUnit.getName() + "_" + accessKey  + "_" + request.getRequestURI();
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            if (jedis.exists(key)) {
                Long newValue = jedis.incr(key);
                if (newValue != null && newValue >limitCount){
                    logger.warn("redis check fail.|{}|{}|{}", key, limitCount, newValue);
                    return false;
                }
                return true;
            } else {
                jedis.setex(key, 60 * (timeUnit == TimeUnit.HOUR ? 60 :1) * (timeUnit == TimeUnit.DATE ? 60 * 60 :1) , "0");
                return true;
            }
        } catch (Exception e) {
            logger.error("longin check faield.|{}", accessKey, e);
            return true;
        } finally {
            // 释放对象池
            // jedisPool.returnResource(jedis);
            jedisPool.returnResourceObject(jedis);
        }
    }

    public static class MinuteCheckHandler implements Handler{
        private int limitCount ;

        public MinuteCheckHandler(int limitCount) {
            this.limitCount = limitCount;
        }

        public boolean handle(HttpServletRequest request, HttpServletResponse response, Object handler) {
            return check(TimeUnit.MINUTE, limitCount, request);
        }

        public int getLimitCount() {
            return limitCount;
        }

        public void setLimitCount(int limitCount) {
            this.limitCount = limitCount;
        }
    }
    public static class HourCheckHandler implements Handler{
        private int limitCount ;
        public HourCheckHandler(int limitCount) {
            this.limitCount = limitCount;
        }
        public boolean handle(HttpServletRequest request, HttpServletResponse response, Object handler) {
            return check(TimeUnit.HOUR, limitCount, request);
        }

        public int getLimitCount() {
            return limitCount;
        }

        public void setLimitCount(int limitCount) {
            this.limitCount = limitCount;
        }
    }
    public static class DateCheckHandler implements Handler{
        private int limitCount ;
        public DateCheckHandler(int limitCount) {
            this.limitCount = limitCount;
        }
        public boolean handle(HttpServletRequest request, HttpServletResponse response, Object handler) {
            return check(TimeUnit.DATE, limitCount, request);
        }

        public int getLimitCount() {
            return limitCount;
        }

        public void setLimitCount(int limitCount) {
            this.limitCount = limitCount;
        }
    }
}
