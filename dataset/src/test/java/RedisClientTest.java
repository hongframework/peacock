import com.clearspring.analytics.util.Lists;
import com.hframework.smartsql.client2.RedisClient;

import java.util.Map;
import java.util.Set;

/**
 * Created by zhangquanhong on 2017/8/25.
 */
public class RedisClientTest {

    public static void main(String[] args) {
        RedisClient.registerRedis("lcs", "127.0.0.1", 7011, "beetlefe@password", 2);
        Set<String> keys = RedisClient.keys("*201708*");
        System.out.println(keys);
        Map<String, Object> objects = RedisClient.hGetList(Lists.newArrayList(keys));
        System.out.println(objects);
    }
}
