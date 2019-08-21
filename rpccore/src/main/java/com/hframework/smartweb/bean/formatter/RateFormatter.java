package com.hframework.smartweb.bean.formatter;

import com.hframework.smartweb.bean.SmartPattern;
import com.hframework.smartweb.bean.pattern.MoneyPattern;
import com.hframework.smartweb.bean.pattern.RatePattern;
import org.apache.commons.lang3.tuple.Pair;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;

/**
 * Created by zhangquanhong on 2017/2/24.
 */
public class RateFormatter extends AbstractSmartFormatter<BigDecimal, RatePattern> {
    private static final String STR="小于";
    private static final String STR_END="万元";

    @Override
    public String format(BigDecimal value, String pattern) {
        String unit = "";

        if(RatePattern.Auto.getKey().equals(pattern)) {
            if(value.compareTo(new BigDecimal(0.01)) >= 0) {
                pattern = String.valueOf(RatePattern.Percent.getKey());
            }else if(value.compareTo(new BigDecimal(0.001)) >= 0) {
                pattern = String.valueOf(RatePattern.Thousandth.getKey());
            }else {
                pattern = String.valueOf(RatePattern.ExtremeOf.getKey());
            }
        }


        if(RatePattern.Percent.getKey().equals(pattern)) {
            unit = "%";
            value = value.multiply(BigDecimal.valueOf(100));
        }else if(RatePattern.Thousandth.getKey().equals(pattern)) {
            unit = "‰";
            value = value.multiply(BigDecimal.valueOf(1000));
        }else if(RatePattern.ExtremeOf.getKey().equals(pattern)) {
            unit = "‱";
            value = value.multiply(BigDecimal.valueOf(10000));
        }else {
            return value.toPlainString();
        }
        return value.setScale(2, RoundingMode.FLOOR).toPlainString() + unit;
    }


    @Override
    public Pair defaultPattern() {
        return RatePattern.Percent;
    }



    public static void main(String[] args) {
        BigDecimal bg = new BigDecimal("0.00121321321321");
        RateFormatter formatter = new RateFormatter();
        System.out.println(formatter.format(bg, "Percent"));
        System.out.println(formatter.format(bg, "Thousandth"));
        System.out.println(formatter.format(bg, "ExtremeOf"));
        System.out.println(formatter.format(bg, "Auto"));

    }

}
