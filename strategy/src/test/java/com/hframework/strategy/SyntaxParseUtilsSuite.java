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
        String str = "((if($age>=25&&$age<40):10elseif(($age>20&&$age<=25)||($age>=40&&$age<45)):8elseif($age>15&&$age<50):6else:4)>8):1+1.2*(if($age>=25&&$age<40):10elseif(($age>20&&$age<=25)||($age>=40&&$age<45)):8elseif($age>15&&$age<50):6else:4)+(if($area=='北京'||$area=='上海'||$area=='广州'||$area=='深圳'):10elseif($area=='杭州'||$area=='成都'||$area=='武汉'||$area=='天津'):8elseif($area=='郑州'||$area=='长沙'||$area=='海口'||$area=='南宁'):6else:4)/3";
        int ifThenSplitIndex = SyntaxParseUtils.findIfThenSplitIndex(str);
        System.out.println(str.substring(0, ifThenSplitIndex));
        System.out.println(str.substring(ifThenSplitIndex + 1));
    }


}
