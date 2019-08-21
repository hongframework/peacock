package com.hframework.smartapi.thirdapi;

import com.hframework.smartapi.CommonSuite;
import com.hframework.smartapi.handler.SmartHandlerFactorySuite;
import com.hframework.smartweb.LogHelper;
import com.hframework.peacock.controller.base.ApiConfigureRegistry;
import com.hframework.peacock.controller.base.ApiManager;
import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

/**
 * Created by zhangquanhong on 2017/11/28.
 */
//@RunWith(SpringJUnit4ClassRunner.class)
//@ContextConfiguration({"classpath:spring/spring-config.xml"})
public class ThirdApiInvokeSuite extends CommonSuite {

    @BeforeClass
    public static void init_context() throws Exception {
        new SmartHandlerFactorySuite().init_classpath_handler();
    }

    @Test
    public void initHandlerConfigureRegistry() {
        ApiConfigureRegistry handlerRegistry = ApiConfigureRegistry.getHandlerInstance();
        handlerRegistry.snapshot();
    }

    @Test
    public void test(){
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setParameter("node","4000060");
        request.setParameter("cycle","day");
        request.setParameter("isleaf","0");
        request.setParameter("orderby","invest");
        request.setParameter("p2p_id","4000060");

        LogHelper.init(request);
        MockHttpServletResponse response = new MockHttpServletResponse();
        System.out.println(ApiManager.invokeHandler("3", "/bzqry/test_third_api", "1.0.0", null, request, response, null));
    }
}
