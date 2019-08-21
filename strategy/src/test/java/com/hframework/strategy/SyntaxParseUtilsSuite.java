package com.hframework.strategy;

import com.hframework.strategy.rule.repository.SyntaxParseUtils;
import org.junit.Test;

/**
 * Created by zhangquanhong on 2018/1/1.
 */
public class SyntaxParseUtilsSuite {

    @Test
    public void removeInvalidEndOfQuote(){
        System.out.println(SyntaxParseUtils.removeInvalidEndOfQuote("(5*(3+4)-9)*2)"));
    }

    @Test
    public void trimQuoteInPairs(){
        System.out.println(SyntaxParseUtils.trimQuoteInPairs(" (a+b) +(b+c)"));
        System.out.println(SyntaxParseUtils.trimQuoteInPairs(" ((a+b) +(b+c))"));
        System.out.println(SyntaxParseUtils.trimQuoteInPairs("(if($age>90):'A'elseif($age>80):'B'else:'C')+(if($income>90):'D'elseif($income>80):'E'else:'F')"));
    }

    @Test
    public void findIfThenSplitIndex(){
        String str = "((if($age>=25&&$age<40):10elseif(($age>20&&$age<=25)||($age>=40&&$age<45)):8elseif($age>15&&$age<50):6else:4)>8):1+1.2*(if($age>=25&&$age<40):10elseif(($age>20&&$age<=25)||($age>=40&&$age<45)):8elseif($age>15&&$age<50):6else:4)+(if($area=='����'||$area=='�Ϻ�'||$area=='����'||$area=='����'):10elseif($area=='����'||$area=='�ɶ�'||$area=='�人'||$area=='���'):8elseif($area=='֣��'||$area=='��ɳ'||$area=='����'||$area=='����'):6else:4)/3";
        int ifThenSplitIndex = SyntaxParseUtils.findIfThenSplitIndex(str);
        System.out.println(str.substring(0, ifThenSplitIndex));
        System.out.println(str.substring(ifThenSplitIndex + 1));
    }


}
