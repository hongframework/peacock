package com.hframework.strategy.rule.fetch.mysql;

import com.hframework.smartsql.client2.DBClient;
import com.hframework.strategy.rule.fetch.FetchData;
import com.hframework.strategy.rule.fetch.Fetcher;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Created by zhangquanhong on 2017/7/17.
 */
public abstract class MysqlFetcher implements Fetcher<Integer> {
    private static final Logger logger = LoggerFactory.getLogger(MysqlFetcher.class);

    public FetchData fetch(Set<Integer> keys, Set<Object[]> aliasKeySet) throws Exception {


        Map<Integer, Map<String, Object>> result = new HashMap<>();

        String sql = null;
        if(StringUtils.isNoneBlank(sql())) {
            sql = sql();
        }else {
            sql = "SELECT * FROM " + table() + " WHERE " + primaryKey() + " = ?";
        }
        logger.debug("mysql fetch :{}, {}, {}", sql, keys, aliasKeySet);

        if(sqlParameters() != null && sqlParameters().length > 1) {
            for (Object[] aliasKey : aliasKeySet) {
                result.put(Integer.valueOf(String.valueOf(aliasKey[0])), DBClient.executeQueryMap(key(), sql, aliasKey));
            }
        }else {
            for (Integer key : keys) {
                result.put(key, DBClient.executeQueryMap(key(), sql, new Object[]{key}));
            }
        }


        return new FetchData().setData(result);
    }

    @Override
    public FetchData fetch(Set<Integer> keys) throws Exception {
        return fetch(keys, null);
    }

    public void register(){
        DBClient.registerDatabase(key(), "jdbc:mysql://" + host() + ":" + port() + "/" + database(), username(), password());
    }

    private String key(){
        return host() + "|" + port() + "|" + database() + "|" + database();
    }

    protected abstract String host();

    protected abstract String port();

    protected abstract String database();

    protected abstract String username();

    protected abstract String password();

    protected abstract String table();

    protected abstract String primaryKey();
    protected abstract String column();
    public abstract String sql();

    public abstract String[] sqlParameters();

}
