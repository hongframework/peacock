package com.hframework.springext.argument;

import com.alibaba.fastjson.JSONArray;
import com.hframework.common.frame.ServiceFactory;
import com.hframework.smartweb.*;
import com.hframework.smartweb.annotation.*;
import com.hframework.smartweb.bean.SmartHandler;
import com.hframework.smartweb.bean.handler.HandlerHelper;
import com.hframework.smartweb.exception.SmartHandlerException;
import org.apache.commons.lang3.StringUtils;
import org.springframework.core.MethodParameter;
import org.springframework.core.convert.ConversionService;
import org.springframework.core.convert.TypeDescriptor;
import org.springframework.web.bind.annotation.ValueConstants;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.AbstractNamedValueMethodArgumentResolver;
import org.springframework.web.method.support.UriComponentsContributor;
import org.springframework.web.util.UriComponentsBuilder;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.IOException;
import java.lang.reflect.Array;
import java.lang.reflect.Method;
import java.util.*;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

/**
 * Created by zhangquanhong on 2017/2/23.
 */
public class SmartBodyResolver extends AbstractNamedValueMethodArgumentResolver
        implements UriComponentsContributor {

    private static final TypeDescriptor STRING_TYPE_DESCRIPTOR = TypeDescriptor.valueOf(String.class);

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        Class<?> paramType = parameter.getParameterType();
        if (parameter.hasParameterAnnotation(SmartBody.class)) {
            if (Map.class.isAssignableFrom(paramType)) {
                String paramName = parameter.getParameterAnnotation(SmartBody.class).name();
                return StringUtils.isNoneBlank(paramName);
            }
            else {
                return true;
            }
        }

        return false;
    }


    @Override
    protected NamedValueInfo createNamedValueInfo(MethodParameter parameter) {
        SmartBody annotation = parameter.getParameterAnnotation(SmartBody.class);
        if(annotation == null) return new NamedValueInfo("", false, ValueConstants.DEFAULT_NONE);
        return new NamedValueInfo(annotation.name(), annotation.required(), "");
    }

    @Override
    protected Object resolveName(String name, MethodParameter parameter, NativeWebRequest webRequest) throws Exception {
        String bodyMessage = readBodyMessage(webRequest);
        System.out.println("request body : " + bodyMessage);
        SmartBody annotation = parameter.getParameterAnnotation(SmartBody.class);
        if(StringUtils.isBlank(bodyMessage)) {
            return null;
        }
        if(parameter.getParameterType().isArray()) {
            Class<?> objectType = parameter.getParameterType().getComponentType();
            if(annotation.type() == SmartBody.Type.JSON) {
                JSONArray array = JSONArray.parseArray(bodyMessage);
                Object result = Array.newInstance(objectType, array.size());
                for (int i = 0; i < array.size(); i++) {
                    if(objectType.isArray()){
                        JSONArray row = null;
                        if(array.get(i) instanceof String) {
                            row = JSONArray.parseArray(String.valueOf(array.get(i)));
                        }else {
                            row = (JSONArray)array.get(i);
                        }
                        Array.set(result, i, row.toArray());
                    }
                }
                return result;
            }
        }
        return null;
    }

    public static String readBodyMessage(NativeWebRequest webRequest) throws IOException {
        HttpServletRequest req = webRequest.getNativeRequest(HttpServletRequest.class);
        BufferedReader br = null;
        try{
            br = req.getReader();
            String inputLine;
            String msg = "";
            while ((inputLine = br.readLine()) != null) {
                msg += inputLine;
            }
            return msg;
        }catch (Exception e) {
            throw e;
        }finally {
            if(br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                }
            }
        }
    }

    private void throwParameterCheckException(String parameterName, String errorInfo) {
        throw new SmartHandlerException(APIErrorType.PARAMETER_ERROR, "<" + parameterName + "> " + errorInfo.trim() + " !");
    }

    @Override
    protected void handleMissingValue(String name, MethodParameter parameter) throws ServletException {
        throwParameterCheckException(name, " is required");
    }



    @Override
    public void contributeMethodArgument(MethodParameter parameter, Object value, UriComponentsBuilder builder, Map<String, Object> uriVariables, ConversionService conversionService) {
        Class<?> paramType = parameter.getParameterType();
        if (Map.class.isAssignableFrom(paramType)) {
            return;
        }

        SmartBody ann = parameter.getParameterAnnotation(SmartBody.class);
        String name = (ann == null || org.springframework.util.StringUtils.isEmpty(ann.name()) ? parameter.getParameterName() : ann.name());

        if (value == null) {
            builder.queryParam(name);
        }
        else if (value instanceof Collection) {
            for (Object element : (Collection<?>) value) {
                element = formatUriValue(conversionService, TypeDescriptor.nested(parameter, 1), element);
                builder.queryParam(name, element);
            }
        }
        else {
            builder.queryParam(name, formatUriValue(conversionService, new TypeDescriptor(parameter), value));
        }
    }

    protected String formatUriValue(ConversionService cs, TypeDescriptor sourceType, Object value) {
        if (value == null) {
            return null;
        }
        else if (value instanceof String) {
            return (String) value;
        }
        else if (cs != null) {
            return (String) cs.convert(value, sourceType, STRING_TYPE_DESCRIPTOR);
        }
        else {
            return value.toString();
        }
    }
}
