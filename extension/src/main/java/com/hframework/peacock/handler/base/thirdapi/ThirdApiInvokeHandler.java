package com.hframework.peacock.handler.base.thirdapi;

import com.hframework.smartweb.annotation.Handler;
import com.hframework.smartweb.annotation.SmartParameter;
import com.hframework.smartweb.bean.ImplicitSmartHandler;
import com.hframework.smartweb.bean.handler.AbstractSmartHandler;
import com.hframework.peacock.controller.base.ThirdApiManager;
import org.springframework.stereotype.Controller;

import java.util.Arrays;

/**
 * Created by zhangquanhong on 2017/2/21.
 */
@Controller
@Handler(path = "/base/third_api_invoke", description = "第三方接口调用")
public class ThirdApiInvokeHandler extends AbstractSmartHandler implements ImplicitSmartHandler {

        @Handler(version = "1.0.0", description = "第三方接口调用")
        public Object invoke(
                @SmartParameter(required = true, description = "第三方域" , defaultValue = "${THIRD.DOMAIN}") String domainId,
                @SmartParameter(required = true, description = "第三方API", defaultValue = "${THIRD.API}") String apiPath,
                @SmartParameter(required = true, description = "第三方Schema", defaultValue = "${THIRD.SCHEMA}") String schema,
                @SmartParameter(required = true, description = "查询值", defaultValue = "${PARAMETERS.VALUES.ARRAY}") Object... parameterValues) {

                Object invoke = ThirdApiManager.invoke(domainId, apiPath, Arrays.asList(parameterValues), schema, null, null);
                return invoke;
        }

        @Handler(version = "1.0.0", description = "第三方接口调用", batch = true)
        public Object invokeBatch(
                @SmartParameter(required = true, description = "第三方域" , defaultValue = "${THIRD.DOMAIN}") String domainId,
                @SmartParameter(required = true, description = "第三方API", defaultValue = "${THIRD.API}") String apiPath,
                @SmartParameter(required = true, description = "第三方Schema", defaultValue = "${THIRD.SCHEMA}") String schema,
                @SmartParameter(required = true, description = "查询值", defaultValue = "${PARAMETERS.VALUES.ARRAY}") Object... parameterValues) {

                Object invoke = ThirdApiManager.invoke(domainId, apiPath, Arrays.asList(parameterValues), schema, null, null);
                return invoke;
        }

        @Override
        public String implicitDataField() {
                return "parameterValues";
        }
}
