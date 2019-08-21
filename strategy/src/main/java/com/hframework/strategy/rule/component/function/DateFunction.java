package com.hframework.strategy.rule.component.function;

import com.hframework.common.util.StringUtils;

/**
 * Created by zhangquanhong on 2018/1/1.
 */
public class DateFunction extends AbstractValueFunction implements IFunction {

    private String pattern = "((now)|(month)|(week)|(day)|(hour))\\(\\d*\\)";

    @Override
    public String getPattern() {
        return pattern;
    }

    @Override
    protected String invokeFunction(String keyword, String[] parameters) {
        String number = parameters != null && parameters.length > 0 && StringUtils.isNotBlank(parameters[0]) ? parameters[0] : "1";

        if(StringUtils.isBlank(number)) number = "1";
        Long target = 0L;
        if("now".equals(keyword)) {
            target = System.currentTimeMillis()/1000;
        }else if("month".equals(keyword)) {
            target = 30 * 24 * 60 * 60 * Long.valueOf(number);
        }else if("week".equals(keyword)) {
            target = 7 * 24 * 60 * 60 * Long.valueOf(number);
        }else if("day".equals(keyword)) {
            target = 24 * 60 * 60 * Long.valueOf(number);
        }else if("hour".equals(keyword)) {
            target = 60 * 60 * Long.valueOf(number);
        }
        return String.valueOf(target);
    }
}
