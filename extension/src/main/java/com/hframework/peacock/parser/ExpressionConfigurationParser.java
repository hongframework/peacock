package com.hframework.peacock.parser;

import com.google.common.collect.Lists;
import com.hframework.strategy.rule.ExpressInvoker;
import com.hframework.strategy.rule.ExpressionEngine;
import com.hframework.strategy.rule.data.EDataSet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.context.request.WebRequest;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by zhangquanhong on 2017/2/24.
 */
public class ExpressionConfigurationParser extends ConfigurationParser {
    private static final Logger logger = LoggerFactory.getLogger(ExpressionConfigurationParser.class);
    protected static final Map<String, ExpressInvoker> rules = new HashMap<>();
    public ExpressionConfigurationParser() throws Exception {
        super.config.ok();
    }


    protected ExpressInvoker getExpressInvoker(String path, String version, String key, Map<String, String> parameters, WebRequest request) {
        String config =  super.parse(path, version, key, parameters, request);
        if(!rules.containsKey(config)) {
            ExpressInvoker invoker = ExpressionEngine.loadAndExplain(config);
            rules.put(config, invoker);
        }
        return rules.get(config);
    }

    @Override
    public String parse(String path, String version, String key, Map<String, String> parameters, WebRequest request) {
        String config =  super.parse(path, version, key, parameters, request);
        if(!rules.containsKey(config)) {
            ExpressInvoker invoker = ExpressionEngine.loadAndExplain(config);
            rules.put(config, invoker);
        }
        EDataSet result = rules.get(config).invoke(new EDataSet(Lists.newArrayList(parameters)));
        logger.info("[path={}, version={}, key={}, config={} ] parameters = {}, label = {} ",
                path, version, key, config, parameters, result.getLabel()[0]);
        return result.getLabel()[0];
    }
}
