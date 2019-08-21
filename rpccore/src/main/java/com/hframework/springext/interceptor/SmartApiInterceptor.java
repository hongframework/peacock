package com.hframework.springext.interceptor;

import com.hframework.smartweb.annotation.SmartApi;
import com.hframework.smartweb.SmartParameterInvoker;
import com.hframework.smartweb.LogHelper;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by zqh on 2016/4/11.
 */
public class SmartApiInterceptor implements HandlerInterceptor {

    public static ThreadLocal<String[]> smartApiInfoThreadLocal = new ThreadLocal<>();

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler1) throws Exception {
        smartApiInfoThreadLocal.remove();
        LogHelper.start(LogHelper.Type.Statistics, request);
        String url = request.getRequestURI();
        String version = null;
        if (handler1 instanceof HandlerMethod) {
            HandlerMethod handlerMethod = (HandlerMethod) handler1;
            SmartApi smartHeader = handlerMethod.getMethod().getAnnotation(SmartApi.class);
            if(smartHeader != null && !SmartParameterInvoker.headChecker(smartHeader.checker(), request)) {
                return false;
            }else if(smartHeader != null){
                smartApiInfoThreadLocal.set(new String[]{url, smartHeader.version(), smartHeader.name()});
            }

        }

        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        LogHelper.end(LogHelper.Type.Statistics);
    }
}
