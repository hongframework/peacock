package com.hframework.strategy.rule.fetch.redis;

import com.hframework.common.util.collect.CollectionUtils;
import com.hframework.smartsql.client2.RedisClient;
import com.hframework.strategy.index.repository.converter.FixRowKeyConverter;
import com.hframework.strategy.index.repository.converter.RowKeyConverter;
import com.hframework.strategy.rule.fetch.FetchData;
import com.hframework.strategy.rule.fetch.Fetcher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

/**
 * Created by zhangquanhong on 2017/7/17.
 */
public abstract class RedisFetcher implements Fetcher<Integer> {

    private static final Logger logger = LoggerFactory.getLogger(RedisFetcher.class);


    @Override
    public FetchData fetch(Set<Integer> keys) throws Exception {

        logger.debug("redis fetch : {}", keys);

        if(rowKeyConverter() instanceof FixRowKeyConverter) {
            Map<Integer, Boolean> result = new HashMap<>();
            final String key = ((FixRowKeyConverter) rowKeyConverter()).getKey();
            if("contain".equals(method())) {
                boolean isSet = "set".equals(dataType());
                for (final Integer value : keys) {
                    if(isSet) result.put(value, RedisClient.sIsMember(key(), key, String.valueOf(value)));

                }
            }
            return new FetchData().setData(result);
        }

        List<Map> list = new ArrayList<>();
        List<String> rowKeys = CollectionUtils.fetch(new ArrayList<>(keys),
                new com.hframework.common.util.collect.bean.Fetcher<Integer, String>() {
                    @Override
                    public String fetch(Integer integer) {
                        return rowKeyConverter().convert(integer);
                    }
                });
        logger.debug("redis get : {}", rowKeys);

        Map<Integer, Map> result = new HashMap<>();
        for (final String key : rowKeys) {
            result.put((Integer) rowKeyConverter().reverse(key), new HashMap(){{
                put("contain", RedisClient.get(key(), key));
            }});
        }
        return new FetchData().setData(result);
    }

    public void register(){
        RedisClient.registerRedis(key(), host(), port(), auth(), database());
    }

    private String key(){
        return host() + "|" + port() + "|" + auth() + "|" + database();
    }


    protected abstract String host();
    protected abstract int port();
    protected abstract String auth();
    protected abstract int database();
    protected abstract RowKeyConverter rowKeyConverter();
    protected abstract String method();
    protected abstract String dataType();
}
