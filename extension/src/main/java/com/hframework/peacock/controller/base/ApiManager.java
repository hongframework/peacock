package com.hframework.peacock.controller.base;

import com.hframework.smartweb.SmartExpanderFactory;
import com.hframework.smartweb.bean.SmartMessage;
import com.hframework.tracer.PeacockSpan;
import com.hframework.peacock.controller.base.dc.DC;
import com.hframework.peacock.controller.base.descriptor.ApiDescriptor;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by zhangquanhong on 2017/11/15.
 */
public class ApiManager {
    private static final Logger logger = LoggerFactory.getLogger(ApiManager.class);

    private static ApiConfigureRegistry apiRegistry = ApiConfigureRegistry.getDefaultInstance();
    private static ApiConfigureRegistry handlerRegistry = ApiConfigureRegistry.getHandlerInstance();

    public static SmartMessage invoke(String program, String module, String name, String version, HttpServletRequest request, HttpServletResponse response) {
        ApiDescriptor apiDescriptor = StringUtils.isBlank(program) ?
                apiRegistry.findApiDescriptor(module, name, version)
                : apiRegistry.findApiDescriptor(program, module, name, version);
        SmartExpanderFactory.setDynResultVOKeyMeta(apiDescriptor.getProgram());
        ApiExecutor executor = apiRegistry.getExecutor(apiDescriptor);
        return executor.execute(request, response);
    }

    public static ApiDescriptor findHandlerDescriptor(String program, String module, String name, String version){
        return handlerRegistry.findApiDescriptor(program, module, name, version);
    }

    public static ApiDescriptor findApiDescriptorByPath(String program, String path, String version){
        return apiRegistry.findApiDescriptorByPath(program, path, version);
    }

    public static ApiDescriptor findHandlerDescriptorByPath(String program, String path, String version){
        return handlerRegistry.findApiDescriptorByPath(program, path, version);
    }

    public static ApiDescriptor findApiDescriptor(String program, String module, String name, String version){
        return apiRegistry.findApiDescriptor(program, module, name, version);
    }



    public static DC invokeHandler(String program, String module, String name, String version, DC requestDC, HttpServletRequest request, HttpServletResponse response, PeacockSpan span) {
        ApiDescriptor apiDescriptor = StringUtils.isBlank(program) ?
                handlerRegistry.findApiDescriptor(module, name, version) :
                handlerRegistry.findApiDescriptor(program, module, name, version);
        ApiExecutor executor = handlerRegistry.getExecutor(apiDescriptor);
        SmartExpanderFactory.setDynResultVOKeyMeta(apiDescriptor.getProgram());
        return executor.execute(requestDC,request, response, span);
    }

    public static DC invokeHandler(String program, String path, String version, DC requestDC, HttpServletRequest request, HttpServletResponse response, PeacockSpan span) {
        String module = path.substring(1).substring(0, path.substring(1).indexOf("/"));
        String name = path.substring(1).substring(path.substring(1).indexOf("/") + 1);
        return invokeHandler(program, module, name, version, requestDC, request, response, span);

    }
}
