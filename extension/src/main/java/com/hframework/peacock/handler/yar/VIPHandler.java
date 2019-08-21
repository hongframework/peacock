package com.hframework.peacock.handler.yar;

import com.google.common.base.Joiner;
import com.hframework.smartweb.annotation.Handler;
import com.hframework.smartweb.annotation.SmartParameter;
import com.hframework.smartweb.bean.SmartHandler;
import com.hframework.smartweb.bean.handler.AbstractSmartHandler;
import com.hframework.client.yar.YarClient;
import com.hframework.client.yar.bean.VipAO;
import org.springframework.stereotype.Controller;

import java.util.Arrays;
import java.util.List;

/**
 * Created by zhangquanhong on 2017/2/21.
 */
@Controller
@Handler(path = "/yar/p2p/vipInfo", owners = {"1"})
public class VIPHandler extends AbstractSmartHandler implements SmartHandler {

        @Handler(version = "1.0.0", description = "获取VIP信息")
        public VipAO getVipInfo(
                @SmartParameter(required = true, description = "用户ID") Integer userId) {
                return YarClient.getVipInfo(userId);
        }

        @Handler(version ="1.0.0", description = "获取VIP信息", batch = true)
        public List getVipInfos(Integer[] userIds) {
                try {
                        return YarClient.getViplist(Joiner.on(",").join(userIds));
                } catch (Exception e) {
                        e.printStackTrace();
                }
                return Arrays.asList();
        };

}
