package com.hframework.generator.thirdplatform.core;

/**
 * Created by zhangquanhong on 2016/4/16.
 */
public interface Generator<K,V> {
    public boolean generate(K k, V v);
}
