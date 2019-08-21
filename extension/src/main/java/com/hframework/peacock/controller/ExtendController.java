package com.hframework.peacock.controller;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.parser.Feature;
import com.google.common.collect.Lists;
import com.hframework.base.bean.KVBean;
import com.hframework.beans.controller.ResultCode;
import com.hframework.beans.controller.ResultData;
import com.hframework.common.util.RegexUtils;
import com.hframework.common.util.StringUtils;
import com.hframework.common.util.UrlHelper;
import com.hframework.common.util.collect.CollectionUtils;
import com.hframework.common.util.collect.bean.Fetcher;
import com.hframework.common.util.collect.bean.Mapping;
import com.hframework.peacock.config.domain.model.*;
import com.hframework.peacock.config.service.interfaces.*;
import com.hframework.smartweb.APIErrorType;
import com.hframework.smartweb.exception.SmartHandlerException;
import com.hframework.peacock.controller.bean.apiconf.Api;
import com.hframework.peacock.controller.bean.apiconf.Node;
import com.hframework.peacock.controller.bean.apiconf.Parameter;
import com.hframework.peacock.controller.xstream.ext.XStreamUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.*;

/**
 * User: zhangqh6
 * Date: 2016/5/11 0:16:16
 */
@Controller
@RequestMapping(value = "/extend")
public class ExtendController {
    private static final Logger logger = LoggerFactory.getLogger(ExtendController.class);

    @Resource
    private ICfgDatasouceMysqlSV cfgDatasourceSV;

    @Resource
    private IThirdDomainParameterSV parameterSV;

    @Resource
    private IThirdPublicRuleSV ruleSV;

    @Resource
    private IThirdDomainSV domainSV;

    @Resource
    private IThirdApiSV apiSV;

    private static final Map<Integer, String> typeMapping = new HashMap<>();
    static {
        typeMapping.put(1, "string");
        typeMapping.put(2, "int");
        typeMapping.put(3, "float");
        typeMapping.put(4, "date");
        typeMapping.put(5, "boolean");
        typeMapping.put(6, "int[]");
        typeMapping.put(7, "string[]");

    }

    /**
     * 获取第三方支持的域名
     * @param dataCondition
     * @return
     */
    @RequestMapping(value = "/getThirdApiDomains.json")
    @ResponseBody
    public ResultData getThirdApiDomains(@ModelAttribute("dataCondition") final String dataCondition){
        logger.debug("dataCondition : {}", dataCondition);
        try{

            ThirdDomain_Example domainExample = new ThirdDomain_Example();
            List<ThirdDomain> domains = domainSV.getThirdDomainListByExample(domainExample);
            List<KVBean> result = CollectionUtils.fetch(domains, new Fetcher<ThirdDomain, KVBean>() {
                @Override
                public KVBean fetch(ThirdDomain thirdDomain) {
                    KVBean kvBean = new KVBean();
                    kvBean.setValue(String.valueOf(thirdDomain.getId()));
                    kvBean.setText(thirdDomain.getName() + "[" + thirdDomain.getUrl() + "]");
                    return kvBean;
                }
            });

            return ResultData.success(result);
        }catch (Exception e) {
            logger.error("error : ", e);
            return ResultData.error(ResultCode.ERROR);
        }
    }

    /**
     * 获取第三方域名下的API
     * @param dataCondition
     * @return
     */
    @RequestMapping(value = "/getThirdApis.json")
    @ResponseBody
    public ResultData getThirdApi(@ModelAttribute("dataCondition") final String dataCondition){
        logger.debug("dataCondition : {}", dataCondition);
        try{
            Long domainId = Long.valueOf(dataCondition.substring(dataCondition.indexOf("domain_id") + "domain_id".length() + 1));
            ThirdApi_Example apiExample = new ThirdApi_Example();
            apiExample.createCriteria().andDomainIdEqualTo(domainId).andStatusEqualTo((byte) 1);
            List<ThirdApi> apis = apiSV.getThirdApiListByExample(apiExample);
            List<KVBean> result = CollectionUtils.fetch(apis, new Fetcher<ThirdApi, KVBean>() {
                @Override
                public KVBean fetch(ThirdApi api) {
                    KVBean kvBean = new KVBean();
                    kvBean.setValue(String.valueOf(api.getPath()));
                    kvBean.setText(api.getName() + "[" + api.getPath() + "]");
                    return kvBean;
                }
            });

            return ResultData.success(result);
        }catch (Exception e) {
            logger.error("error : ", e);
            return ResultData.error(ResultCode.ERROR);
        }
    }
    /**
     * 获取第三方API下响应schema
     * @param dataCondition
     * @return
     */
    @RequestMapping(value = "/getThirdApiSchema.json")
    @ResponseBody
    public ResultData getThirdApiSchema(@ModelAttribute("dataCondition") final String dataCondition){
        logger.debug("dataCondition : {}", dataCondition);
        try{
            Map<String, String> urlParameters = UrlHelper.getUrlParameters("?" + dataCondition.replaceAll("[ ]+", ""), false);
            Long domainId = Long.valueOf(urlParameters.get("domain_id"));
            String apiPath = urlParameters.get("api_path");
            ThirdApi_Example apiExample = new ThirdApi_Example();
            apiExample.createCriteria().andDomainIdEqualTo(domainId).andStatusEqualTo((byte) 1).andPathEqualTo(apiPath);
            List<ThirdApi> apis = apiSV.getThirdApiListByExample(apiExample);
            String apiXml = apis.get(0).getContent();
            Api api = XStreamUtil.readValue(apiXml, Api.class);
            Set<String> schemas = new LinkedHashSet<>();
            schemas.add("/");
            if(api.getResponse().getNode() != null) {
                List<Node> nodeList = api.getResponse().getNode().getNodeList();
                for (Node node : nodeList) {
                    String path = node.getPath();
                    if(path.contains("/") && path.length() > 1) {
                        schemas.add(path.substring(0, path.lastIndexOf("/")));
                    }
                }
            }

            return ResultData.success(CollectionUtils.fetch(Lists.newArrayList(schemas), new Fetcher<String, KVBean>() {
                @Override
                public KVBean fetch(String s) {
                    KVBean kvBean = new KVBean();
                    kvBean.setValue(s);
                    kvBean.setText(s);
                    return kvBean;
                }
            }));
        }catch (Exception e) {
            logger.error("error : ", e);
            return ResultData.error(ResultCode.ERROR);
        }
    }

    @RequestMapping("/dynThirdApiResultParse")
    @ResponseBody
    public ResultData dynThirdApiResultParse(String allValues){
        try {

            if(org.apache.commons.lang3.StringUtils.isNoneBlank(allValues)){
                JSONObject jsonObject = JSONObject.parseObject(allValues, Feature.OrderedField);

                Long domainId = jsonObject.getLong("domainId");
                String apiPath = jsonObject.getString("apiPath");
                String schema = jsonObject.getString("schema");
//                schema = schema.startsWith("/") ? schema.substring(1) : schema;
                ThirdApi_Example apiExample = new ThirdApi_Example();
                apiExample.createCriteria().andDomainIdEqualTo(domainId).andStatusEqualTo((byte) 1).andPathEqualTo(apiPath);
                List<ThirdApi> apis = apiSV.getThirdApiListByExample(apiExample);
                String apiXml = apis.get(0).getContent();
                Api api = XStreamUtil.readValue(apiXml, Api.class);
                final Map<String, String[]> inputStruts = new LinkedHashMap<>();
                if(api.getRequest().getParameters() != null) {
                    List<Parameter> parameterList = api.getRequest().getParameters().getParameterList();
                    for (Parameter histParameter : parameterList) {
                        if(histParameter.isPublic()) continue;
                        inputStruts.put(histParameter.getCode(),
                                new String[]{typeMapping.get(Integer.valueOf(histParameter.getType())), histParameter.getName() ,
                                        histParameter.getRequired() == null? "false": histParameter.getRequired()});
                    }
                }

                if(api.getRequest().getNode() != null) {
                    List<Node> nodeList = api.getRequest().getNode().getNodeList();
                    for (Node node : nodeList) {
                        if(node.isPublic()) continue;
                        String tPath = node.getPath();
                        if(tPath.startsWith(schema) && !tPath.equals(schema)) {
                            inputStruts.put(tPath.substring(schema.length() + (schema.length() > 1 ? 1: 0)).replaceAll("/","_"),
                                    new String[]{typeMapping.get(Integer.valueOf(node.getType())), node.getName() , "false"});
                        }
                    }
                }



                final Map<String, String[]> outputStruts = new LinkedHashMap<>();
                if(api.getResponse().getNode() != null) {
                    List<Node> nodeList = api.getResponse().getNode().getNodeList();
                    for (Node node : nodeList) {
                        String tPath = node.getPath();
                        if(tPath.startsWith(schema) && !tPath.equals(schema)) {
                            outputStruts.put(tPath.substring(schema.length() + (schema.length() > 1 ? 1: 0)), new String[]{typeMapping.get(Integer.valueOf(node.getType())), node.getName()});
                        }
                    }
                }
                return ResultData.success(new HashMap(){{
                    put("in", inputStruts);
                    put("out", outputStruts);
                }});

            }
        }catch (Exception e) {
            e.printStackTrace();
            throw new SmartHandlerException(APIErrorType.SERVER_ERROR);
        }
        return ResultData.success();
    }




    /**
     * 获取第三方接口变量与规则
     * @param dataCondition
     * @return
     */
    @RequestMapping(value = "/getParamValOpts.json")
    @ResponseBody
    public ResultData getThirdApiParameterAndRules(@ModelAttribute("dataCondition") final String dataCondition){
        logger.debug("dataCondition : {}", dataCondition);
        try{
            Long domainId = Long.valueOf(dataCondition.substring(dataCondition.indexOf("domain_id") + "domain_id".length() + 1));
            ThirdDomainParameter_Example parameterExample = new ThirdDomainParameter_Example();
            parameterExample.createCriteria().andDomainIdEqualTo(domainId);
            List<ThirdDomainParameter> parameterList = parameterSV.getThirdDomainParameterListByExample(parameterExample);
            ThirdPublicRule_Example ruleExample = new ThirdPublicRule_Example();
            ruleExample.createCriteria().andDomainIdEqualTo(domainId);
            List<ThirdPublicRule> ruleList = ruleSV.getThirdPublicRuleListByExample(ruleExample);

            List<KVBean> result = CollectionUtils.fetch(parameterList, new Fetcher<ThirdDomainParameter, KVBean>() {
                @Override
                public KVBean fetch(ThirdDomainParameter thirdDomainParameter) {
                    KVBean kvBean = new KVBean();
                    kvBean.setText("变量:{" + thirdDomainParameter.getName() + "}");
                    kvBean.setValue("#val{" + thirdDomainParameter.getCode() + "}");
                    return kvBean;
                }
            });
            result.addAll(CollectionUtils.fetch(ruleList, new Fetcher<ThirdPublicRule, KVBean>() {
                @Override
                public KVBean fetch(ThirdPublicRule thirdPublicRule) {
                    KVBean kvBean = new KVBean();
                    kvBean.setText("规则:{" + thirdPublicRule.getName() + "}");
                    kvBean.setValue("#rule{" + thirdPublicRule.getCode() + "}");
                    return kvBean;
                }
            }));
            return ResultData.success(result);
        }catch (Exception e) {
            logger.error("error : ", e);
            return ResultData.error(ResultCode.ERROR);
        }
    }

    /**
     * 获取表列表
     * @param dataCondition
     * @return
     */
    @RequestMapping(value = "/getDbTables.json")
    @ResponseBody
    public ResultData dictionary(@ModelAttribute("dataCondition") final String dataCondition){
        logger.debug("request : {}", dataCondition);
        try{

            Map<String, String> connectInfoMap = parseConnectInfo(dataCondition);
            CfgDatasouceMysql cfgDatasource = getMysqlDataSource(connectInfoMap);

            if(cfgDatasource == null) {
                return ResultData.error(ResultCode.get("1111", "数据库连接不存在!"));
            }

            List tables = DBUtils.query(cfgDatasource, "show tables");
            Set<String> splitTables  = new HashSet<>();
            for (Object table : tables) {
                String tableName = (String) ((Map) table).values().iterator().next();
                tableName = getSplitTableName(tableName);
                if(StringUtils.isNotBlank(tableName)) {
                    splitTables.add(tableName);
                }
            }


            List <KVBean> kvBeans = CollectionUtils.from(tables, new Mapping() {
                @Override
                public KVBean from(Object table) {
                    KVBean kvBean = new KVBean();
                    String next = (String) ((Map) table).values().iterator().next();
                    kvBean.setText(next);
                    kvBean.setValue(next);
                    return kvBean;
                }
            });

            for (String splitTable : splitTables) {
                KVBean kvBean = new KVBean();
                kvBean.setText(splitTable);
                kvBean.setValue(splitTable);
                kvBeans.add(kvBean);
            }
            return ResultData.success(kvBeans);
        }catch (Exception e) {
            logger.error("error : ", e);
            return ResultData.error(ResultCode.ERROR);
        }
    }

    private CfgDatasouceMysql getMysqlDataSource(Map<String, String> connectInfoMap) throws Exception {
        CfgDatasouceMysql cfgDatasource = null;
        if(connectInfoMap.containsKey("mysql_id")) {
            String cfgDatasourceId = connectInfoMap.get("mysql_id");
            if(StringUtils.isNotBlank(cfgDatasourceId)) {
                cfgDatasource = cfgDatasourceSV.getCfgDatasouceMysqlByPK(Integer.valueOf(cfgDatasourceId.trim()));
            }
        }

        if(connectInfoMap.containsKey("mysql_key")) {
            String cfgDatasourceId = connectInfoMap.get("mysql_key");
            if(StringUtils.isNotBlank(cfgDatasourceId)) {
                CfgDatasouceMysql_Example example = new CfgDatasouceMysql_Example();
                example.createCriteria().andRemarkEqualTo(cfgDatasourceId.trim());
                List<CfgDatasouceMysql> datasourceList = cfgDatasourceSV.getCfgDatasouceMysqlListByExample(example);
                if(datasourceList.size() > 0) {
                    cfgDatasource = datasourceList.get(0);
                }
            }
        }
        return cfgDatasource;
    }

    private String getSplitTableName(String tableName) {
        String[] strings = RegexUtils.find(tableName, "_[0-9]+");
        if(strings != null && strings.length > 0) {
            String endChars = strings[strings.length-1];
            tableName = tableName.substring(0, tableName.length() - endChars.length()) + "_.*";
            return tableName;
        }
        return null;
    }

    /**
     * 获取表列表
     * @param dataCondition
     * @return
     */
    @RequestMapping(value = "/getDbColumns.json")
    @ResponseBody
    public ResultData getDbColumns(@ModelAttribute("dataCondition") final String dataCondition)  {
        try{
            Map<String, String> connectInfoMap = parseConnectInfo(dataCondition);
            List<Map> dbColumns = getDbColumns(connectInfoMap);
            return ResultData.success(CollectionUtils.from(dbColumns, new Mapping<Map, KVBean>() {
                @Override
                public KVBean from(Map map) {
                    KVBean kvBean = new KVBean();
                    kvBean.setText(String.valueOf(map.get("field")));
                    kvBean.setValue(String.valueOf(map.get("field")));
                    return kvBean;
                }
            }));
        }catch (Exception e) {
            logger.error("error : ", e);
            return ResultData.error(ResultCode.ERROR);
        }
    }

    private Map<String, String> parseConnectInfo(String dataCondition) {
        Map<String, String> connectInfoMap = new HashMap<>();
        if(StringUtils.isNotBlank(dataCondition)) {
            String[] conditions = dataCondition.split("&&");
            for (String condition : conditions) {
                if(StringUtils.isNotBlank(condition)) {
                    String key = condition.substring(0, condition.indexOf("=")).trim();
                    String value = condition.substring(condition.indexOf("=") + 1).trim();
                    connectInfoMap.put(key, value);
                }
            }
        }

        return connectInfoMap;
    }


    private List<Map> getDbColumns(Map<String, String> connectInfoMap) throws Exception {

        if(!connectInfoMap.containsKey("table")) {
            return new ArrayList<>();
        }

        String dbObjectName = connectInfoMap.get("table");

        CfgDatasouceMysql cfgDatasource = getMysqlDataSource(connectInfoMap);
        if (cfgDatasource == null) {
            logger.error("数据库连接不存在");
            return new ArrayList<>();
        }

        if(dbObjectName.endsWith("_.*")) {//表明为分表
            List tables = DBUtils.query(cfgDatasource, "show tables");
            for (Object table : tables) {
                String tableName = (String) ((Map) table).values().iterator().next();
                String tableNameTarget = getSplitTableName(tableName);
                if(StringUtils.isNotBlank(tableNameTarget) && dbObjectName.equals(tableNameTarget)) {
                    dbObjectName = tableName;
                    break;
                }
            }
        }

        final List<Map> columns = DBUtils.query(cfgDatasource, "show columns from " + dbObjectName);
        return columns;
    }
}
