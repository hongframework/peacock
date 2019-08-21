package com.hframework.peacock.controller.base.dc;

import com.google.common.collect.Lists;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class NullDC extends AbstractDC implements DC {

    public static final NullDC SINGLETON = new NullDC();

    /**
     * 获取DC容器内数据
     * @return
     */
    @Override
    public Object getData() {
        return null;
    }

    /**
     * 获取DC容器内某属性的集合（或值）
     *
     * @param name
     * @return
     */
    @Override
    public Object fetch(String name) {
        return null;
    }

    /**
     * 获取规则校验的数据集
     *
     * @return
     */
    @Override
    public List getRuleData() {
        return Lists.newArrayList(new HashMap());
    }

    /**
     * 删除多余的属性
     *
     * @param removeKeys
     */
    @Override
    public void removeFields(List removeKeys) {

    }

    /**
     * 仅保留指定的属性
     *
     * @param keepKeys
     */
    @Override
    public void keepFields(Set<String> keepKeys) {

    }

    /**
     * 该指定属性的值（由对象解析为属性Map结构）
     *
     * @param name
     */
    @Override
    public void parseBeanToMap(String name) {

    }

    @Override
    public String toString() {
        return "NullDC{}";
    }

    @Override
    public void merge(Map<String, Object> targetMap) {

    }
}
