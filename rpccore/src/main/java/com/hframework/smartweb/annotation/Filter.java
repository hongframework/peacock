package com.hframework.smartweb.annotation;

import com.hframework.smartweb.bean.SmartFilter;
import com.hframework.smartweb.bean.SmartHandler;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.Arrays;

import static java.lang.annotation.ElementType.METHOD;

/**
 * Created by zhangquanhong on 2017/2/21.
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({METHOD})
public @interface Filter {

    String holder();
    String[] attr() default {};
    String[] when() default {};
    String[] constants() default {};
    Class<? extends SmartFilter> filter();

}
