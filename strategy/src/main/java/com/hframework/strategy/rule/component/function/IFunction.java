package com.hframework.strategy.rule.component.function;

/**
 * Created by zhangquanhong on 2018/1/1.
 */
public interface IFunction {

    public String execute(String express);

    public String removeTemp(String express);

    public String getPattern();


}
