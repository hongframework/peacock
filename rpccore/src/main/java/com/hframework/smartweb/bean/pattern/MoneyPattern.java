package com.hframework.smartweb.bean.pattern;

import com.hframework.smartweb.bean.Pattern;
import org.apache.commons.lang3.tuple.Pair;

/**
 * Created by zhangquanhong on 2017/6/5.
 */
public class MoneyPattern  implements Pattern {

    public static final Pair FormatMoney = Pair.of("formatMoney", "格式化为千分位");
    public static final Pair FormatMoneyByRegion = Pair.of("formatMoneyByRegion", "映射到指定区间");
    public static final Pair IsNormalMoney =
            Pair.of("^((\\d+\\.[1-9]\\d?)|(\\d+\\.\\d?[1-9])|(\\d*[1-9]\\d*\\.\\d{1,2})|(\\d*[1-9]\\d*))$", "可转化为金钱");

}
