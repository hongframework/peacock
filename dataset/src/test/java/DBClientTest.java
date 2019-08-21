import com.alibaba.fastjson.JSONObject;
import com.hframework.smartsql.client2.DBClient;

import java.util.Map;

/**
 * Created by zhangquanhong on 2017/8/10.
 */
public class DBClientTest {
    public static void main(String[] args) {
        DBClient.registerDatabase("hamster", "jdbc:mysql://127.0.0.1:3306/sub?useUnicode=true", "sub", "sub");
        DBClient.setCurrentDatabaseKey("hamster");
        Map<String, Object> map = DBClient.executeQueryMap("SELECT * FROM period_money_out_v2 t WHERE t.user_id = ?", new Object[]{10});
        System.out.println(map);

        JSONObject object = JSONObject.parseObject("{" +
                "    'balance': {" +
                "        'money_balance': 'money'," +
                "        'period_out': 'period'" +
                "    }," +
                "    'long': {" +
                "        'money_long_period_out': 'money'," +
                "        'period_long_period_out': 'period'" +
                "    }," +
                "    'short': {" +
                "        'money_short_period_out': 'money'," +
                "        'period_short_period_out': 'period'" +
                "    }," +
                "    'high': {" +
                "        'money_high_amount_out': 'money'," +
                "        'period_high_amount_out': 'period'" +
                "    }," +
                "    'low': {" +
                "        'money_low_amount_out': 'money'," +
                "        'period_low_amount_out': 'period'" +
                "    }," +
                "    'best': {" +
                "        'money_out': 'money'," +
                "        'period_out': 'period'" +
                "    }" +
                "}");
        System.out.println(object.get("best"));

    }
}
