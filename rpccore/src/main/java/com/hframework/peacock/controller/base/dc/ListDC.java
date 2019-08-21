package com.hframework.peacock.controller.base.dc;

import com.google.common.collect.Lists;
import com.hframework.common.util.collect.CollectionUtils;
import com.hframework.common.util.collect.bean.Fetcher;
import com.hframework.smartweb.bean.Object2MapHelper;

import java.util.*;

public class ListDC extends AbstractDC implements DC {
    private List<Map<String, Object>> list;

    public ListDC(List<Map<String, Object>> list) {
        this.list = list;
    }

    /**
     * 获取DC容器内数据
     * @return
     */
    @Override
    public List<Map<String, Object>> getData() {
        return list;
    }

    /**
     * 获取DC容器内某属性的集合（或值）
     *
     * @param name
     * @return
     */
    @Override
    public Object fetch(final String name) {
        return CollectionUtils.fetch(list, new Fetcher<Map<String,Object>, Object>() {
            @Override
            public Object fetch(Map<String, Object> map) {
                return map.get(name);
            }
        }).toArray(new Object[0]);
    }

    /**
     * 获取规则校验的数据集
     *
     * @return
     */
    @Override
    public List getRuleData() {
        return Lists.newArrayList(list.get(0));
    }

    /**
     * 删除多余的属性
     *
     * @param removeKeys
     */
    @Override
    public void removeFields(List removeKeys) {
        for (Map<String, Object> map : list) {
            for (Object removeKey : removeKeys) {
                map.remove(removeKey);
            }
        }
    }

    /**
     * 仅保留指定的属性
     *
     * @param keepKeys
     */
    @Override
    public void keepFields(Set<String> keepKeys) {
        for (Map<String, Object> map : list) {
            Iterator<String> keyIter = map.keySet().iterator();
            while (keyIter.hasNext()){
                String key = keyIter.next();
                if(!keepKeys.contains(key)) {
                    keyIter.remove();
                }
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
        for (Map<String, Object> map : list) {
            Object value = map.get(name);
            List<Map<String, Object>> subData = Object2MapHelper.transformAllToMapStruts(value);
            if(subData == null || subData.isEmpty()) {
                return;
            }
            if(value instanceof List || value.getClass().isArray()) {
                map.put(name, subData);
            }else {
                map.put(name, subData.get(0));
            }
        }

    }

    @Override
    public void merge(Map<String, Object> targetMap) {
        for (Map<String, Object> map : list) {
            map.putAll(targetMap);
        }
    }

    public void merge(List<Map<String, Object>> data) {
        for (int i = 0; i < list.size(); i++) {
            //目前存在左手打右手情况 TODO
            //在解密时，需要用targetMap中的mobile覆盖原mobile，以这个为主
            //而在头像处理中，如果头像表没有头像，取微信头像，如果微信头像没有则默认头像，逻辑存在由于默认值覆盖，导致没能走下一各分支，
            for (Map.Entry<String, Object> entry : data.get(i).entrySet()) {
                if(list.get(i).containsKey(entry.getKey()) && entry.getValue() == null){
                    continue;
                }
                list.get(i).put(entry.getKey(), entry.getValue());
            }
        }
    }

    @Override
    public String toString() {
        return "ListDC:" + list;
    }

    @Override
    public DC copy() {
        List<Map<String, Object>> newList = new ArrayList<>();
        for (Map<String, Object> map : list) {
            newList.add(new HashMap<String, Object>(map));
        }

        return new ListDC(newList);
    }
}
