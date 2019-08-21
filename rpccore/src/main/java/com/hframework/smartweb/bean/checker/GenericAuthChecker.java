package com.hframework.smartweb.bean.checker;

import com.hframework.smartweb.APIErrorType;
import com.hframework.smartweb.bean.Pattern;
import com.hframework.smartweb.bean.pattern.StringPattern;
import com.hframework.smartweb.config.SmartConfigurer;
import com.hframework.smartweb.exception.SmartHandlerException;
import org.apache.commons.lang3.tuple.Pair;

import javax.servlet.ServletRequest;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

/**
 * 通用权限认证检查器
 * 检验 clientId是否存在以及sign是否正确，sign签名逻辑如下
     i.	所有请求参数(除sign参数)键值对按照参数名排序（字典序）重组,重组时参数名需要全转化为小写，比如：param2=abc&PaRam1=def&sign=SDFDFVYQWGJDAV,那么重组结果为：param1=def&param2=abc
     ii.	在i的结果上拼接client秘钥,形式为：&key={key}，比如秘钥为：Xmp3w那么拼接结果为：param1=def&param2=abc&key=Xmp3w
     iii.	在iii的结果进行md5加密，将加密结果转为大写即为sign的逻辑
     iv.	将iii的结果与参数中sign的值进行字符串比对，如果不等抛出sign check failed ！异常
 * Created by zhangquanhong on 2017/2/24.
 */
public class GenericAuthChecker extends AbstractSmartChecker<ServletRequest, Pattern> {

    @Override
    public boolean check(ServletRequest request, String pattern) {
        if(SmartConfigurer.isDebugMode()) {
            return true;
        }
        Enumeration<String> parameterNames = request.getParameterNames();

        Map<String, String> parameterMap =  new HashMap<>();

        while (parameterNames.hasMoreElements()) {
            String parameterName = parameterNames.nextElement();
            parameterMap.put(parameterName, request.getParameter(parameterName));
        }

        String clientId = request.getParameter("clientId");
        if(SmartConfigurer.notExistsClientId(clientId)) {
            throw new SmartHandlerException(APIErrorType.PARAMETER_ERROR,"clientId is not exist!");
        }
        try {
            Rules.checkAllNotEmptyParamsSign(parameterMap, "&key=" + SmartConfigurer.getKey(clientId));
        } catch (Exception e) {
            throw new SmartHandlerException(APIErrorType.PARAMETER_ERROR,"sign check failed !");
        }
        return true;
    }

    @Override
    public Pair defaultPattern() {
        return StringPattern.EMAIL;
    }

}
