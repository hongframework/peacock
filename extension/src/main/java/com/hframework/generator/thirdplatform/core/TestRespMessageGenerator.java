package com.hframework.generator.thirdplatform.core;

import com.hframework.common.util.StringUtils;
import com.hframework.common.util.file.FileUtils;
import com.hframework.common.util.message.XmlUtils;
import com.hframework.generator.thirdplatform.bean.Descriptor;
import com.hframework.generator.thirdplatform.bean.GeneratorConfig;
import com.hframework.generator.thirdplatform.bean.InterfaceExample;
import com.hframework.generator.thirdplatform.bean.descriptor.Interface;

import java.io.IOException;
import java.util.List;

/**
 * Created by zqh on 2016/4/16.
 */
public class TestRespMessageGenerator extends AbstractGenerator implements Generator<GeneratorConfig,Descriptor>{
    @Override
    protected boolean generateInternal(GeneratorConfig generatorConfig, Descriptor descriptor) {

        //获取属性文件路径
        String resourceFilePath = resourceRootPath + "/" + resourceFolder + "/" + platformName + "/";

        //写入接口明细
        List<Interface> interface1List = descriptor.getInterfaces().getInterface1List();
        for (Interface anInterface : interface1List) {
            try {
                if(StringUtils.isNotBlank(anInterface.getTemplate())) {
                    InterfaceExample interfaceExample =
                            XmlUtils.readValueFromFile("/thirdplatform/" + anInterface.getTemplate(), InterfaceExample.class);
                    String responseMessage = interfaceExample.getResponseMessage().trim();
                    String name = interfaceExample.getName();
                    FileUtils.writeFile(resourceFilePath + anInterface.getName() + ".response", responseMessage);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }




        return false;
    }
}
