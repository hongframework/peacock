package com.hframework.peacock.controller.base.dc;

import com.google.common.collect.Lists;
import com.hframework.smartweb.bean.Object2MapHelper;

import java.util.*;

public class MapDC extends AbstractDC implements DC {
    private Map<String, Object> data;

    public MapDC(Map<String, Object> data) {
        this.data = data;
    }

    /**
     * 获取DC容器内数据
     * @return
     */
    @Override
    public Map<String, Object> getData() {
        return this.data;
    }

    /**
     * 获取DC容器内某属性的集合（或值）
     *
     * @param name
     * @return
     */
    @Override
    public Object fetch(String name) {
        return data.get(name);
    }

    /**
     * 获取规则校验的数据集
     *
     * @return
     */
    @Override
    public List getRuleData() {
        return Lists.newArrayList(data);
    }

    /**
     * 删除多余的属性
     *
     * @param removeKeys
     */
    @Override
    public void removeFields(List removeKeys) {
        for (Object removeKey : removeKeys) {
            this.data.remove(removeKey);
        }
    }

    /**
     * 仅保留指定的属性
     *
     * @param keepKeys
     */
    @Override
    public void keepFields(Set<String> keepKeys) {
        Iterator<String> keyIter = this.data.keySet().iterator();
        while (keyIter.hasNext()){
            String key = keyIter.next();
            if(!keepKeys.contains(key)) {
                keyIter.remove();
            }
        }
    }

    /**
     * 该指定属性的值（由对象解析为属性Map结构）
     *
     * @param name
     */
    @Override
    public void parseBeanToMap(String name) {
        Object value = this.data.get(name);
        List<Map<String, Object>> subData = Object2MapHelper.transformAllToMapStruts(value);
        if(subData == null || subData.isEmpty()) {
            return;
        }
        if(value instanceof List || value.getClass().isArray()) {
            this.data.put(name, subData);
        }else {
            this.data.put(name, subData.get(0));
        }
    }

    @Override
    public void merge(Map<String, Object> targetMap) {
        //目前存在左手打右手情况 TODO
        //在解密时，需要用targetMap中的mobile覆盖原mobile，以这个为主
        //而在头像处理中，如果头像表没有头像，取微信头像，如果微信头像没有则默认头像，逻辑存在由于默认值覆盖，导致没能走下一各分支，
//        this.data.putAll(targetMap);
        for (Map.Entry<String, Object> entry : targetMap.entrySet()) {
            if(this.data.containsKey(entry.getKey()) && entry.getValue() == null){
                continue;
            }
            this.data.put(entry.getKey(), entry.getValue());
        }
    }

    @Override
    public DC copy() {
        return new MapDC(new HashMap<String, Object>(this.data));
    }

    @Override
    public String toString() {
        return "MapDC:" + data;
    }
}
