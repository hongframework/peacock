package com.hframework.smartweb.annotation;

import com.hframework.smartweb.bean.SmartFormatter;
import com.hframework.smartweb.bean.SmartPattern;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.METHOD;

/**
 * Created by zhangquanhong on 2017/2/21.
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({METHOD})
public @interface SubResult {

    String attr() default "";
    String alias() default "";
    boolean keepAttr() default false;
    Class<? extends SmartFormatter> formatter() default SmartFormatter.class;
    SmartPattern pattern() default SmartPattern.None;


    String defaultValue() default "";
}
