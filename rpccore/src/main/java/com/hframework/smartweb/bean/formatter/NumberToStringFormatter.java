package com.hframework.smartweb.bean.formatter;

import com.hframework.smartweb.bean.pattern.MoneyPattern;
import org.apache.commons.lang3.tuple.Pair;

import java.math.BigDecimal;

/**
 * Created by zhangquanhong on 2017/2/24.
 */
public class NumberToStringFormatter extends AbstractSmartFormatter<BigDecimal, MoneyPattern> {

    @Override
    public String format(BigDecimal value, String pattern) {
        return value.toString();
    }

    @Override
    public Pair defaultPattern() {
        return null;
    }


}
