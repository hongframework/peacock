package com.hframework.smartapi.thirdapi;

import com.google.common.collect.Lists;
import com.hframework.common.frame.ServiceFactory;
import com.hframework.smartapi.api.ApiConfigureRegistrySuite;
import com.hframework.smartapi.handler.HandlerConfigureRegistrySuite;
import com.hframework.smartapi.CommonSuite;
import com.hframework.smartapi.handler.SmartHandlerFactorySuite;
import com.hframework.smartweb.LogHelper;
import com.hframework.peacock.controller.base.ApiConfigureRegistry;
import com.hframework.peacock.controller.base.ApiManager;
import com.hframework.peacock.controller.base.ThirdApiConfigureRegistry;
import com.hframework.peacock.controller.base.ThirdApiManager;
import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

/**
 * Created by zhangquanhong on 2017/11/28.
 */
//@RunWith(SpringJUnit4ClassRunner.class)
//@ContextConfiguration({"classpath:spring/spring-config.xml"})
public class ThirdApiManagerSuite extends CommonSuite {

//    @BeforeClass
    public static void init_context() throws Exception {
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext(new String[]{"classpath:spring/spring-test-config.xml"});
        context.start();
    }

    @Test
    public void initHandlerConfigureRegistry() {
        ThirdApiConfigureRegistry registry = ThirdApiConfigureRegistry.getDefaultInstance();
        registry.snapshot();
    }

    @Test
    public void test(){
        System.out.println(ThirdApiManager.invoke("2", "/get_node_data",
                Lists.newArrayList(new Object[]{"4000060","day", 0, "invest", "4000060"}), "data/info", null, null));
        System.out.println(ThirdApiManager.invoke("2", "/get_node_data",
                Lists.newArrayList(new Object[]{"4000060","day", 0, "invest", "4000060"}), "data/groups", null, null));

    }

}
