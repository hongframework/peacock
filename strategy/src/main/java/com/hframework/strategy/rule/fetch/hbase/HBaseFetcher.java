package com.hframework.strategy.rule.fetch.hbase;

import com.hframework.common.client.hbase.HBaseClient;
import com.hframework.common.util.collect.CollectionUtils;
import com.hframework.strategy.index.repository.converter.RowKeyConverter;
import com.hframework.strategy.rule.fetch.FetchData;
import com.hframework.strategy.rule.fetch.Fetcher;
import org.apache.commons.lang3.StringUtils;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.client.ResultScanner;
import org.apache.hadoop.hbase.util.Bytes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

/**
 * Created by zhangquanhong on 2017/7/17.
 */
public abstract class HBaseFetcher implements Fetcher<Integer> {

    private static final Logger logger = LoggerFactory.getLogger(HBaseFetcher.class);
    private HBaseClient hBaseClient;

    private static final Integer HBASE_GET_THRESHOLD = 5;

    private static final String ROW_KEY_NAME = "_HBASE_ROW_KEY";

    @Override
    public FetchData fetch(Set<Integer> keys) throws Exception {

        logger.debug("hbase fetch : {}", keys);

        List<Map> list = new ArrayList<>();
        boolean isScan = false;
        if(keys.size() > HBASE_GET_THRESHOLD) {
            Integer max = Collections.max(keys);
            Integer min = Collections.min(keys);
            if(max - min + 1 < keys.size() * 2) {//如果存在一半的数据
                isScan = true;
                String fromRowKey = rowKeyConverter().convert(min);
                String toRowKey = rowKeyConverter().convert(max);
                logger.debug("hbase scan : {}, {}, {}",tableName(), fromRowKey, toRowKey);
                ResultScanner results = getClient().scan(tableName(),fromRowKey , toRowKey);
                for (Result result : results) {
                    Map map = convertResultToMap(result, familyName());
                    if(map != null && !map.isEmpty()) {
                        list.add(map);
                    }

                }
            }
        }

        if(!isScan) {
            List<String> rowKeys = CollectionUtils.fetch(new ArrayList<>(keys),
                    new com.hframework.common.util.collect.bean.Fetcher<Integer, String>() {
                @Override
                public String fetch(Integer integer) {
                    return rowKeyConverter().convert(integer);
                }
            });
            logger.debug("hbase get : {}", rowKeys);
            for (String key : rowKeys) {
                Result result = getClient().get(tableName(), key, familyName());
                Map map = convertResultToMap(result, familyName());
                if(map != null && !map.isEmpty()) {
                    list.add(map);
                }
            }
        }

        return FetchData.swap(list, new FetchData.KeyMap<Map, Integer>() {
            public Integer map(Map t) {
                if(StringUtils.isBlank(keyQualifier())) {
                    return Integer.valueOf(String.valueOf(t.get(ROW_KEY_NAME)));
                }else {
                    return Integer.valueOf(String.valueOf(t.get(keyQualifier())));
                }
            }
        });
    }

    public Map convertResultToMap(Result result, String familyName) {
        Map<String, Object> tempMap = new HashMap<>();
        if(result.isEmpty()) {
            return null;
        }
        tempMap.put(ROW_KEY_NAME, rowKeyConverter().reverse(new String(result.getRow())));
        NavigableMap<byte[], byte[]> familyMap = result.getFamilyMap(Bytes.toBytes(familyName));
        if(familyMap == null || familyMap.isEmpty()) {
            return null;
        }
        for (Map.Entry<byte[], byte[]> entry : familyMap.entrySet()) {
            String qualifierName = new String(entry.getKey());
            String qualifierValue = new String(entry.getValue());
            tempMap.put(qualifierName, qualifierValue);
        }

        return tempMap;
    }

    public HBaseClient getClient(){
        if(hBaseClient == null) {
            synchronized (this) {
                if(hBaseClient == null) {
                    hBaseClient = HBaseClient.getInstance(zkList(), zkPort());
                }
            }
        }
        return hBaseClient;
    }


    protected abstract String zkList();

    protected abstract String zkPort();

    protected abstract String tableName();

    protected abstract String familyName();

    protected abstract RowKeyConverter rowKeyConverter();

    protected abstract String keyQualifier();


}
