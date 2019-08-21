package com.hframework.peacock.controller.base;

import com.alibaba.fastjson.JSONObject;
import com.google.common.base.Joiner;
import com.hframework.beans.exceptions.BusinessException;
import com.hframework.common.util.message.JsonUtils;
import com.hframework.common.util.message.PropertyReader;
import com.hframework.monitor.ConfigMonitor;
import com.hframework.monitor.MonitorListener;
import com.hframework.smartsql.client2.DBClient;
import com.hframework.smartweb.bean.ApiConf;
import com.hframework.tracer.PeacockSystemCenter;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;
import com.hframework.peacock.controller.base.descriptor.ApiDescriptor;
import com.hframework.peacock.config.PeacockSystemLoader;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.apache.commons.lang3.StringUtils;
//import DBClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.*;

/**
 * Created by zhangquanhong on 2017/11/15.
 */
public class ApiConfigureRegistry implements MonitorListener<TreeMap<String, ApiDescriptor>> {

    private static final Logger logger = LoggerFactory.getLogger(ApiConfigureRegistry.class);

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
    private volatile TreeMap<String, ApiDescriptor>  apis = new TreeMap(new Comparator<String>() {
        @Override
        public int compare(String s1, String s2) {
            return s1.compareTo(s2);
        }
    });

    private volatile Map<ApiDescriptor, ApiExecutor> executors = new HashMap<>();

    private volatile Map<String, String> apiDefaultPaths = new HashMap<>();

    private static ApiConfigureRegistry globalRegistry;

    public static ApiConfigureRegistry getDefaultInstance(){
        if(globalRegistry == null) {
            synchronized (ApiConfigureRegistry.class){
                if(globalRegistry == null) {
                    globalRegistry = getNewInstance();
                    try {
                        new Monitor("SELECT\n" +
                                "  api.id,\n" +
                                "  module.code     AS module,\n" +
                                "  ver.code          AS `version`,\n" +
                                "  api.`name`,\n" +
                                "  api.title,\n" +
                                "  api.description,\n" +
                                "  api.`content`,\n" +
                                "  api.state,\n" +
                                "  api.module_id,\n" +
                                "  api.program_id  AS program\n" +
                                "FROM cfg_runtime_api api\n" +
                                "  LEFT JOIN cfg_mgr_module module\n" +
                                "    ON api.module_id = module.id\n" +
                                "  LEFT JOIN cfg_mgr_version ver\n" +
                                "    ON api.version_id = ver.id", globalRegistry).start();
//                        new Monitor("SELECT * FROM cfg_runtime_api", globalRegistry).start();
                    } catch (Exception e) {
                        logger.error("initialize failed ! => {}", ExceptionUtils.getFullStackTrace(e));
                        throw new RuntimeException("initialize failed !");
                    }
                }
            }
        }
        return globalRegistry;
    }

    public static ApiConfigureRegistry getHandlerInstance(){
        ApiConfigureRegistry registry =getNewInstance();
        try {
            new Monitor("SELECT\n" +
                    "  api.id,\n" +
                    "  module.code     AS module,\n" +
                    "  ver.code AS `version`,\n" +
                    "  api.`name`,\n" +
                    "  api.title,\n" +
                    "  api.description,\n" +
                    "  api.`content`,\n" +
                    "  api.state,\n" +
                    "  api.module_id,\n" +
                    "  api.program_id  AS program\n" +
                    "FROM cfg_runtime_handler api\n" +
                    "  LEFT JOIN cfg_mgr_module module\n" +
                    "    ON api.module_id = module.id\n" +
                    "    LEFT JOIN cfg_mgr_version ver\n" +
                    "    ON api.version_id = ver.id", registry).start();
//            new Monitor("SELECT * FROM cfg_runtime_handler", registry).start();
        } catch (Exception e) {
            logger.error("initialize failed ! => {}", ExceptionUtils.getFullStackTrace(e));
            throw new RuntimeException("initialize failed !");
        }
        return registry;
    }

    public static ApiConfigureRegistry getNewInstance(){
        ApiConfigureRegistry registry = new ApiConfigureRegistry();
        return registry;
    }

    @Override
    public void onEvent(com.hframework.monitor.Monitor<TreeMap<String, ApiDescriptor>> monitor) throws Exception {
        TreeMap<String, ApiDescriptor> newApis = monitor.getObject();
        Map<String, String> defaultPaths = new HashMap<>();
        for (String path : newApis.keySet()) {
            String tmpDefaultPath = path.substring(0, path.lastIndexOf("/"));
            defaultPaths.put(tmpDefaultPath, path);
        }

        for (String path : newApis.keySet()) {
            ApiDescriptor originDescriptor = apis.get(path);
            ApiDescriptor newlyDescriptor = newApis.get(path);
            //这里只需要比对配置即可，不要直接比较ApiDescriptor
//            if(originDescriptor == null || isDiff(originDescriptor.getApiConf(), newlyDescriptor.getApiConf())){
            if(originDescriptor == null || !originDescriptor.getApiConfXml().equals(newlyDescriptor.getApiConfXml())){
                logger.info(" {} reload => {}", path, newlyDescriptor.getApiConfXml());
                newlyDescriptor.initialize();//如果这里抛出Handler不存在的异常，检查RuntimeHandlerLoader是否加载
                apis.put(path, newlyDescriptor);
                executors.put(newlyDescriptor, new ApiExecutor(newlyDescriptor));
                executors.remove(originDescriptor);
            }
        }

        apiDefaultPaths = defaultPaths;
        logger.info("on event success, snapshot => " + snapshot());
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

//    private static  <T> boolean isDiff(T originDescriptor, T newlyDescriptor){
//        try {
//            String originDescriptorJson = JsonUtils.writeValueAsString(originDescriptor);
//            String newlyDescriptorJson = JsonUtils.writeValueAsString(newlyDescriptor);
//            if(!originDescriptorJson.equals(newlyDescriptorJson)) {
//                logger.info("config is change ,old config is [{}], new config is [{}]", originDescriptorJson, newlyDescriptorJson);
//                return true;
//            }
//        } catch (IOException e) {
//            logger.error("{}", ExceptionUtils.getFullStackTrace(e));
//        }
//        return false;
//    }

    public ApiDescriptor findApiDescriptor(String module, String name, String version) {
        if(module == null) module = "";
        if(name == null) name = "";
        if(version == null) version = "";

        for (String program : PeacockSystemCenter.maybePrograms) {
            try{
                return findApiDescriptor(program, module, name, version);
            }catch (Exception e){}
        }
        return findApiDescriptor(PeacockSystemCenter.mainProgram, module, name, version);

    }

    public ApiDescriptor findApiDescriptorByPath(String program, String path, String version) {
        if(program == null) program = "null";
        if(path == null) path = "";
        if(version == null) version = "";

        String runtimePath =  calculatePath(program, path, version);
        if(apis.containsKey(runtimePath)) {
            return apis.get(runtimePath);
        }else if(apiDefaultPaths.containsKey(runtimePath)) {
            return apis.get(apiDefaultPaths.get(runtimePath));
        }else {
            String tmpPath =  calculatePath(program, path);
            if(apiDefaultPaths.containsKey(tmpPath)) {
                return apis.get(apiDefaultPaths.get(tmpPath));
            }
        }
        logger.warn("api descriptor not found [{}] !", runtimePath);
        throw new BusinessException("api descriptor not found [" + runtimePath + "] !");
    }


    public ApiDescriptor findApiDescriptor(String program, String module, String name, String version) {
        if(program == null) program = "null";
        if(module == null) module = "";
        if(name == null) name = "";
        if(version == null) version = "";

        String path = calculatePath(program, module, name, version);
        if(apis.containsKey(path)) {
            return apis.get(path);
        }else if(apiDefaultPaths.containsKey(path)) {
            return apis.get(apiDefaultPaths.get(path));
        }else {
            String tmpPath =  calculatePath(program, module, name);
            if(apiDefaultPaths.containsKey(tmpPath)) {
                return apis.get(apiDefaultPaths.get(tmpPath));
            }
        }
        logger.warn("api descriptor not found [{}] !", path);
        throw new BusinessException("api descriptor not found [" + path + "] !");
    }

    public ApiExecutor getExecutor(ApiDescriptor descriptor){
        return executors.get(descriptor);
    }



    public static class Monitor{

        private String sql = null;

        public  ConfigMonitor<TreeMap<String, ApiDescriptor>> monitor;

        public Monitor(final String sql, MonitorListener listener) {
            this.sql = sql;
            monitor =  new ConfigMonitor<TreeMap<String, ApiDescriptor>>(this.getClass(), 5) {
                @Override
                public TreeMap<String, ApiDescriptor> fetch() throws Exception {
                    List<Map<String, Object>> maps = DBClient.executeQueryMaps("default", sql, new Object[]{});

                    TreeMap<String, ApiDescriptor> result = new TreeMap<>();

                    for (Map<String, Object> map : maps) {
                        String id = String.valueOf(map.get("id"));
                        String program = String.valueOf(map.get("program"));
                        String module = String.valueOf(map.get("module"));
                        String name = String.valueOf(map.get("name"));
                        String version = String.valueOf(map.get("version"));
                        String path = calculatePath(program, module, name, version);
                        String content =map.get("content") == null ? null : String.valueOf(map.get("content"));
                        if(StringUtils.isBlank(content)){
                            continue;
                        }
//                        ApiConf apiConf = null;
//                        try {
//                            apiConf = (ApiConf) apiConfParser.fromXML(content);;
//                        }catch (Exception e) {
//                            logger.error("parse api configure error => {}", ExceptionUtils.getFullStackTrace(e));
//                        }

                        String title = String.valueOf(map.get("title"));
                        String description = String.valueOf(map.get("description"));
                        ApiDescriptor.ApiState state = ApiDescriptor.ApiState.parseFrom(map.get("state"));
                        logger.debug("find => {}|{}",path, content.replaceAll("\n", " "));
                        result.put(path, new ApiDescriptor(Long.valueOf(id), program, module, name, version, state, title, description, path, content));
                    }

                    return result;
                }

            };
            monitor.addListener(listener);
        }

        public void start() throws Exception {
            monitor.start();
        }


    }
    private static String calculatePath(String... pathParts) {
        String path = Joiner.on("/").join(pathParts).replaceAll("//+","/");
        path = path.startsWith("/") ? path : "/" + path;
        path = path.endsWith("/") ? path.substring(0, path.length() - 1) :path;

        return path;
    }

    public Map<String, String> getApiDefaultPaths() {
        return apiDefaultPaths;
    }

    public TreeMap<String, ApiDescriptor> getApis() {
        return apis;
    }
}
