package com.hframework.peacock.controller;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.parser.Feature;
import com.hframework.beans.controller.ResultData;
import com.hframework.common.frame.ServiceFactory;
import com.hframework.common.util.JavaUtil;
import com.hframework.common.util.RegexUtils;
import com.hframework.peacock.controller.base.ApiManager;
import com.hframework.smartsql.client2.DBClient;
import com.hframework.smartweb.APIErrorType;
import com.hframework.smartweb.SmartHandlerFactory;
import com.hframework.smartweb.bean.Object2MapHelper;
import com.hframework.smartweb.bean.SmartHandler;
import com.hframework.smartweb.bean.handler.HandlerHelper;
import com.hframework.smartweb.exception.SmartHandlerException;
import com.hframework.peacock.controller.base.ApiManager;
import com.hframework.peacock.controller.base.descriptor.ApiDescriptor;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.lang.reflect.Method;
import java.util.*;

/**
 * Created by zhangquanhong on 2017/12/25.
 */
@Controller
@RequestMapping("/extend")
public class PeacockXmlContentEditController {

    private static final Logger logger = LoggerFactory.getLogger(PeacockXmlContentEditController.class);

    /**
     * 异步Ajax获取文件编辑器中HelperData（需要走ModelAndView进行视图进行渲染）
     * @return
     */
//    @RequestMapping(value = "/getFileEditorHelperData.html")
//    @ResponseBody
//    public ModelAndView getFileEditorHelperData(HttpServletRequest request,
//                                                HttpServletResponse response) throws Throwable {
//        ModelAndView mav = new ModelAndView();
//        String helperIndex = request.getParameter("helperIndex");
//        String targetId = request.getParameter("targetId");
//        String module = request.getParameter("module");
//        String pageCode = request.getParameter("pageCode");
//        if(StringUtils.isBlank(pageCode)) {
//            String refererUrl = request.getHeader("referer");
//            String[] refererUrlInfo = Arrays.copyOfRange(refererUrl.split("[/]+"), 2, refererUrl.split("[/]+").length);
//            module = refererUrlInfo[0];
//            pageCode = refererUrlInfo[1].substring(0, refererUrlInfo[1].indexOf(".html"));
//
//        }
//
//
//        logger.debug("request referer : {},{},{},{}", module, pageCode, targetId, helperIndex);
//        PageDescriptor pageInfo = WebContext.get().getPageInfo(module, pageCode);
//        Map<String, ComponentDescriptor> components = pageInfo.getComponents();
//
//        ComponentDescriptor fileComponentDescriptor = null;
//        for (ComponentDescriptor componentDescriptor : components.values()) {
//            DataSetDescriptor dataSetDescriptor = componentDescriptor.getDataSetDescriptor();
//            if(dataSetDescriptor != null && dataSetDescriptor.isHelperRuntime()) {
//                fileComponentDescriptor = componentDescriptor;
//                break;
//            }
//        }
//        String id = targetId.substring(targetId.indexOf("#")+ 1);
//
//        DataSetDescriptor dataSetDescriptor = fileComponentDescriptor.getDataSetDescriptor();
//        dataSetDescriptor.resetHelperInfo(true);
//        String helperDataXml = dataSetDescriptor.getHelperDataXml();
//        if(com.hframework.common.util.StringUtils.isBlank(helperDataXml)) helperDataXml = "<xml></xml>";
//        Element helperElement = Dom4jUtils.getDocumentByContent(helperDataXml).getRootElement();
//        DataSetContainer helperContainer = FileComponentInvoker.createRootContainer(fileComponentDescriptor, helperElement, module, true);
//
//        IDataSet targetDataGroups = helperContainer.getDataGroups().get(0).getElementMap().get(id);
//        List<DataSetGroup> dataGroups = ((DataSetContainer) targetDataGroups).getDataGroups();
//        ((DataSetContainer) targetDataGroups).setDataGroups(Lists.newArrayList(dataGroups.get(Integer.valueOf(helperIndex))));
//        mav.addObject("helpCompData", targetDataGroups);
//
//        JSONObject container = null;
//        JSONObject jsonObject = fileComponentDescriptor.getJson();
//        ModelAndView subResult = ServiceFactory.getService(DefaultController.class).
//                gotoPage(module, fileComponentDescriptor.getDataSetDescriptor().getDataSet().getCode() + "#",
//                        null, null, "true", request, response);
//        Object elements = subResult.getModelMap().get("elements");
//        for (Object o : ((Map) elements).values()) {
//            container = (JSONObject)o;
//            if(id.equals(container.getString("dataSet"))) {
//                break;
//            }
//        }
////        ModelAndView subResult = ServiceFactory.getService(DefaultController.class).
////                gotoPage(module, targetId,
////                        null, null, "true", request, response);
//        jsonObject.put("modelMap", subResult.getModelMap());
//        jsonObject.put("view", subResult.getViewName());
//        mav.addObject("container",container);
//        mav.setViewName("component/helperData");
//        return mav;
//    }

    @RequestMapping("/getHandlerIdByPathInfo")
    @ResponseBody
    public ResultData getHandlerIdByPathInfo(String programId, String path, String version){
        final ApiDescriptor handlerDescriptor = ApiManager.findHandlerDescriptorByPath(programId, path, version);
        return ResultData.success(new HashMap(){{
            put("id", handlerDescriptor.getId());
            put("pathEnding", handlerDescriptor.getName());

        }});
    }


    @RequestMapping("/dynSqlResultParse")
    @ResponseBody
    public ResultData dynSqlResultParse(String allValues, String clazz, String path){
        try {
            if(StringUtils.isNoneBlank(allValues)){
                JSONObject jsonObject = JSONObject.parseObject(allValues, Feature.OrderedField);
                List<Object> parameterValues = new ArrayList<>();
                String tableName = null;
                String dbName = null;
                String sql = null;
                for (String key : jsonObject.keySet()) {
                    if("sql".equals(key) && StringUtils.isNoneBlank(jsonObject.getString(key))) {
                        sql = calcDemoSql(jsonObject.getString(key));
                        parameterValues.add(sql);
                    }else if ("${PARAMETERS.VALUES.ARRAY}".equals(jsonObject.getString(key))){
                        parameterValues.add(null);
                    }else if ("${HANDLER.CONFIG.PARAMETERS}".equals(jsonObject.getString(key))){
                        parameterValues.add(null);
                    }else if ("${HANDLER.CONFIG.RESULTS}".equals(jsonObject.getString(key))){
                        parameterValues.add(null);
                    }else if ("${PARAMETERS.PAGE.NO:0}".equals(jsonObject.getString(key))){
                        parameterValues.add(1);
                    }else if ("${PARAMETERS.PAGE.Size:20}".equals(jsonObject.getString(key))){
                        parameterValues.add(1);
//                    }else if ("${DATASOURCE.DBS}".equals(jsonObject.getString(key))){
//                        parameterValues.add(jsonObject.getString(key));
//                        dbName = jsonObject.getString(key);
//                    }else if ("${DATASOURCE.TABLES}".equals(jsonObject.getString(key))){
//                        parameterValues.add(jsonObject.getString(key));
//                        tableName = jsonObject.getString(key);
                    }else if(StringUtils.isNoneBlank(jsonObject.getString(key))) {
                        parameterValues.add(jsonObject.getString(key));
                    }else {
                        parameterValues.add(null);
                    }
                }
                if(StringUtils.isNoneBlank(jsonObject.getString("tableName"))) {
                    List<Map<String, Object>> maps = DBClient.executeQueryMaps(jsonObject.getString("dbKey"), "show full columns from " + jsonObject.getString("tableName"), null);
                    final Map<String, String[]> struts = new LinkedHashMap<>();
                    for (Map<String, Object> map : maps) {
                        struts.put(String.valueOf(map.get("Field")),
                                new String[]{parseType(String.valueOf(map.get("Type"))),map.get("Comment") == null ? null: String.valueOf(map.get("Comment"))});
                    }
                    return ResultData.success(new HashMap(){{
                        put("out", struts);
                    }});
                }else if(StringUtils.isNoneBlank(jsonObject.getString("sql"))) {
                    final Map<String, String> result = DBClient.executeQueryStruts(jsonObject.getString("dbKey"), sql, null);
                    final Map<String, String[]> struts = new LinkedHashMap<>();
                    Map<String, String> columnComments = new HashMap<>();
                    Set<String> aboutTables = getTables(sql);
                    for (String aboutTable : aboutTables) {
                        List<Map<String, Object>> maps = DBClient.executeQueryMaps(jsonObject.getString("dbKey"), "show full columns from " + aboutTable, null);
                        for (Map<String, Object> map : maps) {
                            if(!columnComments.containsKey(String.valueOf(map.get("Field")))) {
                                columnComments.put(String.valueOf(map.get("Field")), String.valueOf(map.get("Comment")));
                                columnComments.put(JavaUtil.getJavaVarName(String.valueOf(map.get("Field"))), String.valueOf(map.get("Comment")));
                            }
                        }
                    }
                    for (String property : result.keySet()) {
                        String comment = columnComments.get(property);
                        if(comment == null) {
                            comment = columnComments.get(property.toLowerCase());
                        }
                        struts.put(property, new String[]{parseType(result.get(property)), comment});
                    }
                    return ResultData.success(new HashMap(){{
                        put("out", struts);
                    }});
                }else{
                        final SmartHandler smartHandler = (SmartHandler) ServiceFactory.getService(Class.forName(clazz));
                        Method method = SmartHandlerFactory.getHandler(Class.forName(clazz))[0].getMethod();
                        Object returnObject = HandlerHelper.handle(smartHandler, method, parameterValues.toArray(new Object[0]));
                        List<Map<String, Object>> maps = Object2MapHelper.transformAllToMapStruts(returnObject);
                        if(maps.size() > 0) {
                            final Map<String, String[]> struts = new LinkedHashMap<>();
                            Map<String, Object> propertiesMap = maps.get(0);
                            for (String property : propertiesMap.keySet()) {
                                struts.put(property, new String[]{parseType(propertiesMap.get(property)), null});
                            }
                            return ResultData.success(new HashMap(){{
                                put("out", struts);
                            }});
                        }
                    }
                }
        }catch (Exception e) {
            e.printStackTrace();
            throw new SmartHandlerException(APIErrorType.SERVER_ERROR);
        }
        return ResultData.success();
    }

    private String parseType(Object o) {
        return o.getClass().getName();
    }

    public static String parseType(String type) {
        if(type.matches(".*\\([0-9]+\\)")) {
            type = type.replaceAll("\\([0-9]+\\)", "");
        }
        if(type == null) return null;
        if(type.toUpperCase().contains("CHAR") || type.toUpperCase().startsWith("TEXT")) {
            return "string";
        }else if(type.toUpperCase().matches("TINYINT\\(1\\)")) {
            return "boolean";
        }else if(type.toUpperCase().contains("INT")) {
            return "int";
        }else if(type.toUpperCase().contains("TIMESTAMP") || type.toUpperCase().contains("DATETIME")) {
            return "date";
        }else if(type.toUpperCase().startsWith("DECIMAL") || type.toUpperCase().startsWith("DOUBLE") || type.toUpperCase().startsWith("FLOAT")) {
            return "float";
//        }else if(type.startsWith("INT")) {
//            return "init";
        }else {
            throw new RuntimeException("类型不支持：" + type);
        }
    }

    public static void main(String[] args) {
        System.out.println(calcDemoSql("SELECT * FROM period_money_out_v2_peacock_data t WHERE t.user_id = ? and p2p_Id = ? and status = 1 order by t.logdate desc limit 12,2"));
        System.out.println(calcDemoSql("SELECT * FROM period_money_out_v2_peacock_data t WHERE t.user_id = ? order by t.logdate desc limit 12,2"));
        System.out.println(calcDemoSql("SELECT * FROM period_money_out_v2_peacock_data t WHERE t.user_id = ? order by t.logdate desc"));
        System.out.println(calcDemoSql("SELECT * FROM period_money_out_v2_peacock_data t WHERE t.user_id = ?"));
        System.out.println(calcDemoSql("SELECT * FROM period_money_out_v2_peacock_data t WHERE status = 1"));
        System.out.println(calcDemoSql("SELECT * FROM period_money_out_v2_peacock_data t order by t.logdate desc"));
        System.out.println(calcDemoSql("SELECT * FROM period_money_out_v2_peacock_data t"));
        System.out.println(calcDemoSql("SELECT * FROM period_money_out_v2_peacock_data t WHERE t.user_id = ? limit ?, ? "));
        System.out.println(getTables("SELECT * FROM period_money_out_v2_peacock_data t WHERE t.user_id = ? and p2p_I"));
        System.out.println(getTables("SELECT\n" +
                "  dlr.deal_id,\n" +
                "  SUM(CASE WHEN dlr.type=1 THEN dlr.money ELSE 0 END) AS principalRepayMoney,\n" +
                "  d.name        AS dealName,\n" +
                "  d.repay_time  AS dealRepayTime,\n" +
                "  SUM(dlr.money) AS totalRepayMoney,\n" +
                "  SUM(CASE WHEN dlr.type!=1 THEN dlr.money ELSE 0 END) AS profitRepayMoney,\n" +
                "  (CASE WHEN dlr.status=0 THEN dlr.time ELSE dlr.real_time END) AS repayTime,\n" +
                "  dlr.real_time\n" +
                "FROM `gold_deal_loan_repay` dlr,\n" +
                "  `gold_deal` d\n" +
                "LIMIT 1;"));
    }

    public static Set<String> getTables(String sql){
        sql = sql.toLowerCase().trim().replaceAll("`", "");
        String tableFormat = "[a-z0-9_]+(\\s*(as)?\\s*[a-z0-9_]+)?";

        Set<String> tables = new LinkedHashSet<>();
        String[] fromParts = RegexUtils.find(sql, "\\s+from\\s+" + tableFormat + "(\\s*,\\s*" + tableFormat + ")*");
        for (String fromPart : fromParts) {
            String[] tableInfos = fromPart.trim().substring(4).trim().split(",");
            for (String tableInfo : tableInfos) {
                tables.add(tableInfo.trim().split("\\s+")[0]);
            }
        }

        String[] joinParts = RegexUtils.find(sql, "\\s+join\\s+" + tableFormat);
        for (String joinPart : joinParts) {
            tables.add(joinPart.trim().substring(4).trim().split("\\s+")[0]);
        }
        return tables;
    }

    public static String calcDemoSql(String sql) {
//        sql = sql.toLowerCase().trim();
        /*
        sql = sql.trim()
                .replaceAll("(?i)AND", "and")
                .replaceAll("(?i)OR", "or")
                .replaceAll("(?i)WHERE", "where")
                .replaceAll("(?i)LIMIT", "limit")
                .replaceAll("(?i)in[ ]+\\([ ]?\\?[ ]?\\)", "=\\?");
        sql = sql.replaceAll("((and)|(or)) [^\\?]*\\?", "");
        sql = sql.replaceAll("(where)[^\\?]*\\?[ ]*((and)|(or ))", "where ");
        sql = sql.replaceAll("(where)[^\\?]*\\?[ ]*", "");
//                System.out.println(sql);
//                System.out.println(sql.matches(".*where.*=.*"));
        if (!sql.matches(".*where.*=.*") && sql.contains("where")) {
            sql = sql.replaceAll("where", " ");
        }
        sql = sql.replaceAll("limit[ ]* [,0-9? ]+", "limit 1 ");
        if (!sql.matches(".*limit 1 ")) {
            sql += " limit 1";
        }
         */
        sql = sql.replaceAll("(\\#|\\$)+\\{[^\\{\\}]+\\}", "?");
        sql = sql.replaceAll("( (limit|LIMIT) )[,0-9?*\\-\\+ ]+", " limit 1 ");//limit (${pageNo} - 1) * ${pageSize}, ${pageSize}

        return sql.replaceAll("\\?", "null");
    }
}
