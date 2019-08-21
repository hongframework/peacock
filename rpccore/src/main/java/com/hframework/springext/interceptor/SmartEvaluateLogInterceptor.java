package com.hframework.springext.interceptor;

import com.hframework.common.frame.ServiceFactory;
import com.hframework.common.util.DateUtils;
import com.hframework.common.util.message.JsonUtils;
import com.hframework.springext.requestmapping.SmartRequestMappingHandlerMapping;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.aop.aspectj.MethodInvocationProceedingJoinPoint;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 *
 */
@Component
@Aspect
public class SmartEvaluateLogInterceptor {
    private static final Logger logger = LoggerFactory.getLogger(SmartEvaluateLogInterceptor.class);

    @Pointcut("@annotation(com.hframework.smartweb.annotation.SmartApi))")
    private void apiMethod(){ }

    @Before(value = "apiMethod()")
    public void invokeApiMethod(JoinPoint joinPoint) {
        String methodString = joinPoint.getSignature().toString();//void com.hframework.peacock.controller.base.DownloadBaizeResultController.download(String,String,String,HttpServletResponse)
        SmartRequestMappingHandlerMapping mapping = ServiceFactory.getService(SmartRequestMappingHandlerMapping.class);
        Set<Integer> methodHolderParameterCache = mapping.getMethodHolderParameterCache(methodString);

        String[] smartApiInfo = SmartApiInterceptor.smartApiInfoThreadLocal.get();
        Object[] args = joinPoint.getArgs();
        List<Object> parameter = new ArrayList<>();
        List<Object> holder = new ArrayList<>();

        HttpServletRequest request = null;
        for (int i = 0; i < args.length; i++) {
            if( args[i] instanceof HttpServletRequest) {
                request = ((HttpServletRequest) args[i]);
                continue;
            }
            if( args[i] instanceof HttpServletResponse) {
                continue;
            }
            if(methodHolderParameterCache.contains(i)) {
                holder.add(args[i]);
            }else {
                parameter.add(args[i]);
            }
        }
        if(request != null) {
            Map<String, String[]> parameterMap = request.getParameterMap();
            for (String[] strings : parameterMap.values()) {
                if(strings != null && strings.length > 0 && !parameter.contains(strings[0])) {
                    parameter.add(strings[0]);
                }
            }
        }


        logger.info("{}|{}|{}|{}|{}|{}", DateUtils.getCurrentDateYYYYMMDDHHMMSS(), smartApiInfo[0], smartApiInfo[1], smartApiInfo[2], holder, parameter);
    }

    @AfterReturning(pointcut = "apiMethod()", returning = "ret")
    public void batchOperateMethodAfter(JoinPoint joinPoint, Object ret) throws Throwable {
        String methodString = joinPoint.getSignature().toString();//void com.hframework.peacock.controller.base.DownloadBaizeResultController.download(String,String,String,HttpServletResponse)
        SmartRequestMappingHandlerMapping mapping = ServiceFactory.getService(SmartRequestMappingHandlerMapping.class);
        Set<Integer> methodHolderParameterCache = mapping.getMethodHolderParameterCache(methodString);

        String[] smartApiInfo = SmartApiInterceptor.smartApiInfoThreadLocal.get();
        Object[] args = joinPoint.getArgs();
        List<Object> parameter = new ArrayList<>();
        List<Object> holder = new ArrayList<>();
        HttpServletRequest request = null;
        for (int i = 0; i < args.length; i++) {
            if( args[i] instanceof HttpServletRequest) {
                request = ((HttpServletRequest) args[i]);
                continue;
            }
            if( args[i] instanceof HttpServletResponse) {
                continue;
            }
            if(methodHolderParameterCache.contains(i)) {
                holder.add(args[i]);
            }else {
                parameter.add(args[i]);
            }
        }
        if(request != null) {
            Map<String, String[]> parameterMap = request.getParameterMap();
            for (String[] strings : parameterMap.values()) {
                if(strings != null && strings.length > 0 && !parameter.contains(strings[0])) {
                    parameter.add(strings[0]);
                }
            }
        }

        String retString = "serialize exception!";
        try{
            retString = JsonUtils.writeValueAsString(ret);
        }catch (Exception e) {
        }

        logger.info("{}|{}|{}|{}|{}|{}|{}", DateUtils.getCurrentDateYYYYMMDDHHMMSS(), smartApiInfo[0], smartApiInfo[1], smartApiInfo[2], holder, parameter, retString);
    }
}
