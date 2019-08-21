package com.hframework.strategy.rule.fetch;

import java.util.Set;

/**
 * Created by zhangquanhong on 2017/6/27.
 */
public interface Fetcher<T> {

//    public FetchData fetch(T key);

    public FetchData fetch(Set<T> keys) throws Exception;

}
