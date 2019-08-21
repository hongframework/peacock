package com.hframework.peacock.handler.base.field;

import com.google.common.collect.Lists;
import com.hframework.smartweb.annotation.Handler;
import com.hframework.smartweb.annotation.SmartParameter;
import com.hframework.smartweb.bean.SmartHandler;
import com.hframework.smartweb.bean.SmartObject;
import com.hframework.smartweb.bean.handler.AbstractSmartHandler;
import org.springframework.stereotype.Controller;

import java.util.List;

/**
 * Created by zhangquanhong on 2017/2/21.
 */
//@Controller
//@Handler(path = "/base/single_property_to_array")
public class SinglePropertyToArrayHandler extends AbstractSmartHandler implements SmartHandler {

        @Handler(version = "1.0.0", description = "将单值property转为数组")
        public SmartObject singlePropertyToArray(
                @SmartParameter(name = "element", required = true, description = "元素") String element) {

                return SmartObject.valueOf(new Object[]{element});
        }

        @Handler(version = "1.0.0", description = "将单值property转为数组", batch = true)
        public List<SmartObject> singlePropertyToArrays(
                @SmartParameter(name = "element", required = true, description = "元素") String[] element) {
                return Lists.newArrayList(SmartObject.valueOf(element));
        }
}
