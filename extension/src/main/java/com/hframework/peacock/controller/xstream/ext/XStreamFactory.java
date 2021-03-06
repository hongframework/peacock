package com.hframework.peacock.controller.xstream.ext;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.core.util.QuickWriter;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;
import com.thoughtworks.xstream.io.naming.NameCoder;
import com.thoughtworks.xstream.io.naming.NoNameCoder;
import com.thoughtworks.xstream.io.xml.PrettyPrintWriter;
import com.thoughtworks.xstream.io.xml.XppDomDriver;
import org.springframework.beans.BeanUtils;

import java.io.Writer;
import java.lang.reflect.Field;

/**
 * Created by GongXunyao on 2015/12/29.
 */

/**
 * XStream工厂类
 */
public class XStreamFactory {
    public static XStream getXStream() {
        final NameCoder nameCoder = new NoNameCoder ();
        XStream xStream = new XStream(new XppDomDriver (nameCoder) {
            @Override
            public HierarchicalStreamWriter createWriter(Writer out) {
                //输出格式化的xml字符串
                return new PrettyPrintWriter (out, nameCoder) {
                    //输出压缩的xml字符串，删除多余的空白符
                    //return new CompactWriter (out, nameCoder) {
                    boolean cdata = false;
                    Class<?> parentClass = null;
                    @Override
                    public void startNode(String name, Class clazz) {
                        super.startNode(name, clazz);
                        if(parentClass == null){
                            parentClass = clazz;
                        }else if(!BeanUtils.isSimpleProperty(clazz)){
                            parentClass = clazz;
                        }
                        cdata = needCDATA(parentClass,name);
                    }

                    @Override
                    protected void writeText(QuickWriter writer, String text) {
                        if (cdata) {
                            writer.write("<![CDATA[\n");
                            writer.write(text);
                            writer.write("\n]]>");
                        } else {
                            writer.write(text);
                        }
                    }
                };
            }
        });
        return xStream;
    }

    private static boolean needCDATA(Class<?> targetClass, String fieldAlias){
        boolean cdata = false;
        //first, scan self
        cdata = existsCDATA(targetClass, fieldAlias);
        if(cdata) return cdata;
        //if cdata is false, scan supperClass until java.lang.Object
        Class<?> superClass = targetClass.getSuperclass();
        while(!superClass.equals(Object.class)){
            cdata = existsCDATA(superClass, fieldAlias);
            if(cdata) return cdata;
            superClass = superClass.getSuperclass();
        }
        return false;
    }

    private static boolean existsCDATA(Class<?> clazz, String fieldAlias){
        //scan fields
        Field[] fields = clazz.getDeclaredFields();
        for (Field field : fields) {
            //1. exists XStreamCDATA
            if(field.getAnnotation(XStreamCDATA.class) != null ){
                XStreamAlias xStreamAlias = field.getAnnotation(XStreamAlias.class);
                //2. exists XStreamAlias
                if(null != xStreamAlias){
                    if(fieldAlias.equals(xStreamAlias.value()))//matched
                        return true;
                }else{// not exists XStreamAlias
                    if(fieldAlias.equals(field.getName()))
                        return true;
                }
            }
        }
        return false;
    }
}
