package com.hframework.strategy.rule.repository;

import com.google.common.collect.Lists;
import com.hframework.common.util.RegexUtils;
import com.hframework.strategy.rule.ExpressInvoker;
import com.hframework.strategy.rule.ExpressionEngine;
import com.hframework.strategy.rule.exceptions.RuleInitializeException;
import com.hframework.strategy.rule.repository.rules.RelateRule;
import com.hframework.strategy.rule.repository.rules.Rule;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by zhangquanhong on 2017/6/28.
 */
public class RuleInfo {
    private Rule rule;
    private Map<String, String> relRuleVersionInfo;
    private Map<String, RuleInfo> relRuleInfo;
    private String expression;

    private boolean dependChecked;
    private boolean expressInitialized;

    private ExpressInvoker invoker;


    public RuleInfo(Rule rule) {
        this.rule = rule;
        if(rule.getRelateRules() != null && rule.getRelateRules().getRelateRuleList() != null){
            relRuleVersionInfo = new HashMap<>();
            for (RelateRule relateRule : rule.getRelateRules().getRelateRuleList()) {
                relRuleVersionInfo.put(relateRule.getCode(), relateRule.getVersion());
            }
        }
        String[] relRules = RegexUtils.find(this.rule.getExpression(), "#[{]?[a-zA-Z]+[a-zA-Z0-9._]*[}]?");
        if(relRules.length > 0) {
            if(relRuleVersionInfo == null)
                relRuleVersionInfo = new HashMap<>();
            for (String relRule : relRules) {
                relRule = relRule.replaceAll("[#{}]+","");
                if(!relRuleVersionInfo.containsKey(relRuleVersionInfo)) {
                    relRuleVersionInfo.put(relRule, null);
                }
            }
        }

    }

    public Rule getRule() {
        return rule;
    }

    public String getExpression() {
        return expression;
    }

    public void setExpression(String expression) {
        this.expression = expression;
    }

    public String getCode(){
        return rule.getCode();
    }
    public String getIdentify() {
        return rule.getCode() + ":"  + rule.getVersion();
    }
    public String getName(){
        return rule.getName();
    }
    public String getVersion(){
        return rule.getVersion();
    }
    public ReturnType getReturnType(){
        return ReturnType.valueOf(rule.getReturnType());
    }

    public String getRelRuleVersion(String ruleCode){
        if(relRuleVersionInfo != null) {
            relRuleVersionInfo.get(ruleCode);
        }
        return null;
    }

    public String getOriginExpression() {
        return rule.getExpression();
    }

    public void setDepend(Map<String, RuleInfo> ruleInfoMap, Map<String, RuleInfo> defaultRuleInfoMap) {
        if(relRuleVersionInfo == null) {
            dependChecked = true;
            return ;
        }
        for (Map.Entry<String, String> entry : relRuleVersionInfo.entrySet()) {
            String relRuleCode = entry.getKey();
            String version = entry.getValue();
            RuleInfo relRule;
            if(version == null) {
                relRule = defaultRuleInfoMap.get(relRuleCode);
            }else {
                relRule = ruleInfoMap.get(relRuleCode + ":"  + version);
            }
            if(relRule == null) {
                throw new RuleInitializeException("rule [ " + getIdentify() + " ]' depend " + relRuleCode + ":" + version + "not exists !");
            }
            if(relRuleInfo == null) relRuleInfo = new HashMap<>();
            this.relRuleInfo.put(relRuleCode, relRule);
        }
        dependChecked = true;
    }

    public void initExpress(List<String> dependChain) {
        if(isExpressInitialized()) return;
        if(dependChain.size() > 50){
            throw new RuleInitializeException("rule [ " + getIdentify() + " ]' maybe self dependence, " +
                    "depend chain as : " + Arrays.toString(dependChain.toArray(new String[0])));
        }
        expression = getOriginExpression();
        expression = expression.replaceAll("[\"\']+[ ]+[\"']+", "#nbsp#").replaceAll("\\s", "").replaceAll("#nbsp#", "\" \"");
        if(relRuleInfo != null) {
            for (RuleInfo ruleInfo : relRuleInfo.values()) {
                if(!ruleInfo.isExpressInitialized()) {
                    ruleInfo.initExpress(Lists.newArrayList(dependChain));
                }
                expression = expression.replaceAll("#[{]?" + ruleInfo.getCode()+ "[}]?","(" + ruleInfo.getExpression().trim().replaceAll("\\$","\\\\\\$") + ")");
            }
            dependChain.add(getIdentify());
        }
        setExpressInitialized(true);
    }

    public boolean isExpressInitialized() {
        return expressInitialized;
    }

    public void setExpressInitialized(boolean expressInitialized) {
        this.expressInitialized = expressInitialized;
    }

    public void loadAndExplain() {
        if(isExpressInitialized()) {
            invoker = ExpressionEngine.loadAndExplain(expression);
        }else {
            throw new RuleInitializeException("express do not initialized !");
        }
    }

    public  static enum ReturnType{
        Boolean, Numeric,Label;
        public boolean isBoolean(){
            return this.equals(Boolean);
        }
        public boolean isNumeric(){
            return this.equals(Numeric);
        }
        public boolean isLabel(){
            return this.equals(Label);
        }
    }

    public boolean isDependChecked() {
        return dependChecked;
    }

    public void setDependChecked(boolean dependChecked) {
        this.dependChecked = dependChecked;
    }

    public ExpressInvoker getInvoker() {
        return invoker;
    }

    public void setInvoker(ExpressInvoker invoker) {
        this.invoker = invoker;
    }
}
