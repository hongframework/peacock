package com.hframework.peacock.controller.base;

import com.hframework.beans.exceptions.BusinessException;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class NodeMeta {


    private String code;

    private NodeMeta root;

    private NodeMeta parent;

    private boolean multi;

    private boolean map;

    private String fullPath;

    private Map<String, NodeMeta> children = new LinkedHashMap<>();

    private Map<String, NodeMeta> pathCache; //仅root节点存在该cache，方便NodeData快速关联Meta使用

    public NodeMeta(){
        this("", false);
    }

    public NodeMeta(String code){
        this(code, false);
    }

    public NodeMeta(String code, boolean multi){
        this.code = code;
        this.multi = multi;
    }

    public NodeMeta(String code, boolean multi, boolean map){
        this.code = code;
        this.multi = multi;
        this.map = map;
        if(map && !multi) {
            throw new BusinessException("map node need multi !");
        }
    }

    public NodeMeta addChild(NodeMeta child) {
        children.put(child.getCode(), child);
        child.parent = this;
        return child;
    }

    public void addChildren(String[] childrenCode , boolean multi) {
        for (String childCode : childrenCode) {
            addChild(new NodeMeta(childCode, multi));
        }
    }

    public String getCode() {
        return code;
    }

    public NodeMeta getParent() {
        return parent;
    }

    public boolean isMulti() {
        return multi;
    }

    public boolean isSingle(){
        return !multi;
    }

    public boolean containChildNodeMeta(String code){
        return children.containsKey(code);
    }

    public NodeMeta getChildNodeMeta(String code){
        return children.get(code);
    }

    public String getFullPath() {
        return fullPath;
    }

    public void build(){
        String parentPath = parent == null ? "" : parent.getFullPath();
        String tmpPath = "/" + code + (map ? "{}" : (multi ? "[]": ""));
        this.fullPath = (parentPath.equals("/") ? "": parentPath) + tmpPath;

        this.root =  parent == null ? this : parent.getRoot();
        this.root.getPathCache().put(this.fullPath, this);
        for (NodeMeta childNode : children.values()) {
            childNode.build();
        }
    }

    public NodeMeta getRoot() {
        return root;
    }

    public Map<String, NodeMeta> getPathCache() {
        if(pathCache == null) pathCache = new LinkedHashMap<>();
        return pathCache;
    }

    public boolean isMap() {
        return map;
    }

    public static NodeMeta getInstanceByPaths(List<String> paths){
        NodeMeta root = new NodeMeta("");
        for (String path : paths) {
            String[] parts = path.substring(1).split("/");
            NodeMeta tempMeta = root;
            for (String part : parts) {
                boolean isMulti = part.endsWith("[]") || part.endsWith("{}");
                boolean isMap = part.endsWith("{}");
                String code = isMulti? part.substring(0, part.length() - 2) : part;
                if(!tempMeta.containChildNodeMeta(code)) {
                    tempMeta.addChild(new NodeMeta(code, isMulti, isMap));
                }
                tempMeta = tempMeta.getChildNodeMeta(code);
            }
        }

        root.build();
        return root;
    }
}
