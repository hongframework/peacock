package com.hframework.smartapi;

import com.hframework.smartapi.handler.SmartHandlerFactorySuite;
import com.hframework.smartsql.client2.DBClient;
import org.junit.BeforeClass;

/**
 * Created by zhangquanhong on 2017/11/28.
 */
public class CommonSuite {

    @BeforeClass
    public static void initDBClient(){
        DBClient.registerDatabase("hamster", "jdbc:mysql://127.0.0.1:3306/sub?useUnicode=true", "sub", "sub");
        DBClient.registerDatabase("peacock", "jdbc:mysql://127.0.0.1:3306/peacock?useUnicode=true", "peacock", "peacock");
        DBClient.setCurrentDatabaseKey("peacock");

    }

    @BeforeClass
    public static void initHandler() throws Exception {
        new SmartHandlerFactorySuite().init_static_handler();
    }


}
