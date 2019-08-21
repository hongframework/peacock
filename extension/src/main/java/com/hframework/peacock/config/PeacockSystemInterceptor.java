package com.hframework.peacock.config;

import com.hframework.smartweb.APIErrorType;
import com.hframework.smartweb.SmartExpanderFactory;
import com.hframework.tracer.PeacockSystemCenter;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by zhangquanhong on 2017/12/22.
 */
public class PeacockSystemInterceptor  implements HandlerInterceptor {

    private static final Logger logger = LoggerFactory.getLogger(PeacockSystemInterceptor.class);

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler1) throws Exception {
        String url = request.getRequestURI();
        boolean isApiInvoke = url.startsWith("/_api/") ||  url.startsWith("/api/")
                || url.startsWith("/_handler/") || url.startsWith("/handler/");
        boolean isDocRead =  url.startsWith("/apidoc.html") || url.startsWith("/handlerdoc.html")
                || url.startsWith("/doc.html") || url.startsWith("/test.html") || url.startsWith("/apitest")||
                url.startsWith("/login")|| url.startsWith("/index")|| url.startsWith("/logout")
                || url.startsWith("/static/")|| url.startsWith("/js/")|| url.startsWith("/dictionary.json");

        if((PeacockSystemCenter.nodeType.isApi() || PeacockSystemCenter.nodeType.isDoc())
                && StringUtils.isNotBlank(PeacockSystemCenter.mainProgram)) {
            SmartExpanderFactory.setDynResultVOKeyMeta(PeacockSystemCenter.mainProgram);
        }

        if((PeacockSystemCenter.nodeType.isApi() && !isApiInvoke)
                || (PeacockSystemCenter.nodeType.isDoc() && !isDocRead && !isApiInvoke)) {
            logger.error("request:{}?{}", url);
//            DynResultVO resultVO = DynResultVO.error(APIErrorType.ILLEGAL_VISIT);
            response.setHeader("content-type", "text/html;charset=UTF-8");
            response.setCharacterEncoding("UTF-8");
            response.getWriter().print(APIErrorType.ILLEGAL_VISIT.getErrorMsg());
            response.getWriter().close();
            return false;
        }
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {

    }
}
