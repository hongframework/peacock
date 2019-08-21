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
public class StringDictionaryMatchFormatter extends AbstractSmartFormatter<String, StringPattern> {
    public static Map<String, JSONObject> patternCache = new HashMap<>();

    public static void initPattern(Map<String, JSONObject> patternCache, String pattern){
        if(!patternCache.containsKey(pattern)) {
//            patternCache.put(pattern, JSONObject.parseObject(pattern, Feature.OrderedField));
            JSONObject jsonObject = new JSONObject(true);
            String[] items = pattern.split(";");
            for (String item : items) {
                if(StringUtils.isNotBlank(item)) {
                    String[] splits = item.split(":");
                    String val = "null".equals(splits[0].trim()) ? null : splits[0].trim();
                    String txt = splits[1].trim();
                    jsonObject.put(val, txt);
                }

            }
            patternCache.put(pattern, jsonObject);
        }
    }

    @Override
    public String format(String value, String pattern) {
//        initPattern(patternCache, pattern);
//        JSONObject dictionary = patternCache.get(pattern);
//        if(value != null && dictionary.containsKey(value)){
//            return String.valueOf(dictionary.get(value));
//        }else {
//            return (String) dictionary.get(null);
//        }
        Map<String, String> dictionaryItems = SmartExpanderFactory.getDictionaryItems(ThreadLocalCenter.programIdTL.get(), pattern);
        if(value != null && dictionaryItems.containsKey(String.valueOf(value))){
            return dictionaryItems.get(value);
        }else {
            return dictionaryItems.get(null);
        }
    }


    @Override
    public Pair defaultPattern() {
        return Pair.of("{}", "");
    }
}
