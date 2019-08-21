package com.hframework.peacock.handler.base.database;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.hframework.beans.exceptions.BusinessException;
import com.hframework.common.util.RegexUtils;
import com.hframework.common.util.SpelExpressionUtils;
import com.hframework.smartsql.client2.DBClient;
import com.hframework.smartweb.annotation.Handler;
import com.hframework.smartweb.annotation.SmartParameter;
import com.hframework.smartweb.bean.ImplicitSmartHandler;
import com.hframework.smartweb.bean.SmartHandler;
import com.hframework.smartweb.bean.handler.AbstractSmartHandler;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.stereotype.Controller;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by zhangquanhong on 2017/2/21.
 */
@Controller
@Handler(path = "/base/sql_query_list")
public class SqlQueryMoreHandler extends AbstractSmartHandler implements ImplicitSmartHandler {

    @Handler(version ="1.0.0", description = "SQL多值查询")
    public List<Map<String, Object>> dbQueryMore(
            @SmartParameter(required = true, description = "数据库" , defaultValue = "${DATASOURCE.DBS}") String dbKey,
            @SmartParameter(required = true, description = "SQL语句") String sql,
            @SmartParameter(required = true, description = "查询字段", defaultValue = "${HANDLER.CONFIG.PARAMETERS}") String[] queryFields,
            @SmartParameter(required = true, description = "查询值", defaultValue = "${PARAMETERS.VALUES.ARRAY}") Object... parameterValues) {
        DBClient.setCurrentDatabaseKey(dbKey);
        Pair<String, Object[]> sqlAndParam = recombinationSqlAndParameters(sql, queryFields, parameterValues);
        List<Map<String, Object>> map = DBClient.executeQueryMaps(sqlAndParam.getLeft(), sqlAndParam.getRight());
        return map;
    }

    @Override
    public String implicitDataField() {
        return "parameterValues";
    }
}
