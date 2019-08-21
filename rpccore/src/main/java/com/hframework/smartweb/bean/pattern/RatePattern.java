package com.hframework.smartweb.bean.pattern;

import com.hframework.smartweb.bean.Pattern;
import org.apache.commons.lang3.tuple.Pair;

/**
 * Created by zhangquanhong on 2017/6/5.
 */
public class RatePattern implements Pattern {

    public static final Pair Percent = Pair.of("Percent", "百分之*");
    public static final Pair Thousandth = Pair.of("Thousandth", "千分之*");
    public static final Pair ExtremeOf = Pair.of("ExtremeOf", "万分之*");
    public static final Pair Auto = Pair.of("Auto", "智能占比");
}
