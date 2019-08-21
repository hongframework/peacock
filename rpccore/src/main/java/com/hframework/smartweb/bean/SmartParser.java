package com.hframework.smartweb.bean;

import org.apache.commons.lang3.tuple.Pair;

/**
 * Created by zhangquanhong on 2017/2/21.
 */
public interface SmartParser<T,P extends Pattern> {

    public T parse(String value) throws Exception;
    public T parse(String value, String pattern) throws Exception;

    public Pair defaultPattern();

}
