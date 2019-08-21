package com.hframework.smartweb.bean.formatter;

import com.hframework.smartweb.bean.pattern.DatePattern;
import com.hframework.common.util.DateUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Pair;

import java.util.*;

/**
 * Created by zhangquanhong on 2017/2/24.
 */
public class DateFormatter extends AbstractSmartFormatter<Date, DatePattern> {

    private static Map<Long, String> unitValueTitle = new LinkedHashMap(){{
        put(30 * 24 * 60 * 60L, "月");
        put(7 * 24 * 60 * 60L, "星期");
        put(24 * 60 * 60L, "天");
        put(60 * 60L, "小时");
        put(60L, "分");
        put(1L, "秒");
    }};


    @Override
    public String format(Date value, String pattern) {
        if(value == null) return "";
        if("NTimeUnitBeforeOrAfter".equals(pattern)) {
            return parseRelateTime(value);
        }

        return DateUtils.getDate(value, pattern);
    }

    public static String parseRelateTime(Date value) {
        long targetTimestamp = value.getTime() / 1000;
        long currentTimestamp = System.currentTimeMillis() / 1000;

        long interval = targetTimestamp - currentTimestamp;
        String afterWords = "之后";
        if(interval < 0) {
            afterWords = "之前";
            interval = - interval;
        }

        String number = "";
        for (Long unit : unitValueTitle.keySet()) {
            if(interval / unit > 0) {
                number = (interval / unit) + unitValueTitle.get(unit);
                break;
            }
        }



        return StringUtils.isNoneBlank(number) ? (number + afterWords) : "当前";
    }

    @Override
    public Pair defaultPattern() {
        return DatePattern.YYYY_MM_DD_24H_MM_SS;
    }

    public static void main(String[] args) {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.SECOND, -1);
        System.out.println(parseRelateTime(calendar.getTime()));
    }

}
