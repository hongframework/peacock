package com.hframework.smartweb.bean.checker;

import com.hframework.smartweb.bean.Pattern;
import com.hframework.smartweb.bean.SmartChecker;

/**
 * Created by zhangquanhong on 2017/2/21.
 */
public abstract class AbstractSmartChecker<T,P extends Pattern> implements SmartChecker<T,P> {

    @Override
    public boolean check(T value) {
        return check(value, String.valueOf(defaultPattern().getKey()));
    }
    @Override
    public boolean check(T value, String pattern) {
        return String.valueOf(value).matches(pattern);
    }
}
