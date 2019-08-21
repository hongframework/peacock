package com.hframework.peacock.controller.base.dc;


import java.util.List;
import java.util.Map;
import java.util.Set;

public interface DC {

    /**
     * 获取DC容器内数据
     * @return
     */
    public Object getData();

    /**
     * 获取DC容器内某属性的集合（或值）
     * @param name
     * @return
     */
    public Object fetch(String name);


    /**
     * 获取规则校验的数据集
     * @return
     */
    public List getRuleData();

    /**
     * 删除多余的属性
     * @param removeKeys
     */
    void removeFields(List removeKeys);

    /**
     * 仅保留指定的属性
     * @param keepKeys
     */
    void keepFields(Set<String> keepKeys);

    /**
     * 该指定属性的值（由对象解析为属性Map结构）
     * @param name
     */
    void parseBeanToMap(String name);


    void merge(Map<String, Object> targetMap);

    public DC getPrevDc();

    public void setPrevDc(DC prevDc);

    public DC copy();
}
