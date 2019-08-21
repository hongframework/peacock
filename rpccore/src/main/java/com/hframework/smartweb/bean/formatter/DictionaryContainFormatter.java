package com.hframework.smartweb.bean.formatter;

import com.alibaba.fastjson.JSONObject;
import com.hframework.smartweb.SmartExpanderFactory;
import com.hframework.smartweb.ThreadLocalCenter;
import com.hframework.smartweb.bean.pattern.StringPattern;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Pair;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by zhangquanhong on 2017/2/24.
 */
public class DictionaryContainFormatter extends AbstractSmartFormatter<String, StringPattern> {
//    public static Map<String, JSONObject> patternCache = new HashMap<>();

    @Override
    public String format(String value, String pattern) {
//        StringDictionaryMatchFormatter.initPattern(patternCache, pattern);
        Map<String, String> dictionary = SmartExpanderFactory.getDictionaryItems(ThreadLocalCenter.programIdTL.get(), pattern);
//        JSONObject dictionary = patternCache.get(pattern);
        if(StringUtils.isBlank(value)){
            return  dictionary.get(null);
        }else if(dictionary.containsKey(value)){
            return dictionary.get(value);
        }else {
            for (String dicValue : dictionary.keySet()) {
                if(dicValue != null && value.contains(dicValue)) {
                    return dictionary.get(dicValue);
                }
            }
            return dictionary.get(null);
        }
    }


    @Override
    public Pair defaultPattern() {
        return Pair.of("{}", "");
    }
}
