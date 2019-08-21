package com.hframework.peacock.handler.base;

import com.google.common.base.Joiner;
import com.hframework.smartweb.annotation.Handler;
import com.hframework.smartweb.annotation.SmartParameter;
import com.hframework.smartweb.bean.SmartHandler;
import com.hframework.smartweb.bean.handler.AbstractSmartHandler;
import com.hframework.client.decrypt.DecryptClient;
import com.hframework.client.decrypt.bean.EncryptResult;
import org.springframework.stereotype.Controller;

import java.util.Arrays;
import java.util.List;

/**
 * Created by zhangquanhong on 2017/2/21.
 */
@Controller
@Handler(path = "/base/mobile_decrypt", owners = {"1"})
public class MobileDecryptHandler extends AbstractSmartHandler implements SmartHandler {

        @Handler(version = "1.0.0", description = "手机号码解密")
        public String decrypt(
                @SmartParameter(required = true, description = "加密手机号") String mobile) {
                return decrypt(new String[]{mobile}).get(0);
        }

        @Handler(version ="1.0.0", description = "手机号码解密", batch = true)
        public List<String> decrypt(String[] mobiles) {
                try {
                        EncryptResult des = DecryptClient.des(Joiner.on(",").join(mobiles));
                        return Arrays.asList(des.getData().split(","));
                } catch (Exception e) {
                        e.printStackTrace();
                }
                return Arrays.asList(mobiles);
        };

}
