package com.hframework.strategy.index;

import com.google.common.base.Joiner;
import com.hframework.common.util.message.XmlUtils;
import com.hframework.strategy.index.repository.Indexs;
import com.hframework.strategy.index.repository.converter.FixRowKeyConverter;
import com.hframework.strategy.index.repository.converter.RowKeyConverter;
import com.hframework.strategy.index.repository.converter.SHA256RowKeyConverter;
import com.hframework.strategy.index.repository.indexs.Hbase;
import com.hframework.strategy.index.repository.indexs.Index;
import com.hframework.strategy.index.repository.indexs.Mysql;
import com.hframework.strategy.index.repository.indexs.Redis;
import com.hframework.strategy.rule.exceptions.IndexInitializeException;
import com.hframework.strategy.rule.fetch.DemoFetcher;
import com.hframework.strategy.rule.fetch.Fetcher;
import com.hframework.strategy.rule.fetch.hbase.GenericHBaseFetcher;
import com.hframework.strategy.rule.fetch.mysql.GenericMysqlFetcher;
import com.hframework.strategy.rule.fetch.redis.GenericRedisFetcher;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by zhangquanhong on 2017/7/17.
 */
public class IndexRepository {
    private static IndexRepository globalRepository ;
    private static final Logger logger = LoggerFactory.getLogger(IndexRepository.class);
    private static final String INDEX_XML_PATH = "indexs.xml";

    private Map<String, Indexs> loadedRules = new HashMap<>();
    private Tripe<String, Fetcher, String> fetchers = new Tripe<String, Fetcher, String>();
    {
//        fetchers.put("totalInvestCount", new DemoFetcher(),"total_bid_count"); //总投资次数
//        fetchers.put("investingAmount", new DemoFetcher(),"user_balance_loan_repay"); //在投金额
//        fetchers.put("totalYearInvestedAmount", new DemoFetcher(),"total_bid_annual_money"); //累计年化投资额
//        fetchers.put("lastRefundTime", new DemoFetcher(),"user_repay_last_time"); //最后一次回款时间
//        fetchers.put("lastInvestedTime", new DemoFetcher(),"last_bid_time"); //最后一次投资时间
//        fetchers.put("lastRechargeTime", new DemoFetcher(),"user_payment_last_time"); //最后一次充值时间
//        fetchers.put("lastWithdrawTime", new DemoFetcher(),"user_withdraw_last_time"); //最后一次提现时间
//        fetchers.put("balance", new DemoFetcher(),"user_attribute_money"); //账户余额
//        fetchers.put("frozen", new DemoFetcher(),"user_attribute_lock_money"); //冻结金额
        fetchers.put("lastLevel", new DemoFetcher(),"lastLevel");//上次用户评级
        fetchers.put("potentialValue", new DemoFetcher(),"potentialValue");//潜力值
        fetchers.put("maxDayInvestingAmountOfSixMonth", new DemoFetcher(),"maxDayInvestingAmountOfSixMonth");//近6月最高日在途
        fetchers.put("planingRefundAmount", new DemoFetcher(),"planingRefundAmount");//计划回款
    };

    Map<String, Fetcher> fetcherCache = new HashMap<>();

    public static IndexRepository getDefaultInstance(){
        if(globalRepository == null) {
            synchronized (IndexRepository.class){
                if(globalRepository == null) {
                    globalRepository = getNewInstance();
                }
            }
        }
        return globalRepository;
    }

    public static IndexRepository getNewInstance(){
        IndexRepository IndexRepository = new IndexRepository();
        IndexRepository.load(INDEX_XML_PATH);
        return IndexRepository;
    }
    public IndexRepository load(String resourcePath) {
        logger.info("加载资源文件：" + resourcePath);
        Enumeration<URL> resources = null;
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        try {
            resources = classLoader.getResources(resourcePath);
        } catch (IOException e) {
            e.printStackTrace();
            throw new IndexInitializeException( resourcePath + " is not exists !");
        }
        while (resources.hasMoreElements()) {
            URL resource = resources.nextElement();
            initRepository(resource);
        }
        return this;
    }

    private void initRepository(URL resource) {
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(resource.openStream(),"UTF-8"));
            String xml = "";
            String str = br.readLine();
            while(str != null) {
                xml += (str + "\n");
                str = br.readLine();
            }
            logger.info("加载资源文件内容：" + xml);
            Indexs indexs = XmlUtils.readValue(xml, Indexs.class);
            loadedRules.put(resource.getPath(), indexs);
            addIndexs(indexs);
        } catch (IOException e) {
            logger.error("加载资源文件出错：{}" + ExceptionUtils.getFullStackTrace(e));
            throw new IndexInitializeException("init index from resource failed, " + resource.getPath());
        }
    }

    public Fetcher getMysqlFetcher(String host, String port, String database, String username, String password, String table, String primaryKey, String column, String sql){
        if(host == null) throw new IndexInitializeException("mysql host is not empty !");
        if(port == null) throw new IndexInitializeException("mysql port is not empty !");
        if(database == null) throw new IndexInitializeException("mysql database is not empty !");
        if(username == null) throw new IndexInitializeException("mysql username is not empty !");
        if(password == null) throw new IndexInitializeException("mysql password is not empty !");
        if(table == null && sql == null) throw new IndexInitializeException("mysql both table and sql are not empty !");
        if(primaryKey == null) throw new IndexInitializeException("mysql primaryKey is not empty !");
        if(sql == null) sql = "";
        if(table == null) table = "";

        String key = Joiner.on("|").join(new String[]{"mysql", host, port, database, table, primaryKey, sql.trim()});
        if(!fetcherCache.containsKey(key)){
            fetcherCache.put(key, new GenericMysqlFetcher(host,port,database,username, password, table, primaryKey, null, sql.trim()));
        }
        return fetcherCache.get(key);
    }

    public Fetcher getRedisFetcher(String host, String port, String auth, String database, String rowkeyConverter,  String keyParts, String method, String dataType){
        if(host == null) throw new IndexInitializeException("redis host is not empty !");
        if(port == null) throw new IndexInitializeException("redis port is not empty !");
        if(auth == null) throw new IndexInitializeException("redis auth is not empty !");
        if(database == null) throw new IndexInitializeException("redis database is not empty !");
        if(rowkeyConverter == null) rowkeyConverter = "";

        String key = Joiner.on("|").join(new String[]{"redis", host, port, auth, database, rowkeyConverter});
        if(!fetcherCache.containsKey(key)){
            RowKeyConverter rowKeyConverter = new SHA256RowKeyConverter();
            if("SHA256RowKeyConverter".equals(rowkeyConverter)) {
                rowKeyConverter  = new SHA256RowKeyConverter();
            }else {
                rowKeyConverter  = new FixRowKeyConverter(keyParts.split(","));
            }
            fetcherCache.put(key, new GenericRedisFetcher(host, Integer.valueOf(port), auth,
                    Integer.valueOf(database), rowKeyConverter, method, dataType));
        }
        return fetcherCache.get(key);
    }

    public Fetcher getHBaseFetcher(String zkList, String zkPort, String table, String family, String rowkeyConverter, String keyQualifier){
        if(zkList == null) throw new IndexInitializeException("hbase zkList is not empty !");
        if(zkPort == null) throw new IndexInitializeException("hbase zkPort is not empty !");
        if(table == null) throw new IndexInitializeException("hbase table is not empty !");
        if(family == null) throw new IndexInitializeException("hbase family is not empty !");
        if(rowkeyConverter == null) throw new IndexInitializeException("hbase rowkeyConverter is not empty !");
        String key = Joiner.on("|").join(new String[]{"hbase", zkList, zkPort, table, family, rowkeyConverter});
        if(!fetcherCache.containsKey(key)){
            RowKeyConverter rowKeyConverter = new SHA256RowKeyConverter();
            if("SHA256RowKeyConverter".equals(rowkeyConverter)) {
                rowKeyConverter  = new SHA256RowKeyConverter();
            }
            fetcherCache.put(key, new GenericHBaseFetcher(zkList, zkPort, table, family, rowKeyConverter , keyQualifier));
        }
        return fetcherCache.get(key);
    }

    private void addIndexs(Indexs indexs) {
        if(indexs == null || indexs.getIndexList() == null || indexs.getIndexList().isEmpty()) return;
        for (Index index : indexs.getIndexList()) {
            String id = index.getId();
            if(fetchers.containsKey(id)){
                throw new IndexInitializeException("index [ " + id + " : " + index.getName() + " ] exists, please check ! ");
            }
            Fetcher fetcher = null;
            List<Hbase> hbaseList = index.getHbaseList();
            List<Mysql> mysqlList = index.getMysqlList();
            List<Redis> redisList = index.getRedisList();
            if(mysqlList != null && mysqlList.size() > 0) {
                Mysql config = mysqlList.get(0);
                fetcher = getMysqlFetcher(config.getHost(),config.getPort(), config.getDatabase(),
                        config.getUsername(), config.getPassword(), config.getTable(), config.getPrimaryKey(),
                        config.getColumn(), config.getText());
                fetchers.put(id, fetcher, config.getColumn());
            }else if(redisList != null && redisList.size() > 0) {
                Redis config = redisList.get(0);
                fetcher = getRedisFetcher(config.getHost(), config.getPort(), config.getAuth(),config.getDb(),
                        config.getKeyConverter(), config.getKeyParts(), config.getMethod(), config.getDataType());
                fetchers.put(id, fetcher, null);
            }else if(hbaseList != null && hbaseList.size() > 0) {
                Hbase config = hbaseList.get(0);
                fetcher = getHBaseFetcher(config.getZklist(), config.getZkport(), config.getTable(), config.getFamily(), config.getRowkeyConverter() , null);
                fetchers.put(id, fetcher, config.getQualifier());
            }
        }
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

    public Tripe<String, Fetcher, String> getFetchers() {
        return fetchers;
    }

    public void setFetchers(Tripe<String, Fetcher, String> fetchers) {
        this.fetchers = fetchers;
    }
}
