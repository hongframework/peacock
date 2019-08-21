package com.hframework.strategy.rule.component.function;

import com.google.common.base.Joiner;
import com.hframework.common.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhangquanhong on 2018/1/1.
 */
public class InFunction extends AbstractExpressFunction implements IFunction {

    private String pattern = "in\\([^\\(\\)]+\\)";

    @Override
    public String getPattern() {
        return pattern;
    }

    @Override
    protected String invokeFunction(String express, String functionBody, String keyword, String[] parameters) {
        List<String> result = new ArrayList<>();
        for (String parameter : parameters) {
            result.add(express.replace(functionBody, " == " + parameter));
        }

        return Joiner.on(" || ").join(result);
    }
}
