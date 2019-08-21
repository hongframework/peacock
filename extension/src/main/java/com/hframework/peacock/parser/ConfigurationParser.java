package com.hframework.peacock.parser;

import com.hframework.common.frame.ServiceFactory;
import com.hframework.monitor.ConfigMonitor;
import com.hframework.smartweb.bean.parser.AbstractHolderParser;
import com.hframework.peacock.config.domain.model.CfgApiConf;
import com.hframework.peacock.config.service.interfaces.ICfgApiConfSV;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.context.request.WebRequest;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by zhangquanhong on 2017/2/24.
 */
public class ConfigurationParser extends AbstractHolderParser<String> {
    private static final Logger logger = LoggerFactory.getLogger(ConfigurationParser.class);

    protected static ConfigMonitor<Map<String, Map<String, String>>> config =  new ConfigMonitor<Map<String, Map<String, String>>>(3) {
        @Override
        public Map<String, Map<String, String>> fetch() throws Exception {
            List<CfgApiConf> allConfig = ServiceFactory.getService(ICfgApiConfSV.class).getCfgApiConfAll();
            Map<String, Map<String, String>> configs = new LinkedHashMap();
            for (CfgApiConf cfgApiConf : allConfig) {
                if((byte)1 == cfgApiConf.getState()) {
                    String path = cfgApiConf.getPath();
                    String version = cfgApiConf.getVersion();
                    String propKey = cfgApiConf.getPropKey();
//                    String propValue = cfgApiConf.getPropValue().replaceAll("\\s", "");
                    String propValue = cfgApiConf.getPropValue().trim();
                    String configKey = path + ":" + version;
                    if(!configs.containsKey(configKey)) configs.put(configKey, new LinkedHashMap<String, String>());
                    logger.debug("store => {}|{}|{}",configKey, propKey, propValue);
                    configs.get(configKey).put(propKey, propValue);
                }
            }
            return configs;
        }
    };

    public ConfigurationParser() throws Exception {
        config.ok();
    }

    @Override
    public String parse(String path, String version, String key, Map<String, String> parameters, WebRequest request) {
        path = path.replaceAll("[/]+", "/");
        String result = null;
        if(config.getObject() != null && config.getObject().containsKey(path + ":" + version)) {
             result = config.getObject().get(path + ":" + version).get(key);
        }
        logger.info("[path={}, version={}, key={} ] config = {} ",
                path, version, key, result);
        return result;
    }
}
