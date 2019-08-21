package com.hframework.strategy.rule.component.function;

import com.google.common.collect.Lists;
import com.hframework.common.util.RegexUtils;

import java.util.Comparator;
import java.util.Set;
import java.util.TreeSet;

/**
 * Created by zhangquanhong on 2018/1/1.
 */
public abstract class AbstractValueFunction implements IFunction {
    @Override
    public String execute(String express) {
        String[] matches = RegexUtils.find(express, getPattern());
        Set<String> matchSet = new TreeSet<>(new Comparator<String>() {
            @Override
            public int compare(String s1, String s2) {
                return - s1.compareTo(s2);
            }
        });
        matchSet.addAll(Lists.newArrayList(matches));
        for (String functionBody : matchSet) {
            express = express.replaceAll(functionBody.replace("(","\\(").replace(")","\\)"), parseFunction(functionBody));
        }
        return express;
    }

    protected  String parseFunction(String functionBody){
        int lt = functionBody.indexOf("(");
        int gt = functionBody.lastIndexOf(")");
        String keyword = functionBody.substring(0, lt);
        String parameterString = functionBody.substring(lt + 1, gt);

        String[] parameters = parameterString.split("[ ]*,[ ]*");

        return invokeFunction(keyword, parameters);
    }

    protected abstract String invokeFunction(String keyword, String[] parameters);

    @Override
    public String removeTemp(String express) {
        return express.replaceAll(getPattern().replace("(","\\(").replace(")","\\)"), "");
    }
}
