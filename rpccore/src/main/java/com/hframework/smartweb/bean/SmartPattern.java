package com.hframework.smartweb.bean;

/**
 * Created by zhangquanhong on 2017/2/24.
 */
public enum SmartPattern {
    None(""),
    YYYYMMDD("yyyyMMdd"),
    YYYY_MM_DD("yyyy-MM-dd"),
    YYYY_MM_DD_24H_MM_SS("yyyy-MM-dd HH:mm:ss"),

    FormatMoneyWan("FormatMoneyWan"),//万元为单位
    FormatMoneyRegion("FormatMoneyRegion"), //计算金额区间
    FormatMoneyThousands("FormatMoneyThousands"), //千分位
    FormatMoneyChinese("FormatMoneyChinese"), //中文格式化


    IsNormalMoney("^((\\d+\\.[1-9]\\d?)|(\\d+\\.\\d?[1-9])|(\\d*[1-9]\\d*\\.\\d{1,2})|(\\d*[1-9]\\d*))$");



    private String pattern;

    SmartPattern(String pattern) {
        this.pattern = pattern;
    }

    public String getPattern() {
        return pattern;
    }

    public void setPattern(String pattern) {
        this.pattern = pattern;
    }

    public boolean match(String pattern) {
        return this.pattern.equals(pattern);
    }
}
