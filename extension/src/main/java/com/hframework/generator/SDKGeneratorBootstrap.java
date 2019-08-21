package com.hframework.generator;

import com.hframework.common.util.message.XmlUtils;
import com.hframework.generator.thirdplatform.bean.Descriptor;
import com.hframework.generator.thirdplatform.bean.GeneratorConfig;
import com.hframework.generator.thirdplatform.core.*;

import java.io.IOException;

/**
 * Created by zqh on 2016/4/15.
 */
public class SDKGeneratorBootstrap {


    public static void generator(String platformName, String descriptorPath, GeneratorConfig generatorConfig) throws IOException {
        Descriptor descriptor = XmlUtils.readValueFromFile(descriptorPath, Descriptor.class);
        descriptor.setPlatformName(platformName);
        new PropertiesGenerator().generate(generatorConfig, descriptor);
        new TestRespMessageGenerator().generate(generatorConfig, descriptor);
        new ConfigureBeanGenerator().generate(generatorConfig, descriptor);

        HelperBeanGenerator helperBeanGenerator = new HelperBeanGenerator();
        helperBeanGenerator.generate(generatorConfig, descriptor);

        ClientBeanGenerator clientBeanGenerator = new ClientBeanGenerator();
        clientBeanGenerator.helperClass = helperBeanGenerator.helperClass;
        clientBeanGenerator.generate(generatorConfig,descriptor);

        ClientTestBeanGenerator clientTestBeanGenerator = new ClientTestBeanGenerator();
        clientTestBeanGenerator.clientClass = clientBeanGenerator.clientClass;
        clientTestBeanGenerator.setRequestBeanMap(clientBeanGenerator.getRequestBeanMap());
        clientTestBeanGenerator.generate(generatorConfig,descriptor);
    }
}
