package com.hframework.smartapi.api;

import com.hframework.smartapi.CommonSuite;
import com.hframework.smartapi.handler.SmartHandlerFactorySuite;
import com.hframework.peacock.controller.base.ApiConfigureRegistry;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * Created by zhangquanhong on 2017/11/28.
 */
public class ApiConfigureRegistrySuite extends CommonSuite {

    @BeforeClass
    public static void init_context() throws Exception {
        new SmartHandlerFactorySuite().init_classpath_handler();
    }

    @Test
    public void initApiConfigureRegistry(){
        ApiConfigureRegistry apiRegistry = ApiConfigureRegistry.getDefaultInstance();
        apiRegistry.snapshot();
    }




}
