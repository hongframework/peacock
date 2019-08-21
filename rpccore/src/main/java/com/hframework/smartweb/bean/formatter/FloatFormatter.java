package com.hframework.smartweb.bean.formatter;

import com.hframework.smartweb.bean.SmartPattern;
import com.hframework.smartweb.bean.pattern.MoneyPattern;
import org.apache.commons.lang3.tuple.Pair;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;

/**
 * Created by zhangquanhong on 2017/2/24.
 */
public class FloatFormatter extends AbstractSmartFormatter<BigDecimal, MoneyPattern> {

    @Override
    public String format(BigDecimal value, String pattern) {
        if(value == null) value = BigDecimal.ZERO;
        DecimalFormat decimalFormat = new DecimalFormat(pattern.replaceAll("\\$","#"));
        return decimalFormat.format(value);
    }


    @Override
    public Pair defaultPattern() {
        return Pair.of("#,###.##", "#,###.##");
    }


    public static void main(String[] args) {
        BigDecimal bg = new BigDecimal("11112313123213.121321321321");
        System.out.println(new FloatFormatter().format(bg, "$,$$0.00克"));

    }

}
