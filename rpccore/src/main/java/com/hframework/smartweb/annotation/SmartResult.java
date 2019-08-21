package com.hframework.smartweb.annotation;

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
public @interface SmartResult {

    Result[] value() default {};

    Expand[] expand() default {};

    Filter[] filter() default {};

    String[] include() default {};
    String[] exclude() default {};

    public static enum Type{
        Extract,
    }
}
