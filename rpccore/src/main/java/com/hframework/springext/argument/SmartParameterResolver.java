package com.hframework.springext.argument;

import com.hframework.smartweb.annotation.*;
import com.hframework.smartweb.bean.SmartHandler;
import com.hframework.smartweb.bean.handler.HandlerHelper;
import com.hframework.smartweb.APIErrorType;
import com.hframework.smartweb.exception.SmartHandlerException;
import com.hframework.common.frame.ServiceFactory;
import com.hframework.smartweb.LogHelper;
import com.hframework.smartweb.SmartHandlerFactory;
import com.hframework.smartweb.SmartParameterInvoker;
import com.hframework.smartweb.SmartThreadPoolExecutorFactory;
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
import java.lang.reflect.Method;
import java.util.*;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

/**
 * Created by zhangquanhong on 2017/2/23.
 */
public class SmartParameterResolver extends AbstractNamedValueMethodArgumentResolver
        implements UriComponentsContributor {

    private ExecutorService executorService = SmartThreadPoolExecutorFactory.getExecutorServiceObject();
    public static final String SMART_WEB_PARAMETER_PREFIX = "hframework.smartweb.parameters.";
    public static final String SMART_WEB_ALL_PARAMETERS = "hframework.smartweb.all.parameters";

    private static final TypeDescriptor STRING_TYPE_DESCRIPTOR = TypeDescriptor.valueOf(String.class);

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        Class<?> paramType = parameter.getParameterType();
        if (parameter.hasParameterAnnotation(SmartParameter.class)) {
            if (Map.class.isAssignableFrom(paramType)) {
                String paramName = parameter.getParameterAnnotation(SmartParameter.class).name();
                return StringUtils.isNoneBlank(paramName);
            }
            else {
                return true;
            }
        }

        if (parameter.hasParameterAnnotation(SmartHolder.class)){
            return true;
        }

        return false;
    }


    @Override
    protected NamedValueInfo createNamedValueInfo(MethodParameter parameter) {
        SmartParameter annotation = parameter.getParameterAnnotation(SmartParameter.class);
        if(annotation == null) return new NamedValueInfo("", false, ValueConstants.DEFAULT_NONE);
        return new NamedValueInfo(annotation.name(), annotation.required(), annotation.defaultValue());
    }

    @Override
    protected Object resolveName(String name, MethodParameter parameter, NativeWebRequest webRequest) throws Exception {



        Object arg = null;
        //获取请求参数
        String[] paramValues = webRequest.getParameterValues(name);
        if (paramValues != null) {
            arg = paramValues.length == 1 ? paramValues[0] : paramValues;
        }

        //参数解析
        SmartParameter annotation = parameter.getParameterAnnotation(SmartParameter.class);
        SmartHolder smartHolder = parameter.getParameterAnnotation(SmartHolder.class);
        if(arg == null &&  smartHolder != null) {
            SmartApi smartApi = parameter.getMethod().getAnnotation(SmartApi.class);
            String path = ((HttpServletRequest)webRequest.getNativeRequest()).getRequestURI();
            Map parameters = (Map) webRequest.getAttribute(SMART_WEB_ALL_PARAMETERS,WebRequest.SCOPE_REQUEST);
            if(parameters == null) parameters = new HashMap();
            arg = SmartParameterInvoker.holderParser(path,smartApi != null ? smartApi.version() : null,StringUtils.isNotBlank(smartHolder.name()) ? smartHolder.name() : name, smartHolder.parser(), parameters, webRequest);
            if(arg == null && !smartHolder.defaultValue() .equals( ValueConstants.DEFAULT_NONE)) arg = smartHolder.defaultValue();
        }else {
            if(arg == null && !ValueConstants.DEFAULT_NONE.equals(annotation.defaultValue())) {
                arg = annotation.defaultValue();
            }
            if(arg != null && arg instanceof String){
                arg = SmartParameterInvoker.parser(parameter.getParameterType(), (String) arg, annotation.parser(), annotation.pattern().getPattern(), name);
            }

            Class<?> parameterType = parameter.getParameterType();

            if(arg == null || arg instanceof String) {
                String parameterValue = (String) arg;

                //参数校验
                //必选
                SmartParameterInvoker.checkRequiredPass(parameterValue, annotation.required(), name);
                SmartParameterInvoker.checkTypePass(parameterType, parameterValue, name);
                SmartParameterInvoker.checkEnumsPass(parameterValue, annotation.enums(), name);
                SmartParameterInvoker.checkOptionsPass(parameterValue, annotation.options(), name);
                SmartParameterInvoker.checkMinMaxPass(parameterType, parameterValue, annotation.min(), annotation.max(), name);
                SmartParameterInvoker.checkRegexPass(parameterValue, annotation.regex(), name);
                SmartParameterInvoker.checker(name, parameterValue, annotation.checker(), annotation.pattern().getPattern());
            }
        }
        SmartResult methodAnnotation = parameter.getMethodAnnotation(SmartResult.class);
        int deep = 0;
        doExpandIfNecessary(methodAnnotation, parameter, arg, webRequest, deep);


        LogHelper.mainStart(LogHelper.Type.Statistics);
        if(webRequest.getAttribute(SMART_WEB_ALL_PARAMETERS,WebRequest.SCOPE_REQUEST) == null ){
            webRequest.setAttribute(SMART_WEB_ALL_PARAMETERS, new HashMap<String, Object>(), WebRequest.SCOPE_REQUEST);
        }
        ((HashMap<String, Object>)webRequest.getAttribute(SMART_WEB_ALL_PARAMETERS,WebRequest.SCOPE_REQUEST)).put(name, arg);
        return arg;
    }

    /**
     * 进行扩展判定（如果满足条件的话），进行扩展异步线程启动处理
     * @param methodAnnotation
     * @param parameter
     * @param parameterValue
     * @param webRequest
     * @param deep
     * @throws InterruptedException
     * @throws ExecutionException
     * @throws InstantiationException
     * @throws IllegalAccessException
     */
    private void doExpandIfNecessary(SmartResult methodAnnotation, MethodParameter parameter, Object parameterValue,
                                     NativeWebRequest webRequest, int deep) throws InterruptedException, ExecutionException,
            InstantiationException, IllegalAccessException, NoSuchMethodException {

        if(deep > 8) {
            throw new SmartHandlerException(APIErrorType.SERVER_ERROR, "parameter' resolver " +
                    "execute expend, but depend deep value greater than 8,  please check");
        }

        if( methodAnnotation != null && methodAnnotation.expand().length > 0){
            String paramName = parameter.getParameterName();
            for (Expand expand : methodAnnotation.expand()) {
                String[] attrs = expand.attr();
                Arrays.sort(attrs);//binarySearch之前需要排序
                Object attribute = webRequest.getAttribute(SMART_WEB_PARAMETER_PREFIX, WebRequest.SCOPE_REQUEST);
                if(attribute == null) {
                    webRequest.setAttribute(SMART_WEB_PARAMETER_PREFIX , new HashMap<String, Object>(), WebRequest.SCOPE_REQUEST);
                }
                Map<String, Object> container =(HashMap<String, Object>) webRequest.getAttribute(SMART_WEB_PARAMETER_PREFIX, WebRequest.SCOPE_REQUEST);
                if(Arrays.binarySearch(attrs, paramName) >= 0){
                    container.put(SMART_WEB_PARAMETER_PREFIX +paramName, parameterValue);
                }
                boolean canExpand = true;
                Map<String, Object> requestParameters = new HashMap<>();
                List<String> values = new ArrayList<>();
                for (String attr : attrs) {
                    if(!container.containsKey(SMART_WEB_PARAMETER_PREFIX +attr)){
                        canExpand = false;
                        break;
                    }else {
                        requestParameters.put(attr, container.get(SMART_WEB_PARAMETER_PREFIX + attr));
                        values.add(String.valueOf(container.get(SMART_WEB_PARAMETER_PREFIX + attr)));
                    }
                }
                if(canExpand) {
                    doSmartExpand(requestParameters,values.toArray(new String[0]), new Expand[]{expand}, webRequest);
                }
            }
        }
        if( methodAnnotation != null && methodAnnotation.value().length > 0){
            for (Result result : methodAnnotation.value()) {
                if(StringUtils.isNotBlank(result.dependMethod())) {//引用注解方法进行子关系依赖
                    Class dependClass = result.dependClass();
                    if(result.dependClass().equals(Object.class)) {
                        dependClass = parameter.getMethod().getDeclaringClass();
                    }
                    Method dependMethod = dependClass.getDeclaredMethod(result.dependMethod());
                    SmartResult annotation = dependMethod.getAnnotation(SmartResult.class);
                    if(annotation != null) {
                        doExpandIfNecessary(annotation, parameter, parameterValue, webRequest, deep+1);
                    }
                }
            }
        }
    }

    private void doSmartExpand(final Map<String, Object> requestParameters, String[] requestParameterValues, Expand[] value, NativeWebRequest webRequest)
            throws IllegalAccessException, InstantiationException, ExecutionException, InterruptedException {
        for (Expand expand : value) {
            //在入参进行扩展时，需要判断是否重复扩展，重复扩展后续忽略
            if(webRequest.getAttribute(Expand.Print.toString(expand, requestParameterValues),
                    RequestAttributes.SCOPE_REQUEST) == null){
                final String[] from = expand.attr();
                final String[] to =expand.newAttr();
                final String patternString = expand.patternString();

                Class<? extends SmartHandler> handler = expand.handler();
//                final SmartHandler smartHandler = handler.newInstance();
//                ServiceFactory.autowireBeanProperties(smartHandler,
//                        AutowireCapableBeanFactory.AUTOWIRE_BY_NAME, false);
//
                final SmartHandler smartHandler = ServiceFactory.getService(handler);

                final Method method = SmartHandlerFactory.getHandler(handler)[0].getMethod();

                final Thread mainThread = Thread.currentThread();
                Future<Map<String, Object>> future = executorService.submit(new Callable<Map<String, Object>>() {
                    @Override
                    public Map<String, Object> call() throws Exception {
                        try {
                            LogHelper.parallelStart(LogHelper.Type.Statistics, mainThread, smartHandler.getClass(), requestParameters);
                            return (Map<String, Object>)HandlerHelper.handle(smartHandler, method, from, to, patternString, requestParameters, null);
                        } finally {
                            LogHelper.parallelEnd(LogHelper.Type.Statistics, smartHandler.getClass());
                        }
                    }
                });
                webRequest.setAttribute(Expand.Print.toString(expand,requestParameterValues),
                        future, RequestAttributes.SCOPE_REQUEST);
                webRequest.setAttribute(Expand.Print.toString(expand), future, RequestAttributes.SCOPE_REQUEST);
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

        SmartParameter ann = parameter.getParameterAnnotation(SmartParameter.class);
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
