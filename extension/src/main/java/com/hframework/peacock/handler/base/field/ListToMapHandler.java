package com.hframework.peacock.handler.base.field;

import com.alibaba.fastjson.JSONObject;
import com.hframework.smartweb.annotation.Handler;
import com.hframework.smartweb.annotation.SmartParameter;
import com.hframework.smartweb.bean.SmartHandler;
import com.hframework.smartweb.bean.handler.AbstractSmartHandler;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;

import java.util.*;

/**
 * Created by zhangquanhong on 2017/2/21.
 */
@Controller
@Handler(path = "/base/list2map")
public class ListToMapHandler extends AbstractSmartHandler implements SmartHandler {

        @Handler(version = "1.0.0", description = "列表转化为字典")
        public Map<String, Object> dbQueryOne(
                @SmartParameter(required = true, description = "数据集合") List<Map<String, Object>> list,
                @SmartParameter(required = true, description = "分组Key属性") String keyProperty,
                @SmartParameter(required = true, description = "一个Key有多个Value", defaultValue = "true") boolean multiValues,
                @SmartParameter(required = true, description = "一个Value有多个属性", defaultValue = "true") boolean valueIsMap
                ){

                if(list == null) return null;
                Map<String, Object> result = new HashMap<>();

                for (Map<String, Object> map : list) {
                        String keyValue = String.valueOf(map.remove(keyProperty));
                        if(multiValues) {
                                if(!result.containsKey(keyValue)) {
                                        result.put(keyValue, new ArrayList());
                                }
                        }

                        Object value = valueIsMap ? map : (map.isEmpty() ? null : map.values().iterator().next());

                        if(multiValues) {
                                ((List)result.get(keyValue)).add(value);
                        }else {
                                result.put(keyValue, value);
                        }
                }
                return result;

        }
}
