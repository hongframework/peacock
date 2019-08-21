package com.hframework.peacock.controller.base.descriptor;

import com.hframework.common.util.StringUtils;
import com.hframework.peacock.controller.bean.apiconf.Parameter;
import com.hframework.peacock.controller.bean.apiconf.Parameter;

/**
 * Created by zhangquanhong on 2017/11/15.
 */
public class ThirdParameterDescriptor {
    private Parameter parameter;

    private String ruleCode;
    private String valueCode;

    public ThirdParameterDescriptor(Parameter parameter) {
        this.parameter = parameter;
        String valueExp = parameter.getValue();
        if(valueExp.matches("#val\\{[a-zA-Z_0-9\\-\\-]+\\}")){
            valueCode = valueExp.substring(5, valueExp.length() -1).trim();
        }else if(valueExp.matches("#rule\\{[a-zA-Z_0-9\\-]+\\}")){
            ruleCode = valueExp.substring(6, valueExp.length() -1).trim();
        }

    }

    @Override
    public String toString() {
        return "ThirdParameterDescriptor:" + parameter.getCode() + ":" + parameter.getName();
    }

    public Parameter getParameter() {
        return parameter;
    }

    public void setParameter(Parameter parameter) {
        this.parameter = parameter;
    }

    public String getRuleCode() {
        return ruleCode;
    }


    public String getValueCode() {
        return valueCode;
    }

    public boolean isRule(){
        return StringUtils.isNotBlank(ruleCode);
    }
    public boolean isFixValue(){
        return StringUtils.isNotBlank(valueCode);
    }

    public boolean isInput() {
        return StringUtils.isBlank(ruleCode) && StringUtils.isBlank(valueCode);
    }

}