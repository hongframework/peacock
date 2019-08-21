package com.hframework.peacock.controller.base.descriptor;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.hframework.smartweb.bean.ApiConf;
import com.hframework.smartweb.bean.apiconf.Handler;
import com.hframework.smartweb.bean.apiconf.Parameter;
import com.hframework.smartweb.bean.apiconf.Result;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhangquanhong on 2017/11/15.
 */
public class ApiDescriptor{

    private static Logger logger = LoggerFactory.getLogger(ApiDescriptor.class);

    public static XStream apiConfParser = new XStream(new DomDriver());
    static {
        apiConfParser.processAnnotations(ApiConf.class);
        apiConfParser.aliasSystemAttribute("BEAN_CLASS", "class");
    }
    private Long id;//配置ID
    private String program;//项目
    private String module;//模块
    private String name;//名称
    private String version;//版本
    private ApiState state = ApiState.INITIALIZE;//状态

    private String title;// 标题
    private String description;// 描述

    private String path;//访问路径

    private ApiConf apiConf;//api配置

    private String apiConfXml;//api配置XML


    private RequestDescriptor request;

    private ResponseDescriptor response;

    private ParametersDescriptor parameters;

    private HandlersDescriptor preHandlers;

    private HandlersDescriptor handlers;

    private ResultDescriptor result;

    private ResultTreeDescriptor resultStruct;

    public ApiDescriptor() {
    }

    public ApiDescriptor(Long id, String program, String module, String name, String version, ApiState state,
                         String title, String description, String path, String apiConfXml) {
        this.id = id;
        this.program = program;
        this.module = module;
        this.name = name;
        this.version = version;
        this.state = state;
        this.title = title;
        this.description = description;
        this.path = path;
        this.apiConfXml = apiConfXml;
    }


//    public ApiDescriptor(Long id, String program, String module, String name, String version, ApiState state,
//                         String title, String description, String path, ApiConf apiConf) {
//        this.id = id;
//        this.program = program;
//        this.module = module;
//        this.name = name;
//        this.version = version;
//        this.state = state;
//        this.title = title;
//        this.description = description;
//        this.path = path;
//        this.apiConf = apiConf;
//    }

    public void initialize() throws ClassNotFoundException {

       apiConf = (ApiConf) apiConfParser.fromXML(apiConfXml);

        if(apiConf.getParameters() != null && apiConf.getParameters().getParameterList() != null
                && apiConf.getParameters().getParameterList().size() > 0) {
            parameters = new ParametersDescriptor(apiConf.getParameters().getParameterList());
        }else {
            parameters = new ParametersDescriptor(new ArrayList<Parameter>());
        }

        flag: if(apiConf.getPrehandlerList() != null  && apiConf.getPrehandlerList().size() > 0) {
            List<Handler> prehandlerList = apiConf.getPrehandlerList();
            if(prehandlerList.size() == 1 && prehandlerList.get(0).getClazz() == null && prehandlerList.get(0).getPath() == null) {
                break flag;
            }
            preHandlers = new HandlersDescriptor(program, prehandlerList);
            preHandlers.initHandlerDependAndTrigger(parameters);
            preHandlers.check();
        }

        flag:if(apiConf.getHandlers() != null && apiConf.getHandlers().getHandlerList() != null
                && apiConf.getHandlers().getHandlerList().size() > 0) {
            List<Handler> handlerList = apiConf.getHandlers().getHandlerList();
            if(handlerList.size() == 1 && handlerList.get(0).getClazz() == null && handlerList.get(0).getPath() == null) {
                break flag;
            }
            handlers = new HandlersDescriptor(program, handlerList, preHandlers);
            handlers.initHandlerDependAndTrigger(parameters);
            handlers.check();
        }
        if(apiConf.getResults() == null || apiConf.getResults().getResultList() == null){
            result = new ResultDescriptor(new ArrayList<Result>(), null);
        }else {
            result = new ResultDescriptor(apiConf.getResults().getResultList(), null);
        }

        resultStruct = new ResultTreeDescriptor(ResultTreeDescriptor.ROOT_PATH,ResultTreeDescriptor.ROOT_NAME);

        if(handlers != null && handlers.getHandlers() != null) {
            for (HandlerDescriptor handlerDescriptor : handlers.getHandlers()) {
                resultStruct.add(handlerDescriptor);
            }
        }

//        resultStruct.add(result);
    }

    public String getModule() {
        return module;
    }

    public String getProgram() {
        return program;
    }

    public void setModule(String module) {
        this.module = module;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public ApiState getState() {
        return state;
    }

    public void setState(ApiState state) {
        this.state = state;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public ApiConf getApiConf() {
        return apiConf;
    }

    public void setApiConf(ApiConf apiConf) {
        this.apiConf = apiConf;
    }

    @JsonIgnore
    public ResultDescriptor getResult() {
        return result;
    }

    private static class RequestDescriptor{

    }
    private static class ResponseDescriptor{

    }

    public ResultTreeDescriptor getResultStruct() {
        return resultStruct;
    }

    @JsonIgnore
    public List<ParameterDescriptor> getParameterDescriptors() {
        return parameters.getParameters();
    }

    @JsonIgnore
    public HandlersDescriptor getPreHandlers() {
        return preHandlers;
    }

    @JsonIgnore
    public HandlersDescriptor getHandlers() {
        return handlers;
    }

    public Long getId() {
        return id;
    }

    /** api 状态 */
    public static enum ApiState{
        INITIALIZE, //初始化
        TESTING, //测试中
        IN_USER, //使用中
        INVALID; //已失效

        public static ApiState parseFrom(Object value){
            short state = Short.valueOf(String.valueOf(value));
            switch (state){
                case 0:
                    return INITIALIZE;
                case 1:
                    return TESTING;
                case 2:
                    return IN_USER;
                case 3:
                    return INVALID;
                default:
                    return INITIALIZE;
            }
        }

        public String toText(){
            if(INITIALIZE == this) {
                return "开发中";
            }
            if(TESTING == this) {
                return "测试中";
            }
            if(IN_USER == this) {
                return "运行中";
            }
            if(INVALID == this) {
                return "已失效";
            }
            return "已过时";
        }
    }

    public String getApiConfXml() {
        return apiConfXml;
    }

    public void setApiConfXml(String apiConfXml) {
        this.apiConfXml = apiConfXml;
    }
}




