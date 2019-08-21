package com.hframework.smartweb.bean.parser;

import com.hframework.smartweb.bean.SmartParser;
import com.hframework.smartweb.bean.pattern.DatePattern;
import org.apache.commons.lang3.tuple.Pair;

/**
 * Created by zhangquanhong on 2017/2/24.
 */
public class P2pIdSmartParser implements SmartParser<Long, DatePattern> {

    @Override
    public Long parse(String value) throws Exception {
        return 1999L;//TODO
    }

    @Override
    public Long parse(String value, String pattern) throws Exception {
        return 1999L;//TODO
    }

    @Override
    public Pair defaultPattern() {
        return null;
    }
}
