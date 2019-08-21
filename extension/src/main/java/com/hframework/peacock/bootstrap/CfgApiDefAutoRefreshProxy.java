package com.hframework.peacock.bootstrap;

import com.alibaba.fastjson.JSONObject;
import com.hframework.common.util.DateUtils;
import com.hframework.common.util.StringUtils;
import com.hframework.common.util.collect.CollectionUtils;
import com.hframework.common.util.collect.bean.Mapper;
import com.hframework.common.util.message.JsonUtils;
import com.hframework.smartweb.annotation.SmartApi;
import com.hframework.smartweb.annotation.SmartHolder;
import com.hframework.springext.requestmapping.SmartRequestMappingHandlerMapping;
import com.hframework.peacock.config.domain.model.CfgApiDef;
import com.hframework.peacock.config.service.interfaces.ICfgApiDefSV;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.core.LocalVariableTableParameterNameDiscoverer;
import org.springframework.stereotype.Component;
import org.springframework.web.context.ServletContextAware;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;

import javax.servlet.ServletContext;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by zhangquanhong on 2017/6/23.
 */
@Component
public class CfgApiDefAutoRefreshProxy implements ApplicationContextAware, ServletContextAware {
    private static final LocalVariableTableParameterNameDiscoverer parameterNameDiscoverer = new LocalVariableTableParameterNameDiscoverer();
    private static final Logger logger = LoggerFactory.getLogger(CfgApiDefAutoRefreshProxy.class);
    @Autowired
    private ICfgApiDefSV cfgApiDefSV;
    /**
     * 获取方法所有参数名
     * @param method
     * @return
     */
    public static String[] getParameterNames(Method method) {
        return parameterNameDiscoverer.getParameterNames(method);
    }
    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        logger.info("start refresh api def ...");
        try {
            List<CfgApiDef> cfgApiDefAll = cfgApiDefSV.getCfgApiDefAll();
            List<CfgApiDef> newCfgApiDefList = new ArrayList<>();
            Map<String, CfgApiDef> dbConfigs = CollectionUtils.convert(cfgApiDefAll, new Mapper<String, CfgApiDef>() {
                @Override
                public <K> K getKey(CfgApiDef cfgApiDef) {
                    return (K) (cfgApiDef.getPath() + ":" + cfgApiDef.getVersion() + ":" + cfgApiDef.getPropKey());
                }
            });
            Map<RequestMappingInfo, HandlerMethod> map = applicationContext.getBean(SmartRequestMappingHandlerMapping.class).getHandlerMethods();
            for (Map.Entry<RequestMappingInfo, HandlerMethod> m : map.entrySet()) {
                String url = m.getKey().getPatternsCondition().getPatterns().iterator().next();
                HandlerMethod method = m.getValue();
                SmartApi methodAnnotation = method.getMethodAnnotation(SmartApi.class);
                String path = url;
                String name = methodAnnotation != null ? methodAnnotation.name() : "";
                String version = methodAnnotation != null ? methodAnnotation.version() : "";
                String[] parameterNames = getParameterNames(method.getMethod());
                for (int i=0;i<parameterNames.length;i++){
                    SmartHolder annotation = method.getMethodParameters()[i].getParameterAnnotation(SmartHolder.class);
                    if(annotation != null) {
                        String propKey = StringUtils.isNotBlank(annotation.name()) ? annotation.name() : parameterNames[i];
                        String propType = String.class.equals(method.getMethod().getParameterTypes()[i]) ? "string" : "integer";
                        String propOptions = null;

                        if(annotation.options().length != 0) {
                            JSONObject jsonObject = new JSONObject(true);
                            for (String info : annotation.options()) {
                                jsonObject.put(info, info);
                            }
                            propOptions = jsonObject.toJSONString();
                        }

                        String propDescription = "";
                        if(StringUtils.isNotBlank(annotation.description())) {
                            if(StringUtils.isNotBlank(propDescription)) {
                                propDescription += "，";
                            }
                            propDescription += annotation.description().trim();
                        }
                        if(StringUtils.isNotBlank(annotation.defaultValue())) {
                            if(StringUtils.isNotBlank(propDescription)) {
                                propDescription += "，";
                            }
                            propDescription += "默认值：" + annotation.defaultValue().trim();
                        }

                        String configKey = path + ":" + version + ":" + propKey;
                        CfgApiDef cfgApiDef = new CfgApiDef();
                        cfgApiDef.setPath(path);
                        cfgApiDef.setName(name);
                        cfgApiDef.setVersion(version);
                        cfgApiDef.setPropKey(propKey);
                        cfgApiDef.setPropType(propType);
                        cfgApiDef.setPropOptions(propOptions);
                        cfgApiDef.setPropDescription(propDescription);
                        if(!dbConfigs.containsKey(configKey)) {
                            cfgApiDef.setCtime((int) (System.currentTimeMillis() / 1000L));
                            newCfgApiDefList.add(cfgApiDef);
                        }else {
                            CfgApiDef dbConfig = dbConfigs.get(configKey);
                            if(isDiff(dbConfig.getName(), name)
                                    || isDiff(dbConfig.getPropType(), propType)
                                    || isDiff(dbConfig.getPropOptions(), propOptions)
                                    || isDiff(dbConfig.getPropDescription(), propDescription)){
                                cfgApiDef.setId(dbConfig.getId());
                                String newRemark = dbConfig.getRemark();
                                if(StringUtils.isNotBlank(newRemark)) {
                                    newRemark += "\n";
                                }
                                newRemark +=(DateUtils.getCurrentDateYYYYMMDDHHMMSS() + " : " +
                                        dbConfig.getName() + ", " + dbConfig.getPropType() + ", "
                                        + dbConfig.getPropOptions() + ", " + dbConfig.getPropDescription() + ";");
                                cfgApiDef.setRemark(newRemark);
                                cfgApiDef.setMtime((int) (System.currentTimeMillis() / 1000L));
                                newCfgApiDefList.add(cfgApiDef);
                            }
                        }
                    }
                }
            }

            if(newCfgApiDefList.size() > 0) {
                logger.info("start refresh api def data => " + JsonUtils.writeValueAsString(newCfgApiDefList));
                cfgApiDefSV.batchOperate(newCfgApiDefList.toArray(new CfgApiDef[0]));
            }
            logger.info("start refresh api def finish !");
        } catch (Exception e) {
            logger.error("start refresh api def error => " + ExceptionUtils.getFullStackTrace(e));
            e.printStackTrace();
        }

    }

    private boolean isDiff(String name, String name1) {
        return false;
    }

    @Override
    public void setServletContext(ServletContext servletContext) {

    }
}
