package com.hframework.client.decrypt;

import com.google.common.base.Joiner;
import com.hframework.common.util.message.JsonUtils;
import com.hframework.client.yar.YarClient;
import com.hframework.client.yar.bean.VipAO;
import org.junit.Test;

import java.util.List;
import java.util.Map;

public class YarClientTest {

    @Test
    public void getVipInfo()  throws Exception{
        VipAO vipInfo = YarClient.getVipInfo(501528);
        System.out.println(JsonUtils.writeValueAsString(vipInfo));
    }
    @Test
    public void getVipInfos() {
        List<Map> maps = YarClient.getViplist(Joiner.on(",").join(new Integer[]{501528,6250}));
        for (Map map : maps) {
            System.out.println(map);
        }
    };
}
