package com.hframework.tracer;

import brave.sampler.RateLimitingSampler;
import brave.sampler.Sampler;
import org.apache.commons.lang3.StringUtils;

import java.math.BigDecimal;
import java.util.*;

public class PeacockSampler extends Sampler{

    public static final String SAMPLER_LIMIT = "sampler.limit.per.second"; //每分钟多少条,1-Long.Max
    public static final String SAMPLER_RATE = "sampler.rate"; //采集率,0.0~1.0
    public static final String SAMPLER_APIS = "sampler.apis.regex"; //适用API的正则表达式,多个正则表达式用“;”分隔
    public static final String SAMPLER_PARAMETERS = "sampler.parameters.regex"; //适用API参数的正则表达式,多个正则表达式用“;”分隔

    public static final String SAMPLER_TAGS_REQUEST_API = "sampler.tags.request.api"; //采集是否添加API的请求参数TAG
    public static final String SAMPLER_TAGS_REQUEST_HANDLER = "sampler.tags.request.handler"; //采集是否添加Handler的请求参数TAG
    public static final String SAMPLER_TAGS_REQUEST_IO = "sampler.tags.request.io"; //采集是否添加底层JAVA服务的请求参数TAG
    public static final String SAMPLER_TAGS_RESPONSE_API = "sampler.tags.response.api"; //采集是否添加API的响应参数TAG
    public static final String SAMPLER_TAGS_RESPONSE_HANDLER = "sampler.tags.response.handler"; //采集是否添加Handler的响应参数TAG
    public static final String SAMPLER_TAGS_RESPONSE_IO = "sampler.tags.response.io"; //采集是否添加底层JAVA服务的响应参数TAG


    public static  ThreadLocal<String> reqTL = new ThreadLocal<>();
    private Map<String, Object> configs;
    private Sampler innerSampler = null;

    public PeacockSampler(Map<String, Object> configs, Sampler innerSampler) {
        this.innerSampler = innerSampler;
        this.configs = configs;
    }

    /**
     * Returns true if the trace ID should be measured.
     *
     * @param traceId The trace ID to be decided on, can be ignored
     */
    @Override
    public boolean isSampled(long traceId) {
        boolean pass = true;
        if(configs.containsKey(SAMPLER_PARAMETERS)) {
            String requestQueryString = reqTL.get();
            pass = false;
            if(StringUtils.isNotBlank(requestQueryString)) {
                for (String regex : (List<String>) configs.get(SAMPLER_PARAMETERS)) {
                    if(requestQueryString.trim().matches(regex)) {
                        pass = true;
                    }
                }
            }
        }
        return pass ? innerSampler.isSampled(traceId) : false;
    }

    public boolean hasApiRequestTag(){
        return check(SAMPLER_TAGS_REQUEST_API, true);
    }
    public boolean hasApiResponseTag(){
        return check(SAMPLER_TAGS_RESPONSE_API, true);
    }
    public boolean hasHandlerRequestTag(){
        return check(SAMPLER_TAGS_REQUEST_HANDLER, false);
    }
    public boolean hasHandlerResponseTag(){
        return check(SAMPLER_TAGS_RESPONSE_HANDLER, false);
    }
    public boolean hasIORequestTag(){
        return check(SAMPLER_TAGS_REQUEST_IO, false);
    }
    public boolean hasIOResponseTag(){
        return check(SAMPLER_TAGS_RESPONSE_IO, false);
    }


    public boolean check(String key, boolean defaultVal) {
        if(!configs.containsKey(key)) return defaultVal;
        return (boolean)configs.get(key);
    }

    public static class Build{
        private Long programId;
        private Set<String> nodes;

        private Map<String, Object> configs = new HashMap<>();

        public Build(Long programId, String nodeString, String content) {
            this.programId = programId;
            if(StringUtils.isNotBlank(nodeString)) {
                nodes = new HashSet(Arrays.asList(nodeString.split(",")));

            }
            if(StringUtils.isNotBlank(content)) {
                String[] rows = content.split("\n");
                for (String row : rows) {
                    if(StringUtils.isNotBlank(row)) {
                        String[] kv = row.split("=");
                        if(kv.length == 2) {
                            if("*".equals(kv[1].trim()) || StringUtils.isBlank(kv[1].trim())) {
                                continue;
                            }
                            if (SAMPLER_LIMIT.equals(kv[0].trim())) {
                                configs.put(SAMPLER_LIMIT, Integer.valueOf(kv[1].trim()));
                            }else if (SAMPLER_RATE.equals(kv[0].trim())) {
                                configs.put(SAMPLER_RATE, new BigDecimal(kv[1].trim()));
                            }else if (SAMPLER_TAGS_REQUEST_API.equals(kv[0].trim())
                                    || SAMPLER_TAGS_REQUEST_HANDLER.equals(kv[0].trim())
                                    || SAMPLER_TAGS_REQUEST_IO.equals(kv[0].trim())
                                    || SAMPLER_TAGS_RESPONSE_API.equals(kv[0].trim())
                                    || SAMPLER_TAGS_RESPONSE_HANDLER.equals(kv[0].trim())
                                    || SAMPLER_TAGS_RESPONSE_IO.equals(kv[0].trim())
                                    ) {
                                configs.put(kv[0].trim(), Boolean.valueOf(kv[1].trim()));
                            }else if (SAMPLER_APIS.equals(kv[0].trim()) || SAMPLER_PARAMETERS.equals(kv[0].trim())) {
                                String[] regexes = kv[1].trim().split(";");
                                List<String> regexList = new ArrayList<>();
                                for (String regex : regexes) {
                                    if(StringUtils.isNotBlank(regex)) {
                                        regexList.add(regex.trim());
                                    }
                                }
                                if(!regexList.contains("*")) {
                                    configs.put(kv[0].trim(), regexList);
                                }

                            }else {
                                configs.put(kv[0].trim(), kv[1].trim());
                            }
                        }

                    }
                }
            }
        }
        public boolean match(Long programId, String apiPath) {

            if(this.programId != programId) {
                return false;
            }

            if(nodes != null && PeacockSystemCenter.nodeCode != null && !nodes.contains(PeacockSystemCenter.nodeCode)) {
                return false;
            }

            if(!configs.containsKey(SAMPLER_APIS)) {
                return true;
            }

            for (String regex : (List<String>) configs.get(SAMPLER_APIS)) {
                if(apiPath.matches(regex)) {
                    return true;
                }
            }

            return false;
        }

        public PeacockSampler build(){
            Sampler sampler = null;
            if (configs.containsKey(SAMPLER_LIMIT)) {//采集每分钟前多少条
                sampler = RateLimitingSampler.create((Integer) configs.get(SAMPLER_LIMIT));
            }else if (configs.containsKey(SAMPLER_RATE)) {//按比例采集
                sampler = Sampler.create(((BigDecimal)configs.get(SAMPLER_RATE)).floatValue());
            }else  sampler = Sampler.NEVER_SAMPLE;//不采集
            return new PeacockSampler(configs, sampler);
        }
    }






}
