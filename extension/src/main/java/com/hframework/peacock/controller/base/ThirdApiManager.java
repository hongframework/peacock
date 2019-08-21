package com.hframework.peacock.controller.base;

import com.hframework.beans.exceptions.BusinessException;
import com.hframework.smartweb.bean.SmartMessage;
import com.hframework.peacock.controller.base.descriptor.ApiDescriptor;
import com.hframework.peacock.controller.base.descriptor.ThirdApiDescriptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.InvocationTargetException;
import java.text.ParseException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

/**
 * Created by zhangquanhong on 2017/11/15.
 */
public class ThirdApiManager {
    private static final Logger logger = LoggerFactory.getLogger(ThirdApiManager.class);

    private static ThirdApiConfigureRegistry apiRegistry = ThirdApiConfigureRegistry.getDefaultInstance();

    public static Object invoke(String domainId, String path, List<Object> parameterValues, String schema, HttpServletRequest request, HttpServletResponse response) {
        ThirdApiDescriptor apiDescriptor = apiRegistry.findThirdApiDescriptor(domainId, path);

        ThirdApiExecutor executor = apiRegistry.getExecutor(apiDescriptor);
        return executor.execute(parameterValues, schema,request, response);
    }
}
