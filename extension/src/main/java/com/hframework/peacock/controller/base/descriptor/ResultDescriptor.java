package com.hframework.peacock.controller.base.descriptor;

import com.beust.jcommander.internal.Lists;
import com.google.common.collect.Sets;
import com.hframework.common.util.collect.CollectionUtils;
import com.hframework.common.util.collect.bean.Fetcher;
import com.hframework.peacock.controller.base.dc.*;
import com.hframework.smartweb.bean.SmartFormatter;
import com.hframework.smartweb.bean.apiconf.Result;
import com.hframework.smartweb.bean.handler.ResultHelper;
import com.hframework.peacock.controller.base.HelperRegistry;
import org.apache.commons.lang3.StringUtils;

import java.text.ParseException;
import java.util.*;

/**
 * Created by zhangquanhong on 2017/11/16.
 */
public class ResultDescriptor {

    private Result result;
    private String name;
    private String path;
    private String alias;
    private String defaultValue;
    private String pattern;

    private Class<? extends SmartFormatter> formatter;


    private List<ResultDescriptor> subResult;
    private List<ResultDescriptor> parentResult;

    private boolean isEmpty = true;


    public ResultDescriptor(List<Result> resultList, String parentPath) {
        if(StringUtils.isNoneBlank(parentPath)) {
            parentPath = ResultTreeDescriptor.trimPath(parentPath);
            Result packageResult = new Result();
            packageResult.setPath(parentPath);
            packageResult.setResultList(resultList);
            resultList = Lists.newArrayList(packageResult);
        }
        if(resultList != null && resultList.size() > 0) {
            isEmpty = false;
            if(resultList.size() == 1) {
                Result result = resultList.get(0);
                String dbAliasName = HandlerDescriptor.parseDBAliasName(result.getName());
                if(StringUtils.isNoneBlank(dbAliasName)) {
                    name = dbAliasName;
                }else {
                    name = result.getName();
                }
                if(StringUtils.isNotBlank(name) && name.endsWith("[]")) name = name.substring(0, name.length() - 2);

                path = result.getPath();
                alias = result.getAlias();
                defaultValue = result.getDefaultValue();
                String formatter = result.getFormatter();
                if(StringUtils.isNoneBlank(formatter)) {
                   this.formatter = HelperRegistry.getFormatter(formatter);
                }
                pattern = result.getPattern();
                this.result = resultList.get(0);
                resultList = result.getResultList();
            }
            if(resultList != null && resultList.size() > 0) {
                for (Result sub : resultList) {
                    if(isParentPrefix(sub.getName())) {
                        if(parentResult == null) parentResult = new ArrayList<>();
                        parentResult.add(new ResultDescriptor(sub));
                    }else {
                        if(subResult == null) subResult = new ArrayList<>();
                        subResult.add(new ResultDescriptor(sub));
                    }

                }
            }
        }
    }
    public ResultDescriptor(Result result) {
        this(Lists.newArrayList(result),  null);
    }


    public void removeUnusedField(DC responseData) throws IllegalAccessException, ParseException, InstantiationException {
        if(StringUtils.isNoneBlank(alias)) {
            removeUnusedAttr(responseData, Sets.<String>newHashSet(alias));
        }

    }
    public void formatResult(DC responseData) throws Exception {
        if(responseData == null) return ;
        if(subResult != null && subResult.size() > 0) {
            List<String> keepAttr = CollectionUtils.fetch(subResult, new Fetcher<ResultDescriptor, String>() {
                @Override
                public String fetch(ResultDescriptor resultDescriptor) {
                    String name =StringUtils.isNoneBlank(resultDescriptor.getAlias()) ? resultDescriptor.getAlias() : resultDescriptor.getName();
                    return isParentPrefix(name) ? name.substring(3) : name;
                }
            });

            keepAttr.add("NEXT_PAGINATION_FLAT");
            Set<String> keepAttrSet = new HashSet<>(keepAttr);
            if(StringUtils.isNoneBlank(name)) {
                responseData.parseBeanToMap(name);
                DC childDC = DCUtils.valueOf(responseData.fetch(name));
                for (ResultDescriptor resultDescriptor : subResult) {
                    resultDescriptor.formatResult(childDC);
                }
                childDC.keepFields(keepAttrSet);

//                for (Map<String, Object> objectMap : responseData) {
//                    //将Bean对象转化为Map对象
//                    List<Map<String, Object>> subData = Object2MapHelper.transformAllToMapStruts(objectMap.get(name));
//                    if(subData != null && subData.size() > 0) {
//                        if(objectMap.get(name) instanceof List || objectMap.get(name).getClass().isArray()) {
//                            objectMap.put(name, subData);
//                        }else {
//                            objectMap.put(name, subData.get(0));
//                        }
//                    }
//                    for (ResultDescriptor resultDescriptor : subResult) {
//                        resultDescriptor.formatResult(subData);
//                    }
//                    removeUnusedAttr(subData, keepAttrSet);
//                }
            }else {
                for (ResultDescriptor resultDescriptor : subResult) {
                    resultDescriptor.formatResult(responseData);
                }
                responseData.keepFields(keepAttrSet);
//                removeUnusedAttr(responseData, keepAttrSet);

            }
        }else if(result != null && StringUtils.isNoneBlank(name)){
            doFormat(responseData,
                    isParentPrefix(name) ? name.substring(3): name,
                    isParentPrefix(alias) ? alias.substring(3): alias,
                    true, defaultValue, formatter, pattern, new HashSet<String>());
        }

        if(parentResult != null && parentResult.size() > 0) {
            for (ResultDescriptor resultDescriptor : parentResult) {
                resultDescriptor.formatResult(responseData.getPrevDc());
            }
            List<String> keepAttr = CollectionUtils.fetch(parentResult, new Fetcher<ResultDescriptor, String>() {
                @Override
                public String fetch(ResultDescriptor resultDescriptor) {
                    String name =StringUtils.isNoneBlank(resultDescriptor.getAlias()) ? resultDescriptor.getAlias() : resultDescriptor.getName();
                    return isParentPrefix(name) ? name.substring(3) : name;
                }
            });

            Set<String> keepAttrSet = new HashSet<>(keepAttr);
            responseData.getPrevDc().keepFields(keepAttrSet);
        }
    }

    public boolean isParentPrefix(String name) {
        return name != null && name.startsWith("../");
    }

    private void doFormat(DC responseData, String name, String alias, boolean b, String defaultValue, Class<? extends SmartFormatter> formatter, String pattern, HashSet<String> strings) throws Exception {
        if(DCUtils.isMapDC(responseData)) {
            ResultHelper.doSmartAliasAndFormat(((MapDC)responseData).getData(), name, alias, b,
                    defaultValue, formatter, pattern, new HashSet<String>());
        }else if(DCUtils.isListDC(responseData)) {
            for (Map<String, Object> map : ((ListDC) responseData).getData()) {
                ResultHelper.doSmartAliasAndFormat(map, name, alias, b,
                        defaultValue, formatter, pattern, new HashSet<String>());
            }
        }else if(DCUtils.isGroupDC(responseData)) {
            for (DC dc : ((GroupDC) responseData).getData()) {
                doFormat(dc, name, alias, b, defaultValue, formatter, pattern, strings);
            }
        }
    }


    public void removeUnusedAttr(DC responseData, Set<String> keepAttrSet) {
        responseData.keepFields(keepAttrSet);
//        for (Map<String, Object> map : responseData) {
//            Iterator<String> keyIter = map.keySet().iterator();
//            while (keyIter.hasNext()){
//                String key = keyIter.next();
//                if(!keepAttrSet.contains(key)) {
//                    keyIter.remove();
//                }
//            }
//        }
    }

    public String getPath() {
        return path;
    }

    public boolean isEmpty() {
        return isEmpty;
    }

    public String getName() {
        return name;
    }

    public String getAlias() {
        return alias;
    }

    public List<Result> getItems(){
        if(subResult != null && subResult.size() > 0) {
            return CollectionUtils.fetch(subResult, new Fetcher<ResultDescriptor, Result>() {
                @Override
                public Result fetch(ResultDescriptor resultDescriptor) {
                    return resultDescriptor.result;
                }
            });
        }else {
            return Lists.newArrayList(result);
        }

    }
}

