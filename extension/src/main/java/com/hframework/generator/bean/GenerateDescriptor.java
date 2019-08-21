package com.hframework.generator.bean;

import com.hframework.beans.class0.Field;
import com.hframework.beans.class0.XmlNode;
import com.hframework.beans.class0.Class;

import java.lang.*;

/**
 * Created by zhangquanhong on 2016/4/19.
 */
public interface GenerateDescriptor {

    public void setJavaPackage(String javaPackage);

    public String getJavaPackage();

    public void setJavaRootPath(String javaRootPath);

    public String getJavaRootPath();

    public void setTemplatePath(String templatePath);

    public String getTemplatePath();

    public void executeClassExtend(Class clazz, XmlNode xmlNode);

    public void executeFieldExtend(Field field, XmlNode childXmlNode);

    public void executeXmlNodeExtend(XmlNode xmlNode);
}
