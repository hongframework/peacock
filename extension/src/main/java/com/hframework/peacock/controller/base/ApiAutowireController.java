package com.hframework.peacock.controller.base;

import com.hframework.smartweb.annotation.SmartApi;
import com.hframework.smartweb.annotation.SmartResult;
import com.hframework.smartweb.bean.SmartMessage;
import com.hframework.smartweb.bean.SmartObject;
import com.hframework.peacock.config.PeacockSystemLoader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by zhangquanhong on 2017/11/15.
 */
@Controller
//@SmartApi("/")
public class ApiAutowireController {

    private static final Logger logger = LoggerFactory.getLogger(ApiAutowireController.class);

    /**
     * 显示请求转发（带项目ID,主要多租户下Manger使用)
     * @param module api归属模块
     * @param name api名称
     * @param request 请求对象
     * @param response 响应对象
     * @return 响应报文
     */
    @RequestMapping(value = "/_api/{program}/{module}/{name}")
    @SmartResult
    public SmartObject dispatch_(@PathVariable("program") String program,
                                @PathVariable("module") String module,
                                @PathVariable("name") String name,
                                HttpServletRequest request, HttpServletResponse response){
        return dispatch_(program, module, name, null, request, response);
    }

    /**
     * 显示请求转发（带项目ID,主要多租户下Manger使用)
     * @param module api归属模块
     * @param name api名称
     * @param version api版本
     * @param request 请求对象
     * @param response 响应对象
     * @return 响应报文
     */
    @RequestMapping(value = "/_api/{program}/{module}/{name}/{version}")
    @SmartResult
    public SmartObject dispatch_(@PathVariable("program") String program,
                            @PathVariable("module") String module,
                           @PathVariable("name") String name,
                           @PathVariable("version") String version,
                           HttpServletRequest request, HttpServletResponse response){
        logger.debug("request path : {}/{}/{}", module, name,version);
        SmartMessage result = ApiManager.invoke(program, module, name, version, request, response);
        logger.debug("response content : {}", module, name,result);
        return SmartObject.valueOf(result);
    }

    /**
     * 显示请求转发（带项目ID,主要多租户下Manger使用)
     * @param module api归属模块
     * @param request 请求对象
     * @param response 响应对象
     * @return 响应报文
     */
    @RequestMapping(value = "/_api/{program}/{module}/**")
    @SmartResult
    public SmartObject dispatch_(@PathVariable("program") String program,
                                @PathVariable("module") String module,
                                HttpServletRequest request, HttpServletResponse response){
        String requestURI = request.getRequestURI();
        String name = requestURI.substring(("/_api/" + program + "/" + module + "/").length());
        String version = null;
        if(name.substring(name.lastIndexOf("/") +1).matches("[0-9.]+")){
            version = name.substring(name.lastIndexOf("/") + 1);
            name = name.substring(0, name.lastIndexOf("/"));
        }
        logger.debug("request path : {}/{}/{}", module, name,version);

        SmartMessage result = ApiManager.invoke(program,module, name, version, request, response);

        logger.debug("response content : {}", module, name,result);
        return SmartObject.valueOf(result);
    }

    /**
     * 隐式请求转发（不带项目ID,主要多租户下可能API冲突，只显示第一条)
     * @param module api归属模块
     * @param name api名称
     * @param request 请求对象
     * @param response 响应对象
     * @return 响应报文
     */
    @RequestMapping(value = "/api/{module}/{name}")
    @SmartResult
    public SmartObject dispatch(@PathVariable("module") String module,
                                @PathVariable("name") String name,
                                HttpServletRequest request, HttpServletResponse response){
        return dispatch(module, name, null, request, response);
    }

    /**
     * 隐式请求转发（不带项目ID,主要多租户下可能API冲突，只显示第一条)
     * @param module api归属模块
     * @param name api名称
     * @param version api版本
     * @param request 请求对象
     * @param response 响应对象
     * @return 响应报文
     */
    @RequestMapping(value = "/api/{module}/{name}/{version}")
    @SmartResult
    public SmartObject dispatch(@PathVariable("module") String module,
                                @PathVariable("name") String name,
                                @PathVariable("version") String version,
                                HttpServletRequest request, HttpServletResponse response){
        logger.debug("request path : {}/{}/{}", module, name,version);

        SmartMessage result = ApiManager.invoke(null, module, name, version, request, response);

        logger.debug("response content : {}", module, name,result);
        return SmartObject.valueOf(result);
    }

    /**
     * 隐式请求转发（不带项目ID,主要多租户下可能API冲突，只显示第一条)
     * @param module api归属模块
     * @param request 请求对象
     * @param response 响应对象
     * @return 响应报文
     */
    @RequestMapping(value = "/api/{module}/**")
    @SmartResult
    public SmartObject dispatch(@PathVariable("module") String module,
                                HttpServletRequest request, HttpServletResponse response){
        String requestURI = request.getRequestURI();
        String name = requestURI.substring(("/api/" + module + "/").length());
        String version = null;
        if(name.substring(name.lastIndexOf("/") +1).matches("[0-9.]+")){
            version = name.substring(name.lastIndexOf("/") + 1);
            name = name.substring(0, name.lastIndexOf("/"));
        }
        logger.debug("request path : {}/{}/{}", module, name,version);

        SmartMessage result = ApiManager.invoke(null, module, name, version, request, response);

        logger.debug("response content : {}", module, name,result);
        return SmartObject.valueOf(result);
    }

}
