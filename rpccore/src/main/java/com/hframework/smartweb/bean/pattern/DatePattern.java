package com.hframework.smartweb.bean.pattern;

import com.hframework.smartweb.bean.Pattern;
import org.apache.commons.lang3.tuple.Pair;

/**
 * Created by zhangquanhong on 2017/6/5.
 */
public  class DatePattern implements Pattern {
    public static final Pair YYYYMMDD = Pair.of("yyyyMMdd", "YYYYMMDD");
    public static final Pair YYYY_MM_DD = Pair.of("yyyy-MM-dd", "YYYY_MM_DD");
    public static final Pair YYYY_MM_DD_24H_MM_SS = Pair.of("yyyy-MM-dd HH:mm:ss", "YYYY_MM_DD_24H_MM_SS");


    public static final Pair LessNow = Pair.of("LessNow", "小于当前时间");
}