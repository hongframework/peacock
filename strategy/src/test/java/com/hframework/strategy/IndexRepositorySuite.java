package com.hframework.strategy;

import com.google.common.collect.Sets;
import com.hframework.strategy.index.IndexRepository;
import com.hframework.strategy.rule.fetch.Fetcher;
import org.junit.Test;

/**
 * Created by zhangquanhong on 2017/9/15.
 */
public class IndexRepositorySuite {

    public Fetcher fetcher(String id){
        return IndexRepository.getDefaultInstance().getFetchers().getMiddle(id);
    }

    @Test
    public void redis_set_contain() throws Exception {
        System.out.println(fetcher("isLossWarningUser").fetch(Sets.newHashSet(7261923,123)).getData());
    }

    @Test
    public void mysql_table_query() throws Exception {
        System.out.println(fetcher("balance").fetch(Sets.newHashSet(6089,123)).getData());
    }

    @Test
    public void mysql_sql_query() throws Exception {
        System.out.println(fetcher("bonusBalance").fetch(Sets.newHashSet(6089,123)).getData());
    }

    @Test
    public void hbase_query() throws Exception {
        System.out.println(fetcher("lastRefundTime").fetch(Sets.newHashSet(6089,123)).getData());
    }
}
