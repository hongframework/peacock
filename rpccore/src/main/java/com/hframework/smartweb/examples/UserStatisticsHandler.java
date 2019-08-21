package com.hframework.smartweb.examples;

import com.hframework.smartweb.annotation.Handler;
import com.hframework.smartweb.annotation.SmartIdentity;
import com.hframework.smartweb.annotation.SmartParameter;
import com.hframework.smartweb.bean.SmartHandler;
import com.hframework.smartweb.bean.SmartMap;
import com.hframework.smartweb.bean.handler.AbstractSmartHandler;
import org.springframework.stereotype.Controller;

import java.math.BigDecimal;
import java.util.HashMap;

/**
 * Created by zhangquanhong on 2017/11/28.
 */
@Controller
@Handler(path = "/demo/uerCenter/p2pUser/queryUserStatisticsInfo",owners = "2")
public class UserStatisticsHandler  extends AbstractSmartHandler implements SmartHandler {
    @Handler(version ="1.0.0", description = "根据规则查询一条用户")
    public @SmartIdentity({"totalmoney","investing"}) SmartMap queryOneUserV100(
            @SmartParameter(required = true) Long p2pId) {
        return SmartMap.build(new HashMap<Object, Object>(){{
            put("totalmoney", 1232321321);
        }});
    }
}
