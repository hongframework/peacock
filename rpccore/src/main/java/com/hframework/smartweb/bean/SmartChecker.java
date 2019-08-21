package com.hframework.smartweb.bean;

import org.apache.commons.lang3.tuple.Pair;

/**
 * Created by zhangquanhong on 2017/2/21.
 */
public interface SmartChecker<T,P extends Pattern> {

    public boolean check(T value);

    public boolean check(T value, String pattern);

    public Pair defaultPattern();
}
