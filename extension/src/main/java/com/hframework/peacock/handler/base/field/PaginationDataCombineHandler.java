package com.hframework.peacock.handler.base.field;

import com.hframework.smartweb.annotation.Handler;
import com.hframework.smartweb.annotation.SmartParameter;
import com.hframework.smartweb.bean.CombineResult;
import com.hframework.smartweb.bean.SmartHandler;
import com.hframework.smartweb.bean.handler.AbstractSmartHandler;
import org.springframework.stereotype.Controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Controller
@Handler(path = "/base/pagination_data_combine")
public class PaginationDataCombineHandler extends AbstractSmartHandler implements SmartHandler {

    @Handler(version = "1.0.0", description = "分页记录合并")
    public CombineResult combineList(
            @SmartParameter(required = true, description = "数据集合A") List<Map<String, Object>> listA,
            @SmartParameter(required = true, description = "数据集合B") List<Map<String, Object>> listB,
            @SmartParameter(required = true, description = "集合A偏移量") Long offsetA,
            @SmartParameter(required = true, description = "集合B偏移量") Long offsetB,
            @SmartParameter(required = true, description = "排序说明") final String orderBy,
            @SmartParameter(required = true, description = "保留记录数", defaultValue = "20") Integer limit) {

        List<Map<String, Object>> result = new ArrayList<>();

        if(listA != null ) result.addAll(listA);
        if(listB != null ) result.addAll(listB);

        DataCombineUtils.sortByOrderBy(result, orderBy);
        long useA = 0;
        if(limit > 0 && result.size() > limit) {
            result = result.subList(0, limit);
            if(listA != null){
                for (Map<String, Object> map : listA) {
                    if(result.contains(map)) useA ++;
                }
            }
        }else {
            useA = listA == null ?  0 : listA.size();
        }

        long userB = result.size() - useA;

        return new CombineResult(offsetA + useA, offsetB + userB, result);
    }


}
