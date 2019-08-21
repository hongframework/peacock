package com.hframework.peacock.config;

import com.hframework.monitor.ConfigMonitor;
import com.hframework.monitor.Monitor;
import com.hframework.monitor.MonitorListener;
import com.hframework.smartsql.client2.DBClient;
import com.hframework.peacock.config.domain.model.CfgDatasouceMysql;
import com.hframework.peacock.config.service.interfaces.ICfgDatasouceMysqlSV;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;

import java.util.*;

/**
 * Created by zhangquanhong on 2017/12/22.
 */
public class DataSourceLoader implements ApplicationListener<ContextRefreshedEvent> , MonitorListener<List<CfgDatasouceMysql>> {

    private static Logger logger = LoggerFactory.getLogger(DataSourceLoader.class);

    private ConfigMonitor<List<CfgDatasouceMysql>> handlesMonitor = null;
    @Override
    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {
        logger.info("我的父容器为：" + contextRefreshedEvent.getApplicationContext().getParent());
        logger.info("初始化时我被调用了。");
        //在web 项目中（spring mvc），系统会存在两个容器，一个是root application context ,
        //另一个就是我们自己的 projectName-servlet context（作为root application context的子容器）。
        //这种情况下，就会造成onApplicationEvent方法被执行两次
        if(contextRefreshedEvent.getApplicationContext().getParent() != null){
            return;
        }
        ApplicationContext context = contextRefreshedEvent.getApplicationContext();
        final ICfgDatasouceMysqlSV sv = context.getBean(ICfgDatasouceMysqlSV.class);
        try {

            handlesMonitor = new ConfigMonitor<List<CfgDatasouceMysql>>(60) {
                @Override
                public List<CfgDatasouceMysql> fetch() throws Exception {
                    return sv.getCfgDatasouceMysqlAll();
                }
            };
            handlesMonitor.addListener(this);
            handlesMonitor.start();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    public void onEvent(Monitor<List<CfgDatasouceMysql>> monitor) throws ClassNotFoundException, Exception {
        List<CfgDatasouceMysql> datasouceMysqls = monitor.getObject();
        if(datasouceMysqls == null) return;
        for (CfgDatasouceMysql datasouceMysql : datasouceMysqls) {
            String key = datasouceMysql.getRemark();
            String host = datasouceMysql.getHost();
            Integer port = datasouceMysql.getPort();
            String database = datasouceMysql.getDatabase();
            String username = datasouceMysql.getUsername();
            String password = datasouceMysql.getPassword();
            if(!DBClient.containDataSource(key)) {
                DBClient.registerDatabase(key , "jdbc:mysql://" + host + ":" + port + "/" + database +
                        "?useUnicode=true&characterEncoding=UTF-8&zeroDateTimeBehavior=convertToNull&tinyInt1isBit=false", username, password);
            }
        }

    }
}
