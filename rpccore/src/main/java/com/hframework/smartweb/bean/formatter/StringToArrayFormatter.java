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
public class StringToArrayFormatter extends AbstractSmartFormatter<String, StringPattern> {


    @Override
    public Object format(String value, String pattern) {
        if(StringUtils.isBlank(value)) return null;
        if(StringUtils.isBlank(pattern)) {
            pattern = ",";
        }

        List<String> results = new ArrayList<>();
        String[] split = value.split(pattern);
        for (String s : split) {
            if(StringUtils.isNoneBlank(s)) {
                results.add(s.trim());
            }
        }
        return results.toArray(new String[0]);
    }

    @Override
    public Pair defaultPattern() {
        return Pair.of(",",",") ;
    }

    public static void main(String[] args) {
        StringToArrayFormatter formatter = new StringToArrayFormatter();
        System.out.println(Arrays.toString((String[])formatter.format("张生,,,二狗", ",")));
    }
}
