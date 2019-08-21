package com.hframework.smartweb.bean;

import org.apache.commons.lang3.tuple.Pair;

/**
 * Created by zhangquanhong on 2017/2/21.
 */
public interface SmartFormatter<T,P extends Pattern> {

    public Object format(T value);

    public Object format(T value, String pattern);

    public Pair defaultPattern();
}
