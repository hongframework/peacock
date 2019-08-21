package com.hframework.smartweb.bean.formatter;

import com.hframework.common.util.DateUtils;
import com.hframework.smartweb.bean.pattern.DatePattern;
import org.apache.commons.lang3.tuple.Pair;

import java.util.Date;

/**
 * Created by zhangquanhong on 2017/2/24.
 */
public class TimestampFormatter extends AbstractSmartFormatter<Integer, DatePattern> {

    @Override
    public String format(Integer value, String pattern) {
        if(value == null) return "";
        Date date = new Date(value * 1000L);
        if("NTimeUnitBeforeOrAfter".equals(pattern)) {
            return DateFormatter.parseRelateTime(date);
        }
        return DateUtils.getDate(date, pattern);
    }

    @Override
    public Pair defaultPattern() {
        return DatePattern.YYYY_MM_DD_24H_MM_SS;
    }

    public static void main(String[] args) {
        TimestampFormatter formatter = new TimestampFormatter();
        System.out.println(formatter.format(1504195200, String.valueOf(DatePattern.YYYY_MM_DD_24H_MM_SS.getKey())));

    }

}
