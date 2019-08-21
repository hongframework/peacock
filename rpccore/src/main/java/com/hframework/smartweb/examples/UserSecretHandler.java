package com.hframework.smartweb.examples;

import com.google.common.collect.Lists;
import com.hframework.smartweb.annotation.Handler;
import com.hframework.smartweb.annotation.SmartIdentity;
import com.hframework.smartweb.annotation.SmartParameter;
import com.hframework.smartweb.bean.SmartHandler;
import com.hframework.smartweb.bean.handler.AbstractSmartHandler;
import org.springframework.stereotype.Controller;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhangquanhong on 2017/2/21.
 */
@Controller
@Handler(path = "/demo/uerCenter/p2pUser/userSecret",owners = "2")
public class UserSecretHandler extends AbstractSmartHandler implements SmartHandler {

    @Handler(version ="1.0.0", description = "通过token获取用户ID")
    public @SmartIdentity("p2pId") Long getUserIdByToken(
            @SmartParameter(required = true, description = "登录TOKEN") String token) {
        return 1999L;//TODO
    }

}
