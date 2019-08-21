package com.hframework.generator.thirdplatform.core;

import com.hframework.common.util.RegexUtils;
import com.hframework.common.util.file.FileUtils;
import com.hframework.common.util.message.XmlUtils;
import com.hframework.generator.thirdplatform.bean.Descriptor;
import com.hframework.generator.thirdplatform.bean.GeneratorConfig;
import com.hframework.generator.thirdplatform.bean.InterfaceExample;
import com.hframework.generator.thirdplatform.bean.descriptor.Interface;
import org.apache.commons.lang.StringUtils;

import java.io.IOException;
import java.text.MessageFormat;
import java.util.List;

/**
 * Created by zqh on 2016/4/16.
 */
public class PropertiesGenerator extends AbstractGenerator implements Generator<GeneratorConfig,Descriptor>{
    @Override
    protected boolean generateInternal(GeneratorConfig generatorConfig, Descriptor descriptor) {

        //获取属性文件路径
        String resourceFilePath = resourceRootPath + "/" + resourceFolder + "/" + platformName + ".properties";

        //写入静态参数
        List<String> staticParameterList = descriptor.getGlobal().getStaticParameters().getStaticParameterList();
        String fileContext = "";
        for (String staticParameter : staticParameterList) {
            fileContext += MessageFormat.format(resourceKeyPrefix,platformName) + staticParameter + "=" + "\n";
        }

        fileContext += "\n";

        //写入接口明细
        List<Interface> interface1List = descriptor.getInterfaces().getInterface1List();
        for (Interface anInterface : interface1List) {
            try {
                if(StringUtils.isNotBlank(anInterface.getTemplate())) {
                    InterfaceExample interfaceExample =
                            XmlUtils.readValueFromFile("/thirdplatform/" + anInterface.getTemplate(), InterfaceExample.class);
                    String urlTemplate = getUrlTemplate(interfaceExample.getUrl().trim());
                    fileContext += MessageFormat.format(resourceKeyPrefix,platformName) +
                            "interface." +  anInterface.getName() + "=" + urlTemplate + "\n";
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }


        FileUtils.writeFile(resourceFilePath, fileContext);

        return false;
    }

    private String getUrlTemplate(String url) {
        String[] params = RegexUtils.find(url, "=[^=\\&]*");
        if(params != null) {
            for (int i = 0; i < params.length; i++) {
                url = url.replace(params[i], "={" + i + "}");
            }
        }
        return url;
    }
}
