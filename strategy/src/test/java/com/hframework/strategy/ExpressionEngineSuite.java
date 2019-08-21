package com.hframework.strategy;

import com.google.common.collect.Lists;
import com.hframework.strategy.rule.ExpressInvoker;
import com.hframework.strategy.rule.ExpressionEngine;
import com.hframework.strategy.rule.data.EDataSet;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

/**
 * Created by zhangquanhong on 2017/9/5.
 */
public class ExpressionEngineSuite {
    private static final Logger logger = LoggerFactory.getLogger(ExpressionEngineSuite.class);

    @Test
    public void test_rules() {
        List<Map> datas = new ArrayList<>();
        for (int i = 0; i < 2000; i++) {
            datas.add(new LinkedHashMap() {{
                put("userId", (int) Math.round(Math.random()*10000));
            }});
        }
        ExpressionEngine engine = ExpressionEngine.getDefaultInstance();
        EDataSet eDataSet =engine.execute("ZhouQi", "1.0.0", new EDataSet(datas));
        logger.debug("rule [ {} ] result :\n {}", "ZhouQi:1.0.0", eDataSet);
        System.out.println(eDataSet.info());
    }

    @Test
    public void test_express(){
        ExpressInvoker invoker = ExpressionEngine.loadAndExplain("$userId % 2 == 1");
        EDataSet result = invoker.invoke(new EDataSet(Lists.newArrayList(
                new HashMap() {{
                    put("userId", 3);
                }},
                new HashMap() {{
                    put("userId", 50);
                }},
                new HashMap() {{
                    put("userId", 9);
                }},
                new HashMap() {{
                    put("userId", 8);
                }}
        )));
        System.out.println(Arrays.asList(result.getLabel()));
    }

    @Test
    public void test_abgroup(){
        ExpressInvoker invoker = ExpressionEngine.loadAndExplain(
                " if($result.2 == 'newUser' && $sequence % 2 == 1 && $sequence <= 2000){\n" +
                        "\t'A'\n" +
                " }else if($result.2 == 'newUser' && $sequence % 2 == 0 && $sequence <= 2000){\n" +
                        "\t'B'\n" +
                " }else if($result.2 == 'lossWarningUser' && $sequence % 2 == 1 && $sequence <= 2000){\n" +
                        "\t'A'\n" +
                " }else if($result.2 == 'lossWarningUser' && $sequence % 2 == 0 && $sequence <= 2000){\n" +
                        "\t'B'\n" +
                " }else if($result.2 == 'lossUser' && $sequence % 2 == 1 && $sequence <= 2000){\n" +
                        "\t'A'\n" +
                " }else if($result.2 == 'lossUser' && $sequence % 2 == 0 && $sequence <= 2000){\n" +
                        "\t'B'\n" +
                " }else{\n" +
                        "\t'X'\n" +
                        " }\n");
        EDataSet result = invoker.invoke(new EDataSet(Lists.newArrayList(

                new HashMap() {{put("result.2", "newUser"); put("sequence", 1);}},
                new HashMap() {{put("result.2", "newUser"); put("sequence", 3);}},
                new HashMap() {{put("result.2", "lossWarningUser"); put("sequence", 2);}},
                new HashMap() {{put("result.2", "lossWarningUser"); put("sequence", 3);}},
                new HashMap() {{put("result.2", "lossUser"); put("sequence", 1001);}},
                new HashMap() {{put("result.2", "lossUser"); put("sequence", 2002);}}
        )));
        System.out.println(Arrays.asList(result.getLabel()));
    }

    @Test
    public void test_print(){
        ExpressInvoker invoker = ExpressionEngine.loadAndExplain(
                " if($result.2 == 'newUser' && $sequence % 2 == 1 && $sequence <= 2000){\n" +
                        "\t'A'\n" +
                        " }else if($result.2 == 'newUser' && $sequence % 2 == 0 && $sequence <= 2000){\n" +
                        "\t'B'\n" +
                        " }else if($result.2 == 'lossWarningUser' && $sequence % 2 == 1 && $sequence <= 2000){\n" +
                        "\t'A'\n" +
                        " }else if($result.2 == 'lossWarningUser' && $sequence % 2 == 0 && $sequence <= 2000){\n" +
                        "\t'B'\n" +
                        " }else if($result.2 == 'lossUser' && $sequence % 2 == 1 && $sequence <= 2000){\n" +
                        "\t'A'\n" +
                        " }else if($result.2 == 'lossUser' && $sequence % 2 == 0 && $sequence <= 2000){\n" +
                        "\t'B'\n" +
                        " }else{\n" +
                        "\t'X'\n" +
                        " }\n");
        invoker.print();
        System.out.println(invoker.thenExpresses());
    }
    @Test
    public void test_CustomTicket(){
        EDataSet eDataSet1 = new EDataSet(Lists.newArrayList(
                new LinkedHashMap() {{
                    put("userId", 1);
                    put("repayId", 2);
                }},
                new LinkedHashMap() {{
                    put("userId", 13);
                    put("repayId", 615);
                }},
                new LinkedHashMap() {{
                    put("userId", 6090);
                    put("repayId", 9857);
                }}
                ,
                new LinkedHashMap() {{
                    put("userId", 6109);
                    put("repayId", -1);
                }}


        ));
        ExpressionEngine engine = ExpressionEngine.getDefaultInstance();
        EDataSet eDataSet =engine.execute("CustomTicket", "1.0.0", eDataSet1);
        logger.debug("rule [ {} ] result :\n {}", "CustomTicket:1.0.0", eDataSet);
    }

    @Test
    public void test_Graded(){
        EDataSet eDataSet1 = new EDataSet(Lists.newArrayList(
//                new LinkedHashMap() {{
//                    put("age", 1);
//                    put("repayId", 2);
//                }},
//                new LinkedHashMap() {{
//                    put("userId", 13);
//                    put("repayId", 615);
//                }},
//                new LinkedHashMap() {{
//                    put("userId", 6090);
//                    put("repayId", 9857);
//                }}
//                ,
                new LinkedHashMap() {{
                    put("age", 24);
                    put("area", "北京");
                }},
                new LinkedHashMap() {{
                    put("age", 15);
                    put("area", "成都");
                }},
                new LinkedHashMap() {{
                    put("age", 34);
                    put("area", "郑州");
                }}
        ));
        ExpressionEngine engine = ExpressionEngine.getDefaultInstance();
        EDataSet eDataSet =engine.execute("Graded", "1.0.0", eDataSet1);
        logger.debug("rule [ {} ] result :\n {}", "Graded:1.0.0", eDataSet);
    }
}
