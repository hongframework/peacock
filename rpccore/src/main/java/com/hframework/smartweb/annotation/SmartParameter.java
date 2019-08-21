package com.hframework.smartweb.annotation;

import com.hframework.smartweb.bean.SmartChecker;
import com.hframework.smartweb.bean.SmartParser;
import com.hframework.smartweb.bean.SmartPattern;
import org.springframework.web.bind.annotation.ValueConstants;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.PARAMETER;

/**
 * Created by zqh on 2016/4/16.
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({PARAMETER})
public @interface SmartParameter {

    //参数的名称
    String name() default "";

    //##########校验类##########
    //必选参数
    boolean required() default false;

    //选项范围
    String[] options() default {};
    String optionJson() default "";
    //枚举范围
    Class<? extends Enum<?>> enums() default None.class;

    //最大值
    long max() default Long.MAX_VALUE;
    //最小值
    long min() default Long.MIN_VALUE;
    //检查器
    Class<? extends SmartChecker> checker() default SmartChecker.class;

    SmartPattern pattern() default SmartPattern.None;

    //正则表达式
    String regex() default "";

    //##########赋值类##########
    //参数的默认值
    String defaultValue() default ValueConstants.DEFAULT_NONE;

    String description() default "";

    //值处理类
    Class<? extends SmartParser> parser() default SmartParser.class;

    public enum None {
    }
}
