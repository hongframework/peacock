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
public class IntDictionaryMatchFormatter extends AbstractSmartFormatter<Integer, StringPattern> {
    public static Map<String, JSONObject> patternCache = new HashMap<>();

    @Override
    public String format(Integer value, String pattern) {
        Map<String, String> dictionaryItems = SmartExpanderFactory.getDictionaryItems(ThreadLocalCenter.programIdTL.get(), pattern);
        if(value != null && dictionaryItems.containsKey(String.valueOf(value))){
            return dictionaryItems.get(String.valueOf(value));
        }else {
            return dictionaryItems.get(null);
        }
//        StringDictionaryMatchFormatter.initPattern(patternCache, pattern);
//        JSONObject dictionary = patternCache.get(pattern);
//        if(value != null && dictionary.containsKey(String.valueOf(value))){
//            return String.valueOf(dictionary.get(value));
//        }else {
//            return (String) dictionary.get(null);
//        }
    }


    @Override
    public Pair defaultPattern() {
        return Pair.of("{}", "");
    }
}
