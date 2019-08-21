package com.hframework.smartapi.handler;

import com.hframework.common.frame.ServiceFactory;
import com.hframework.smartweb.SmartHandlerFactory;
import com.hframework.smartweb.examples.QueryUserBaseInfoHandler;
import com.hframework.smartweb.examples.UserSecretHandler;
import com.hframework.smartweb.examples.UserStatisticsHandler;
import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * Created by zhangquanhong on 2017/11/28.
 */
public class SmartHandlerFactorySuite {
    @Test
    public void init_static_handler() throws Exception {
        new UserSecretHandler().afterPropertiesSet();
        new QueryUserBaseInfoHandler().afterPropertiesSet();
        new UserStatisticsHandler().afterPropertiesSet();
        SmartHandlerFactory.snapshot();
    }

    @Test
    public void init_classpath_handler() throws Exception {
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext(new String[]{"classpath:spring/spring-test-config.xml"});

        context.start();
        ServiceFactory.initContext(context);
        SmartHandlerFactory.snapshot();

    }


}
