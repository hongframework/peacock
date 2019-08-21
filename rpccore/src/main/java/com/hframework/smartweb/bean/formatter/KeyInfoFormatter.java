package com.hframework.smartweb.bean.formatter;

import com.google.common.base.Joiner;
import com.hframework.smartweb.bean.pattern.StringPattern;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Pair;

import java.util.*;

/**
 * Created by zhangquanhong on 2017/2/24.
 */
public class KeyInfoFormatter extends AbstractSmartFormatter<String, StringPattern> {


    @Override
    public String format(String value, String pattern) {
        if(StringUtils.isBlank(value)) return "";

        if(StringPattern.NameTitle.getKey().equals(pattern)) {
            return value.trim().charAt(0) + "**";
//        }else if(StringPattern.TelPhoneVague.getKey().equals(pattern)) {
//            if(value.length() > 7) {
//                return value.substring(0, 3) + "****" + value.substring(7);
//            }
//        }else if(StringPattern.IdCardVague.getKey().equals(pattern)) {
//            Character[] vagues = new Character[value.length() - 7];
//            Arrays.fill(vagues, '*');
//            return value.substring(0, 3) + Joiner.on("").join(vagues) + value.substring(value.length() -4);
        }else {
            if(value.length() > 7) {
                Character[] vagues = new Character[value.length() - 7];
                Arrays.fill(vagues, '*');
                return value.substring(0, 3) + Joiner.on("").join(vagues) + value.substring(value.length() -4);
            }else {
                return value;
            }
        }

    }

    @Override
    public Pair defaultPattern() {
        return null;
    }

    public static void main(String[] args) {
        KeyInfoFormatter formatter = new KeyInfoFormatter();
        String bg = "张生";
        System.out.println(formatter.format("张生", "NameTitle"));
        System.out.println(formatter.format("51231231321312321444", "TelPhoneVague"));
        System.out.println(formatter.format("51231231321312321444", "IdCardVague"));
        System.out.println(formatter.format("51231231321312321444", "1"));
    }
}
