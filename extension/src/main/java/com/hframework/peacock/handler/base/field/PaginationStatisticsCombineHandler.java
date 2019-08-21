package com.hframework.peacock.handler.base.field;

import com.hframework.common.util.collect.CollectionUtils;
import com.hframework.common.util.collect.bean.Fetcher;
import com.hframework.smartweb.annotation.Handler;
import com.hframework.smartweb.annotation.SmartParameter;
import com.hframework.smartweb.bean.CombineResult;
import com.hframework.smartweb.bean.SmartHandler;
import com.hframework.smartweb.bean.handler.AbstractSmartHandler;
import org.spark_project.guava.collect.Lists;
import org.springframework.stereotype.Controller;

import java.util.*;


/**
 * 分页汇总数据合并
 * 对【数据集合A】与【数据集合B】进行数据合并，并对相同的KEY进行值合并:MAX,MIN,SUM,DISCINT,COUNT
 */
@Controller
@Handler(path = "/base/pagination_value_combine")
public class PaginationStatisticsCombineHandler extends AbstractSmartHandler implements SmartHandler {

    @Handler(version = "1.0.0", description = "分页数据合并")
    public CombineResult combineList(
            @SmartParameter(required = true, description = "数据集合A") List<Map<String, Object>> listA,
            @SmartParameter(required = true, description = "数据集合B") List<Map<String, Object>> listB,
            @SmartParameter(required = true, description = "集合A偏移量", defaultValue = "0") Long offsetA,
            @SmartParameter(required = true, description = "集合B偏移量", defaultValue = "0") Long offsetB,
            @SmartParameter(description = "分组条件") final String groupBy,
            @SmartParameter(required = true, description = "合并策略", options ={"max", "min", "sum", "head", "last"}, defaultValue = "head") final String aggFunc,
            @SmartParameter(description = "排序条件") final String orderBy,
            @SmartParameter(required = true, description = "保留记录数", defaultValue = "-1") Integer limit) {

        List<Map<String, Object>> result = new ArrayList<>();

        if(listA != null ) result.addAll(listA);
        if(listB != null ) result.addAll(listB);
        Map<String, List<Map<String, Object>>> group = DataCombineUtils.groupByGroupBy(result, groupBy);
        final Set<String> groupItems = DataCombineUtils.getGroupByMapInfo(groupBy);
        List<Map<String, Object>> calcResult = CollectionUtils.fetch(Lists.newArrayList(group.entrySet()),
                new Fetcher<Map.Entry<String, List<Map<String, Object>>>, Map<String, Object>>() {
                    @Override
                    public Map<String, Object> fetch(Map.Entry<String, List<Map<String, Object>>> entry) {
                        Map<String, Object> result = DataCombineUtils.agg(entry.getValue(), groupItems, aggFunc);
                        return result;
                    }
            });

        DataCombineUtils.sortByOrderBy(calcResult, orderBy);

        long useA;
        long userB;
        if(limit > 0 && calcResult.size() > limit) {
            calcResult = calcResult.subList(0, limit);
            Set<String> resultKeys = DataCombineUtils.groupByGroupBy(calcResult, groupBy).keySet();
            Set<String> listAKeys = DataCombineUtils.groupByGroupBy(listA, groupBy).keySet();
            Set<String> listBKeys = DataCombineUtils.groupByGroupBy(listB, groupBy).keySet();

            useA = listAKeys.size();
            userB = listBKeys.size();

            listAKeys.removeAll(resultKeys);
            listBKeys.removeAll(resultKeys);

            useA -= listAKeys.size();
            userB -= listBKeys.size();
        }else {
            useA = listA != null ?listA.size() : 0;
            userB = listB != null ?listB.size() : 0;
        }


        return new CombineResult(offsetA + useA, offsetB + userB, calcResult.isEmpty() ? null : calcResult);
    }




}
