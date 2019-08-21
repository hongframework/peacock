package com.hframework.smartweb.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.TYPE;
/**
 * Created by zhangquanhong on 2017/6/5.
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({METHOD,TYPE})
public @interface Handler {

    String path() default "";
    String version() default "";
    String description() default "";
    State state() default State.publish;

    String[] owners() default {};

    boolean batch() default false;

    public static enum State {
        publish,beta,test
    }
}
