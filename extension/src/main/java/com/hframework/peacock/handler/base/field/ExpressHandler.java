package com.hframework.peacock.handler.base.field;

import com.alibaba.fastjson.JSONObject;
import com.hframework.common.util.DateUtils;
import com.hframework.smartweb.annotation.Handler;
import com.hframework.smartweb.annotation.SmartParameter;
import com.hframework.smartweb.bean.ImplicitSmartHandler;
import com.hframework.smartweb.bean.SmartHandler;
import com.hframework.smartweb.bean.handler.AbstractSmartHandler;
import com.hframework.smartweb.bean.handler.HandlerHelper;
import com.hframework.strategy.rule.component.Express;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;

import java.lang.reflect.Array;
import java.sql.Timestamp;
import java.util.*;

/**
 * Created by zhangquanhong on 2017/2/21.
 */
@Controller
@Handler(path = "/base/express_invoke")
public class ExpressHandler extends AbstractSmartHandler implements ImplicitSmartHandler {

        @Handler(version = "1.0.0", description = "表达式执行")
        public Object expressInvoke(
                @SmartParameter(name = "express", required = true, description = "表达式") String expr,
                @SmartParameter(required = true, description = "查询字段", defaultValue = "${HANDLER.CONFIG.PARAMETERS}") String[] queryFields,
                @SmartParameter(required = true, description = "查询值", defaultValue = "${PARAMETERS.VALUES.ARRAY}") Object... parameterValues) throws Exception {
                Map<String, Object> parameters = new HashMap<>();
                for (int i = 0; i < queryFields.length; i++) {
                        if(parameterValues[i] != null && parameterValues[i] instanceof Timestamp) {
                                parameters.put(queryFields[i], ((Timestamp)parameterValues[i]).toString());
                        }else if(parameterValues[i] != null && parameterValues[i] instanceof Date) {
                                parameters.put(queryFields[i], DateUtils.getDateYYYYMMDDHHMMSS((Date)parameterValues[i]));
                        }else {
                                parameters.put(queryFields[i], parameterValues[i]);
                        }

                }
                return Express.SpelExpressionUtils.executeSenior(expr, parameters);
        }

        @Handler(version = "1.0.0", description = "表达式执行", batch = true)
        public Object expressInvokeBatch(
                @SmartParameter(name = "express", required = true, description = "表达式") String expr,
                @SmartParameter(required = true, description = "查询字段", defaultValue = "${HANDLER.CONFIG.PARAMETERS}") String[] queryFields,
                @SmartParameter(required = true, description = "查询值", defaultValue = "${PARAMETERS.VALUES.ARRAY}") Object... parameterValues) throws Exception {
                List<Map<String, Object>> parameters = new ArrayList<>();
                for (int i = 0; i < queryFields.length; i++) {
                        Object values = parameterValues[i];
                        if(HandlerHelper.isArray(values)) {
                                for (int j = 0; j < Array.getLength(values); j++) {
                                        Object value = Array.get(values, j);
                                        if(parameters.size() <= j) {
                                                parameters.add(new HashMap<String, Object>());
                                        }

                                        if(value!= null && value instanceof Timestamp) {
                                                parameters.get(j).put(queryFields[i], ((Timestamp)value).toString());
                                        }else if(value != null && value instanceof Date) {
                                                parameters.get(j).put(queryFields[i], DateUtils.getDateYYYYMMDDHHMMSS((Date)value));
                                        }else {
                                                parameters.get(j).put(queryFields[i], value);
                                        }
                                }
                        }
                }
                List result = new ArrayList();
                for (Map<String, Object> parameter : parameters) {
                        result.add(Express.SpelExpressionUtils.executeSenior(expr, parameter));
                }
                return result;
        }

        @Override
        public String implicitDataField() {
                return "parameterValues";
        }
}
