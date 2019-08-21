package com.hframework.smartweb.bean.formatter;

import com.hframework.smartweb.bean.pattern.MoneyPattern;
import org.apache.commons.lang3.tuple.Pair;

import java.math.BigDecimal;
import java.text.DecimalFormat;

/**
 * Created by zhangquanhong on 2017/2/24.
 */
public class BooleanToNumberFormatter extends AbstractSmartFormatter<Boolean, MoneyPattern> {

    @Override
    public Integer format(Boolean value, String pattern) {
        if(value == null) return null;
        return value ? 1 : 0;
    }


    @Override
    public Pair defaultPattern() {
        return Pair.of("", "");
    }
}
