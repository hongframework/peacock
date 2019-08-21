package com.hframework.strategy;

import com.hframework.common.util.SpelExpressionUtils;
import com.hframework.strategy.rule.component.function.DateFunction;
import com.hframework.strategy.rule.component.function.InFunction;
import org.junit.Test;

/**
 * Created by zhangquanhong on 2018/1/1.
 */
public class FunctionSuite {

    @Test
    public void DateFunction(){
        System.out.println(new DateFunction().execute("month(2)"));
        System.out.println(new DateFunction().execute("now()"));
    }


    @Test
    public void InFunction(){
        System.out.println(SpelExpressionUtils.execute(new InFunction().execute("'北京'in('北京','上海','广州','深圳')")));
        System.out.println(SpelExpressionUtils.execute(new InFunction().execute("'成都'in('北京','上海','广州','深圳')")));
    }


}
