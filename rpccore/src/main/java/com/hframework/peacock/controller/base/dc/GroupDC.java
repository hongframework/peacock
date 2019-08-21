package com.hframework.peacock.controller.base.dc;

import com.hframework.common.util.collect.CollectionUtils;
import com.hframework.common.util.collect.bean.Fetcher;

import java.util.*;

public class GroupDC extends AbstractDC implements DC {
    private List<DC> list;

    public GroupDC() {
        list = new ArrayList<>();
    }

    public GroupDC(List<DC> list) {
        this.list = list;
    }

    /**
     * 获取DC容器内数据
     * @return
     */
    @Override
    public List<DC> getData() {
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
        return CollectionUtils.fetch(list, new Fetcher<DC, Object>() {
            @Override
            public Object fetch(DC subDC) {
                return subDC.fetch(name);
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
        return list.get(0).getRuleData();
    }

    /**
     * 删除多余的属性
     *
     * @param removeKeys
     */
    @Override
    public void removeFields(List removeKeys) {
        for (DC dc : list) {
            dc.removeFields(removeKeys);
        }
    }

    /**
     * 仅保留指定的属性
     *
     * @param keepKeys
     */
    @Override
    public void keepFields(Set<String> keepKeys) {
        for (DC dc : list) {
            dc.keepFields(keepKeys);
        }
    }

    /**
     * 该指定属性的值（由对象解析为属性Map结构）
     *
     * @param name
     */
    @Override
    public void parseBeanToMap(String name) {
        for (DC dc : list) {
            dc.parseBeanToMap(name);
        }
    }

    public void add(DC dc) {
        list.add(dc);
    }

    @Override
    public String toString() {
        return "GroupDC{" + list +
                '}';
    }

    @Override
    public void merge(Map<String, Object> targetMap) {
        for (DC dc : list) {
            dc.merge(targetMap);
        }
    }

    public void merge(ListDC listDC) {
        for (int i = 0; i < list.size(); i++) {
            DC item = list.get(i);
            if(DCUtils.isNullDC(item) && listDC.getData().get(i) != null) {
                list.set(i,new MapDC(listDC.getData().get(i)));
            }else {
                item.merge(listDC.getData().get(i));
            }

        }
    }
    public void merge(GroupDC groupDC) {
        for (int i = 0; i < list.size(); i++) {
            DC item = list.get(i);
            if(DCUtils.isNullDC(item) && groupDC.getData().get(i) != null) {
                list.set(i,groupDC.getData().get(i));
            }else {
                list.set(i, DCUtils.mergeDC(item, groupDC.getData().get(i), true));
            }

        }
    }

    @Override
    public DC copy() {
        GroupDC newDC = new GroupDC();
        for (DC dc : list) {
            newDC.add(dc.copy());
        }
        return newDC;
    }
}
