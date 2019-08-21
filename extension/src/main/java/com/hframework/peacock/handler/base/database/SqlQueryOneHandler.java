package com.hframework.peacock.handler.base.database;

import com.hframework.beans.exceptions.BusinessException;
import com.hframework.common.util.RegexUtils;
import com.hframework.common.util.SpelExpressionUtils;
import com.hframework.smartsql.client2.DBClient;
import com.hframework.smartweb.annotation.Handler;
import com.hframework.smartweb.annotation.SmartParameter;
import com.hframework.smartweb.bean.ImplicitSmartHandler;
import com.hframework.smartweb.bean.handler.AbstractSmartHandler;
import org.apache.commons.lang3.tuple.Pair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;

import java.util.*;

/**
 * Created by zhangquanhong on 2017/2/21.
 */
@Controller
@Handler(path = "/base/sql_query_one")
public class SqlQueryOneHandler extends AbstractSmartHandler implements ImplicitSmartHandler {
        private static Logger logger = LoggerFactory.getLogger(SqlQueryOneHandler.class);

        @Handler(version = "1.0.0", description = "SQL单值查询")
        public Map<String, Object> dbQueryOne(
                @SmartParameter(required = true, description = "数据库" , defaultValue = "${DATASOURCE.DBS}") String dbKey,
                @SmartParameter(required = true, description = "SQL语句") String sql,
                @SmartParameter(required = true, description = "是否允许信息不存在", defaultValue = "false") boolean notExistsAllowed,
                @SmartParameter(required = false, description = "信息不存在话述", defaultValue = "对应记录不存在!") String notExistsMessage,
                @SmartParameter(required = true, description = "查询字段", defaultValue = "${HANDLER.CONFIG.PARAMETERS}") String[] queryFields,
                @SmartParameter(required = true, description = "查询值", defaultValue = "${PARAMETERS.VALUES.ARRAY}") Object... parameterValues) {
                DBClient.setCurrentDatabaseKey(dbKey);
                Pair<String, Object[]> sqlAndParam = recombinationSqlAndParameters(sql, queryFields, parameterValues);
                Map<String, Object> map = DBClient.executeQueryMap(sqlAndParam.getLeft(), sqlAndParam.getRight());
                if (map == null && !notExistsAllowed) {
                        throw new BusinessException("100023", notExistsMessage);
                }
                return map;
        }

        @Handler(version = "1.0.0", description = "SQL单值查询", batch = true)
        public List<Map<String, Object>> dbQueryOnes(
                @SmartParameter(required = true, description = "数据库" , defaultValue = "${DATASOURCE.DBS}") String dbKey,
                @SmartParameter(required = true, description = "SQL语句") String sql,
                @SmartParameter(required = true, description = "是否允许信息不存在", defaultValue = "false") boolean notExistsAllowed,
                @SmartParameter(required = false, description = "信息不存在话述", defaultValue = "对应记录不存在!") String notExistsMessage,
                @SmartParameter(required = true, description = "查询字段", defaultValue = "${HANDLER.CONFIG.PARAMETERS}") String[] queryFields,
                @SmartParameter(required = true, description = "查询值", defaultValue = "${PARAMETERS.VALUES.ARRAY}") Object... parameterValues) {
                DBClient.setCurrentDatabaseKey(dbKey);
                Pair<String, Object[]> sqlAndParam = recombinationSqlAndParameters(sql, queryFields, parameterValues);
                List<Map<String, Object>> maps = DBClient.executeQueryMaps(sqlAndParam.getLeft(), sqlAndParam.getRight());
//                if (maps == null && !notExistsAllowed) {
//                        throw new BusinessException("100023", notExistsMessage);
//                }

                /*
                        批量方法需要做到等长与有序，防止下一步在数据组装时，无法与源数据对应。
                        因此，判断数据量不一样，且查询fields不在返回结果集中，则这里进行映射,这里处理即不严谨，因此需要在配置功能上进行限制。TODO
                 */
                if(queryFields.length == 1 && maps.size() > 0 && ((Object[]) parameterValues[0]).length != maps.size()) {
                        if(!maps.get(0).containsKey(queryFields[0])) {
                                logger.warn("SqlQueryOneHandler batch mode [{}| {}]: request data number not eq response data number!"
                                        , queryFields[0], maps.get(0).keySet().iterator().next());
                                Map<String, Map<String, Object>> mapsKeyInfo = new HashMap<>();
                                for (Map<String, Object> temp : maps) {
                                        mapsKeyInfo.put(String.valueOf(temp.values().iterator().next()), temp);
                                }
                                List<Map<String, Object>> result = new ArrayList<>();
                                for (Object reqParam : (Object[])parameterValues[0]) {
                                        result.add(new LinkedHashMap<>(mapsKeyInfo.get(String.valueOf(reqParam))));
                                }

                                return result;
                        }
                }
                return maps;
        }

        @Override
        public String implicitDataField() {
                return "parameterValues";
        }
}
