package com.hframework.smartweb.annotation;

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
public @interface Expand {

    String[] attr() default {};
    String[] newAttr() default {};

    String type() default "";

    Class<? extends SmartHandler> handler();

    String patternString() default "";

    public static class Print{
        public static String toString(Expand expand, String[] value){
            return "SmartExpand : newAttr=" + Arrays.toString(expand.newAttr()) + "; attr=[name=" + Arrays.toString(expand.attr()) + ",value=" + Arrays.toString(value) + "]";
        }
        public static String toString(Expand expand){
            return "SmartExpand : newAttr=" + Arrays.toString(expand.newAttr()) + "; attr=[name=" + Arrays.toString(expand.attr()) + ",]";
        }
    }

//    public static enum MapType{
//        AddChild, //数据对象增加额外返回项
//        AddSibling;//数据对象外层增加额外返回项
//    }
}
