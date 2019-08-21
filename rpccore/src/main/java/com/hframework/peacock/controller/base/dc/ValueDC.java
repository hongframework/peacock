package com.hframework.peacock.controller.base.dc;

import java.util.HashMap;
import java.util.Map;

public class ValueDC extends MapDC {

    private Object value;

    public ValueDC(final String key, final Object value) {
        super(new HashMap<String, Object>(){{
            put(key, value);
        }});
        this.value = value;
    }
    public ValueDC(Map<String, Object> data) {
        super(data);
    }

    /**
     * 获取DC容器内数据
     *
     * @return
     */
    public Object getValue() {
        return value;
    }

    @Override
    public String toString() {
        return "ValueDC:" + value ;
    }
}
