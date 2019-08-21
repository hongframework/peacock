package com.hframework.smartapi;

import com.hframework.smartweb.SmartExpanderFactory;
import org.junit.Test;

public class SmartExpanderFactorySuite {

    @Test
    public void test(){
        System.out.println(SmartExpanderFactory.getParameterXml(null));
        System.out.println(SmartExpanderFactory.getFormatterXml());
        System.out.println(SmartExpanderFactory.getFormatterPatternXml(null));
        System.out.println(SmartExpanderFactory.getCheckerXml());
        System.out.println(SmartExpanderFactory.getCheckerPatternXml(null));
        System.out.println(SmartExpanderFactory.getParserPatternXml());
        System.out.println(SmartExpanderFactory.getParserXml());

    }
}
