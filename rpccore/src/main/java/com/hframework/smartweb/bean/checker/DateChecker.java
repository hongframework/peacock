package com.hframework.smartweb.bean.checker;

import com.hframework.common.util.DateUtils;
import com.hframework.smartweb.bean.pattern.DatePattern;
import com.hframework.smartweb.bean.pattern.MoneyPattern;
import org.apache.commons.lang3.tuple.Pair;

import java.util.Date;

/**
 * 日期检查
 *
 * Created by zhangquanhong on 2017/11/28.
 */
public class DateChecker extends AbstractSmartChecker<Date, DatePattern> {

    @Override
    public Pair defaultPattern() {
        return DatePattern.LessNow;
    }

    @Override
    public boolean check(Date value, String pattern) {
        if(DatePattern.LessNow.getLeft().equals(pattern)) {
            return value.before(DateUtils.getCurrentDate());
        }
        return false;
    }
}
