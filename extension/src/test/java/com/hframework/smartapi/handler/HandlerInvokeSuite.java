package com.hframework.smartapi.handler;

import com.hframework.smartweb.LogHelper;
import com.hframework.peacock.controller.base.ApiConfigureRegistry;
import com.hframework.peacock.controller.base.ApiManager;
import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

public class HandlerInvokeSuite {
    @BeforeClass
    public static void init_context() throws Exception {
        new SmartHandlerFactorySuite().init_classpath_handler();
    }


    @Test
    public void test(){
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setParameter("userId","1");
        request.setParameter("status","1");
        request.setParameter("offset","1");
        request.setParameter("size","20");
        LogHelper.init(request);
        MockHttpServletResponse response = new MockHttpServletResponse();
        System.out.println(ApiManager.invokeHandler("1", "/cust/customer_repay_list_4_gold", "1.0.0", null, request, response, null));
    }
}
