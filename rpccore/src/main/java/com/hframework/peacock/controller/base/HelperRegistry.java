package com.hframework.peacock.controller.base;

import com.hframework.smartweb.bean.SmartChecker;
import com.hframework.smartweb.bean.SmartFormatter;
import com.hframework.smartweb.bean.SmartParser;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by zhangquanhong on 2017/11/15.
 */
public class HelperRegistry {
    private static final Map<String, Class<? extends SmartParser>> parserCenter = new HashMap<>();
    static {
        parserCenter.put("",null);
    }

    private static final Map<String, Class> typeCenter = new HashMap<>();
    static {
        typeCenter.put("string",String.class);
        typeCenter.put("int",Integer.class);
        typeCenter.put("float",Float.class);
        typeCenter.put("long",Long.class);
        typeCenter.put("date",Date.class);
        typeCenter.put("enum",Enum.class);
        typeCenter.put("boolean",Boolean.class);
        typeCenter.put("string[]",String[].class);
        typeCenter.put("int[]",Integer[].class);
    }

    public static Class<? extends SmartParser> getParser(String parserClass) {
//        return parserCenter.get(parserClass);
        try {
            Class<?> aClass = Class.forName(parserClass);
            return (Class<? extends SmartParser>) aClass;
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    public static Class<? extends SmartFormatter> getFormatter(String formatterClass) {
//        return parserCenter.get(parserClass);
        try {
            Class<?> aClass = Class.forName(formatterClass);
            return (Class<? extends SmartFormatter>) aClass;
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    public static Class<? extends SmartChecker> getChecker(String checkerClass) {
        try {
            Class<?> aClass = Class.forName(checkerClass);
            return (Class<? extends SmartChecker>) aClass;
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    public static Class getType(String typeName) {
        return typeCenter.get(typeName);
    }

    public static Class<? extends Enum<?>> getEnum(String enumClass) {
        try {
            Class<?> aClass = Class.forName(enumClass);
            return (Class<? extends Enum<?>>) aClass;
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }
}
