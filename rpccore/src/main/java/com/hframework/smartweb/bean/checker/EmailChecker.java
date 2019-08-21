package com.hframework.smartweb.bean.checker;

import com.hframework.smartweb.bean.pattern.StringPattern;
import org.apache.commons.lang3.tuple.Pair;

/**
 * 邮箱检查器
 * 满足正则表达式：\w+([-+.]\w+)*@\w+([-.]\w+)*\.\w+([-.]\w+)*
 * Created by zhangquanhong on 2017/2/24.
 */

public class EmailChecker extends AbstractSmartChecker<String, StringPattern> {

    @Override
    public Pair defaultPattern() {
        return StringPattern.EMAIL;
    }

}
