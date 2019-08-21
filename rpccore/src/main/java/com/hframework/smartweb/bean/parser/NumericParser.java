package com.hframework.smartweb.bean.parser;


import com.hframework.smartweb.bean.pattern.MoneyPattern;
import org.apache.commons.lang3.tuple.Pair;

import java.math.BigDecimal;

/**
 * Created by zhangquanhong on 2017/2/24.
 */
public class NumericParser extends AbstractSmartParser<BigDecimal, MoneyPattern>{

    @Override
    public BigDecimal parse(String value, String pattern) throws Exception {
        return new BigDecimal(value);
    }

    @Override
    public Pair defaultPattern() {
        return MoneyPattern.IsNormalMoney;
    }

}
