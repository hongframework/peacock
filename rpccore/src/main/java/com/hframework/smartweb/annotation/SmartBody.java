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
public @interface SmartBody {

    //消息类型
    Type type() default Type.JSON;

    //必选参数
    boolean required() default true;

    //参数的名称
    String name() default "";

    String description() default "";

    public enum Type {
        JSON, XML, TXT
    }
}
