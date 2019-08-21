package com.hframework.peacock.controller.xstream.ext;

import com.thoughtworks.xstream.XStream;

import java.io.IOException;

public class XStreamUtil {

    private static final XStream xStream = XStreamFactory.getXStream ();

    public static  <T> String writeValueAsString(T t) throws IOException {
        xStream.processAnnotations(t.getClass());// 识别obj类中的注解
        return xStream.toXML(t);
    }

    public static <T> T readValue(String content, Class<T> valueType) {
        xStream.processAnnotations(valueType);
        xStream.aliasSystemAttribute("BEAN_CLASS", "class");
        return (T) xStream.fromXML(content);
    }
}