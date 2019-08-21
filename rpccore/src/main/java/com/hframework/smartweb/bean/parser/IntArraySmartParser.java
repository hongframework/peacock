package com.hframework.smartweb.bean.parser;

import com.hframework.smartweb.bean.pattern.StringPattern;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Pair;

public class IntArraySmartParser extends AbstractSmartParser<Integer[], StringPattern>{
    @Override
    public Integer[] parse(String value, String pattern) throws Exception {
        if(StringUtils.isBlank(value)) return new Integer[0];
        String[] splits = value.split(pattern);
        Integer[] result = new Integer[splits.length];
        for (int i = 0; i < splits.length; i++) {
            result[i] = StringUtils.isNotBlank(splits[i])? Integer.parseInt(splits[i]): null;
        }
        return result;
    }

    @Override
    public Pair defaultPattern() {
        return Pair.of(",", "英文逗号");
    }
}
