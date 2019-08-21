package com.hframework.smartweb.bean.parser;

import com.hframework.smartweb.bean.Pattern;
import com.hframework.smartweb.bean.SmartParser;

/**
 * Created by zhangquanhong on 2017/2/21.
 */
public abstract class AbstractSmartParser<T,P extends Pattern> implements SmartParser<T,P> {

    @Override
    public T parse(String value) throws Exception {
        return parse(value, String.valueOf(defaultPattern().getValue()));
    }
}
