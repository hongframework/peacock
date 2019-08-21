package com.hframework.smartweb.bean.parser;

import com.hframework.smartweb.bean.SmartParser;
import com.hframework.smartweb.bean.SmartPattern;
import com.hframework.smartweb.bean.pattern.DatePattern;
import org.apache.commons.lang3.tuple.Pair;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by zhangquanhong on 2017/2/24.
 */
public class DateSmartParser extends AbstractSmartParser<Date, DatePattern> {

    @Override
    public Date parse(String value, String pattern) throws Exception {
        SimpleDateFormat simDateFormat = new SimpleDateFormat(pattern);
        return simDateFormat.parse(value);
    }

    @Override
    public Pair defaultPattern() {
        return DatePattern.YYYY_MM_DD_24H_MM_SS;
    }
}
