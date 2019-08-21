package com.hframework.strategy.rule.repository;

import com.hframework.common.util.RegexUtils;
import com.hframework.strategy.rule.exceptions.RuleInitializeException;
import org.apache.commons.lang3.StringUtils;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by zhangquanhong on 2018/1/1.
 */
public class SyntaxParseUtils {


    public static Map<Integer, String> getMatchIndexMapping(String compressRule, String pattern) {
        Map<Integer, String> result = new LinkedHashMap<Integer, String>();
        String[] matches = RegexUtils.find(compressRule, pattern);
        int removeLength = 0;
        for (String match : matches) {
            result.put(removeLength + compressRule.indexOf(match), match);
            removeLength += compressRule.indexOf(match) + match.length();
            compressRule = compressRule.substring(compressRule.indexOf(match) + match.length());
        }
        return result;
    }

    public static String removeInvalidEndOfQuote(String string) {
        if(StringUtils.isBlank(string)) {
            return string;
        }
        if(string.contains(")")) {
            Map<Integer, String> indexKeyMap = getMatchIndexMapping(string, "[\\(\\)]");
            int count = 0;
            for (Integer index : indexKeyMap.keySet()) {
                String keyWork = indexKeyMap.get(index);
                if("(".equals(keyWork)){
                    count ++;
                }else{
                    count --;
                    if(count == -1) {
                        return string.substring(0, index);
                    }
                }
            }
            if(count > 0) {
                throw new RuleInitializeException("syntax parse exception : [ " + string + " ] can't trim to a valid expression !");
            }
        }

        return string;
    };

    public static String trimQuoteInPairs(String string) {
        if(StringUtils.isBlank(string)) {
            return string;
        }

        string = string.trim();
        String firstInvalidString =  null;
        while (string.startsWith("(") && string.endsWith(")")){
            Map<Integer, String> indexKeyMap = getMatchIndexMapping(string, "[\\(\\)]");
            int count = 0;
            for (Integer index : indexKeyMap.keySet()) {
                String keyWork = indexKeyMap.get(index);
                if("(".equals(keyWork)){
                    count ++;
                }else{
                    count --;
                    if(count == 0) {
                        firstInvalidString =  string.substring(0, index + 1);
                        break;
                    }
                }
            }
            if(string.equals(firstInvalidString)) {//case : ((a+b)+(b+c))
                string = string.substring(1, string.length() - 1);
            }else {//case : (a+b) +(b+c)
                return string;
            }
        }

        return string;
    };


    public static String trimQuoteOnVars(String string) {
        if(StringUtils.isBlank(string)) {
            return string;
        }

        String[] varInfos = RegexUtils.find(string, "\\(\\$[a-zA-Z_0-9]+\\)");
        if(varInfos.length > 0) {
            for (String varInfo : varInfos) {
                String varName = varInfo.substring(1, varInfo.length() -1);
                string =string.replace(varInfo, varName);
            }
            return trimQuoteOnVars(string);
        }

        return string;
    }

    public static int findIfThenSplitIndex(String ifThenWords) {
        int index = 0;
        if(ifThenWords.contains(":")){
            if(ifThenWords.indexOf(":") == ifThenWords.lastIndexOf(":")) {//仅有一个条件
                return ifThenWords.indexOf(":");
            }else{
                String[] parts = ifThenWords.split(":");//用String.split()，不用StringUtils.split()，因为不能将""trim掉

                int quoteCount = 0;
                int ifElseCount = 0;
                int partsLength = 0;
                for (String part : parts) {
                    quoteCount += RegexUtils.find(part, "\\(").length;
                    quoteCount -= RegexUtils.find(part, "\\)").length;
                    ifElseCount += RegexUtils.find(part, "if").length;
                    ifElseCount -= RegexUtils.find(part, "else").length;
                    partsLength += part.length() + ":".length();
                    if(quoteCount == 0 && ifElseCount == 0){
                        return partsLength -1;                    }
                }
            }
        }

        return 0;

    }
}
