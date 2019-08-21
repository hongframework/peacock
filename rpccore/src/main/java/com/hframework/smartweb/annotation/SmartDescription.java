package com.hframework.smartweb.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.PARAMETER;

/**
 * Created by zqh on 2016/4/16.
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({PARAMETER, FIELD, METHOD})
public @interface SmartDescription {

    //参数的描述
    String value() default "";

    //参数的名称
    String name() default "";

    //是否忽略
    boolean main() default false;
}
