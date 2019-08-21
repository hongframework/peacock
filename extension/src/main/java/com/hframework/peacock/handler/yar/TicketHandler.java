package com.hframework.peacock.handler.yar;

import com.hframework.smartweb.annotation.Handler;
import com.hframework.smartweb.annotation.SmartParameter;
import com.hframework.smartweb.bean.SmartHandler;
import com.hframework.smartweb.bean.handler.AbstractSmartHandler;
import com.hframework.client.yar.YarClient;
import org.springframework.stereotype.Controller;

@Controller
@Handler(path = "/yar/o2o" , owners = {"1"})
public class TicketHandler extends AbstractSmartHandler implements SmartHandler {

    @Handler(path = "/ticket/amount/unused", version = "1.0.0", description = "获取未使用投资券数量")
    public Long queryTicketAmount(
            @SmartParameter(required = true, description = "用户ID") Integer userId,
            @SmartParameter(required = true, description = "用户ID", defaultValue = "0") Integer type,
            @SmartParameter(required = true, description = "用户ID", defaultValue = "1", optionJson = "{'1':'投资券','4':'黄金券'}") Integer consumeType) {
        return YarClient.queryTicketAmount(userId, type, consumeType);
    }
}