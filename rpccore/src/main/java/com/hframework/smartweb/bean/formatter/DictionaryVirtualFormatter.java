package com.hframework.smartweb.bean.formatter;

import com.alibaba.fastjson.JSONObject;
import com.hframework.smartweb.SmartExpanderFactory;
import com.hframework.smartweb.ThreadLocalCenter;
import com.hframework.smartweb.bean.pattern.StringPattern;
import org.apache.commons.lang3.tuple.Pair;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by zhangquanhong on 2017/2/24.
 */
public class DictionaryVirtualFormatter extends AbstractSmartFormatter<Object, StringPattern> {

    @Override
    public Object format(Object value, String pattern) {
        return value;
    }

    @Override
    public Pair defaultPattern() {
        return Pair.of("{}", "");
    }
}
