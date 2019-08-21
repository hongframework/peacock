package com.hframework.peacock.handler.base.field;

import com.hframework.smartweb.annotation.Handler;
import com.hframework.smartweb.annotation.SmartParameter;
import com.hframework.smartweb.bean.SmartHandler;
import com.hframework.smartweb.bean.handler.AbstractSmartHandler;
import org.springframework.stereotype.Controller;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;


/**
 * 单条记录合并
 * 对【记录A】与【记录A】进行合并，并对相同的KEY进行值合并:MAX,MIN,SUM,ORDER,COUNT
 */
@Controller
@Handler(path = "/base/record_combine")
public class RecordCombineHandler extends AbstractSmartHandler implements SmartHandler {

    @Handler(version = "1.0.0", description = "单条记录合并")
    public Map<String, Object> combineList(
            @SmartParameter(required = true, description = "记录A") Map<String, Object> recordA,
            @SmartParameter(required = true, description = "记录B") Map<String, Object> recordB,
            @SmartParameter(required = false, description = "合并排除字段") String excludeMergeFields,
            @SmartParameter(required = true, description = "合并策略", options ={"max", "min", "sum", "head", "last", "g_min", "g_max", "g_head", "g_last"},
                    defaultValue = "g_head") final String mergeFunc) {


        if(recordA == null || recordA.isEmpty()) return recordB;
        if(recordB == null || recordB.isEmpty()) return recordA;

        Set<String> excludeMergeFieldSet = DataCombineUtils.getStringSetInfo(excludeMergeFields);

        if("g_head".equals(mergeFunc)) {
            return recordA;
        }else if("g_last".equals(mergeFunc)) {
            return recordB;
        }else if(mergeFunc.startsWith("g_")) {
            for (String key : recordA.keySet()) {
                if(excludeMergeFieldSet.contains(key)) {
                    continue;
                }
                Object aValue = recordA.get(key);
                Object bValue = recordA.get(key);
                if(aValue == bValue) {
                    continue;
                }
                Object mergeValue = DataCombineUtils.merge(mergeFunc.substring(2), aValue, bValue);
                if(mergeValue.equals(aValue)) {
                    return recordA;
                }else {
                    return recordB;
                }
            }
            return recordA;
        }else {
            Map<String, Object> result = new LinkedHashMap<>();
            result.putAll(recordA);

            for (String key : recordB.keySet()) {
                if(excludeMergeFieldSet.contains(key)) {
                    continue;
                }
                Object targetValue = recordB.get(key);
                result.put(key, DataCombineUtils.merge(mergeFunc, result.get(key), targetValue));
            }
            return result;
        }
    }




}
