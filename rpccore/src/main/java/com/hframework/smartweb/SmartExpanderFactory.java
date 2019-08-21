package com.hframework.smartweb;

import com.google.common.collect.Lists;
import com.hframework.common.util.UrlHelper;
import com.hframework.common.util.collect.CollectionUtils;
import com.hframework.common.util.collect.bean.Grouper;
import com.hframework.common.util.collect.bean.Mapper;
import com.hframework.common.util.message.PropertyReader;
import com.hframework.common.util.message.XmlUtils;
import com.hframework.monitor.ConfigMonitor;
import com.hframework.monitor.Monitor;
import com.hframework.monitor.MonitorListener;
import com.hframework.smartsql.client.DBClient;
import com.hframework.smartweb.bean.apiconf.*;
import com.hframework.smartweb.bean.checker.IntDictionaryChecker;
import com.hframework.smartweb.bean.checker.StringDictionaryChecker;
import com.hframework.smartweb.bean.formatter.DictionaryContainFormatter;
import com.hframework.smartweb.bean.formatter.DictionaryVirtualFormatter;
import com.hframework.smartweb.bean.formatter.IntDictionaryMatchFormatter;
import com.hframework.smartweb.bean.formatter.StringDictionaryMatchFormatter;
import com.hframework.tracer.PeacockSampler;
import com.hframework.tracer.TracerFactory;
import com.hframework.web.config.bean.dataset.*;
import com.hframework.web.config.bean.dataset.Enum;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.*;

/**
 * Created by zhangquanhong on 2016/9/22.
 */
public class SmartExpanderFactory {

    private static final Logger logger = LoggerFactory.getLogger(SmartExpanderFactory.class);

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

    private static List<Map<String, Object>> dictionaryConf = new ArrayList<>();
    private static List<Map<String, Object>> dictionaryItemConf = new ArrayList<>();
    private static List<Map<String, Object>> responseConf = new ArrayList<>();
    private static List<Map<String, Object>> parameterConf = new ArrayList<>();
    private static List<Map<String, Object>> expanderConf = new ArrayList<>();
    private static List<Map<String, Object>> expanderParameterConf = new ArrayList<>();
    private static Map<String ,String> expanderParameterDescriptionConf = new HashMap<>();

    private static Map<Long, Map<String, String>> programResponseCache = new HashMap<>();
    private static Map<Long, Map<String, String>> programDictionaryCache = new HashMap<>();
    private static Map<String, Map<String, String>> programDictionaryItemCache = new HashMap<>();

    private static String[] expanderXmls = new String[7];
    private static Map<Long, String> helperItemXml = new HashMap<>();

    private static Map<Long, String[]> dictionaryHelperXml = new HashMap<>();

    private static ConfigMonitor<List<Map<String, Object>>> cfgTraceMonitor =
            new ConfigMonitor<List<Map<String, Object>>>(SmartExpanderFactory.class, 5) {
                private String sql = "SELECT program_id, node_id, content FROM cfg_runtime_trace WHERE STATUS=1  ORDER BY modify_time DESC, create_time DESC;";
                @Override
                public List<Map<String, Object>> fetch() throws Exception {
                    return DBClient.executeQueryMaps("default", sql, new Object[]{});
                }
            };

    private static ConfigMonitor<List<Map<String, Object>>> dictionaryMonitor =
            new ConfigMonitor<List<Map<String, Object>>>(SmartExpanderFactory.class, 5) {
                private String sql = "SELECT id, program_id, `code`, `name` FROM cfg_runtime_dictionary WHERE STATUS =1";
                @Override
                public List<Map<String, Object>> fetch() throws Exception {
                    return DBClient.executeQueryMaps("default", sql, new Object[]{});
                }
            };


    private static ConfigMonitor<List<Map<String, Object>>> dictionaryItemMonitor =
            new ConfigMonitor<List<Map<String, Object>>>(SmartExpanderFactory.class, 5) {
                private String sql = "SELECT dictionary_id, `code`, `name` FROM cfg_runtime_dictionary_items WHERE STATUS =1";
                @Override
                public List<Map<String, Object>> fetch() throws Exception {
                    return DBClient.executeQueryMaps("default", sql, new Object[]{});
                }
            };

    private static ConfigMonitor<List<Map<String, Object>>> responseMonitor =
            new ConfigMonitor<List<Map<String, Object>>>(SmartExpanderFactory.class, 5) {
                private String sql = "SELECT id, program_id, `code`, `name`, `value` FROM cfg_runtime_response";
                @Override
                public List<Map<String, Object>> fetch() throws Exception {
                    return DBClient.executeQueryMaps("default", sql, new Object[]{});
                }
            };


    private static ConfigMonitor<List<Map<String, Object>>> parameterMonitor =
            new ConfigMonitor<List<Map<String, Object>>>(SmartExpanderFactory.class, 5) {
                private String sql = "SELECT id, program_id, name, type,description, default_val,min_val,max_val,required, expanders FROM cfg_runtime_parameter WHERE STATUS = 1;";
                @Override
                public List<Map<String, Object>> fetch() throws Exception {
                    return DBClient.executeQueryMaps("default", sql, new Object[]{});
                }
            };
    private static ConfigMonitor<List<Map<String, Object>>> expanderMonitor =
            new ConfigMonitor<List<Map<String, Object>>>(SmartExpanderFactory.class, 5) {
                private String sql = "SELECT id, type,name,trigger_data_type, expander_class, description FROM cfg_static_expander WHERE STATUS =1;";
                @Override
                public List<Map<String, Object>> fetch() throws Exception {
                    return DBClient.executeQueryMaps("default", sql, new Object[]{});
                }
            };
    private static ConfigMonitor<List<Map<String, Object>>> expanderParameterMonitor =
            new ConfigMonitor<List<Map<String, Object>>>(SmartExpanderFactory.class, 5) {
                private String sql = "SELECT id, name, value,description,expander_id FROM cfg_static_expander_parameter WHERE STATUS = 1;";
                @Override
                public List<Map<String, Object>> fetch() throws Exception {
                    return DBClient.executeQueryMaps("default", sql, new Object[]{});
                }
            };

    static {

        cfgTraceMonitor.addListener(new MonitorListener<List<Map<String, Object>>>() {
            @Override
            public void onEvent(Monitor<List<Map<String, Object>>> monitor) throws ClassNotFoundException, Exception {
                reloadTraceConf(monitor.getObject());
            }
        });

        dictionaryMonitor.addListener(new MonitorListener<List<Map<String, Object>>>() {
            @Override
            public void onEvent(Monitor<List<Map<String, Object>>> monitor) throws ClassNotFoundException, Exception {
                dictionaryConf = monitor.getObject();
                reloadDictionary();
                reloadExpander();
            }
        });

        dictionaryItemMonitor.addListener(new MonitorListener<List<Map<String, Object>>>() {
            @Override
            public void onEvent(Monitor<List<Map<String, Object>>> monitor) throws ClassNotFoundException, Exception {
                dictionaryItemConf = monitor.getObject();
                reloadDictionary();
            }
        });

        responseMonitor.addListener(new MonitorListener<List<Map<String, Object>>>() {
            @Override
            public void onEvent(Monitor<List<Map<String, Object>>> monitor) throws ClassNotFoundException, Exception {
                responseConf = monitor.getObject();
                reloadResponses();
            }
        });

        parameterMonitor.addListener(new MonitorListener<List<Map<String, Object>>>() {
            @Override
            public void onEvent(Monitor<List<Map<String, Object>>> monitor) throws ClassNotFoundException, Exception {
                parameterConf = monitor.getObject();
                reloadParameters();
            }
        });
        expanderMonitor.addListener(new MonitorListener<List<Map<String, Object>>>() {
            @Override
            public void onEvent(Monitor<List<Map<String, Object>>> monitor) throws ClassNotFoundException, Exception {
                expanderConf = monitor.getObject();
                reloadExpander();
            }
        });
        expanderParameterMonitor.addListener(new MonitorListener<List<Map<String, Object>>>() {
            @Override
            public void onEvent(Monitor<List<Map<String, Object>>> monitor) throws ClassNotFoundException, Exception {
                expanderParameterConf = monitor.getObject();
                reloadExpander();
            }
        });
        try {
            startMonitor();
        } catch (Exception e) {
            logger.error("monitor start failed ..", e);
            e.printStackTrace();
        }
    }

    private synchronized static void reloadTraceConf(List<Map<String, Object>> cfgTraceConf) {
        Map<Long,  List<PeacockSampler.Build>> result = new HashMap<>();
        for (Map<String, Object> map : cfgTraceConf) {
            Long programId = (Long)map.get("program_id");
            String nodeId = (String) map.get("node_id");
            String content = (String) map.get("content");
            PeacockSampler.Build sampler = new PeacockSampler.Build(programId, nodeId, content);
            if(!result.containsKey(programId)) {
                result.put(programId, new ArrayList<PeacockSampler.Build>());
            }
            result.get(programId).add(sampler);
        }

        TracerFactory.clearAllTrance(result);
    }

    private  synchronized static void reloadDictionary() throws IOException {

        Map<Long, List<Map<String, Object>>> dictionaryItems = CollectionUtils.group(dictionaryItemConf, new Grouper<Long, Map<String, Object>>() {
            @Override
            public <K> K groupKey(Map<String, Object> map) {
                return (K) map.get("dictionary_id");
            }
        });

        Map<Long, Map<String, String>> dictionaryCache = new HashMap<>();
        Map<String, Map<String, String>> dictionaryItemCache = new HashMap<>();
        for (Map<String, Object> map : dictionaryConf) {
            Long dictionaryId = (Long) map.get("id");
            Long programId = (Long) map.get("program_id");
            String code = (String) map.get("code");
            String name = (String) map.get("name");

            if(!dictionaryCache.containsKey(programId)) {
                dictionaryCache.put(programId, new LinkedHashMap<String, String>());
            }
            dictionaryCache.get(programId).put(code, name);

            Map<String, String> items = new HashMap<>();
            List<Map<String, Object>> maps = dictionaryItems.get(dictionaryId);
            if(maps != null) {
                for (Map<String, Object> itemMap : maps) {
                    if("null".equals(itemMap.get("code"))) {
                        items.put(null, (String) itemMap.get("name"));
                    }else {
                        items.put((String) itemMap.get("code"), (String) itemMap.get("name"));
                    }

                }
            }
            dictionaryItemCache.put(programId + "_" + code, items);
        }
        programDictionaryCache = dictionaryCache;
        programDictionaryItemCache = dictionaryItemCache;

        //刷新字典关联的信息
        Map<Long, String> enumExtendXml = getEnumExtendXml(IntDictionaryChecker.class, StringDictionaryChecker.class);
        for (Long programId : enumExtendXml.keySet()) {
            dictionaryHelperXml.put(programId, new String[3]);
            dictionaryHelperXml.get(programId)[0] = enumExtendXml.get(programId);
        }
        enumExtendXml = getEnumExtendXml(StringDictionaryMatchFormatter.class,
                IntDictionaryMatchFormatter.class, DictionaryContainFormatter.class, DictionaryVirtualFormatter.class);
        for (Long programId : enumExtendXml.keySet()) {
            dictionaryHelperXml.get(programId)[2] = enumExtendXml.get(programId);
        }

    }

    public static Map<Long, String> getEnumExtendXml(Class... parentClass) throws IOException {
        Map<Long, String> result = new HashMap<>();

        for (Map.Entry<Long, Map<String, String>> dictionary : programDictionaryCache.entrySet()) {
            List<Enum> enums = new ArrayList<>();
            for (Class aClass : parentClass) {
                for (Map.Entry<String, String> dict : dictionary.getValue().entrySet()) {
                    Enum anEnum = new Enum();
                    anEnum.setValue(dict.getKey());
                    anEnum.setName(dict.getValue());
                    anEnum.setParent(aClass.getName());
                    enums.add(anEnum);
                }
            }
            Field field = new Field();
            field.setEnumList(enums);
            String xml = XmlUtils.writeValueAsString(field);
            result.put(dictionary.getKey(), xml);
        }
        return result;
    }


    private  synchronized static void reloadResponses() throws IOException {
        Map<Long, List<Map<String, Object>>> programCfgs = CollectionUtils.group(responseConf, new Grouper<Long, Map<String, Object>>() {
            @Override
            public <K> K groupKey(Map<String, Object> item) {
                return (K) item.get("program_id");
            }
        });
        Map<Long, Map<String, String>> result = new HashMap<>();
        for (Long programId : programCfgs.keySet()) {
            result.put(programId, new HashMap<String, String>());
            for (Map<String, Object> item : programCfgs.get(programId)) {
                result.get(programId).put(String.valueOf(item.get("code")), String.valueOf(item.get("value")));
            }
        }

        programResponseCache = result;

    }

    private  synchronized static void reloadParameters() throws IOException {
        Map<Long, Map<String, Object>> expanderMap = CollectionUtils.convert(expanderConf, new Mapper<Long, Map<String, Object>>() {
            @Override
            public <K> K getKey(Map<String, Object> item) {
                return (K) item.get("id");
            }
        });
        Map<Long, Map<String, Object>> expanderParameterMap = CollectionUtils.convert(expanderParameterConf, new Mapper<Long, Map<String, Object>>() {
            @Override
            public <K> K getKey(Map<String, Object> item) {
                return (K) item.get("id");
            }
        });
        Map<Long, List<HelperItem>> helperItems = new HashMap<>();
        for (Map<String, Object> item : parameterConf) {
            Long programId = (Long) item.get("program_id");
            Parameter parameter = new Parameter();
            parameter.setName((String) item.get("name"));
            parameter.setDescription((String) item.get("description"));
            parameter.setType((String) item.get("type"));
            parameter.setDefaultValue(StringUtils.isBlank((String) item.get("default_val"))? null : (String) item.get("default_val"));
            parameter.setMin(item.get("min_val") != null ? String.valueOf(item.get("min_val")) : null);
            parameter.setMax(item.get("max_val") != null ? String.valueOf(item.get("max_val")) : null);
            parameter.setRequired((Integer) item.get("required") == 1 ? "true" : null);
            String expanders = (String) item.get("expanders");
            if(StringUtils.isNotBlank(expanders)) {
                String[] ids = expanders.split(",");
                for (String idItem : ids) {
                    String[] itemIds = idItem.split(":");
                    Map<String, Object> expander = expanderMap.get(Long.valueOf(itemIds[0]));
                    String type = (String) expander.get("type");
                    String expanderClass = (String) expander.get("expander_class");
                    String pattern = expanderParameterMap.containsKey(Long.valueOf(itemIds[1])) ?
                            (String) expanderParameterMap.get(Long.valueOf(itemIds[1])).get("value") : null;
                    if("checker".equals(type)) {
                        Checker checker = new Checker();
                        checker.setClazz(expanderClass);
                        checker.setPattern(pattern);
                        if(parameter.getCheckerList() == null) {
                            parameter.setCheckerList(new ArrayList<Checker>());
                        }
                        parameter.getCheckerList().add(checker);
                    }else {
                        Parser parser = new Parser();
                        parser.setClazz(expanderClass);
                        parser.setPattern(pattern);
                        parameter.setParser(parser);
                    }
                }
            }

            HelperItem helperItem = new HelperItem();
            helperItem.setName(parameter.getDescription());
            helperItem.setText(XmlUtils.writeValueAsString(parameter));
            if(!helperItems.containsKey(programId)) {
                helperItems.put(programId, new ArrayList<HelperItem>());
            }
            helperItems.get(programId).add(helperItem);
        }

        helperItemXml = new HashMap<>();
        for (Long programId : helperItems.keySet()) {
            HelperLabel label = new HelperLabel();
            label.setName("通用");
            label.setHelpItems(helperItems.get(programId));
            helperItemXml.put(programId, XmlUtils.writeValueAsString(label));
        }
    }

    private  synchronized static void reloadExpander() throws IOException {
        Map<Long, Map<String, Object>> expanderMap = CollectionUtils.convert(expanderConf, new Mapper<Long, Map<String, Object>>() {
            @Override
            public <K> K getKey(Map<String, Object> item) {
                return (K) item.get("id");
            }
        });
        List<Enum> formatters = new ArrayList<>();
        List<Enum> checkers = new ArrayList<>();
        List<Enum> parsers = new ArrayList<>();

        List<Enum> formatterPatterns = new ArrayList<>();
        List<Enum> checkerPatterns = new ArrayList<>();
        List<Enum> parserPatterns = new ArrayList<>();

        for (Map<String, Object> item : expanderConf) {
            String type = (String) item.get("type");
            String dataType = (String) item.get("trigger_data_type");
            String expanderClass = (String) item.get("expander_class");
            Enum anEnum = new Enum();
            anEnum.setValue(expanderClass);
            anEnum.setName((String) item.get("name"));
            anEnum.setParent(StringUtils.isNoneBlank(dataType)? dataType: null);
            addEnum(formatters, checkers, parsers, anEnum, type);
        }


        for (Map<String, Object> item : expanderParameterConf) {
            Map<String, Object> expanderItem = expanderMap.get(item.get("expander_id"));
            if(expanderItem != null){
                Enum anEnum = new Enum();
                anEnum.setValue((String) item.get("value"));
                anEnum.setName((String) item.get("name"));
                anEnum.setParent((String) expanderItem.get("expander_class"));
                String type = (String) expanderItem.get("type");
                addEnum(formatterPatterns, checkerPatterns, parserPatterns, anEnum, type);
            }
        }

        for (Map<String, Object> item : expanderParameterConf) {
            Map<String, Object> expanderItem = expanderMap.get(item.get("expander_id"));
            if(expanderItem != null){
                String pattern = (String) item.get("value");
                String expander = (String) expanderItem.get("expander_class");
                String description = (String) item.get("description");
                expanderParameterDescriptionConf.put(expander + "|" + pattern, StringUtils.isBlank(description)? "" : description.trim());
            }
        }


        Field field = new Field();
        field.setEnumList(checkers);
        expanderXmls[0] = XmlUtils.writeValueAsString(field);

        field.setEnumList(checkerPatterns);
        expanderXmls[1] = XmlUtils.writeValueAsString(field);

        field.setEnumList(parsers);
        expanderXmls[2] = XmlUtils.writeValueAsString(field);

        field.setEnumList(parserPatterns);
        expanderXmls[3] = XmlUtils.writeValueAsString(field);

        field.setEnumList(formatters);
        expanderXmls[4] = XmlUtils.writeValueAsString(field);

        field.setEnumList(formatterPatterns);
        expanderXmls[5] = XmlUtils.writeValueAsString(field);
    }

    private static void addEnum(List<Enum> formatterPatterns, List<Enum> checkerPatterns, List<Enum> parserPatterns, Enum anEnum, String type) {
        if("formatter".equals(type)) {
            formatterPatterns.add(anEnum);
        }else if("checker".equals(type)) {
            checkerPatterns.add(anEnum);
        }else {
            parserPatterns.add(anEnum);
        }
    }

    public static void startMonitor() throws Exception {
        cfgTraceMonitor.start();
        dictionaryMonitor.start();
        dictionaryItemMonitor.start();
        responseMonitor.start();
        expanderMonitor.start();
        expanderParameterMonitor.start();
        parameterMonitor.start();
    }

    public static Map<String, String> getProgramDictionary(Long programId) {
        if(programDictionaryCache.containsKey(programId)) {
            return programDictionaryCache.get(programId);
        }else {
            return new HashMap<>();
        }
    }
    public static Map<String, String> getDictionaryItems(Long programId, String dictionaryCode) {
        if(programDictionaryItemCache.containsKey(programId + "_" + dictionaryCode)) {
            return programDictionaryItemCache.get(programId + "_" + dictionaryCode);
        }else {
            return new HashMap<>();
        }
    }

    public static String getResponseConf(Long programId, String code, String defaultVal) {
        if(programResponseCache.containsKey(programId)) {
            String value = programResponseCache.get(programId).get(code);
            return StringUtils.isBlank(value) ? defaultVal : value;
        }else {
            return defaultVal;
        }
    }

    public static void setDynResultVOKeyMeta(String programId) {
        setDynResultVOKeyMeta(Long.valueOf(programId));
    }

    public static void setDynResultVOKeyMeta(Long programId) {
        DynResultVO.setKey(
                getResponseConf(programId, "RootCodeKey", "resultCode"),
                getResponseConf(programId, "RootMsgKey", "resultMessage"),
                getResponseConf(programId, "RootDataKey", "data"),
                getResponseConf(programId, "RootCodeType", "string"));
    }

    public static String getParameterXml(HttpServletRequest request){
        return helperItemXml.get(getProgramFromRequest(request));
    }

    public static Long getProgramFromRequest(HttpServletRequest request) {
        String programId = request.getParameter("programId");
        if(StringUtils.isBlank(programId)) {
            Map<String, String> referParameters = UrlHelper.getUrlParameters(request.getHeader("Referer"), false);
            if(referParameters.containsKey("programId")) {
                programId = referParameters.get("programId");
            }
        }
        if(StringUtils.isBlank(programId)) {
            throw new RuntimeException("项目ID[ " + programId + "]不存在,请检查URL合法性！");
        }
        return Long.valueOf(programId);
    }

    public static String getExpanderParameterDescription(String expander, String pattern){
        return expanderParameterDescriptionConf.get(expander + "|" + pattern);
    }

    public static String getCheckerXml(){
        return expanderXmls[0];
    }
    public static String getCheckerPatternXml(HttpServletRequest request){
        Long program = getProgramFromRequest(request);
        if(dictionaryHelperXml.containsKey(program)) {
            return merge(expanderXmls[1], dictionaryHelperXml.get(program)[0]);
        }else {
            return expanderXmls[1];
        }

    }

    private static String merge(String baseXml, String extXml) {
        return baseXml.replace("<field>",extXml.replace("\n</field>", ""));
    }

    public static String getParserXml(){
        return expanderXmls[2];
    }
    public static String getParserPatternXml(){
        return expanderXmls[3];
    }
    public static String getFormatterXml(){
        return expanderXmls[4];
    }
    public static String getFormatterPatternXml(HttpServletRequest request){
        Long program = getProgramFromRequest(request);
        if(dictionaryHelperXml.containsKey(program)) {
            return merge(expanderXmls[5], dictionaryHelperXml.get(program)[2]);
        }else return expanderXmls[5];
    }

}
