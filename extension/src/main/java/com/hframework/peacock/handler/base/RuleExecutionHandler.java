package com.hframework.peacock.handler.base;

import com.hframework.smartweb.annotation.Handler;
import com.hframework.smartweb.bean.SmartHandler;
import com.hframework.smartweb.bean.handler.AbstractSmartHandler;
import com.hframework.strategy.rule.ExpressionEngine;
import com.hframework.strategy.rule.data.EDataSet;
import org.springframework.stereotype.Controller;

/**
 * Created by zhangquanhong on 2017/2/21.
 */
@Controller
@Handler(path = "/base/rule_execution", owners = {"2"})
public class RuleExecutionHandler extends AbstractSmartHandler implements SmartHandler {

    @Handler(version ="1.0.0", description = "规则运行")

    public String[] ruleExecution(String ruleCode, String ruleVersion,
                                  Object[][] executeData, Integer keyIndex) {
        EDataSet eDataSet = ExpressionEngine.getDefaultInstance().execute(ruleCode, ruleVersion, new EDataSet(executeData, keyIndex));
        System.out.println("9999999999999999999999999999");
        System.out.println(eDataSet);
        System.out.println(eDataSet.info());
        return eDataSet.getLabel();
    };

}
