package com.hframework.peacock.controller.base;

import com.alibaba.fastjson.JSONObject;
import com.google.common.base.Joiner;
import com.hframework.beans.exceptions.BusinessException;
import com.hframework.common.util.message.JsonUtils;
import com.hframework.common.util.message.PropertyReader;
import com.hframework.common.util.message.XmlUtils;
import com.hframework.peacock.controller.bean.apiconf.Api;
import com.hframework.monitor.ConfigMonitor;
import com.hframework.monitor.Monitor;
import com.hframework.monitor.MonitorListener;
import com.hframework.smartsql.client2.DBClient;
import com.hframework.smartweb.bean.ApiConf;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;
import com.hframework.peacock.controller.base.descriptor.ThirdApiDescriptor;
import com.hframework.peacock.controller.bean.apiconf.Api;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.*;

//import DBClient;

/**
 * Created by zhangquanhong on 2019/03/06.
 */
public class ThirdApiConfigureRegistry implements MonitorListener {

    private static final Logger logger = LoggerFactory.getLogger(ThirdApiConfigureRegistry.class);

    private static PropertyReader propertyReader =
            PropertyReader.read("properties/dataSource.properties");
    private static final String DEFAULT_JDBC_URL = "jdbc.url";
    private static final String DEFAULT_JDBC_USER = "jdbc.user";
    private static final String DEFAULT_JDBC_PASSWORD = "jdbc.password";
    static {
        DBClient.registerDatabase("default", propertyReader.get(DEFAULT_JDBC_URL),
                propertyReader.get(DEFAULT_JDBC_USER), propertyReader.get(DEFAULT_JDBC_PASSWORD));
        DBClient.setCurrentDatabaseKey("default");
    }

    //所有的api信息
    private volatile TreeMap<String, ThirdApiDescriptor>  apis = new TreeMap(new Comparator<String>() {
        @Override
        public int compare(String s1, String s2) {
            return s1.compareTo(s2);
        }
    });

    private volatile ParameterMap parameterMap;
    private volatile RuleMap ruleMap;
    private volatile DomainMap domainMap;
    private volatile DomainProtocol protocolMap;



    private volatile Map<ThirdApiDescriptor, ThirdApiExecutor> executors = new HashMap<>();

    private static ThirdApiConfigureRegistry globalRegistry;

    public static ThirdApiConfigureRegistry getDefaultInstance(){
        if(globalRegistry == null) {
            synchronized (ThirdApiConfigureRegistry.class){
                if(globalRegistry == null) {
                    globalRegistry = getNewInstance();
                    try {
                        new Monitor(globalRegistry).start();
                    } catch (Exception e) {
                        logger.error("initialize failed ! => {}", ExceptionUtils.getFullStackTrace(e));
                        throw new RuntimeException("initialize failed !");
                    }
                }
            }
        }
        return globalRegistry;
    }


    public static ThirdApiConfigureRegistry getNewInstance(){
        ThirdApiConfigureRegistry registry = new ThirdApiConfigureRegistry();
        return registry;
    }

    @Override
    public void onEvent(com.hframework.monitor.Monitor monitor) throws Exception {

        Object eventData = monitor.getObject();
        if(eventData instanceof TreeMap) {
            TreeMap<String, ThirdApiDescriptor> newApis = (TreeMap<String, ThirdApiDescriptor>) eventData;
            for (String path : newApis.keySet()) {
                ThirdApiDescriptor originDescriptor = apis.get(path);
                ThirdApiDescriptor newlyDescriptor = newApis.get(path);
                //这里只需要比对配置即可，不要直接比较ThirdApiDescriptor
                if(originDescriptor == null || isDiff(originDescriptor.getApiConf(), newlyDescriptor.getApiConf())){
                    newlyDescriptor.initialize();
                    apis.put(path, newlyDescriptor);
                    executors.put(newlyDescriptor, new ThirdApiExecutor(this, newlyDescriptor));
                    executors.remove(originDescriptor);
                }else if(originDescriptor != null) {
                    originDescriptor.setApiType(newlyDescriptor.getApiType());
                    originDescriptor.setMethod(newlyDescriptor.getMethod());
                    originDescriptor.setName(newlyDescriptor.getName());
                    originDescriptor.setPath(newlyDescriptor.getPath());
                    originDescriptor.setTags(newlyDescriptor.getTags());
                }
            }
            snapshot();
        }else if (eventData instanceof ParameterMap) {
            parameterMap = (ParameterMap) eventData;
        }else if (eventData instanceof RuleMap) {
            ruleMap = (RuleMap) eventData;
        }else if(eventData instanceof DomainMap) {
            domainMap = (DomainMap) eventData;
        }else if(eventData instanceof DomainProtocol) {
            protocolMap = (DomainProtocol) eventData;
        }
    }

    public String snapshot(){
        JSONObject jsonObject = new JSONObject();
        for (String path : apis.keySet()) {
            JSONObject attr = new JSONObject();
            attr.put("descriptor", apis.get(path).toString());
            attr.put("executor", executors.get(apis.get(path)).toString());
            jsonObject.put(path, attr);
        }
        String json = jsonObject.toJSONString();
        logger.info("registry => " + json);
        return json;
    }

    private static  <T> boolean isDiff(T originDescriptor, T newlyDescriptor){
        try {
            String originDescriptorJson = JsonUtils.writeValueAsString(originDescriptor);
            String newlyDescriptorJson = JsonUtils.writeValueAsString(newlyDescriptor);
            if(!originDescriptorJson.equals(newlyDescriptorJson)) {
                logger.info("config is change ,old config is [{}], new config is [{}]", originDescriptorJson, newlyDescriptorJson);
                return true;
            }
        } catch (IOException e) {
            logger.error("{}", ExceptionUtils.getFullStackTrace(e));
        }
        return false;
    }

    public ThirdApiDescriptor findThirdApiDescriptor(String domainId, String path) {
        String cacheKey = domainId + "." + path;
        if(!apis.containsKey(cacheKey)) {
            logger.warn("third api descriptor not found [{}] !", cacheKey);
            throw new BusinessException("third api descriptor not found [" + cacheKey + "] !");
        }
        return apis.get(cacheKey);
    }

    public ThirdApiExecutor getExecutor(ThirdApiDescriptor descriptor){
        return executors.get(descriptor);
    }


    public static class ParameterMap extends HashMap<Long, Map<String, String>>{}
    public static class RuleMap extends HashMap<Long, Map<String, String>>{}
    public static class DomainMap extends HashMap<Long, String>{};
    public static class DomainProtocol extends HashMap<Long, String>{};


    public static class Monitor{
        private   ConfigMonitor<TreeMap<String, ThirdApiDescriptor>> monitor =
                new ConfigMonitor<TreeMap<String, ThirdApiDescriptor>>(this.getClass(), 3) {
                    private String sql = "SELECT api.domain_id, domain.name AS domain_name, domain.url AS " +
                            "domain_url, api.id AS api_id, api.api_type, api.name, api.path, api.method, " +
                            "api.tags, api.request_type, api.response_type, api.content FROM third_api api " +
                            "LEFT JOIN third_domain domain ON api.domain_id = domain.id WHERE domain.status = 1 " +
                            "AND api.status = 1 AND api.content IS NOT NULL";
                    @Override
                    public TreeMap<String, ThirdApiDescriptor> fetch() throws Exception {
                        List<Map<String, Object>> maps = DBClient.executeQueryMaps("default", sql, new Object[]{});

                        TreeMap<String, ThirdApiDescriptor> result = new TreeMap<>();

                        for (Map<String, Object> map : maps) {
                            Long domainId = Long.valueOf(String.valueOf(map.get("domain_id")));
                            Long apiId = Long.valueOf(String.valueOf(map.get("api_id")));
                            Long apiType = Long.valueOf(String.valueOf(map.get("api_type")));

                            String name = String.valueOf(map.get("name"));
                            String path = String.valueOf(map.get("path"));
                            String method = String.valueOf(map.get("method"));
                            String tags = String.valueOf(map.get("tags"));
                            ThirdApiDescriptor.RequestType requestType = ThirdApiDescriptor.RequestType.parseFrom(
                                    String.valueOf(map.get("request_type")));
                            ThirdApiDescriptor.ResponseType responseType = ThirdApiDescriptor.ResponseType.parseFrom(
                                    String.valueOf(map.get("response_type")));
                            String content = String.valueOf(map.get("content"));

                            Api apiConf = null;
                            if(StringUtils.isBlank(content)){
                                continue;
                            }
                            try {
                                apiConf = XmlUtils.readValue(content, Api.class);
                            }catch (Exception e) {
                                logger.error("parse api configure error => {}", ExceptionUtils.getFullStackTrace(e));
                            }

                            logger.debug("find => {}|{}|{}",domainId, path, content.replaceAll("\n", " "));
                            result.put(domainId + "." + path, new ThirdApiDescriptor(domainId, apiId, apiType, name, path,
                                    method, tags, content, apiConf, requestType, responseType));
                        }

                        return result;
                    }
                };

        private  ConfigMonitor<ParameterMap> parameterMonitor =
                new ConfigMonitor<ParameterMap>(ThirdApiConfigureRegistry.class, 3) {
                    private String sql = "SELECT id, domain_id, code, value FROM third_domain_parameter";
                    @Override
                    public ParameterMap fetch() throws Exception {
                        ParameterMap result = new ParameterMap();
                        List<Map<String, Object>> maps = DBClient.executeQueryMaps("default", sql, new Object[]{});
                        for (Map<String, Object> record : maps) {
                            Long domainId = Long.valueOf(String.valueOf(record.get("domain_id")));
                            String code =  String.valueOf(record.get("code"));
                            String value =  String.valueOf(record.get("value"));
                            if(!result.containsKey(domainId)) result.put(domainId, new HashMap<String, String>());
                            result.get(domainId).put(code, value);
                        }
                        return result;
                    }
                };

        private  ConfigMonitor<RuleMap> ruleMonitor =
                new ConfigMonitor<RuleMap>(ThirdApiConfigureRegistry.class, 3) {
                    private String sql = "SELECT id, domain_id, code, expression FROM third_public_rule";
                    @Override
                    public RuleMap fetch() throws Exception {
                        RuleMap result = new RuleMap();
                        List<Map<String, Object>> maps = DBClient.executeQueryMaps("default", sql, new Object[]{});
                        for (Map<String, Object> record : maps) {
                            Long domainId = Long.valueOf(String.valueOf(record.get("domain_id")));
                            String code =  String.valueOf(record.get("code"));
                            String expression =  String.valueOf(record.get("expression"));
                            if(!result.containsKey(domainId)) result.put(domainId, new HashMap<String, String>());
                            result.get(domainId).put(code, expression);
                        }
                        return result;
                    }
                };

        private  ConfigMonitor<DomainMap> domainMonitor =
                new ConfigMonitor<DomainMap>(ThirdApiConfigureRegistry.class, 3) {
                    private String sql = "SELECT id, url FROM third_domain;";
                    @Override
                    public DomainMap fetch() throws Exception {
                        DomainMap result = new DomainMap();
                        List<Map<String, Object>> maps = DBClient.executeQueryMaps("default", sql, new Object[]{});
                        for (Map<String, Object> record : maps) {
                            Long id = Long.valueOf(String.valueOf(record.get("id")));
                            String url =  String.valueOf(record.get("url"));
                            result.put(id, url);
                        }
                        return result;
                    }
                };
        private  ConfigMonitor<DomainProtocol> protocolMonitor =
                new ConfigMonitor<DomainProtocol>(ThirdApiConfigureRegistry.class, 3) {
                    private String sql = "SELECT id, protocol FROM third_domain;";
                    @Override
                    public DomainProtocol fetch() throws Exception {
                        DomainProtocol result = new DomainProtocol();
                        List<Map<String, Object>> maps = DBClient.executeQueryMaps("default", sql, new Object[]{});
                        for (Map<String, Object> record : maps) {
                            Long id = Long.valueOf(String.valueOf(record.get("id")));
                            String protocol =  String.valueOf(record.get("protocol"));
                            result.put(id, protocol);
                        }
                        return result;
                    }
                };

        public Monitor(MonitorListener listener) {
            protocolMonitor.addListener(listener);
            domainMonitor.addListener(listener);
            monitor.addListener(listener);
            ruleMonitor.addListener(listener);
            parameterMonitor.addListener(listener);
        }

        public void start() throws Exception {
            protocolMonitor.start();
            domainMonitor.start();
            parameterMonitor.start();
            ruleMonitor.start();
            monitor.start();
        }
    }

    public String getFixVal(Long domainId, String code){
        return parameterMap.get(domainId).get(code);
    }

    public Map<String, String> getAllFixVal(Long domainId){
        return parameterMap.get(domainId);
    }

    public String getRulExpr(Long domainId, String code){
        return ruleMap.get(domainId).get(code);
    }

    public String getDomain(Long domainId) {
        return domainMap.get(domainId);
    }

    public String getProtocol(Long domainId) {
        return protocolMap.get(domainId);
    }

}
