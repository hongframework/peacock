package com.hframework.peacock.controller.base.descriptor;

import com.hframework.common.util.StringUtils;
import com.hframework.peacock.controller.bean.apiconf.Node;
import com.hframework.peacock.controller.bean.apiconf.Node;

/**
 * Created by zhangquanhong on 2017/11/15.
 */
public class ThirdNodeDescriptor {
    private Node node;

    private String ruleCode;
    private String valueCode;

    public ThirdNodeDescriptor(Node node) {
        this.node = node;
        String valueExp = node.getValue();
        if(valueExp.matches("#val\\{[a-zA-Z_0-9]+\\}")){
            valueCode = valueExp.substring(5, valueExp.length() -1).trim();
        }else if(valueExp.matches("#rule\\{[a-zA-Z_0-9]+\\}")){
            ruleCode = valueExp.substring(6, valueExp.length() -1).trim();
        }

    }

    @Override
    public String toString() {
        return "ThirdParameterDescriptor:" + node.getPath() + ":" + node.getName();
    }

    public Node getNode() {
        return node;
    }

    public void setNode(Node node) {
        this.node = node;
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
        return StringUtils.isNotBlank(ruleCode) || StringUtils.isNotBlank(valueCode);
    }

}