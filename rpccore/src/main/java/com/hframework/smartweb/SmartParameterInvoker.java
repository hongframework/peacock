package com.hframework.smartweb;

import com.hframework.common.frame.ServiceFactory;
import com.hframework.smartweb.annotation.SmartParameter;
import com.hframework.smartweb.bean.HolderParser;
import com.hframework.smartweb.bean.SmartChecker;
import com.hframework.smartweb.bean.SmartParser;
import com.hframework.smartweb.bean.SmartPattern;
import com.hframework.smartweb.bean.handler.HandlerHelper;
import com.hframework.smartweb.bean.parser.DateSmartParser;
import com.hframework.smartweb.exception.SmartHandlerException;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.web.context.request.NativeWebRequest;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by zhangquanhong on 2017/6/12.
 */
public class SmartParameterInvoker {

    private static Map<Class, Class> defaultParser = new HashMap(){{
        put(Date.class, DateSmartParser.class);
    }};

    private static Map<Class, Object> cacheSmartHelper = new HashMap<>();

    public static Object holderParser(String path, String version, String key, Class<? extends HolderParser> parser, Map<String, Object> parameters, NativeWebRequest webRequest)
            throws IllegalAccessException, InstantiationException {
        try{
            if(!parser.isInterface()){
                try{
                    LogHelper.stageStart(LogHelper.Type.Statistics, parser.getName(), path, key);
                    return getSmartHelperBean(parser).parse(path,version, key, parameters, webRequest);
                }finally {
                    LogHelper.stageEnd(LogHelper.Type.Statistics, parser.getName(), path, key);
                }
            }

        }catch (Exception e) {
            if(e instanceof SmartHandlerException) {
                throw (SmartHandlerException)e ;
            }else {
                e.printStackTrace();
                throwParameterCheckException(path + "." + key, "'parse error");
            }

            LogHelper.stageException(LogHelper.Type.Statistics, parser.getName(), path, key);
        }

        return null;
    }

    public  static <T> T getSmartHelperBean(Class<T> tClass) throws IllegalAccessException, InstantiationException {

        if(!cacheSmartHelper.containsKey(tClass)) {
            synchronized (SmartParameterInvoker.class) {
                if(!cacheSmartHelper.containsKey(tClass)) {
                    T bean = tClass.newInstance();
                    ServiceFactory.autowireBeanProperties(bean, AutowireCapableBeanFactory.AUTOWIRE_BY_NAME, false);
                    cacheSmartHelper.put(tClass, bean);
                }
            }
        }
        return (T) cacheSmartHelper.get(tClass);
    }

    public static Object parser(Class<?> parameterType, String parameterValue, Class<? extends SmartParser> parser, String pattern, String name) throws IllegalAccessException, InstantiationException {
        try{


            if((parser == null || parser.isInterface()) && defaultParser.containsKey(parameterType)){
                parser = defaultParser.get(parameterType);
            }

            if(parser == null || parser.isInterface()) {
                try{
                    return HandlerHelper.transformType(parameterValue, parameterType);
                }catch (Exception e) {
                    return parameterValue;
                }
            }else if(!parser.isInterface()){
                try{
                    LogHelper.stageStart(LogHelper.Type.Statistics, parser.getName(), pattern, parameterValue);
                    if(StringUtils.isNoneBlank(pattern)) {
                        return  getSmartHelperBean(parser).parse(parameterValue, pattern);
                    }else {
                        return getSmartHelperBean(parser).parse(parameterValue);
                    }
                }finally {
                    LogHelper.stageEnd(LogHelper.Type.Statistics, parser.getName(), pattern, parameterValue);
                }
            }

        }catch (Exception e) {
            if(e instanceof SmartHandlerException) {
                throw (SmartHandlerException)e ;
            }else {
                throwParameterCheckException(name, "'parse error");
            }

            LogHelper.stageException(LogHelper.Type.Statistics, parser.getName(), pattern, parameterValue);
        }

        return parameterValue;
    }

    public static void checker(String parameterName, String parameterValue, Class<? extends SmartChecker> checker,
                         String pattern) throws IllegalAccessException, InstantiationException {
        if(!checker.isInterface()){
            if(null != pattern ) {
                if( !getSmartHelperBean(checker).check(parameterValue, pattern)) {
                    throwParameterCheckException(parameterName, " check not pass");
                }
            }else{
                if(!getSmartHelperBean(checker).check(parameterValue)){
                    throwParameterCheckException(parameterName, " check not pass");
                }
            }
        }
    }

    public static boolean headChecker(Class<? extends SmartChecker> checker, HttpServletRequest request) throws IllegalAccessException, InstantiationException {
        if(!checker.isInterface()){
            return getSmartHelperBean(checker).check(request);
        }
        return true;
    }

    private static void throwParameterCheckException(String parameterName, String errorInfo) {
        throw new SmartHandlerException(APIErrorType.PARAMETER_ERROR, "<" + parameterName + "> " + errorInfo.trim() + " !");
    }


    public static void checkRequiredPass(String parameterValue, boolean required, String name) {
        if(required && StringUtils.isBlank(parameterValue)) {
            throwParameterCheckException(name, " is required");
        }
    }



    public static void checkRegexPass(String parameterValue, String regex, String name) {
        if(parameterValue != null && org.apache.commons.lang.StringUtils.isNotBlank(regex) && !parameterValue.matches(regex)) {
            throwParameterCheckException(name, " format is not satisfied");
        }
    }

    public static void checkOptionsPass(String parameterValue,String[] options, String parameterName) {
        if(options != null && options.length > 0) {
            if (parameterValue == null) {
                throwParameterCheckException(parameterName, "value [" + parameterValue  + "] not in " + Arrays.toString(options) + "");
            }
            Arrays.sort(options);
            int index = Arrays.binarySearch(options, parameterValue.trim());
            if (index < 0) {
                throwParameterCheckException(parameterName, "value [" + parameterValue  + "] not in " + Arrays.toString(options) + "");
            }
        }
    }

    public static void checkEnumsPass(String parameterValue,Class<? extends Enum<?>> enumClass, String parameterName)
            throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {

        if(enumClass == null || SmartParameter.None.class == enumClass) {
            return ;
        }

        Method method = enumClass.getMethod("values");
        Object[] values = (Object[]) method.invoke(null);


        if(parameterValue != null) {
            for (Object value : values) {
                if(parameterValue.equals(value.toString())) {
                    return ;
                }
            }
        }
        throwParameterCheckException(parameterName, " not in " + Arrays.toString(values) + "");
    }

    public static void checkMinMaxPass(Class<?> parameterType, String parameterValue, long min, long max,String parameterName){

        if(Number.class.isAssignableFrom(parameterType)
                || parameterType.equals(int.class)
                || parameterType.equals(long.class)
                || parameterType.equals(float.class)
                || parameterType.equals(double.class)) {
            if(max == Long.MAX_VALUE && min == Long.MIN_VALUE) {
                return ;
            }
            if(parameterValue == null) {
                throwParameterCheckException(parameterName, " not in [" + min + ", " + max + "]");
            }
            Long longVal = Long.parseLong(parameterValue);
            if(min > longVal || longVal > max) {
                throwParameterCheckException(parameterName, " not in [" + min + ", " + max + "]");
            }
        }
    }

    public static void checkMinMaxPass(String parameterValue, long min, long max,String parameterName){

            if(max == Long.MAX_VALUE && min == Long.MIN_VALUE) {
                return ;
            }
            if(parameterValue == null) {
                throwParameterCheckException(parameterName, "value [" + parameterValue  + "]  not in [" + (min == Long.MIN_VALUE ? "-": min) + ", " + (max == Long.MAX_VALUE ? "-": max) + "]");
            }
            Long longVal = Long.parseLong(parameterValue);
            if(min > longVal || longVal > max) {
                throwParameterCheckException(parameterName, "value [" + parameterValue  + "]  not in [" + (min == Long.MIN_VALUE ? "-": min) + ", " + (max == Long.MAX_VALUE ? "-": max) + "]");
            }
    }

    public static void checkTypePass(Class<?> parameterType, String parameterValue, String parameterName) {
        if(parameterType == Long.class) {
            if (!NumberUtils.isDigits(parameterValue)) {
                throwParameterCheckException(parameterName, " not digital for long");
            }
        }else if(parameterType == Integer.class) {
            if (!NumberUtils.isDigits(parameterValue)) {
                throwParameterCheckException(parameterName, " not digital for integer");
            }
        }
    }
}
