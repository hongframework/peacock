package com.hframework.smartapi.api;

import com.hframework.common.frame.ServiceFactory;
import com.hframework.common.util.message.JsonUtils;
import com.hframework.smartapi.CommonSuite;
import com.hframework.smartapi.handler.HandlerConfigureRegistrySuite;
import com.hframework.smartapi.handler.SmartHandlerFactorySuite;
import com.hframework.smartweb.LogHelper;
import com.hframework.peacock.controller.base.ApiConfigureRegistry;
import com.hframework.peacock.controller.base.ApiManager;
import com.hframework.peacock.controller.base.descriptor.ApiDescriptor;
import com.hframework.peacock.controller.base.descriptor.ResultTreeDescriptor;
import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import java.io.IOException;

/**
 * Created by zhangquanhong on 2017/11/28.
 */
//@RunWith(SpringJUnit4ClassRunner.class)
//@ContextConfiguration({"classpath:spring/spring-config.xml"})
public class ApiManagerSuite extends CommonSuite {

    @BeforeClass
    public static void init_context() throws Exception {
        new SmartHandlerFactorySuite().init_classpath_handler();
    }

    @Test
    public void getResponseInfo() throws IOException {
        ApiDescriptor descriptor = ApiManager.findApiDescriptor("1", "cust", "/fp/customerDetail/gold", null);
        ResultTreeDescriptor resultStruct = descriptor.getResultStruct();


        System.out.println(JsonUtils.writeValueAsString(resultStruct.getStruct()));
    }

    @Test
    public void test(){
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setParameter("token","234343432FDSAFDSAFDSAF23432432432FDSAFDSAfd");
        request.setParameter("userId","520");
        request.setParameter("money","100");
        request.setParameter("startTime","20171210");
        LogHelper.init(request);
        MockHttpServletResponse response = new MockHttpServletResponse();
        System.out.println(ApiManager.invoke("3", "test", "daily_summary", null, request, response));
    }
}
