package com.hframework.peacock.controller.base;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.parser.Feature;
import com.beust.jcommander.internal.Lists;
import com.hframework.beans.exceptions.BusinessException;
import com.hframework.common.util.StringUtils;
import com.hframework.common.util.collect.CollectionUtils;
import com.hframework.common.util.collect.bean.Fetcher;
import com.hframework.common.util.message.Dom4jUtils;
import com.hframework.smartweb.exception.SmartHandlerException;
import org.dom4j.Element;
import org.dom4j.Node;
import org.dom4j.tree.DefaultElement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;

import java.util.*;

public class NodeData {

    private static final Logger logger = LoggerFactory.getLogger(NodeData.class);
    private NodeMeta meta;

    private String code;
    private Object value;

    private NodeData parent;

    private String fullPath;

    private boolean isRuntime = true;//节点动态扩展而来，不符合meta规范

    private Map<String, List<NodeData>> children = new LinkedHashMap<>();

    private Map<String, NodeData> pathCache; //仅root节点存在该cache，方便NodeData快速查找子节点信息

    public NodeData() {
        this.code = "";
    }

    public NodeData(String code) {
        this.code = code;
    }


    public NodeData(String code, Object value) {
        this.code = code;
        this.value = value;
    }

    public void addChildren(NodeData nodeData) {
        if(!children.containsKey(nodeData.getCode())) {
            children.put(nodeData.getCode(), new ArrayList<NodeData>());
        }
        nodeData.parent = this;
        children.get(nodeData.getCode()).add(nodeData);
    }

    private List<NodeData> getChildrenNode(String code){
        return children.get(code);
    }

    private NodeData getChildNode(String code, int arrayIndex) {
        return children.get(code).get(arrayIndex);
    }

    private void addChildren(NodeData nodeData, int arrayIndex) {
        if(!children.containsKey(nodeData.getCode())) {
            children.put(nodeData.getCode(), new ArrayList<NodeData>());
        }
        nodeData.parent = this;
        if(children.get(nodeData.getCode()).size() != arrayIndex) {
            throw new BusinessException("nodeData add children is not continuous (miss:"
                    + children.get(nodeData.getCode()).size() + ")!");
        }
        children.get(nodeData.getCode()).add(nodeData);
    }

    public NodeMeta getMeta() {
        return meta;
    }

    public String getCode() {
        return code;
    }

    public Object getValue() {
        return value;
    }

    public NodeData getParent() {
        return parent;
    }

    public String getFullPath() {
        return fullPath;
    }

    public void build(NodeMeta meta) {
        build(meta, -1);
    }

    public void build(NodeMeta meta, int index){
        if(meta == null) {
            System.out.println(meta);
        }
        if(!meta.getCode().equals(this.code) && !meta.isMap()) {
            System.out.println("["+ meta.getCode() +" & " + this.code + "]!");
            throw new BusinessException("data mapping meta failed ["+ meta.getCode() +" & " + this.code + "]!");
        }

        this.meta = meta;
        String parentPath = parent == null ? "" : parent.getFullPath();
        if(parentPath.endsWith("/")) parentPath = parentPath.substring(0, parentPath.length() -1);
        String tmpPath;
        if(!meta.getCode().equals(this.code) && meta.isMap()) {
            tmpPath = "{" + code + "}";
        }else {
            tmpPath = "/" + code + (meta.isMulti() && !meta.isMap() ? "[" + index+ "]" : "");
        }
        this.fullPath = (parentPath.equals("/") ? "" : parentPath) + tmpPath;
        addRootCache(this.fullPath, this);

        for (String code : children.keySet()) {
            List<NodeData> childrenData = children.get(code);
            NodeMeta childMeta = meta.getChildNodeMeta(code);
            if(childMeta == null && meta.isMap()) {
                childMeta = meta;
            }
            if(childMeta == null) {
                logger.warn("[" + this.fullPath + "]: " + code + " not exists, meta need update !");
            }else {
                for (int i = 0; i < childrenData.size(); i++) {
                    childrenData.get(i).build(childMeta, i);
                }
            }

        }
    }

    private void addRootCache(String fullPath, NodeData nodeData) {
        if(this.parent == null) {
            if(this.pathCache == null) {
                this.pathCache = new LinkedHashMap<>();
            }
            this.pathCache.put(fullPath, nodeData);
        }else {
            this.parent.addRootCache(fullPath, nodeData);
        }
    }

    private boolean containChildren(String code, int arrayIndex) {
        return children.containsKey(code) && children.get(code).size() > arrayIndex;
    }

    private static int getArrayIndex(String info) {
        if(info.matches("\\[\\d+\\]")) {
            return Integer.parseInt(info.substring(1, info.length() - 1));
        }else if(info.matches("\\[\\]")) {
            return 0;
        }else {
            return 0;
        }
    }


    public Map<String, Object> toMap(){
        Map<String, Object> result = new LinkedHashMap<>();

        Map<String, NodeData> pathCache = this.getPathCache();
        for (String path : pathCache.keySet()) {
            result.put(path, pathCache.get(path).getValue());
        }

        return result;
    }

    public Element toXml(){
        return toXml(new DefaultElement("xml"));
    }

    public Element toXml(Element parent){

        if(children.isEmpty()) {
            if(value != null && value.getClass().isArray()) {
                for (Object val : (Object[]) value) {
                    Element element = new DefaultElement(code);
                    if(val != null) {
                        element.setText(String.valueOf(val));
                    }
                    parent.add(element);
                }
            }else {
                Element element = new DefaultElement(code);
                if(value != null) {
                    element.setText(String.valueOf(value));
                }
                parent.add(element);
            }
        }else {
            Element temp = parent;
            if(!StringUtils.isBlank(code)) {
                Element element = new DefaultElement(code);
                parent.add(element);
                temp = element;
            }

            for (List<NodeData> nodeDatas : children.values()) {
                for (NodeData nodeData : nodeDatas) {
                    nodeData.toXml(temp);
                }
            }
        }
        return parent;
    }

    public JSONObject toJson(){
        return (JSONObject) toJson(null);
    }

    public JSON toJson(JSON parentJson){

        if(!children.isEmpty()) {
            final JSONObject childrenObject = new JSONObject(true);
            for (final String key : children.keySet()) {
                List<NodeData> values = children.get(key);
                NodeMeta childNodeMeta = meta.getChildNodeMeta(key);
                if(childNodeMeta == null && meta.isMap()) {
                    childNodeMeta = meta;
                }
                System.out.println(key + " => " + values);
                System.out.println(childNodeMeta);
                if((childNodeMeta.isSingle() || childNodeMeta.isMap()) && values.size() > 0) {//单值
                    children.get(key).get(0).toJson(childrenObject);
                }else if(childNodeMeta.isSingle() && values.size() == 0) {
                    childrenObject.put(key, null);
                }else if(childNodeMeta.isMulti() && !childNodeMeta.isMap()) {//多值
                    JSONArray jsonArray = new JSONArray();
                    childrenObject.put(key, jsonArray);
                    for (NodeData nodeData : values) {
                        nodeData.toJson(jsonArray);
                    }
//                    childrenObject.put(key, CollectionUtils.fetch(values, new Fetcher<NodeData, Object>() {
//                        @Override
//                        public Object fetch(NodeData nodeData) {
////                            System.out.println(key + " => " + childrenObject.get(key));
//                            return nodeData.toJson((JSON) childrenObject.get(key));
//                        }
//                    }));
                }
            }

            if(parentJson == null) {
                parentJson = childrenObject;
            }else if(parentJson instanceof JSONArray) {
                ((JSONArray) parentJson).add(childrenObject);
            }else {
                ((JSONObject) parentJson).put(code, childrenObject);
            }

        }else {
            if(parentJson instanceof JSONArray) {
                if(value != null && value.getClass().isArray()) {
                    for (Object val : (Object[])value) {
                        ((JSONArray) parentJson).add(val);
                    }
                }else {
                   ((JSONArray) parentJson).add(value);
                }
            }else {
                ((JSONObject) parentJson).put(code, value);
            }
        }
        return parentJson;
    }

    /**
     * 扁平的Path转化为NodeData
     * @param flatPathMap
     * @return
     */
    public static NodeData parseByPathMap(Map<String, Object> flatPathMap) {
        NodeData root = new NodeData();
        for (String path : flatPathMap.keySet()) {
            if("/".equals(path) || StringUtils.isBlank(path)){
                continue;
            }
            Object value = flatPathMap.get(path);
            NodeData tempObject = root;
            String[] items = (path.startsWith("/")? path.substring(1):path).split("/");
            for (int i = 0; i < items.length; i++) {
                String item = items[i];
                String itemCode = item.replaceAll("\\[\\d*\\]|\\{\\w*\\}", "");
                String mapKey = item.contains("{") && item.contains("}") ? item.substring(item.indexOf("{") + 1, item.indexOf("}")) : null;
                int arrayIndex = getArrayIndex(item.substring(itemCode.length()));
                if(i == items.length - 1 && mapKey == null) {//叶子节点
                    tempObject.addChildren(new NodeData(itemCode, value));
                }else {//非叶子节点
                    tempObject = getOrCreateChildNode(tempObject, itemCode, arrayIndex);
                    if(mapKey != null) {
                        tempObject = getOrCreateChildNode(tempObject, mapKey, arrayIndex);
                    }
                }
            }
        }
        return root;
    }

    private static NodeData getOrCreateChildNode(NodeData bootNode, String itemCode, int arrayIndex) {
        if(!bootNode.containChildren(itemCode, arrayIndex)){
            NodeData childNode = new NodeData(itemCode);
            bootNode.addChildren(childNode, arrayIndex);
            return childNode;
        }else {
            return bootNode.getChildNode(itemCode, arrayIndex);
        }
    }


    /**
     * 解析JSON为NodeData
     * @param jsonString
     * @return
     */
    public static NodeData parseJson(String jsonString) {
        Object json = JSON.parse(jsonString, JSON.DEFAULT_PARSER_FEATURE |= Feature.OrderedField.getMask());
        NodeData root = addChildrenDataByJson(null , json, "");
        return root;
    }


    /**
     * 解析XML为NodeData
     * @param xmlString
     * @return
     */
    public static NodeData parseXml(String xmlString) {
        NodeData root = new NodeData();
        Element rootElement = Dom4jUtils.getDocumentByContent(xmlString).getRootElement();
        addChildrenDataByXml(root, rootElement);
        return root;
    }

    private static NodeData addChildrenDataByJson(NodeData parent, Object json, String code) {
        if(json instanceof JSONObject) {
            JSONObject jsonObject = (JSONObject) json;
            NodeData thisNode = new NodeData(code);
            if(parent != null) {
                parent.addChildren(thisNode);
            }else {
                parent = thisNode;
            }
            for (String subCode : jsonObject.keySet()) {
                Object value = jsonObject.get(subCode);
                if(value instanceof JSON) {
                    addChildrenDataByJson(thisNode, value, subCode);
                }else {
                    thisNode.addChildren(new NodeData(subCode, value));
                }
            }
        }else if(json instanceof JSONArray) {
            JSONArray jsonArray = (JSONArray) json;
            if(jsonArray.size() > 0 && BeanUtils.isSimpleProperty(jsonArray.get(0).getClass())) {
                parent.addChildren(new NodeData(code, jsonArray.toArray()));
            }else {
                for (Object o : jsonArray) {
                    addChildrenDataByJson(parent, o, code);
                }
            }
        }else {
            throw new SmartHandlerException(json + " type is not supported !");
        }

        return parent;
    }

//    private static void addChildrenDataByJson(NodeData root, Object json) {
//        if(json instanceof JSONObject) {
//            JSONObject jsonObject = (JSONObject) json;
//            for (String code : jsonObject.keySet()) {
//                Object value = jsonObject.get(code);
//                if(value instanceof JSON) {
//                    NodeData childNode = new NodeData(code);
//                    addChildrenDataByJson(childNode, value);
//                    root.addChildren(childNode);
//                }else {
//                    root.addChildren(new NodeData(code, value));
//                }
//            }
//        }else if(json instanceof JSONArray) {
//            JSONArray jsonArray = (JSONArray) json;
//            if(jsonArray.size() > 0 && BeanUtils.isSimpleProperty(jsonArray.get(0).getClass())) {
//                root.value = jsonArray.toArray();
//            }else {
//                for (Object o : jsonArray) {
//                    addChildrenDataByJson(root, o);
//                }
//            }
//        }else {
//            throw new BusinessException(json + " type is not supported !");
//        }
//    }

    private static void addChildrenDataByXml(NodeData root, Element rootElement) {
        for (int i = 0; i < rootElement.nodeCount(); i++) {
            Node node = rootElement.node(i);
            if(node instanceof DefaultElement){
                String code = node.getName();
                if(((DefaultElement)node).elements().isEmpty()) { //没有子节点
                    String value = node.getText();
                    root.addChildren(new NodeData(code, value));
                }else {//存在子节点
                    NodeData childNode = new NodeData(code);
                    addChildrenDataByXml(childNode, (Element) node);
                    root.addChildren(childNode);
                }

            }
        }
    }

    public Map<String, NodeData> getPathCache() {
        return pathCache;
    }

    public Object getSchema(String schema) {
        schema = schema.replaceAll("\\[\\]", "").replaceAll("\\{\\}", "");
        if(!schema.startsWith("/")) schema = "/" + schema;
        if(!"/".equals(schema) && schema.endsWith("/")) schema = schema.substring(0, schema.length() - 1);
        NodeData nodeData = pathCache.get(schema);
        if(nodeData == null) {//表明获取路径不只有一个对象，比如XX[0]，比如XX{123}
            nodeData = pathCache.get(schema.substring(0, schema.lastIndexOf("/")));
            List<NodeData> childrenNode = nodeData.getChildrenNode(schema.substring(schema.lastIndexOf("/") + 1));
            return CollectionUtils.fetch(childrenNode, new Fetcher<NodeData, Object>() {
                @Override
                public Object fetch(NodeData nodeData) {
                    return nodeData.getTreeValue();
                }
            });
        }
        return nodeData.getTreeValue();
    }

    /**
     * 获取该节点下所有取值，通过List&Map&SimpleProperty作为容器进行组装
     * @return
     */
    public Object getTreeValue(){
        if(children.isEmpty()) {
            return value;
        }else {
            boolean isRootMap = meta.isMap() && meta.getCode().equals(this.code);
            if(isRootMap) {
                System.out.println();
            }
            Map<String, Object> tempMap = new HashMap<>();
            for (String childCode : children.keySet()) {
                boolean isSingle = isRootMap || meta.getChildNodeMeta(childCode) == null
                        || meta.getChildNodeMeta(childCode).isSingle() || meta.getChildNodeMeta(childCode).isMap();
                List<NodeData> nodeData = children.get(childCode);
                if(isSingle && !nodeData.isEmpty()){
                    if(nodeData.size() > 1 && nodeData.get(0).isRuntime()) {
                        //出现这种情况，主要是ThirdApiExecutor中节点存在batchHelper,
                        // 比如解密手机字段data,如果是多个手机号，则用逗号分隔，则人工修改了NodeData
                        List tmpList = new ArrayList();
                        for (NodeData childData : nodeData) {
                            tmpList.add(childData.getTreeValue());
                        }
                        tempMap.put(childCode, tmpList);
                    }else {
                        tempMap.put(childCode, nodeData.get(0).getTreeValue());
                    }

                }else if(!isSingle){
                    List tmpList = new ArrayList();
                    for (NodeData childData : nodeData) {
                        tmpList.add(childData.getTreeValue());
                    }
                    tempMap.put(childCode, tmpList);
                }
            }

            if(isRootMap) {
                for (String dynCode : tempMap.keySet()) {
                    Object dynVal = tempMap.get(dynCode);
                    if(dynVal instanceof Map){
                        ((Map)dynVal).put("_key_", dynCode);
                    }
                }
                return Lists.newArrayList(tempMap.values());
            }else {
                return tempMap;
            }

        }
    }

    public void setValue(Object value) {
        this.value = value;
    }

    public boolean isRuntime() {
        return isRuntime;
    }

    public void setRuntime(boolean runtime) {
        isRuntime = runtime;
    }
}
