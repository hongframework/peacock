package com.hframework.peacock.controller.base;

import com.hframework.beans.exceptions.BusinessException;
import com.hframework.common.client.http.HttpClient;
import com.hframework.common.util.UrlHelper;
import com.hframework.peacock.controller.base.descriptor.ThirdApiDescriptor;
import org.apache.http.HeaderElement;
import org.apache.http.HeaderElementIterator;
import org.apache.http.HttpResponse;
import org.apache.http.client.fluent.Executor;
import org.apache.http.conn.ConnectionKeepAliveStrategy;
import org.apache.http.impl.client.DefaultConnectionKeepAliveStrategy;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicHeaderElementIterator;
import org.apache.http.protocol.HTTP;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.Args;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StreamUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class HttpProtocolExecutor extends AbstractProtocolExecutor{

    private static Logger logger = LoggerFactory.getLogger(HttpProtocolExecutor.class);

    private static ScheduledExecutorService blockMonitorScheduler = Executors.newScheduledThreadPool(1);
    static {
        blockMonitorScheduler.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                //DefaultConnectionKeepAliveStrategy 默认实现
                Executor.closeIdleConnections();
            }
        }, 3, 3, TimeUnit.SECONDS);
    }


    public HttpProtocolExecutor(ThirdApiDescriptor apiDescriptor, ThirdApiConfigureRegistry registry) {
        super(apiDescriptor, registry);
    }

    /**
     * 协议按照descriptor进行API调用
     *
     * @param parameters
     * @param nodes
     * @return
     */
    @Override
    public String execute(Map<String, Object> parameters, Map<String, Object> nodes) throws Exception {
        String result;
        Map<String, String> stringParameters = getStringMapByObjMap(parameters);
        if(apiDescriptor.isGetMethod()) {
            result = HttpClient.doGet(registry.getDomain(getDomainId()) + apiDescriptor.getPath(), stringParameters);
        }else if(apiDescriptor.getRequestType().hasJsonBody()) {
            String url = getFinalUrl(registry.getDomain(getDomainId()) + apiDescriptor.getPath(), stringParameters);
            String json = getJson(nodes);
            result = HttpClient.doJsonPost(url, json);
        }else if(apiDescriptor.getRequestType().hasXmlBody()) {
            String url = getFinalUrl(registry.getDomain(getDomainId()) + apiDescriptor.getPath(), stringParameters);
            String xml = getXml(nodes);
            result = HttpClient.doXmlPost(url, xml);
        }else if(apiDescriptor.getRequestType().hasTxtBody()) {
            throw new BusinessException("unsupport txt body post !");
        }else {
            result = HttpClient.doPost(registry.getDomain(getDomainId()) + apiDescriptor.getPath(), stringParameters);
        }

        logger.info("http response=>{}", result);

        return result;
    }
}
