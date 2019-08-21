package com.hframework.smartweb.bean.formatter;

import com.hframework.smartweb.bean.pattern.StringPattern;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Pair;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by zhangquanhong on 2017/2/24.
 */
public class ArrayMethodFormatter extends AbstractSmartFormatter<List, StringPattern> {


    @Override
    public Object format(List list, String pattern) {
        if(list == null || list.isEmpty()) return null;

        if("head".equals(pattern)){
            return list.get(0);
        }else if("last".equals(pattern)){
            return list.get(list.size() - 1);
//        }else if("min".equals(pattern)){
//            return list.get(list.size() - 1);
//        }else if("max".equals(pattern)){
//            return list.get(list.size() - 1);
        }else{
            return list.size();
        }
    }

    @Override
    public Pair defaultPattern() {
        return null;
    }

}
