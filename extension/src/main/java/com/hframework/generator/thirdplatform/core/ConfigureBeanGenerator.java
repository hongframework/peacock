package com.hframework.generator.thirdplatform.core;

import com.hframework.beans.class0.Class;
import com.hframework.beans.class0.Field;
import com.hframework.common.resource.ResourceWrapper;
import com.hframework.common.util.JavaUtil;
import com.hframework.common.util.file.FileUtils;
import com.hframework.common.util.message.VelocityUtil;
import com.hframework.generator.thirdplatform.bean.Descriptor;
import com.hframework.generator.thirdplatform.bean.GeneratorConfig;
import com.hframework.generator.thirdplatform.bean.descriptor.Interface;

import java.text.MessageFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by zqh on 2016/4/16.
 */
public class ConfigureBeanGenerator extends AbstractGenerator implements Generator<GeneratorConfig,Descriptor>{
    @Override
    protected boolean generateInternal(GeneratorConfig generatorConfig, Descriptor descriptor) {

        Class beanClass = new Class();
        beanClass.setSrcFilePath(javaRootPath);
        beanClass.setClassPackage(javaPackage);
        beanClass.setClassName(JavaUtil.getJavaClassName(platformName) + "Config");
        beanClass.addAnnotation("@Source(\"" + resourceFolder + "/" + platformName + ".properties\")");


        beanClass.addImportClass("com.hframework.common.annotation.*");
        beanClass.addImportClass("com.hframework.common.util.ResourceWrapper");
        beanClass.addImportClass("java.lang.reflect.InvocationTargetException");


        List<String> staticParameterList = descriptor.getGlobal().getStaticParameters().getStaticParameterList();

        for (String staticParameter : staticParameterList) {
            Field field = new Field("String",
                    ResourceWrapper.JavaUtil.getJavaVarName(staticParameter.replaceAll(resourceKeyPrefix, "")));
            field.addFieldAnno("@Key( \"" + MessageFormat.format(resourceKeyPrefix,platformName) + staticParameter + "\")");
            beanClass.addField(field);
        }

        //写入接口明细
        List<Interface> interface1List = descriptor.getInterfaces().getInterface1List();
        for (Interface anInterface : interface1List) {
            Field field = new Field("String", ResourceWrapper.JavaUtil.getJavaVarName(anInterface.getName()));
            field.addFieldAnno("@Key( \"" + MessageFormat.format(resourceKeyPrefix,platformName) + "interface." +  anInterface.getName() + "\")");
            beanClass.addField(field);
        }

        Map map = new HashMap();
        map.put("CLASS", beanClass);
        String content = VelocityUtil.produceTemplateContent("com/hframework/generator/vm/configureBeanByTemplate.vm", map);
        System.out.println(content);
        FileUtils.writeFile(beanClass.getFilePath(), content);

        return false;
    }
}
