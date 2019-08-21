package com.hframework.smartweb.bean.formatter;

import com.hframework.smartweb.bean.Pattern;
import com.hframework.smartweb.bean.SmartFormatter;

/**
 * Created by zhangquanhong on 2017/2/21.
 */
public abstract class AbstractSmartFormatter<T,P extends Pattern> implements SmartFormatter<T,P> {

    @Override
    public Object format(T value) {
        return format(value, String.valueOf(defaultPattern().getValue()));
    }
}
