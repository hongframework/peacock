package com.hframework.strategy.index.repository.converter;

/**
 * Created by zhangquanhong on 2017/7/17.
 */
public interface RowKeyConverter<T>{
    public String convert(T t);

    public T reverse(String key);
}