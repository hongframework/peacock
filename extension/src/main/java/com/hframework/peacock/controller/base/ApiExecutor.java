package com.hframework.peacock.controller.base;

import com.hframework.beans.exceptions.BusinessException;
import com.hframework.common.util.message.JsonUtils;
import com.hframework.peacock.controller.base.descriptor.ResultDescriptor;
import com.hframework.peacock.controller.base.descriptor.ResultTreeDescriptor;
import com.hframework.smartweb.SmartParameterInvoker;
import com.hframework.smartweb.bean.ArrayParam;
import com.hframework.smartweb.bean.SmartChecker;
import com.hframework.smartweb.bean.SmartMessage;
import com.hframework.smartweb.bean.SmartParser;
import com.hframework.smartweb.bean.handler.HandlerHelper;
import com.hframework.smartweb.exception.SmartHandlerException;
import com.hframework.tracer.PeacockSampler;
import com.hframework.tracer.PeacockSpan;
import com.hframework.tracer.TracerFactory;
import com.hframework.peacock.controller.base.dc.DC;
import com.hframework.peacock.controller.base.dc.DCUtils;
import com.hframework.peacock.controller.base.descriptor.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

/**
 * Created by zhangquanhong on 2017/11/15.
 */
public class ApiExecutor {
    private static final Logger logger = LoggerFactory.getLogger(ApiExecutor.class);

    private ApiDescriptor apiDescriptor;



    public ApiExecutor(ApiDescriptor apiDescriptor){
        this.apiDescriptor = apiDescriptor;
        initialize();
    }

    public String getApiPath(){
        return this.apiDescriptor.getPath();
    }

    public Long getApiProgram(){
        return Long.valueOf(apiDescriptor.getProgram());
    }

    private void initialize() {
//        List<Parameter> parameterList = apiDescriptor.getApiConf().getParameters().getParameterList();
//        for (Parameter parameter : parameterList) {
//            parameterDescriptors.add(new ParameterDescriptor(parameter));
//        }
    }


    public SmartMessage execute(HttpServletRequest request, HttpServletResponse response){
        PeacockSampler.reqTL.set(request.getQueryString());
        PeacockSpan root = TracerFactory.startNewTrace(apiDescriptor.getProgram(), apiDescriptor.getPath(), apiDescriptor.getTitle(), apiDescriptor.getPath());

        try {
            SmartMessage smartMessage = executeInternal(request, response, root);
            TracerFactory.finishCurrentSpan(root);
            return smartMessage;
        } catch (ExecutionException e) {
            TracerFactory.errorCurrentSpan(root, e);
            if(e.getCause() instanceof BusinessException) {
                throw (BusinessException)e.getCause();
            }else{
                e.printStackTrace();
            }
        } catch (RuntimeException e) {
            TracerFactory.errorCurrentSpan(root, e);
            e.printStackTrace();
            throw  e;
        }catch (Exception e) {
            TracerFactory.errorCurrentSpan(root, e);
            e.printStackTrace();
        }
        return null;
    }

    public SmartMessage executeInternal(HttpServletRequest request, HttpServletResponse response, PeacockSpan rootSpan) throws Exception {

        Object result = ApiInvokeData.getInstance(this, request, response)
                .setRootSpan(rootSpan)
                .parseParameter(true)
                .preHandle()
                .preCheck()
                .handle()
                .result();
        String json =  toJson(result);
        logger.debug("json result : {}", json);
        TracerFactory.addTag(rootSpan, TracerFactory.TagKey.RESPONSE, json, rootSpan.hasApiResponseTag());
        return SmartMessage.valueOf(json, SmartMessage.MessageType.Json);
    }

    public DC execute(DC requestDC, HttpServletRequest request,
                                             HttpServletResponse response, PeacockSpan span){
        try {
            return executeInternal(requestDC,request, response, span);
        }  catch (ExecutionException e) {
            if(e.getCause() instanceof BusinessException) {
                throw (BusinessException)e.getCause();
            }else if(e.getCause() instanceof SmartHandlerException) {
                throw (SmartHandlerException)e.getCause();
            }else{
                e.printStackTrace();
            }
        } catch (SmartHandlerException e) {
            throw e;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public DC executeInternal(DC requestDC, HttpServletRequest request,
                                        HttpServletResponse response, PeacockSpan span) throws Exception {
        //可能存在joinSpan
        PeacockSpan root = span != null ? span : TracerFactory.startNewTrace(
                apiDescriptor.getProgram(), apiDescriptor.getPath(), apiDescriptor.getName(), apiDescriptor.getPath());

        try {
            DC result = ApiInvokeData.getInstance(this, request, response, requestDC)
                    .setRootSpan(root)
                    .parseParameter(false)
                    .preHandle()
                    .preCheck()
                    .handle()
                    .resultMergeAndFormat();
            if(span == null) {
                TracerFactory.finishCurrentSpan(root);
            }
            return result;
        }catch (Exception e) {
            TracerFactory.errorCurrentSpan(root, e);
            throw e;
        }
    }

    private String toJson(Object result) {
//        return JSON.toJSONString(result);
        try {
            return JsonUtils.writeValueAsString(result);
        } catch (IOException e) {
            e.printStackTrace();
            throw new SmartHandlerException("object to json error !");
        }
    }

    public List<ParameterDescriptor> getParameterDescriptors() {

        return apiDescriptor.getParameterDescriptors();
    }

    public ResultTreeDescriptor getResultStruct() {

        return apiDescriptor.getResultStruct();
    }

    public ResultDescriptor getResultDescriptor(){
        return apiDescriptor.getResult();
    }

    public HandlersDescriptor getPreHandlers() {
        return apiDescriptor.getPreHandlers();
    }

    public HandlersDescriptor getHandlers() {
        return apiDescriptor.getHandlers();
    }


    public Map<String, Object> parseParameter(HttpServletRequest request, DC requestMapList) throws IllegalAccessException, InstantiationException, NoSuchMethodException, InvocationTargetException {
        Map<String, Object> parameterKVPair = new LinkedHashMap<>();
        for (ParameterDescriptor parameter : getParameterDescriptors()) {
            Object parameterValue = getParameterValue(request, requestMapList, parameter.getName(), parameter.getType());
            parameterValue = setDefaultValueIfNull(parameterValue, parameter.getDefaultValue());
            parameterValue = parseValueIfTypeMissMatch(parameter.getName(), parameterValue, parameter.getType(), parameter.getParser(), parameter.getPattern());

            if(parameterValue == null || parameterValue instanceof String) {
                Class parameterType = parameter.getType();
                String parameterName = parameter.getName();
                //参数校验
                //必选
                SmartParameterInvoker.checkRequiredPass((String) parameterValue, parameter.isRequired(), parameterName);
                SmartParameterInvoker.checkTypePass(parameterType, (String) parameterValue, parameterName);
                SmartParameterInvoker.checkEnumsPass((String) parameterValue, parameter.getEnums(), parameterName);
                SmartParameterInvoker.checkOptionsPass((String) parameterValue, parameter.getOptions(), parameterName);
                SmartParameterInvoker.checkMinMaxPass((String) parameterValue, parameter.getMin(),
                        parameter.getMax(), parameterName);
                SmartParameterInvoker.checkRegexPass((String) parameterValue, parameter.getRegex(), parameterName);
                if(parameter.getChecker() != null) {
                    for (Map.Entry<Class<? extends SmartChecker>, String> checkerInfo : parameter.getChecker().entrySet()) {
                        SmartParameterInvoker.checker(parameterName, (String)parameterValue, checkerInfo.getKey(), checkerInfo.getValue());
                    }
                }
            }
            parameterKVPair.put(parameter.getName(), parameterValue);
        }
        return parameterKVPair;
    }
    private Object parseValueIfTypeMissMatch(String parameterName, Object parameterValue, Class type, Class<? extends SmartParser> parser, String pattern) throws InstantiationException, IllegalAccessException {
        if(parameterValue == null || !(parameterValue instanceof String)) return parameterValue;
        return SmartParameterInvoker.parser(type, (String) parameterValue, parser, pattern, parameterName);
    }

    private Object setDefaultValueIfNull(Object parameterValue, String defaultValue) {
        if(parameterValue != null) {
            return parameterValue;
        }
        return defaultValue;
    }

    public static Object getParameterValue(HttpServletRequest request, DC requestDC, String parameterName, Class type){

        if(DCUtils.isNullDC(requestDC)) {
            //获取请求参数
            String[] paramValues = request.getParameterValues(parameterName);
            if (paramValues != null) {
                return paramValues.length == 1 ? paramValues[0] : paramValues;
            }else {
                return null;
            }
        }else if(DCUtils.isMapDC(requestDC)){
            Object fetchObject = requestDC.fetch(parameterName);
            if(HandlerHelper.isArray(fetchObject) && type.isArray()) {
                return ArrayParam.valueOf(fetchObject);
            }else {
                return requestDC.fetch(parameterName);
            }

        }else {
            return requestDC.fetch(parameterName);
        }
    }
}
