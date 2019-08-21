package com.hframework.peacock.handler.base.database;

import com.hframework.beans.exceptions.BusinessException;
import com.hframework.smartsql.client2.DBClient;
import com.hframework.smartweb.annotation.Handler;
import com.hframework.smartweb.annotation.SmartParameter;
import com.hframework.smartweb.bean.ImplicitSmartHandler;
import com.hframework.smartweb.bean.handler.AbstractSmartHandler;
import org.springframework.stereotype.Controller;

import java.util.List;
import java.util.Map;

/**
 * Created by zhangquanhong on 2017/2/21.
 */
@Controller
@Handler(path = "/base/db_query_one")
public class DBQueryOneHandler extends AbstractSmartHandler implements ImplicitSmartHandler {

        @Handler(version = "1.0.0", description = "数据表单值查询")
        public Map<String, Object> dbQueryOne(
                @SmartParameter(required = true, description = "数据库", defaultValue = "${DATASOURCE.DBS}") String dbKey,
                @SmartParameter(required = true, description = "表名", defaultValue = "${DATASOURCE.TABLES}") String tableName,
                @SmartParameter(required = true, description = "是否允许信息不存在", defaultValue = "false") boolean notExistsAllowed,
                @SmartParameter(required = false , description = "信息不存在话述", defaultValue = "对应记录不存在!") String notExistsMessage,
                @SmartParameter(required = true, description = "查询字段", defaultValue = "${HANDLER.CONFIG.PARAMETERS}") String[] queryFields,
                @SmartParameter(required = true, description = "返回值段", defaultValue = "${HANDLER.CONFIG.RESULTS}") String[] returnFields,
                @SmartParameter(required = true, description = "查询值", defaultValue = "${PARAMETERS.VALUES.ARRAY}") Object[] parameterValues) {
                DBClient.setCurrentDatabaseKey(dbKey);

                Map<String, Object> map = DBClient.executeQueryMap(
                        HandlerHelper.concatQueryOneSql(tableName, queryFields, returnFields, parameterValues),
                        parameterValues);
                if (map == null && !notExistsAllowed) {
                        throw new BusinessException("100023", notExistsMessage);
                }
                return map;
        }

        @Handler(version = "1.0.0", description = "数据表单值查询", batch = true)
        public List<Map<String, Object>> dbQueryOnes(
                @SmartParameter(required = true, description = "数据库", defaultValue = "${DATASOURCE.DBS}") String dbKey,
                @SmartParameter(required = true, description = "表名", defaultValue = "${DATASOURCE.TABLES}") String tableName,
                @SmartParameter(required = true, description = "是否允许信息不存在", defaultValue = "false") boolean notExistsAllowed,
                @SmartParameter(required = false , description = "信息不存在话述", defaultValue = "对应记录不存在!") String notExistsMessage,
                @SmartParameter(required = true, description = "查询字段", defaultValue = "${HANDLER.CONFIG.PARAMETERS}") String[] queryFields,
                @SmartParameter(required = true, description = "返回值段", defaultValue = "${HANDLER.CONFIG.RESULTS}") String[] returnFields,
                @SmartParameter(required = true, description = "查询值", defaultValue = "${PARAMETERS.VALUES.ARRAY}") Object[] parameterValues) {
                DBClient.setCurrentDatabaseKey(dbKey);

                List<Map<String, Object>> maps = DBClient.executeQueryMaps(
                        HandlerHelper.concatSql(tableName, queryFields, returnFields, parameterValues).toString(),
                        parameterValues);
//                if (maps == null && !notExistsAllowed) {
//                        throw new BusinessException("100023", notExistsMessage);
//                }
                return maps;
        }

        @Override
        public String implicitDataField() {
                return "parameterValues";
        }
}
