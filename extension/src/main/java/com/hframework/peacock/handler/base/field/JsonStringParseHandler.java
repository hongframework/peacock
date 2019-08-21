package com.hframework.peacock.handler.base.field;

import com.alibaba.fastjson.JSONObject;
import com.hframework.smartweb.annotation.Handler;
import com.hframework.smartweb.annotation.SmartParameter;
import com.hframework.smartweb.bean.SmartHandler;
import com.hframework.smartweb.bean.handler.AbstractSmartHandler;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by zhangquanhong on 2017/2/21.
 */
@Controller
@Handler(path = "/base/json_parse")
public class JsonStringParseHandler extends AbstractSmartHandler implements SmartHandler {

        @Handler(version = "1.0.0", description = "JSON字符串解析")
        public Map<String, Object> dbQueryOne(
                @SmartParameter(name = "jsonString", required = true, description = "JSON字符串") String jsonString,
                @SmartParameter(required = true, description = "解析后删除Json字符串", defaultValue = "true") boolean deleteJson,
                @SmartParameter(required = true, description = "返回值段", defaultValue = "${HANDLER.CONFIG.RESULTS}") String[] returnFields
                ) {
                Map<String, Object> result = new LinkedHashMap<>();
                if(StringUtils.isBlank(jsonString)) {
                        return result;
                }
                JSONObject jsonObject = JSONObject.parseObject(jsonString);
                for (String returnField : returnFields) {
                        result.put(returnField, jsonObject.get(returnField));
                }

                return result;
        }
}
