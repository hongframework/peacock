package com.hframework.strategy.rule;

import com.hframework.common.util.message.XmlUtils;
import com.hframework.strategy.rule.data.EDataSet;
import com.hframework.strategy.rule.exceptions.ExpressExecuteException;
import com.hframework.strategy.rule.exceptions.RuleInitializeException;
import com.hframework.strategy.rule.fetch.DemoFetcher;
import com.hframework.strategy.rule.fetch.Fetcher;
import com.hframework.strategy.rule.repository.RuleInfo;
import com.hframework.strategy.rule.repository.Rules;
import com.hframework.strategy.rule.repository.rules.Rule;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.URL;
import java.util.*;


/**
 * 目标支持如下形式表达式
 * ((($user % 5 = 3 || $user % 5 = 3) && $user_type in (1,2,3) || $vip == true ) && (name != " ");
 *  或者
 *  (($user % 5 eq 3 || $user % 5 eq 3) && $user_type in(1,2,3) || $vip eq(true) && true
 *  或者
 * (($user % 5 = 3 OR $user % 5 = 3) AND $user_type in (1,2,3) OR $vip == true AND true
 * User: zhangqh6
 * Date: 2017/06/26 10:06:06
 */
public class ExpressionEngine {
    private static ExpressionEngine globalEngine ;
    private static final Logger logger = LoggerFactory.getLogger(ExpressionEngine.class);
    private static final String RULE_XML_PATH = "rules.xml";

    private Map<String, Rules> loadedRules = new HashMap<>();
    private Map<String, RuleInfo> ruleInfoMap = new HashMap<>();
    private Map<String, RuleInfo> defaultRuleInfoMap = new HashMap<>();

    public static ExpressionEngine getDefaultInstance(){
        if(globalEngine == null) {
            synchronized (ExpressionEngine.class){
                if(globalEngine == null) {
                    globalEngine = getNewInstance();
                }
            }
        }
        return globalEngine;
    }

    public static ExpressionEngine getNewInstance(){
        ExpressionEngine expressionEngine = new ExpressionEngine();
        expressionEngine.load(RULE_XML_PATH);
        return expressionEngine;
    }
    public ExpressionEngine load(String resourcePath) {
        logger.info("加载资源文件：" + resourcePath);
        Enumeration<URL> resources = null;
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        try {
            resources = classLoader.getResources(resourcePath);
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuleInitializeException( resourcePath + " is not exists !");
        }
        while (resources.hasMoreElements()) {
            URL resource = resources.nextElement();
            initEngineFromResource(resource);
        }
        return this;
    }

    private void initEngineFromResource(URL resource) {
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(resource.openStream(),"UTF-8"));
            String xml = "";
            String str = br.readLine();
            while(str != null) {
                xml += (str + "\n");
                str = br.readLine();
            }
            logger.info("加载资源文件内容：" + xml);
            Rules rules = XmlUtils.readValue(xml, Rules.class);
            loadedRules.put(resource.getPath(), rules);
            addRules(rules);
            checkAndFix();
        } catch (IOException e) {
            logger.error("加载资源文件出错：{}" + ExceptionUtils.getFullStackTrace(e));
            throw new RuleInitializeException("init engine from resource failed, " + resource.getPath());
        }
    }

    public void checkAndFix(){
        for (RuleInfo ruleInfo : ruleInfoMap.values()) {
            ruleInfo.setDepend(ruleInfoMap, defaultRuleInfoMap);
        }

        for (RuleInfo ruleInfo : ruleInfoMap.values()) {
            List<String> dependChain = new ArrayList<>();
            ruleInfo.initExpress(dependChain);
        }

        for (RuleInfo ruleInfo : ruleInfoMap.values()) {
            ruleInfo.loadAndExplain();
        }
        logger.info("express engine load finish :");
        for (RuleInfo ruleInfo : ruleInfoMap.values()) {
            logger.info("- code [ " + ruleInfo.getCode() + " ], version [ " + ruleInfo.getVersion() + " ]" + ", name [ " + ruleInfo.getName() + " ] : " + ruleInfo.getExpression());
        }
    }

    public void addRules(Rules rules) {
        if(rules == null) return;
        for (Rule rule : rules.getRuleList()) {
            RuleInfo ruleInfo = new RuleInfo(rule);
            ruleInfoMap.put(ruleInfo.getIdentify(), ruleInfo);
            RuleInfo beforeLoadRuleInfo = defaultRuleInfoMap.get(ruleInfo.getCode());
            boolean defaultReplace = true;
            if(beforeLoadRuleInfo != null) {
                char[] olds = beforeLoadRuleInfo.getVersion().toCharArray();
                char[] news = ruleInfo.getVersion().toCharArray();
                for (int i = 0; i < Math.min(olds.length, news.length); i++) {
                    if(olds[i] > news[i]) {
                        defaultReplace = false;
                        break;

                    }
                }
            }
            if(defaultReplace) defaultRuleInfoMap.put(ruleInfo.getCode(), ruleInfo);
        }
    }

    public static ExpressInvoker loadAndExplain(String rule){
        ExpressInvoker expressInvoker = ExpressInvoker.load(rule, true, true);
        expressInvoker.explain();
        return expressInvoker;
    }

    public EDataSet execute(String rule, String version, EDataSet eDataSet) {
        logger.debug("rule [ {} ] execute :\n {}",rule + ":" + version, eDataSet);
        RuleInfo ruleInfo;
        if(version == null) {
             ruleInfo = defaultRuleInfoMap.get(rule);
        }else {
            ruleInfo = ruleInfoMap.get(rule + ":" + version);
        }
        if(ruleInfo == null) {
            throw new ExpressExecuteException("rule [ " + rule + ":" + version +  " ] not exists !");
        }
        ExpressInvoker invoker = ruleInfo.getInvoker();
        if(invoker == null) {
            throw new ExpressExecuteException("rule [ " + rule + ":" + version +  " ] express invoker not exists, please fix it firstly !");
        }
        logger.debug("rule [ {} ] expression : {}", rule + ":" + version, ruleInfo.getExpression());
        return invoker.invoke(eDataSet);
    }

    private EDataSet execute(String rule, EDataSet eDataSet) {
        return execute(rule, null, eDataSet);
    }

    public static class Tripe<L,M,R>{
        private Map<L, Object[]> info = new HashMap<L, Object[]>();

        public void put(L var, M fetcher, R resultAttr) {
            info.put(var, new Object[]{fetcher, resultAttr});
        }
        public boolean containsKey(L var){
            return info.containsKey(var);
        }
        public M getMiddle(L var){
            return (M) info.get(var)[0];
        }
        public R getRight(L var){
            return (R) info.get(var)[1];
        }
    }
}
