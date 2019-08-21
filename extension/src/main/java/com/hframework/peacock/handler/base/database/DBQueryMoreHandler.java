package com.hframework.peacock.handler.base.database;

import com.hframework.smartsql.client2.DBClient;
import com.hframework.smartweb.annotation.Handler;
import com.hframework.smartweb.annotation.SmartParameter;
import com.hframework.smartweb.bean.ImplicitSmartHandler;
import com.hframework.smartweb.bean.SmartHandler;
import com.hframework.smartweb.bean.handler.AbstractSmartHandler;
import org.springframework.stereotype.Controller;

import java.util.List;
import java.util.Map;

/**
 * Created by zhangquanhong on 2017/2/21.
 */
@Controller
@Handler(path = "/base/db_query_list")
public class DBQueryMoreHandler extends AbstractSmartHandler implements ImplicitSmartHandler {

    @Handler(version ="1.0.0", description = "数据表多值查询")
    public List<Map<String, Object>> dbQueryMore(
            @SmartParameter(required = true, description = "数据库", defaultValue = "${DATASOURCE.DBS}") String dbKey,
            @SmartParameter(required = true, description = "表名", defaultValue = "${DATASOURCE.TABLES}") String tableName,
            @SmartParameter(required = true, description = "查询字段", defaultValue = "${HANDLER.CONFIG.PARAMETERS}") String[] queryFields,
            @SmartParameter(required = true, description = "返回值段", defaultValue = "${HANDLER.CONFIG.RESULTS}") String[] returnFields,
            @SmartParameter(required = true, description = "查询值", defaultValue = "${PARAMETERS.VALUES.ARRAY}") Object[] parameterValues,
            @SmartParameter(required = true, description = "页码", defaultValue = "${PARAMETERS.PAGE.NO:1}") Integer pageNo,
            @SmartParameter(required = true, description = "页码", defaultValue = "${PARAMETERS.PAGE.Size:20}")Integer pageSize) {
        DBClient.setCurrentDatabaseKey(dbKey);
        for (int i = 0; i < returnFields.length; i++) {//如果Return需要已数组的形式返回，那么这里在sql查询时需要临时将"[]"处理掉
            returnFields[i]  =returnFields[i].endsWith("[]") ? returnFields[i].substring(0, returnFields[i].length() - 2): returnFields[i];
        }
        List<Map<String, Object>> map = DBClient.executeQueryMaps(
                HandlerHelper.concatQueryPaginationSql(tableName, queryFields, returnFields, parameterValues, pageNo, pageSize),
                parameterValues);
        return map;
    }

    @Override
    public String implicitDataField() {
        return "parameterValues";
    }
}
