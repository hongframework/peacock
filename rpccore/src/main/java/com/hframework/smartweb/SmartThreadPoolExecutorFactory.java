package com.hframework.smartweb;

import org.apache.commons.lang.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.concurrent.ThreadPoolExecutorFactoryBean;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by zhangquanhong on 2017/4/17.
 */
public class SmartThreadPoolExecutorFactory {

    private static final Logger logger = LoggerFactory.getLogger(SmartThreadPoolExecutorFactory.class);

    private static ExecutorService executorService = Executors.newCachedThreadPool();

    public static ExecutorService getExecutorServiceObject(){
        if(executorService == null) {
            synchronized (SmartThreadPoolExecutorFactory.class) {
                if(executorService == null) {
                    logger.info("init smart thread pool...");
                    ThreadPoolExecutorFactoryBean defaultThreadPoolExecutorFactoryBean = new ThreadPoolExecutorFactoryBean();
                    defaultThreadPoolExecutorFactoryBean.setBeanName("smart-executor");
                    defaultThreadPoolExecutorFactoryBean.afterPropertiesSet();
                    try{
                        executorService = defaultThreadPoolExecutorFactoryBean.getObject();
                    }catch (Exception e ) {
                        logger.error("init smart thread pool error, {}", ExceptionUtils.getFullStackTrace(e));
                        executorService = Executors.newCachedThreadPool();
                    }
                    logger.info("init smart thread pool finish...");
                }
            }
        }
        return executorService;
    }
}
