package com.hframework.smartweb.bean.checker;

import com.hframework.common.util.DateUtils;
import com.hframework.smartweb.bean.pattern.DatePattern;
import com.hframework.smartweb.bean.pattern.MoneyPattern;
import org.apache.commons.lang3.tuple.Pair;

import java.util.Date;

/**
 * 金额检查器
 * 满足正则表达式：^((\d+\.[1-9]\d?)|(\d+\.\d?[1-9])|(\d*[1-9]\d*\.\d{1,2})|(\d*[1-9]\d*))$
 * Created by zhangquanhong on 2017/2/24.
 */
public class MoneyChecker extends AbstractSmartChecker<String, MoneyPattern> {

    @Override
    public Pair defaultPattern() {
        return MoneyPattern.IsNormalMoney;
    }

    @Override
    public boolean check(String value, String pattern) {
        if("IsNormalMoney".equals(pattern)) {
            return super.check(value, (String) MoneyPattern.IsNormalMoney.getLeft());
        }
        return false;
    }
}
