package com.hframework.smartweb.bean.formatter;

import com.google.common.base.Joiner;
import com.hframework.smartweb.bean.pattern.StringPattern;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Pair;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by zhangquanhong on 2017/2/24.
 */
public class ArrayToStringFormatter extends AbstractSmartFormatter<Object, StringPattern> {


    @Override
    public String format(Object listOrArray, String pattern) {
        if(listOrArray == null) return null;
        if(listOrArray.getClass().isArray()) {
            return Joiner.on(pattern).join((Object[]) listOrArray);
        }else {
            return Joiner.on(pattern).join((List)listOrArray);
        }
    }

    @Override
    public Pair defaultPattern() {
        return Pair.of(",",",") ;
    }

}
