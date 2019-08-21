package com.hframework.smartweb.bean.parser;

import com.hframework.smartweb.bean.HolderParser;
import com.hframework.smartweb.bean.Pattern;
import com.hframework.smartweb.bean.SmartParser;
import org.apache.commons.lang3.tuple.Pair;

/**
 * Created by zhangquanhong on 2017/2/21.
 */
public abstract class AbstractHolderParser<T> extends AbstractSmartParser implements HolderParser<T>{
    @Override
    public Object parse(String value, String pattern) throws Exception {
        return null;
    }

    @Override
    public Pair defaultPattern() {
        return null;
    }
}
