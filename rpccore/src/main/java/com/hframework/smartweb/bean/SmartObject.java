package com.hframework.smartweb.bean;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by zhangquanhong on 2017/3/1.
 */
public class SmartObject<T> {

    private static final String DEFAULT_KEY = "_SMART_OBJECT_KEY";

    private T object = null;
    private Map<String, T> map = null;

    public static <T> SmartObject valueOf(T object) {
        return new SmartObject(object);
    }

    public static <T>  boolean isAssignableFrom(Map<String, T> map) {
        return map != null && map.containsKey(DEFAULT_KEY) && map.size() == 1;
    }

    public static <T> T getObject(Map<String, T> map) {
        if(isAssignableFrom(map)) {
            return map.get(DEFAULT_KEY);
        }
        throw new RuntimeException("smart object not exists !");
    }

    public Map<String, T> getMap(){
        return map;
    }

    public T getObject(){
        return object;
    }


    private SmartObject(final T object){
        this.object = object;
        map = new HashMap<String, T>(){{
            put(DEFAULT_KEY, object);
        }};
    }

}
