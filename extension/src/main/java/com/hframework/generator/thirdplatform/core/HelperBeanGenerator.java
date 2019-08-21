package com.hframework.generator.thirdplatform.core;

import com.hframework.beans.class0.Class;
import com.hframework.beans.class0.Field;
import com.hframework.beans.class0.Method;
import com.hframework.common.resource.ResourceWrapper;
import com.hframework.common.util.*;
import com.hframework.common.util.file.FileUtils;
import com.hframework.common.util.message.VelocityUtil;
import com.hframework.generator.thirdplatform.bean.Descriptor;
import com.hframework.generator.thirdplatform.bean.GeneratorConfig;
import com.hframework.generator.thirdplatform.bean.descriptor.Rule;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by zqh on 2016/4/16.
 */
public class HelperBeanGenerator extends AbstractGenerator implements Generator<GeneratorConfig,Descriptor>{
    @Override
    protected boolean generateInternal(GeneratorConfig generatorConfig, Descriptor descriptor) {

        Class beanClass = new Class();
        beanClass.setSrcFilePath(javaRootPath);
        beanClass.setClassPackage(javaPackage);
        beanClass.setClassName(JavaUtil.getJavaClassName(platformName) + "Helper");
        beanClass.addImportClass("com.hframework.common.helper.Rules");
        helperClass = beanClass;

        beanClass.addImportClass("com.hframework.common.util.security.*");

        List<Rule> ruleList = descriptor.getGlobal().getRules().getRuleList();
        for (Rule rule : ruleList) {
            Method method = new Method();
            method.setModifier("public static");
            method.setName(rule.getId());
            method.setReturnType("String");
            method.setExceptionStr(" throws Exception");

            String text = rule.getText().replaceAll("[ ]+", "");
            if(text != null && text.trim().startsWith("//TODO")) {
                method.addCodeLn("//TODO");
                method.addCodeLn("return null;");
            }else if(text != null && text.startsWith("Rules.")) {
                String[] strings = RegexUtils.find(text, "[\\#\\$]\\{[ a-zA-Z:0-9_]+\\}");
                for (String string : strings) {
                    String paramName = JavaUtil.getJavaVarName(string.substring(2, string.length() - 1));
                    if(string.startsWith("#")) {
                        text = text.replace(string, JavaUtil.getJavaClassName(platformName) + "Config.getInstance().get"
                                + ResourceWrapper.JavaUtil.getJavaClassName(paramName) + "()");
                    }else {
                        if("this".equals(paramName)) {
                            text = text.replace(string, "object");
                        }
                        method.addParameter(new Field("Object", "object"));
                    }
                }
                method.addCodeLn("return " + text + ";");
            }else if("encrypt".equals(rule.getType()) || "decrypt".equals(rule.getType())) {
                method.addCodeLn("return " + getCodeLine(text, method) + ";");
            }else {
                method.addParameter(new Field("String", "sourceValue"));
                beanClass.addImportClass(text);
                createDefaultValueMapperClass(text);
                method.addCodeLn("return new " + text.substring(text.lastIndexOf(".") + 1) + "().mapping(sourceValue);");

            }

        beanClass.addMethod(method);
        }

        Map map = new HashMap();
        map.put("CLASS", beanClass);
        String content = VelocityUtil.produceTemplateContent("com/hframework/generator/vm/bean.vm", map);
        System.out.println(content);
        FileUtils.writeFile(beanClass.getFilePath(), content);

        return false;
    }

    private void createDefaultValueMapperClass(String text) {
        String packageName = javaPackage + ".mapping";
        String className = null;
        if(text.contains(".")) {
            packageName = text.substring(0, text.lastIndexOf("."));
            className = text.substring(text.lastIndexOf(".") + 1);
        }else {
            className = text;
        }
        Class beanClass = new Class();
        beanClass.setSrcFilePath(javaRootPath);
        beanClass.setClassPackage(packageName);
        beanClass.setClassName(className);
        beanClass.addInterface("ValueMapper");

        beanClass.addImportClass("com.hframework.common.bean.ValueMapper");


        Method method = new Method();
        method.setName("mapping");
        method.setReturnType("String");
        method.setParameterStr("String sourceValue");
        method.addCodeLn("//TODO add you code here ..");
        method.addCodeLn("return null;");
        beanClass.addMethod(method);

        Map map = new HashMap();
        map.put("CLASS", beanClass);
        String content = VelocityUtil.produceTemplateContent("com/hframework/generator/vm/bean.vm", map);
        System.out.println(content);
        FileUtils.writeFile(beanClass.getFilePath(), content);
    }

    private String getCodeLine(String text, Method method) {
        boolean encrypt = true;
        if(text.startsWith("(-)")) {
            encrypt = false;
            text = text.substring(3);
        }else if(text.startsWith("(+)")){
            encrypt = true;
            text = text.substring(3);
        }

        String codeLine = "";
        if(text.contains("(") && text.contains(")")) {
            String keyword = text.substring(0, text.indexOf("("));
            String subExpress = text.substring(text.indexOf("(") + 1, text.lastIndexOf(")"));
            codeLine = keyword + "Util." + (encrypt ? "encrypt" :"decrypt") + "(" + getCodeLine(subExpress,method) +")";
        }else {
            String[] params = text.split(",");
            for (String param : params) {
                if(StringUtils.isNotBlank(param)) {
                    String[] strings = RegexUtils.find(param, "[\\#\\$]\\{[ a-zA-Z:0-9_]+\\}");
                    String codeSegment = "";
                    for (String string : strings) {
                        String paramName = JavaUtil.getJavaVarName(string.substring(2,string.length()-1));
                        if(string.startsWith("#")) {
                            codeSegment += (codeSegment =="" ? "" : " + " )
                                    + JavaUtil.getJavaClassName(platformName) + "Config.getInstance().get"
                                    + ResourceWrapper.JavaUtil.getJavaClassName(paramName) + "()" ;
                        }else {
                            if("this".equals(paramName)) {
                                paramName = encrypt ? "encryptString" : "decryptString";
                            }
                            method.addParameter(new Field("String", paramName));
                            codeSegment += ( (codeSegment =="" ? "" : " + ") + paramName );
                        }
                    }
                    codeLine += codeSegment + ", ";
                }
            }

            codeLine = codeLine.substring(0,codeLine.lastIndexOf(", "));
        }
        return codeLine;
    }
}
