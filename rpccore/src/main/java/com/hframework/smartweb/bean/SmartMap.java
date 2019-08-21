package com.hframework.smartweb.bean;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by zhangquanhong on 2017/3/1.
 */
public class SmartMap<K, V> {

    private Map<K, V> map = new HashMap();

    public static <K, V> SmartMap build(Map<K, V> map) {
        return new SmartMap(map);
    }

    public static SmartMap hashMap(){
        return new SmartMap(new HashMap());
    }

    public  static <K, V> SmartMap linkedHashMap(){
        return new SmartMap<K, V>(new HashMap());
    }

    public SmartMap put(K key, V value) {
        map.put(key, value);
        return this;
    }

    public Map<K,V> build(){
        return this.map;
    }

    private SmartMap(Map<K, V> map){
        this.map = map;
    }

}
