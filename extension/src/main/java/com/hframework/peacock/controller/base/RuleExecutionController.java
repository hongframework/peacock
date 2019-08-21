package com.hframework.peacock.controller.base;

import com.google.common.collect.Lists;
import com.hframework.common.frame.ServiceFactory;
import com.hframework.smartweb.annotation.*;
import com.hframework.smartweb.bean.SmartMap;
import com.hframework.smartweb.bean.SmartObject;
import com.hframework.smartweb.bean.checker.GenericAuthChecker;
import com.hframework.peacock.filter.RedisRoundRobinFilter;
import com.hframework.peacock.handler.base.RuleExecutionHandler;
import com.hframework.peacock.parser.ExpressionConfigurationParser;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * Created by zhangquanhong on 2017/6/12.
 */
@Controller
@SmartApi("/base")
public class RuleExecutionController {

    private static final List<String> RULE_SYSTEM_PARAMETERS = Lists.newArrayList("clientId", "timestamp", "sign", "ruleCode", "ruleVersion", "keyCode");

    //http://localhost:15459//api/base/rule/execute/batch?ruleCode=ZhouQi&clientId=50003&sign=6196A9AB46DE873ABEB6032BA3B6602E
    @SmartApi(path = "/rule/execute/batch", version = "1.0.0", name = "批量规则运行", description = "规则运行批量接口，执行数据通过请求体中传入",checker = GenericAuthChecker.class, owners = "2")
    @ResponseBody
    public String[] batchRuleExecution(
            @SmartParameter(required = true, description = "规则编码") String ruleCode,
            @SmartParameter(description = "规则版本") String ruleVersion,
            @SmartBody(required = true, description = "执行数据") Object[][] executeData,
            @SmartParameter(description = "Key数据索引", defaultValue = "0") Integer keyIndex){
        return ServiceFactory.getService(RuleExecutionHandler.class).ruleExecution(ruleCode, ruleVersion, executeData, keyIndex);
    }

    //http://localhost:8190//api/base/rule/execute/single?ruleCode=ZhouQi&clientId=50003&dataCode=userId&dataValue=1&sign=704F6023E60718BC78DFF0C32CDABA22
    @SmartApi(path = "/rule/execute/single", version = "1.0.0", name = "单个规则运行", description = "规则运行个接口，执行数据通过参数传值即可",checker = GenericAuthChecker.class, owners = "2")
    @SmartResult(filter = {
//            @Filter(holder = "abtest", attr = "userId", when = "A", filter = RedisRoundRobinFilter.class)
    })
    @Deprecated
    public SmartMap singleRuleExecution(
            @SmartParameter(required = true, description = "规则编码") String ruleCode,
            @SmartParameter(description = "规则版本") String ruleVersion,
            @SmartParameter(required = true, description = "运行数据编码") String dataCode,
            @SmartParameter(required = true, description = "运行数据值") String dataValue,
            @SmartParameter(description = "Key数据索引", defaultValue = "0") Integer keyIndex){
        String[] code = dataCode.split(",");
        String[] data = dataValue.split(",");
        Object[][] executeData = new Object[][]{code, data};
        String s =  ServiceFactory.getService(RuleExecutionHandler.class).ruleExecution(ruleCode, ruleVersion, executeData, keyIndex)[0];
        return SmartMap.hashMap()
                .put("label", s);
    }

    @SmartApi(path = "/rule/execute", version = "1.0.0", name = "单个规则运行", description = "规则运行个接口，执行数据通过参数传值即可",checker = GenericAuthChecker.class, owners = "2")
    @SmartResult(filter = {
//            @Filter(holder = "abtest", attr = "userId", when = "A", filter = RedisRoundRobinFilter.class)
    })
    public SmartObject singleRuleExecutionNew(
            @SmartParameter(required = true, description = "规则编码") String ruleCode,
            @SmartParameter(description = "规则版本") String ruleVersion,
            @SmartParameter(required = false, description = "主参数编码,该参数传入主参数的编码") String keyCode,
            HttpServletRequest request){
        if(StringUtils.isBlank(keyCode)) {
            String[] parameterPairs = request.getQueryString().split("&");
            for (String parameterPair : parameterPairs) {
                if(!RULE_SYSTEM_PARAMETERS.contains(parameterPair.split("=")[0])) {
                    keyCode = parameterPair.split("=")[0];
                    break;
                }
            }
        }

        Map<String, String[]> parameterMap = request.getParameterMap();
        List<String> codeList = Lists.newArrayList(keyCode);
        List<String> dataList = Lists.newArrayList(request.getParameter(keyCode));

        for (Map.Entry<String, String[]> parameterEntry : parameterMap.entrySet()) {
            if(!keyCode.equals(parameterEntry.getKey())) {
                codeList.add(parameterEntry.getKey());
                if(parameterEntry.getValue() != null && parameterEntry.getValue().length > 0) {
                    dataList.add(parameterEntry.getValue()[0]);
                }else {
                    dataList.add(null);
                }
            }

        }
        String[] code = codeList.toArray(new String[0]);
        String[] data = dataList.toArray(new String[0]);
        Object[][] executeData = new Object[][]{code, data};
        String s = ServiceFactory.getService(RuleExecutionHandler.class).ruleExecution(ruleCode, ruleVersion, executeData, 0)[0];
        return SmartObject.valueOf(s);
    }
}
