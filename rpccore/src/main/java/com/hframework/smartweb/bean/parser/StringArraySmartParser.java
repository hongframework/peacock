package com.hframework.smartweb.bean.parser;

import com.hframework.smartweb.bean.pattern.StringPattern;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Pair;

import java.util.Date;

public class StringArraySmartParser extends AbstractSmartParser<String[], StringPattern>{
    @Override
    public String[] parse(String value, String pattern) throws Exception {
        if(StringUtils.isBlank(value)) return new String[0];
        return value.split(pattern);
    }

    @Override
    public Pair defaultPattern() {
        return Pair.of(",", "英文逗号");
    }
}
