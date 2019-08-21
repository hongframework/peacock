import com.hframework.common.client.http.HttpClient;
import com.hframework.common.util.file.FileUtils;
import com.hframework.smartweb.bean.checker.Rules;
import com.hframework.smartweb.config.SmartConfigurer;
import org.junit.Test;

import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by zhangquanhong on 2017/9/26.
 */
public class HttpClientTest {

    public List<String> request_data() throws IOException {
        return FileUtils.readFileToArray("D:\\my_workspace\\peacock\\extension\\src\\test\\java\\in.txt");
    }


    @Test
    public void batch_request() throws Exception {
        List<String> strings = request_data();
        String clientId = "50004";
        for (String string : strings) {
            System.out.println("=> " + string);
            String[] split = string.split(",");
            String userId = split[0].trim();
            String repayId = split[1].trim();
            Map parameters = new HashMap();
            parameters.put("clientId", clientId);
            parameters.put("ruleCode","CustomTicket");
            parameters.put("userId", userId);
            parameters.put("repayId",repayId);
            parameters.put("keyCode","userId");
            parameters.put("sign", Rules.signAllParams(parameters, "&key=" + "LaRk4!DcApI"));
            String s = HttpClient.doPost("http://peacock.beta.ncfgroup.com/api/base/rule/execute", parameters);
            FileUtils.appendMethodB("D:\\my_workspace\\peacock\\extension\\src\\test\\java\\out.txt", string + " => " + s + "\n");
            System.out.println(parameters);
        }


    }

}
