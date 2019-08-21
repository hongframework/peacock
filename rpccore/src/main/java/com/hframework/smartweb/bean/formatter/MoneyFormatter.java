package com.hframework.smartweb.bean.formatter;

import com.hframework.smartweb.bean.SmartPattern;
import com.hframework.smartweb.bean.pattern.MoneyPattern;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Pair;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;

/**
 * Created by zhangquanhong on 2017/2/24.
 */
public class MoneyFormatter extends AbstractSmartFormatter<BigDecimal, MoneyPattern> {
    private static final String STR="小于";
    private static final String STR_END="万元";

    @Override
    public String format(BigDecimal value, String pattern) {
        if(SmartPattern.FormatMoneyWan.match(pattern)) {
            return formatWan(value);
        }else if(SmartPattern.FormatMoneyRegion.match(pattern)) {
            return formatRegion(value);
        }else if(SmartPattern.FormatMoneyThousands.match(pattern)) {
            return formatThousands(value);
        }else if(SmartPattern.FormatMoneyChinese.match(pattern)) {
            return formatChinese(value);
        }else {
            return String.valueOf(value.floatValue());
        }
    }


    @Override
    public Pair defaultPattern() {
        return MoneyPattern.FormatMoney;
    }


    public static String formatThousands(BigDecimal money){
        if(money == null) {
            return "0.00元";
        }
        // 不处理
        DecimalFormat decimalFormat = new DecimalFormat("#,###.##");
        return decimalFormat.format(money) + "元";
    }

    public static String formatChinese(BigDecimal value){
        if(value == null) {
            return "0元";
        }
        DecimalFormat decimalFormat = new DecimalFormat("#,###.##");
        long i = value.longValue();
        String unit = "";
        String val = "";
        if(i > 1 * 10000 * 10000) {
            unit = "亿";
            val = decimalFormat.format(value.divide(new BigDecimal(1 * 10000 * 10000), 2, RoundingMode.FLOOR));
        }else if(i > 1 * 10000) {
            unit = "万";
            val = decimalFormat.format(value.divide(new BigDecimal(1 * 10000), 2, RoundingMode.FLOOR));
        }else {
            val = decimalFormat.format(value.divide(new BigDecimal(1 * 10000), 2, RoundingMode.FLOOR));
        }
        return val + unit + "元";
    }

    public static String formatWan(BigDecimal money){
        if(money == null) {
            return "0.00元";
        }
        if (money.compareTo(new BigDecimal(10000)) >=0) {
            money = money.setScale(2, RoundingMode.FLOOR).divide(new BigDecimal(10000), RoundingMode.FLOOR);
            DecimalFormat decimalFormat = new DecimalFormat("####0.00" );
            return  decimalFormat.format(money) + "万元";
        } else {
            // 不处理
            DecimalFormat decimalFormat = new DecimalFormat("####0.00");
            return decimalFormat.format(money) + "元";
        }
    }

    public static String formatRegion(BigDecimal value){
        if(value == null) {
            return "小于1万";
        }
        long i = value.longValue();
        if(i == 0) {
            return "0.00元";
        }else if(i < 10000) {
            return STR + 1 + STR_END;
        }else {
            i = i/10000;
            if(i < 5) {
                return STR + 5 + STR_END;
            }else if(i < 10) {
                return STR + 10 + STR_END;
            }else if(i < 100) {
                return STR + (i + 1) + STR_END;
            }else if(i < 500) {
                return STR + (i + 1) + STR_END;
            }else if(i < 1000) {
                return STR + (i + 1) + STR_END;
            }else if(i < 5000) {
                return STR + 5000 + STR_END;
            }else if(i < 10000) {
                return STR + 10000 + STR_END;
            }else if(i < 5 * 10000) {
                return STR + 5 * 10000 + STR_END;
            }else if(i < 10 * 10000) {
                return STR + 10 * 10000 + STR_END;
            }else {
                return "大于" + 10 * 10000 + STR_END;
            }
        }
    }


    public static void main(String[] args) {
        BigDecimal bg = new BigDecimal("11112313123213.121321321321");
        System.out.println(formatWan(bg));
        System.out.println(formatRegion(bg));
        System.out.println(formatThousands(bg));
        System.out.println(formatChinese(bg));

    }

}
