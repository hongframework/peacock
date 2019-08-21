package com.hframework.peacock.controller.base.descriptor;

import com.beust.jcommander.internal.Lists;
import com.hframework.common.util.collect.CollectionUtils;
import com.hframework.common.util.collect.bean.Fetcher;
import com.hframework.peacock.controller.base.dc.*;
import com.hframework.smartweb.bean.apiconf.Result;
import com.hframework.peacock.controller.base.ApiInvokeData;
import com.hframework.peacock.controller.base.ApiManager;
import org.apache.commons.lang3.StringUtils;

import java.util.*;

/**
 * Created by zhangquanhong on 2017/11/16.
 */
public class ResultTreeDescriptor {

    public static final String ROOT_PATH = "/";
    public static final String ROOT_NAME = "";

    private String path;
    private String name;

    public ResultTreeDescriptor parent;

    public Map<String, ResultTreeDescriptor> children;

    public Set<HandlerDescriptor> data;

    public ResultTreeDescriptor(String path, String name) {

        this.path = trimPath(path);
        this.name = name;
    }


    public void add(HandlerDescriptor handlerDescriptor) {
        ResultDescriptor result = handlerDescriptor.getResult();
        if(result.isEmpty()) return;
        String path = result.getPath();
        if(StringUtils.isBlank(path) || ROOT_PATH.equals(path)) {
            this.addData(handlerDescriptor);
        }else{
            this.addData(path, handlerDescriptor);
        }
    }

    public static String trimPath(String path) {
        if(path == null) path = "";
        path = ("/" + path).replaceAll("//+", "/");
        if(path.endsWith("/")) path.substring(0, path.length() - 1);
        return path;
    }

    public String[] getPathComponents(String path) {
        path = trimPath(path);
        String[] split = path.split("/");
        return split.length > 0 ? Arrays.copyOfRange(split, 1, split.length) : split;
    }

    public void addData(String targetPath, HandlerDescriptor handlerDescriptor) {
        targetPath = trimPath(targetPath);
        if(path.equals(targetPath)){
            addData(handlerDescriptor);
        }else if (targetPath.startsWith(path.equals(ROOT_PATH) ? path : (path + "/"))){
            getChildOrCreate(targetPath).addData(handlerDescriptor);
        }else if (path.startsWith(targetPath + "/")){
            getParent().addData(handlerDescriptor);
        }else {
            getParent().addData(handlerDescriptor);
        }
    }

    private ResultTreeDescriptor getParent() {
        return this.parent;
    }

    private ResultTreeDescriptor getChildOrCreate(String targetPath) {
        String relativePath = targetPath.substring(path.equals(ROOT_PATH) ? path.length() :(path.length() + 1));
        String child = relativePath.contains("/") ? relativePath.substring(0, relativePath.indexOf("/")) : relativePath;
        if(children == null) children = new LinkedHashMap<>();
        if(!children.containsKey(child)) {
            children.put(child, new ResultTreeDescriptor(this.path + "/" + child,""));
        }
        ResultTreeDescriptor result = children.get(child);
        if(!relativePath.equals(child)) {//如果是多层次结构，则需要递归获取（或创建）
            result = result.getChildOrCreate(targetPath);
        }
        return result;
    }

    public void addData(HandlerDescriptor resultDescriptor) {
        if(data == null) data = new LinkedHashSet<>();
        data.add(resultDescriptor);
    }

    /**
     * 打包时，需要按照层级进行打包，从上往下
     * @param handleResult
     */
    public DC packaging(Map<HandlerDescriptor, ApiInvokeData.HandleSwapData> handleResult) {

        //子节点优先组装，递归向上
        DC resultDC = NullDC.SINGLETON;
        DC prevDc = NullDC.SINGLETON;
        if(children != null) {
            for (ResultTreeDescriptor child : children.values()) {
                final DC result = child.packaging(handleResult);
                final String fieldName = child.path.substring(child.path.lastIndexOf("/") + 1);

                if(DCUtils.isGroupDC(result)) {//这里是否存在两种情况，一种是真实的GroupDC，需要如此处理，另外一种是内部处理失误，将ListDC处理成为了GroupDC？
                    List<Map<String, Object>> list = new ArrayList<>();
                    for (final DC itemDC : ((GroupDC) result).getData()) {
                        prevDc = DCUtils.mergeDC(prevDc, itemDC.getPrevDc(), true);
                        list.add(new HashMap<String, Object>(){{
                            put(fieldName, itemDC);
                        }});
                    }
                    ListDC listDC = new ListDC(list);
                    resultDC = DCUtils.mergeDCSimple(resultDC, listDC, true);
                }else if(DCUtils.isListDC(result) || DCUtils.isMapDC(result)){
                    prevDc = DCUtils.mergeDC(prevDc, result.getPrevDc(), true);
                    MapDC mapDC = new MapDC(new HashMap<String, Object>() {{
                        put(fieldName, result.getData());
                    }});
                    resultDC = DCUtils.mergeDCSimple(resultDC, mapDC, true);
                }

            }
        }

        //如果子的DC包含PrevDC，这里直接合并到当前层次
        if(!DCUtils.isNullDC(prevDc)) {
            resultDC = DCUtils.mergeDC(resultDC, prevDc, false);
        }

        if(data != null && !data.isEmpty()) {
            for (HandlerDescriptor handlerDescriptor : data) {
                ApiInvokeData.HandleSwapData handleSwapData = handleResult.get(handlerDescriptor);
                if(handleSwapData == null) continue;
                DC responseData = handleSwapData.getResponseData();
                resultDC = DCUtils.mergeDC(resultDC, responseData, false);
            }
        }

//        System.out.println(resultDC);

        if(DCUtils.isListDC(resultDC)) {
            List<Map<String, Object>> data = ((ListDC) resultDC).getData();
            if(!data.isEmpty() && data.get(data.size() - 1).containsKey("NEXT_PAGINATION_FLAT")) {
                data.remove(data.size() - 1);
                for (Map<String, Object> datum : data) {
                    datum.remove("NEXT_PAGINATION_FLAT");
                }
            }

        }

        return resultDC;
    }



    /**
     * @deprecated
     * 已经由DC packaging(Map<HandlerDescriptor, ApiInvokeData.HandleSwapData> handleResult)取代
     * 打包时，需要按照层级进行打包，从上往下
     * @param result
     * @param handleResult
     */
    public boolean packaging(List<Map<String, Object>> result, Map<HandlerDescriptor, ApiInvokeData.HandleSwapData> handleResult) {

        DataContainer dataContainer = getOrCreate(result, this.path);

        if(data != null) {
            for (HandlerDescriptor handlerDescriptor : data) {
                ApiInvokeData.HandleSwapData handleSwapData = handleResult.get(handlerDescriptor);
                List<Map<String, Object>> responseData = DCUtils.getList(handleSwapData.getResponseData());
                dataContainer.addData(responseData, handlerDescriptor.isManyResult());
            }
        }

        if(dataContainer.getData() instanceof List
                && ((List) dataContainer.getData()).size() > 0 ) {
            for (int i = 0; i < ((List) dataContainer.getData()).size(); i++) {
                Object ele = ((List) dataContainer.getData()).get(i);
                if(ele instanceof DataContainer) {
                    ((DataContainer)ele).removeListSwapperIfOne();
                    ((DataContainer)ele).replaceContainerToData();
//                    ((List) dataContainer.getData()).set(i, ((DataContainer) ele).getData());
                }
            }
        }

        dataContainer.removeListSwapperIfOne();
        dataContainer.replaceContainerToData();

        if(children != null) {
            for (ResultTreeDescriptor child : children.values()) {
                child.packaging(result, handleResult);
            }
        }
        return dataContainer.isManyResult();
    }

    private DataContainer getOrCreate(List<Map<String, Object>> result, String path) {
        String[] pathComponents = getPathComponents(path);

        DataContainer dataContainer = new DataContainer(result);
        DataContainer parentDataContainer = dataContainer;
        for (int i = 0; i < pathComponents.length; i++) {
            String pathComponent = pathComponents[i];
            parentDataContainer = getDataContainer(parentDataContainer, pathComponent);
        }
        return parentDataContainer;
    }

    private DataContainer getDataContainer(DataContainer result, String pathComponent) {
        return result.getChildData(pathComponent);
    }

    public Map<String, Result> getParentStruct(){
        Map<String, Result> result = new HashMap<>();
        if(this.data != null) {
            for (HandlerDescriptor descriptor : this.data) {
                if(StringUtils.isBlank(descriptor.getHandler().getClazz())) {//如果不为代码扩展类
                    ApiDescriptor handlerDescriptor = ApiManager.findHandlerDescriptorByPath(descriptor.getProgram(),
                            descriptor.getHandler().getPath(), descriptor.getHandler().getVersion());
                    Map<String, Result> parentStruct = handlerDescriptor.getResultStruct().getParentStruct();
                    for (Result subResult : descriptor.getResult().getItems()) {
                        String resultName = StringUtils.isNoneBlank(subResult.getAlias()) ? subResult.getAlias() : subResult.getName();
                        if(StringUtils.isBlank(subResult.getFormatter())
                                && parentStruct.containsKey(subResult.getName())) {
                            result.put(resultName, parentStruct.get(subResult.getName()));
                        }else if(StringUtils.isNotBlank(subResult.getFormatter())){
                            result.put(resultName, subResult);
                        }
                    }
                }else {//JAVA代码实现的处理器
                    for (Result subResult : descriptor.getResult().getItems()) {
                        if(StringUtils.isNotBlank(subResult.getFormatter())){
                            String resultName = StringUtils.isNoneBlank(subResult.getAlias()) ? subResult.getAlias() : subResult.getName();
                            result.put(resultName, subResult);
                        }
                    }
                }
            }
        }
        return result;
    }

    public Map<String[], List<Result>> getStruct(){
        boolean isMulti = false;
        Map<String, Result> parentStruct = getParentStruct();
        Map<String[], List<Result>> result = new LinkedHashMap<>();
        List<Result> base = new ArrayList<>();
        Set<String> tempResults = new HashSet<>();
        if(this.data != null) {
            for (HandlerDescriptor descriptor : this.data) {
                if(descriptor.isManyResult() && (descriptor.getPrecheckMap() == null ||descriptor.getPrecheckMap().isEmpty())) {//如果返回为多个结果，且没有前置校验，有前置校验，可能是互斥的多个multi-handler，所以不处理。
                    if(isMulti) throw new RuntimeException("同层次结构不能同时为multi");
                    isMulti = true;
                }

                for (Result subResult : descriptor.getResult().getItems()) {
                    if(!tempResults.contains(subResult.getName())) {
                        tempResults.add(subResult.getName());

                        if(StringUtils.isBlank(subResult.getFormatter()) && parentStruct.containsKey(subResult.getName())) {
                            Result parent = parentStruct.get(subResult.getName());
                            Result mergeResult = new Result();
                            mergeResult.setName(subResult.getName());
                            mergeResult.setAlias(subResult.getAlias());
                            mergeResult.setType(subResult.getType());
                            mergeResult.setDescription(subResult.getDescription());
                            mergeResult.setResultList(subResult.getResultList());
                            mergeResult.setPath(subResult.getPath());
                            mergeResult.setDefaultValue(subResult.getDefaultValue());
                            mergeResult.setFormatter(parent.getFormatter());
                            mergeResult.setPattern(parent.getPattern());
                            base.add(mergeResult);
                        }else {
                            base.add(subResult);
                        }
                    }
                }
            }
        }

        result.put(new String[]{"", "", String.valueOf(isMulti)}, base);
        if(children != null) {
            for (String path : children.keySet()) {
                if("/".equals(this.path) && "tmp".equals(path)) {//tmp路径不做临时返回
                    continue;
                }
                Map<String[], List<Result>> childStruct = children.get(path).getStruct();
                for (Map.Entry<String[], List<Result>> child : childStruct.entrySet()) {
                    String[] key = child.getKey();
                    if(StringUtils.isBlank(key[0])) {
                        result.put(new String[]{path, "", String.valueOf(key[2])},  child.getValue());
                    }else {
                        result.put(key, child.getValue());
                    }
                }
            }
        }


        return result;
    };

    public static class DataContainer{
        private Map parentMap;
        private String parentKey;
        private Object data;

        private boolean manyResult = false;
        public DataContainer(){}
        public DataContainer(Map parentMap, String parentKey){
            this.parentMap = parentMap;
            this.parentKey = parentKey;
        }

        public DataContainer(Object data){
            this.data = data;
        }

        public Object getData() {
            return data;
        }



        public void setData(Object data) {
            this.data = data;
        }

        public DataContainer getChildData(final String childName){
            if(data == null) {
                data = new LinkedHashMap<>();
            }
            if(data instanceof List){
                List dataList = (List) this.data;
                if(dataList.isEmpty()) {
                    /*
                        扩展分为两种情况：
                            1.客户列表-客户标签扩展，这里扩展的是列表中每个用户的标签，如果没有用户，那么当然没有客户标签的扩展问题。
                            2.客户详情-客户标签扩展，如果没有其他客户信息，有标签应该需要扩展
                        这里进行列表默认添加一条并不能兼容上述两种情况，可能存在bug,todo
                     */
                    HashMap newMap = new LinkedHashMap();
                    newMap.put(childName, new DataContainer(newMap, childName));
                    dataList.add(newMap);

                    return (DataContainer) newMap.get(childName);
                }else {
                    List<DataContainer> fetch = CollectionUtils.fetch((List) this.data, new Fetcher<Object, DataContainer>() {
                        @Override
                        public DataContainer fetch(Object object) {
                            if(object instanceof Map) {
                                Map map = (Map) object;
                                if (!map.containsKey(childName)) {
                                    map.put(childName, new DataContainer(map, childName));
                                }
                                return (DataContainer) map.get(childName);
                            }else {
                                return null;
                            }
                        }
                    });
//                    if(fetch.size() == 1 && fetch.get(0) != null) {
//                        return fetch.get(0);
//                    }else {
                        return new DataContainer(fetch);
//                    }

                }
            }else if(data instanceof Map){
                 Map parentMap = (Map)data;
                if(!parentMap.containsKey(childName)) {
                    parentMap.put(childName, new DataContainer(parentMap, childName));
                }
                return (DataContainer) parentMap.get(childName);
            }else {
                return null;
            }
        }

        public void addData(Map<String, Object> responseData, boolean manyResult) {
            addData(Lists.newArrayList(responseData), manyResult);
        }

        public void addData(List<Map<String, Object>> responseData, boolean manyResult) {
            this.manyResult |= manyResult;
            if(responseData == null || responseData.size() == 0){
                return;
            }
            if(data == null) {
                data = responseData;
                return;
            }

            if(data instanceof Map) {
                ((Map) data).putAll(responseData.get(0));
                return;
            }
            if(data instanceof List && ((List) data).isEmpty()) {
                ((List) data).addAll(responseData);
                return;
            }


            if(data instanceof List) {
                List listData = (List) this.data;
                if(listData.size() == responseData.size()) {
                    for (int i = 0; i < listData.size(); i++) {
                        //todo 这里是否有BUG
                        if(listData.get(0) instanceof DataContainer) {
                            ((DataContainer)listData.get(0)).addData(responseData.get(i), manyResult);
                            ((DataContainer)listData.get(0)).replaceContainerToData();
                        }else {
                            Map map = (Map) listData.get(i);
                            map.putAll(responseData.get(i));
                        }

                    }
                }else if(listData.size() == 1){
                    if(listData.get(0) instanceof DataContainer) {
                        ((DataContainer)listData.get(0)).addData(responseData, manyResult);
                        ((DataContainer)listData.get(0)).replaceContainerToData();
                    }else {
                        for (Map<String, Object> map : responseData) {
                            map.putAll((Map) listData.get(0));
                        }
                    }
                    this.data = responseData;
                }else if(responseData.size() == 1) {
                    for (int i = 0; i < listData.size(); i++) {
                        if(listData.get(i) instanceof DataContainer) {
                            ((DataContainer)listData.get(i)).addData(responseData, manyResult);
                            ((DataContainer)listData.get(i)).replaceContainerToData();
                        }else {
                            Map map = (Map) listData.get(i);
                            map.putAll(responseData.get(0));
                        }

                    }
                }else {
                    throw new RuntimeException();
                }
            }

        }

        public void replaceContainerToData(){
            if(parentMap != null && StringUtils.isNoneBlank(parentKey)){
                parentMap.put(parentKey, data);
            }
        }

        public boolean isManyResult() {
            return manyResult;
        }

        public void removeListSwapperIfOne(){
            if(!isManyResult() && data instanceof List
                    && ((List) data).size() > 0 ) {
                this.setData(((List) data).get(0));
            }
        }
    }
}

