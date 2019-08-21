package com.hframework.smartweb.bean.pattern;

import com.hframework.smartweb.bean.Pattern;
import org.apache.commons.lang3.tuple.Pair;

/**
 * Created by zhangquanhong on 2017/6/5.
 */
public  class StringPattern implements Pattern {
    public static final Pair EMAIL = Pair.of("\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*", "邮箱");

    public static final Pair NameTitle = Pair.of("NameTitle", "先生/女生称谓");
    public static final Pair TelPhoneVague = Pair.of("TelPhoneVague", "手机号加※");
    public static final Pair IdCardVague = Pair.of("IdCardVague", "身份证加※");
}