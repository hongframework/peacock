package com.hframework.peacock.handler.base.field;

import com.google.common.base.Joiner;
import com.hframework.common.util.collect.CollectionUtils;
import com.hframework.common.util.collect.bean.Grouper;
import com.hframework.smartweb.exception.SmartHandlerException;
import org.apache.commons.lang3.StringUtils;

import java.math.BigDecimal;
import java.util.*;

public class DataCombineUtils {

    public static Map<String, Map<String, OrderEnum>> orderByCache = new HashMap<>();
    public static Map<String, Set<String>> groupByCache = new HashMap<>();

    public static Map<String, Set<String>> stringSetCache = new HashMap<>();

    public static void sortByOrderBy(List<Map<String, Object>> result, String orderBy){
        final Map<String, OrderEnum> orderByMap = getOrderByMapInfo(orderBy);
        Collections.sort(result, new Comparator<Map<String, Object>>() {
            @Override
            public int compare(Map<String, Object> o1, Map<String, Object> o2) {
                Iterator<Map.Entry<String, OrderEnum>> iterator = orderByMap.entrySet().iterator();
                while (iterator.hasNext()) {
                    Map.Entry<String, OrderEnum> entry = iterator.next();
                    String field = entry.getKey();
                    OrderEnum order = entry.getValue();
                    Object f1 = o1.get(field);
                    Object f2 = o2.get(field);
                    if(f1 != null && f2 != null) {
                        if(f1.equals(f2)) return 0;

                        if(isNumber(f1) && isNumber(f2)) {
                            if(f1 instanceof BigDecimal) {
                                return ((BigDecimal)f1).compareTo((BigDecimal)f2);
                            }else if(f1 instanceof Long) {
                                return (Long)f1 - (Long)f2 > 0 ? 1 : -1;
                            }else if(f1 instanceof Integer) {
                                return  (Integer)f1 - (Integer)f2 > 0 ? 1 : -1;
                            }else if(f1 instanceof Float) {
                                return  (Float)f1 - (Float)f2 > 0 ? 1 : -1;
                            }else if(f1 instanceof Double) {
                                return  (Double)f1 - (Double)f2 > 0 ? 1 : -1;
                            }else {
                                return String.valueOf(f1).compareTo(String.valueOf(f2)) * (order == OrderEnum.DESC ? -1 : 1);
                            }
                        }else{
                            return String.valueOf(f1).compareTo(String.valueOf(f2)) * (order == OrderEnum.DESC ? -1 : 1);
                        }
                    }else if(f1 != null || f2 != null){
                        return (f1 == null && order == OrderEnum.ASC) || (f2 == null && order == OrderEnum.DESC) ? 1: -1;
                    }
                }
                return 0;
            }
        });
    }

    public static Map<String, List<Map<String, Object>>> groupByGroupBy(List<Map<String, Object>> result, String groupBy) {
        final Set<String> groupItems = getGroupByMapInfo(groupBy);
        return CollectionUtils.group(result, new Grouper<Object, Map<String, Object>>() {
            @Override
            public <K> K groupKey(Map<String, Object> map) {
                List<Object> groupKeyList = new ArrayList<>();
                for (String groupItem : groupItems) {
                    groupKeyList.add(map.get(groupItem));
                }
                return (K) Joiner.on(",").join(groupKeyList);
            }
        });
    }

    public static Set<String> getStringSetBySplit(String string) {
        Set<String> set = new LinkedHashSet<>();
        if(StringUtils.isNotBlank(string)) {
            String[] groupItems = string.split(",");
            for (String groupItem : groupItems) {
                if(StringUtils.isNotBlank(groupItem)) {
                    set.add(groupItem.trim());
                }
            }
        }
        return set;
    }

    public static Set<String> getStringSetInfo(String string) {
        if(!stringSetCache.containsKey(string)) {
            stringSetCache.put(string, getStringSetBySplit(string));
        }
        return stringSetCache.get(string);
    }

    public static Set<String> getGroupByMapInfo(String groupBy) {
        if(!groupByCache.containsKey(groupBy)) {
            groupByCache.put(groupBy, getStringSetBySplit(groupBy));
        }
        return groupByCache.get(groupBy);
    }

    public static Map<String, OrderEnum> getOrderByMapInfo(String orderBy) {
        if(!orderByCache.containsKey(orderBy)) {
            Map<String, OrderEnum> map = new LinkedHashMap<>();
            if(StringUtils.isNotBlank(orderBy)) {
                String[] orderItems = orderBy.split(",");
                for (String orderItem : orderItems) {
                    if(StringUtils.isNotBlank(orderItem)) {
                        String[] itemPair = orderItem.trim().split("[ ]+");
                        String field = itemPair[0].trim();
                        OrderEnum order = (itemPair.length > 1 && "DESC".equals(itemPair[1].trim().toUpperCase())) ? OrderEnum.DESC: OrderEnum.ASC;
                        map.put(field, order);
                    }
                }
            }
            orderByCache.put(orderBy, map);
        }
        return orderByCache.get(orderBy);

    }

    public static Map<String, Object> agg(List<Map<String, Object>> list, Set<String> excludeFields, String aggFunc) {
        Map<String, Object> result = new LinkedHashMap<>();
        for (Map<String,Object> map : list) {
            for (String field : map.keySet()) {
                Object curValue = map.get(field);
                if(excludeFields.contains(field)) {
                    result.put(field, curValue);
                }else{
                    Object tarValue = result.get(field);
                    result.put(field, merge(aggFunc, curValue, tarValue));
                }

            }
        }
        return result;
    }

    public static Object merge(String aggFunc, Object curValue, Object tarValue){
        Object resultVal;
        if("sum".equals(aggFunc)) {
            resultVal = sum(curValue, tarValue);
        }else if("min".equals(aggFunc)) {
            resultVal = min(curValue, tarValue);
        }else if("max".equals(aggFunc)) {
            resultVal = max(curValue, tarValue);
        }else if("head".equals(aggFunc)||"distinct".equals(aggFunc)){
            resultVal = curValue != null ? curValue : tarValue;
        }else if("last".equals(aggFunc)){
            resultVal = tarValue != null ? tarValue : curValue;
        }else {
            throw new SmartHandlerException("mergeFunction:[" + aggFunc + "] not supported !");
        }
        return resultVal;
    }

    public static <T> Object min(T a, T b){

        if(a == null && b == null) {
            return null;
        }
        if(a == null || b == null) {
            T result = a != null? a : b;
            return result;
        }

        if(isNumber(a) && isNumber(b)) {
            if(a instanceof BigDecimal) {
                return ((BigDecimal)a).min((BigDecimal)b);
            }else if(a instanceof Long) {
                return Math.min((Long)a,(Long)b);
            }else if(a instanceof Integer) {
                return  Math.min((Integer)a,(Integer)b);
            }else if(a instanceof Float) {
                return  Math.min((Float)a,(Float)b);
            }else if(a instanceof Double) {
                return  Math.min((Double)a,(Double)b);
            }else {
                return a;
            }
        }else {
            return a;
        }
    }

    public static <T> Object max(T a, T b){

        if(a == null && b == null) {
            return null;
        }
        if(a == null || b == null) {
            T result = a != null? a : b;
            return result;
        }

        if(isNumber(a) && isNumber(b)) {
            if(a instanceof BigDecimal) {
                return ((BigDecimal)a).max((BigDecimal)b);
            }else if(a instanceof Long) {
                return Math.max((Long)a,(Long)b);
            }else if(a instanceof Integer) {
                return  Math.max((Integer)a,(Integer)b);
            }else if(a instanceof Float) {
                return  Math.max((Float)a,(Float)b);
            }else if(a instanceof Double) {
                return  Math.max((Double)a,(Double)b);
            }else {
                return a;
            }
        }else {
            return a;
        }
    }


    public static <T> Object sum(T a, T b){

        if(a == null && b == null) {
            return null;
        }
        if(a == null || b == null) {
            T result = a != null? a : b;
            return result;
        }

        if(isNumber(a) && isNumber(b)) {
            if(a instanceof BigDecimal) {
                return ((BigDecimal)a).add((BigDecimal)b);
            }else if(a instanceof Long) {
                return (Long)a + (Long)b;
            }else if(a instanceof Integer) {
                return  (Integer)a + (Integer)b;
            }else if(a instanceof Float) {
                return  (Float)a + (Float)b;
            }else if(a instanceof Double) {
                return  (Double)a + (Double)b;
            }else {
                return a;
            }
        }else {
            return a;
        }
    }

    public static boolean isNumber(Object a){
        return a instanceof BigDecimal || a instanceof Long || a instanceof Integer || a instanceof Double || a instanceof Float;
    }

    public enum OrderEnum{DESC,ASC}
}
