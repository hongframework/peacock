package com.hframework.smartweb.bean.checker;

import com.hframework.common.util.DateUtils;
import com.hframework.smartweb.SmartExpanderFactory;
import com.hframework.smartweb.ThreadLocalCenter;
import com.hframework.smartweb.bean.pattern.DatePattern;
import com.hframework.smartweb.bean.pattern.StringPattern;
import org.apache.commons.lang3.tuple.Pair;

import java.util.Date;
import java.util.Map;

/**
 * 字典检查期
 * 满足正则表达式：\w+([-+.]\w+)*@\w+([-.]\w+)*\.\w+([-.]\w+)*
 * Created by zhangquanhong on 2017/2/24.
 */

public class IntDictionaryChecker extends AbstractSmartChecker<Integer, StringPattern> {

    @Override
    public Pair defaultPattern() {
        return StringPattern.EMAIL;
    }

    @Override
    public boolean check(Integer value, String pattern) {
        Map<String, String> dictionary = SmartExpanderFactory.getDictionaryItems(ThreadLocalCenter.programIdTL.get(), pattern);
        return dictionary.containsKey(value);
    }

}
