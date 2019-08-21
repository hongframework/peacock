package com.hframework.strategy;

import com.hframework.strategy.rule.component.Express;
import org.junit.Test;

import java.util.HashMap;

/**
 * Created by zhangquanhong on 2017/9/27.
 */
public class SpelExpressionUtilsSuite {

    @Test
    public void test() throws Exception {
        System.out.println(Express.SpelExpressionUtils.execute(" null < 12"));
        System.out.println(Express.SpelExpressionUtils.execute(" null > 12"));
        System.out.println(Express.SpelExpressionUtils.execute(" null != null"));
        System.out.println(Express.SpelExpressionUtils.execute(" null != 12"));
        System.out.println(Express.SpelExpressionUtils.execute(" null != ''"));
        System.out.println(Express.SpelExpressionUtils.execute(" 1 == 2 ? true :'1233444'.substring(0,1) + 1"));

        System.out.println(Express.SpelExpressionUtils.execute("app_role == 'userb' && !name.contains('未实名')? (name.substring(0,1) + (sex == '1' ? '先生': '女士')) : name",
                new HashMap<String, Object>(){{
                    put("app_role", "userb");
                    put("name", "未实名");
                    put("sex", "0");
                }}));

        System.out.println(Express.SpelExpressionUtils.execute("name + (offline == '1' ? '（线下标）' : '')",
                new HashMap<String, Object>(){{
                    put("name", "供应链A000012");
                    put("offline", "0");
                }}));

        System.out.println(Express.SpelExpressionUtils.execute("'恭喜您，你的项目：' + name + '已经成功放款，开始计息时间：' + time + ', 总金额为：' + money + '元!'",
                new HashMap<String, Object>(){{
                    put("name", "供应链A000012");
                    put("time", "2018-12-25");
//                    put("money", 1000);

                }}));
        System.out.println(Express.SpelExpressionUtils.executeSenior("'恭喜您，你的项目：${name}已经成功放款，开始计息时间：$time, 总金额为：$money元!'",
                new HashMap<String, Object>(){{
                    put("name", "供应链A000012");
                    put("time", "2018-12-25");
                    put("money", 1000);

                }}));

        System.out.println(Express.SpelExpressionUtils.executeSenior("'摩羯座,水瓶座,双鱼座,白羊座,金牛座,双子座,巨蟹座,狮子座,处女座,天秤座,天蝎座,射手座,魔羯座'.split(',')[T(java.lang.Integer).parseInt('$birth'.substring(0,2)) - (T(java.lang.Integer).parseInt('$birth'.substring(3)) < T(java.lang.Integer).parseInt('20, 19, 21, 20, 21, 22, 23, 23, 23, 24, 23, 22'.split(',')[T(java.lang.Integer).parseInt('$birth'.substring(0,2))-1].trim()) ? 1 : 0)]",
                new HashMap<String, Object>(){{
            put("birth", "09/01");
        }}));
        System.out.println(Express.SpelExpressionUtils.executeSenior("'mojie,shuiping,shuangyu,baiyang,jinniu,shuagnzi,juxie,shizi,chulv,tianping,tianxie,sheshou,mojie'.split(',')[T(java.lang.Integer).parseInt('$birth'.substring(0,2)) - (T(java.lang.Integer).parseInt('$birth'.substring(3)) < T(java.lang.Integer).parseInt('20, 19, 21, 20, 21, 22, 23, 23, 23, 24, 23, 22'.split(',')[T(java.lang.Integer).parseInt('$birth'.substring(0,2))-1].trim()) ? 1 : 0)]",
                new HashMap<String, Object>(){{
                    put("birth", "09/01");
                }}));

        System.out.println(Express.SpelExpressionUtils.execute("switch(1){case 0: 0 break; default: 1}"));

    }
}
