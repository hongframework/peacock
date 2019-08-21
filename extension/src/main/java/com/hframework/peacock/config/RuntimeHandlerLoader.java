package com.hframework.peacock.config;

import com.google.common.base.Joiner;
import com.hframework.common.util.collect.CollectionUtils;
import com.hframework.common.util.collect.bean.Mapper;
import com.hframework.common.util.message.JsonUtils;
import com.hframework.monitor.ConfigMonitor;
import com.hframework.monitor.Monitor;
import com.hframework.monitor.MonitorListener;
import com.hframework.smartweb.SmartHandlerFactory;
import com.hframework.smartweb.exception.SmartHandlerException;
import com.hframework.peacock.config.domain.model.CfgMgrModule;
import com.hframework.peacock.config.domain.model.CfgMgrProgram;
import com.hframework.peacock.config.domain.model.CfgRuntimeHandler;
import com.hframework.peacock.config.service.interfaces.ICfgMgrModuleSV;
import com.hframework.peacock.config.service.interfaces.ICfgMgrProgramSV;
import com.hframework.peacock.config.service.interfaces.ICfgRuntimeHandlerSV;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;

import java.util.*;

/**
 * Created by zhangquanhong on 2017/12/22.
 */
public class RuntimeHandlerLoader implements ApplicationListener<ContextRefreshedEvent> , MonitorListener<List<CfgRuntimeHandler>> {

    private static Logger logger = LoggerFactory.getLogger(RuntimeHandlerLoader.class);

    private ConfigMonitor<List<CfgRuntimeHandler>> handlesMonitor = null;
    private Map<String, Map<String, CfgRuntimeHandler>> globalHandlersXmlCache = new HashMap<>();
    private ApplicationContext context;
    @Override
    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {
        logger.info("我的父容器为：" + contextRefreshedEvent.getApplicationContext().getParent());
        logger.info("初始化时我被调用了。");
        //在web 项目中（spring mvc），系统会存在两个容器，一个是root application context ,
        //另一个就是我们自己的 projectName-servlet context（作为root application context的子容器）。
        //这种情况下，就会造成onApplicationEvent方法被执行两次
        if(contextRefreshedEvent.getApplicationContext().getParent() != null){
            return;
        }
        context = contextRefreshedEvent.getApplicationContext();
        final ICfgRuntimeHandlerSV sv = context.getBean(ICfgRuntimeHandlerSV.class);
        try {

            handlesMonitor = new ConfigMonitor<List<CfgRuntimeHandler>>(2) {
                @Override
                public List<CfgRuntimeHandler> fetch() throws Exception {
                    return sv.getCfgRuntimeHandlerAll();
                }
            };
            handlesMonitor.addListener(this);
            handlesMonitor.start();

//            List<CfgRuntimeHandler> cfgRuntimeHandlers = sv.getCfgRuntimeHandlerAll();
//            for (CfgRuntimeHandler handler : cfgRuntimeHandlers) {
//                if(StringUtils.isBlank(handler.getContent())) continue;
//                SmartHandlerFactory.addHandler(handler.getModule(), handler.getVersion(),
//                        calculatePath(handler.getModule(), handler.getName()),
//                        handler.getTitle(), handler.getContent(), handler.getState(), false);
//            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private static String calculatePath(String... pathParts) {
        String path = Joiner.on("/").join(pathParts).replaceAll("//+", "/");
        path = path.startsWith("/") ? path : "/" + path;
        path = path.endsWith("/") ? path.substring(0, path.length() - 1) :path;

        return path;
    }

    @Override
    public void onEvent(Monitor<List<CfgRuntimeHandler>> monitor) throws ClassNotFoundException, Exception {
        List<CfgRuntimeHandler> handleList = monitor.getObject();
        if(handleList == null) return;

        Map<Long, CfgMgrModule> modules = CollectionUtils.convert(context.getBean(ICfgMgrModuleSV.class).getCfgMgrModuleAll(), new Mapper<Long, CfgMgrModule>() {
            @Override
            public <K> K getKey(CfgMgrModule cfgMgrModule) {
                return (K) cfgMgrModule.getId();
            }
        });
        Map<Long, CfgMgrProgram> programs = CollectionUtils.convert(context.getBean(ICfgMgrProgramSV.class).getCfgMgrProgramAll(), new Mapper<Long, CfgMgrProgram>() {
            @Override
            public <K> K getKey(CfgMgrProgram cfgMgrProgram) {
                return (K) cfgMgrProgram.getId();
            }
        });

        Map<String, Map<String, CfgRuntimeHandler>> newGlobalHandlersXml = new LinkedHashMap<>();
        for (CfgRuntimeHandler handler : handleList) {
            if(StringUtils.isBlank(handler.getContent())){
                continue;
            }
            String programCode = String.valueOf(handler.getProgramId());//programs.get(handler.getProgramId()).getCode();
            String moduleCode = modules.get(handler.getModuleId()).getCode();
            String path = calculatePath(programCode, moduleCode, handler.getName());
            String version = handler.getVersion();
            if(!newGlobalHandlersXml.containsKey(path)){
                newGlobalHandlersXml.put(path, new LinkedHashMap<String, CfgRuntimeHandler>());
            }
            if(newGlobalHandlersXml.containsKey(path) && newGlobalHandlersXml.get(path).containsKey(version))  {
                throw new SmartHandlerException("same path handler exists : " + path + "; " + version);
            }
            newGlobalHandlersXml.get(path).put(version, handler);
        }
        synchronized (this){
            MapTreeDiff<String, String, CfgRuntimeHandler> diff = MapTreeDiff.execute(globalHandlersXmlCache, newGlobalHandlersXml, new MapTreeDiff.EqualFunc<CfgRuntimeHandler>() {
                @Override
                public boolean equal(CfgRuntimeHandler v1, CfgRuntimeHandler v2) {
                    return StringUtils.isNoneBlank(v1.getContent()) && v1.getContent().equals(v2.getContent());
                }

            });

            logger.info("diff : " + JsonUtils.writeValueAsString(diff));
            Map<String, Map<String, CfgRuntimeHandler>> addMap = diff.getAddMap();
            if(addMap != null){
                for (String path : addMap.keySet()) {
                    for (String version : addMap.get(path).keySet()) {
                        CfgRuntimeHandler handler = addMap.get(path).get(version);
                        String programCode = String.valueOf(handler.getProgramId());//programs.get(handler.getProgramId()).getCode();
                        String moduleName = modules.get(handler.getModuleId()).getName();
                        SmartHandlerFactory.addHandler(programCode, moduleName, version, path,
                                handler.getTitle(), handler.getContent(), handler.getState(), false);
                    }
                }
            }

            Map<String, Map<String, CfgRuntimeHandler>> modMap = diff.getModMap();
            if(modMap != null){
                for (String path : modMap.keySet()) {
                    for (String version : modMap.get(path).keySet()) {
                        CfgRuntimeHandler handler = modMap.get(path).get(version);
                        String programCode = String.valueOf(handler.getProgramId());//programs.get(handler.getProgramId()).getCode();
                        String moduleName = modules.get(handler.getModuleId()).getName();
                        SmartHandlerFactory.modifyHandler(programCode, moduleName, version, path,
                                handler.getTitle(), handler.getContent(), handler.getState(), false);
                    }
                }
            }

            Map<String, Map<String, CfgRuntimeHandler>> delMap = diff.getDelMap();
            if(delMap != null){
                for (String path : delMap.keySet()) {
                    for (String version : delMap.get(path).keySet()) {
                        CfgRuntimeHandler handler = delMap.get(path).get(version);
                        String programCode = String.valueOf(handler.getProgramId());//programs.get(handler.getProgramId()).getCode();
                        SmartHandlerFactory.deleteHandler(programCode,
                                path, version, handler.getTitle(), false);
                    }
                }
            }

            globalHandlersXmlCache = newGlobalHandlersXml;
        }

    }


    public static class MapTreeDiff<K1, K2, V>{
        private Map<K1, Map<K2, V>> addMap;
        private Map<K1, Map<K2, V>> modMap;
        private Map<K1, Map<K2, V>> delMap;
        public  static <K1, K2, V> MapTreeDiff<K1, K2, V> execute(Map<K1, Map<K2, V>> originMap, Map<K1, Map<K2, V>> targetMap, EqualFunc<V> equalFunc) {
            Map<K1, Map<K2, V>> addMap = null;
            Map<K1, Map<K2, V>> modMap = null;
            Map<K1, Map<K2, V>> delMap = null;
            Set<K1> targetK1s = targetMap.keySet();
            Set<K1> originK1s = originMap.keySet();
            for (K1 targetK1 : targetK1s) {
                if(!originMap.containsKey(targetK1)) {
                    addMap = putAndNewMapIfAbsent(addMap, targetK1, targetMap.get(targetK1));
                    continue;
                }
                Set<K2> targetK2s = targetMap.get(targetK1).keySet();
                Set<K2> originK2s = originMap.get(targetK1).keySet();
                for (K2 targetK2 : targetK2s) {
                    if(!originMap.get(targetK1).containsKey(targetK2)) {
                        addMap = putAndNewMapIfAbsent(addMap, targetK1, targetK2, targetMap.get(targetK1).get(targetK2));
                    }
                    V targetValue = targetMap.get(targetK1).get(targetK2);
                    V originValue = originMap.get(targetK1).get(targetK2);
                    if(!equalFunc.equal(originValue, targetValue)) {
                        modMap = putAndNewMapIfAbsent(modMap, targetK1, targetK2, targetMap.get(targetK1).get(targetK2));
                    }
                }

                Set<K2> delK2s = new HashSet(originK2s);
                delK2s.removeAll(targetK2s);
                for (K2 delK2 : delK2s) {
                    delMap = putAndNewMapIfAbsent(delMap, targetK1, delK2, originMap.get(targetK1).get(delK2));
                }
            }

            Set<K1> delK1s = new HashSet(originK1s);
            delK1s.removeAll(targetK1s);
            for (K1 delK1 : delK1s) {
                delMap = putAndNewMapIfAbsent(delMap, delK1, originMap.get(delK1));
            }

            MapTreeDiff<K1, K2, V> result = new MapTreeDiff<>();
            result.setAddMap(addMap);
            result.setModMap(modMap);
            result.setDelMap(delMap);
            return result;
        }

        public static  <K1, K2, V> Map<K1, Map<K2, V>> putAndNewMapIfAbsent(Map<K1, Map<K2, V>> target, K1 key, Map<K2, V> value) {
            if(target == null) target = new LinkedHashMap<>();
            target.put(key, value);
            return target;
        }

        public static  <K1, K2, V> Map<K1, Map<K2, V>> putAndNewMapIfAbsent(Map<K1, Map<K2, V>> target, K1 key1 , K2 key2, V value) {
            if(target == null) target = new LinkedHashMap<>();
            if(!target.containsKey(key1)) target.put(key1, new LinkedHashMap<K2, V>());
            target.get(key1).put(key2, value);
            return target;
        }

        public static interface EqualFunc<V>{
            public boolean equal(V v1, V v2);
        }

        public Map<K1, Map<K2, V>> getAddMap() {
            return addMap;
        }

        public void setAddMap(Map<K1, Map<K2, V>> addMap) {
            this.addMap = addMap;
        }

        public Map<K1, Map<K2, V>> getModMap() {
            return modMap;
        }

        public void setModMap(Map<K1, Map<K2, V>> modMap) {
            this.modMap = modMap;
        }

        public Map<K1, Map<K2, V>> getDelMap() {
            return delMap;
        }

        public void setDelMap(Map<K1, Map<K2, V>> delMap) {
            this.delMap = delMap;
        }

    }
}
