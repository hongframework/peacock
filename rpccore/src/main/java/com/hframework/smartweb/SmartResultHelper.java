package com.hframework.smartweb;

import com.google.common.base.Joiner;
import com.google.common.collect.Lists;
import com.hframework.common.util.StringUtils;
import com.hframework.smartweb.bean.checker.IntDictionaryChecker;
import com.hframework.smartweb.bean.checker.StringDictionaryChecker;
import com.hframework.smartweb.bean.formatter.*;

import java.util.*;

public class SmartResultHelper {

    public static Map<Class, String> expanderResultType = new HashMap<>();
    static {
        expanderResultType.put(ArrayToStringFormatter.class, "string");
        expanderResultType.put(BooleanToNumberFormatter.class, "int");
        expanderResultType.put(DateFormatter.class, "string");
        expanderResultType.put(DictionaryContainFormatter.class, "string");
        expanderResultType.put(FloatFormatter.class, "string");
        expanderResultType.put(IntDictionaryMatchFormatter.class, "string");
        expanderResultType.put(KeyInfoFormatter.class, "string");
        expanderResultType.put(MoneyFormatter.class, "string");
        expanderResultType.put(NumberToStringFormatter.class, "string");
        expanderResultType.put(RateFormatter.class, "string");
        expanderResultType.put(StringDictionaryMatchFormatter.class, "string");
        expanderResultType.put(StringToArrayFormatter.class, "string[]");
        expanderResultType.put(TimestampFormatter.class, "string");
    }

    public static Map<Class, Map<String, String>> expanderParameterResultType = new HashMap<>();
    static {
        expanderParameterResultType.put(ArrayMethodFormatter.class, new HashMap<String, String>(){{
            put("head", "object");
            put("last", "object");
            put("length", "int");

        }});
    }

    public static Set<Class> dictionaryExpander = new HashSet<>();
    static {
        dictionaryExpander.add(IntDictionaryMatchFormatter.class);
        dictionaryExpander.add(StringDictionaryMatchFormatter.class);
        dictionaryExpander.add(DictionaryContainFormatter.class);
        dictionaryExpander.add(IntDictionaryChecker.class);
        dictionaryExpander.add(StringDictionaryChecker.class);
        dictionaryExpander.add(DictionaryVirtualFormatter.class);

    }

    public static StringBuffer mergeDescription(StringBuffer description, Long program, String expanderClass, String pattern) {
        if(StringUtils.isNotBlank(expanderClass)) {
            try {
                Class<?> expanderCls = Class.forName(expanderClass.trim());
                if(DictionaryVirtualFormatter.class.equals(expanderCls)) {
                    Map dictionaryItems = SmartExpanderFactory.getDictionaryItems(program, pattern);
                    appendDescriptionOptions(description, dictionaryItems);
                    return description;
                }else if(dictionaryExpander.contains(expanderCls)) {
                    Map<String, String> dictionaryItems = SmartExpanderFactory.getDictionaryItems(program, pattern);
                    appendDescriptionOptions(description, Lists.newArrayList(dictionaryItems.values()));
                    return description;
                }else {
                    String patternDescription = SmartExpanderFactory.getExpanderParameterDescription(expanderClass, pattern);
                    if(StringUtils.isNotBlank(patternDescription)) {
                        appendDescriptionLine(description, patternDescription);
                        return description;
                    }
                }
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }

        return description;
    }

    public static String mergeType(String originType, String expanderClass, String pattern) {
        if(StringUtils.isNotBlank(expanderClass)) {
            try {
                Class<?> expanderCls = Class.forName(expanderClass.trim());
                if(expanderResultType.containsKey(expanderCls)){
                    return expanderResultType.get(expanderCls);
                }else if(expanderParameterResultType.containsKey(expanderCls)){
                    String type = expanderParameterResultType.get(expanderCls).get(pattern);
                    if(StringUtils.isBlank(type))
                        return originType;
                    else
                        return type;
                }else {
                    return originType;
                }
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
                return originType;
            }
        } else return originType;
    }

    public static StringBuffer appendDescriptionOptions(StringBuffer description, List<String> items){
        if(items == null || items.isEmpty()) return description;
        if(description.length() > 0) description.append("<br/>");
        return description.append("支持选项：<br/>&nbsp;" +  Joiner.on("<br/>&nbsp;").join(items));
    }
    public static StringBuffer appendDescriptionOptions(StringBuffer description, Map<String, Object> items){
        if(items == null || items.isEmpty()) return description;
        if(description.length() > 0) description.append("<br/>");
        List entryList = new ArrayList();
        for (Map.Entry<String, Object> entry : items.entrySet()) {
            entryList.add(entry.getKey() + " - " + entry.getValue());
        }
        return description.append("支持选项：<br/>&nbsp;" +  Joiner.on("<br/>&nbsp;").join(entryList));
    }

    public static StringBuffer appendDescriptionMinMax(StringBuffer description, String min, String max){
        if(StringUtils.isBlank(min) && StringUtils.isBlank(max)) return description;
        if(Long.valueOf(min) == Long.MIN_VALUE && Long.valueOf(max) == Long.MAX_VALUE) return description;
        if(description.length() > 0) description.append("<br/>");
        return description.append("取值范围：&nbsp;[" + min + ", " + (Long.valueOf(max) == Long.MAX_VALUE ? "∞" : max) + "]");
    }

    public static StringBuffer appendDescriptionLine(StringBuffer description, String line){
        if(StringUtils.isBlank(line)) return description;
        if(description.length() > 0) description.append("<br/>");
        return description.append(line);
    }

    public static StringBuffer appendDescriptionMinMax(StringBuffer description, Long min, Long max){
        return appendDescriptionMinMax(description, min == null ? null : String.valueOf(min),max == null ? null : String.valueOf(max));
    }

    public static StringBuffer appendDescriptionRule(StringBuffer description, String regex){
        if(StringUtils.isBlank(regex)) return description;
        if(description.length() > 0) description.append("<br/>");
        return description.append("满足规则：&nbsp;" +  regex);
    }

}
