package com.hframework.peacock.controller.base.descriptor;

import com.hframework.smartweb.annotation.Expand;
import com.hframework.smartweb.bean.apiconf.*;
import com.hframework.smartweb.exception.SmartHandlerException;

import java.util.*;

/**
 * Created by zhangquanhong on 2017/11/15.
 */
public class HandlersDescriptor {

    private String program;
    private HandlersDescriptor relHandlersDescriptor;

    private List<HandlerDescriptor> handlers;

//    private Map<Long, Long> handlerTriggers; // <handlerIndex, handlerIndex>
//    private Map<Long, Long> handlerDepends; // <handlerIndex, handlerIndex>

//    private Triples<HandlerDescriptor, String, String> missTriples;// handler缺省参数三元信息<依赖参数名，用于handler对象, 入参名>
//    private Triples<String, HandlerDescriptor, String> feedTriples;// 缺省入参补给二元信息<参数名，补给handler对象, null>
//    private Triples<HandlerDescriptor, HandlerDescriptor, String> triggerTriples;// handler处理结束后触发关系<补给handler对象，handler对象, null>

    private boolean runtimeExist;

    private Set<HandlerDescriptor> missHandlers;

    public HandlersDescriptor(String program, List<Handler> handlerList) throws ClassNotFoundException {
        this(program, handlerList, null);
    }

    public HandlersDescriptor(String program, List<Handler> handlerList, HandlersDescriptor relHandlersDescriptor) throws ClassNotFoundException {
        this.program = program;
        this.relHandlersDescriptor = relHandlersDescriptor;
        handlers = new ArrayList<>();
        for (Handler handler : handlerList) {
            handlers.add(new HandlerDescriptor(program, handler));
        }
    }

    public void initHandlerDependAndTrigger(ParametersDescriptor parameters){
        for (HandlerDescriptor handler : handlers) {
            handler.initHandlerDependAndTrigger(this, parameters);
        }
//        initHandlerDependAndTrigger();

        for (HandlerDescriptor handler : handlers) {
            if(handler.isRuntimeResult()) {
                runtimeExist = true;
            }
        }

        for (HandlerDescriptor handler : handlers) {
            if(handler.getMissParameters() != null && handler.getMissParameters().size() > 0) {
                if(missHandlers == null) {
                    missHandlers = new HashSet<>();
                }
                missHandlers.add(handler);
            }
        }




    }

    private void initHandlerDependAndTrigger() {
//        missTriples = new Triples<>();
//        for (int i = 0; i < handlers.size(); i++) {
//            HandlerDescriptor handler = handlers.get(i);
//            if(handler.getRefParameters() != null) {
//                for (Parameter parameter : handler.getRefParameters().values()) {
//                    String ref = parameter.getRef();
//                    //TODO 需要支持这种形式ref="#P2pIdSmartParser.p2pId"
//                    missTriples.put(handler, parameter.getName(), ref);
//                }
//            }
//            if(handler.getMissParameters() != null) {
//                for (String parameterName : handler.getMissParameters()) {
//                    missTriples.putIfNotExists(handler, parameterName, parameterName);
//                }
//            }
//        }
//
//        feedTriples = new Triples<>();
//        for (String otherHandlerResult : missTriples.values()) {
//            for (int i = 0; i < handlers.size(); i++) {
//                HandlerDescriptor handler = handlers.get(i);
//                if(handler.containResult(otherHandlerResult)){
//                    feedTriples.put(otherHandlerResult, handler, null);
//                }else if(handler.isRuntimeResult()) {
//                    runtimeExist = true;
//                }
//            }
//        }
//
//        triggerTriples = new Triples<>();
//
//        for (String triggerResult : feedTriples.keySet()) {
//            for (HandlerDescriptor feedHandler : feedTriples.get(triggerResult).keySet()) {
//                for (HandlerDescriptor missHandler : missTriples.keySet()) {
//                    for (Map.Entry<String, String> entry : missTriples.get(missHandler).entrySet()) {
//                        if(entry.getValue().equals(triggerResult)) {
//                            triggerTriples.put(feedHandler, missHandler, null);
//                            feedHandler.triggerTo(missHandler);
////                            missHandler.triggerBy(feedHandler);
//                        }
//                    }
//                }
//            }
//        }
    }

    public boolean check() {
        if(missHandlers == null) return true;
        StringBuffer info = new StringBuffer();
        boolean result = true;
        for (HandlerDescriptor missHandler : missHandlers) {
            result &= missHandler.check(info);
        }
        if(!result) {
            throw new SmartHandlerException("config exception [ " + info.toString() + " ]");
        }
        return result;
    }

    public static class Triples<K, V, T>{
        private Map<K, Map<V, T>> map = new HashMap<>();


        public boolean contain(K k, V v) {
            return map.containsKey(k) && map.get(k).containsKey(v);
        }
        public boolean putIfNotExists(K k, V v, T t) {
            if(!map.containsKey(k)) {
                map.put(k, new HashMap<V, T>());
            }
            if(map.get(k).containsKey(t)) {
                return false;
            }
            map.get(k).put(v, t);
            return true;
        }

        public void put(K k, V v, T t) {
            if(!map.containsKey(k)) {
                map.put(k, new HashMap<V, T>());
            }
            map.get(k).put(v, t);
        }
        public Set<K> keySet(){
            return map.keySet();
        }

        public Map<V, T> get(K k){
            return map.get(k);
        }

        public Set<T> values(){
            Set<T> result = new HashSet<>();
            for (Map<V, T> vtMap : map.values()) {
                result.addAll(vtMap.values());
            }
            return result;
        }
    }


    private Map<Expand, Set<Expand>> handlerDepends(Expand[] depands) {
        Map<Expand, Set<Expand>> expandDependRelation = new HashMap<>();
        Map<String, Expand> newAttrExpandRelation = new HashMap<>();
        for (Expand expand : depands) {
            expandDependRelation.put(expand, new HashSet<Expand>());
            String[] attrs = expand.attr();
            for (String attr : attrs) {
                if(newAttrExpandRelation.containsKey(attr)){
                    expandDependRelation.get(expand).add(newAttrExpandRelation.get(attr));
                }
            }
            String[] newAttrs = expand.newAttr();
            for (String newAttr : newAttrs) {
                newAttrExpandRelation.put(newAttr, expand);
            }
        }
        return expandDependRelation;
    }

    public List<HandlerDescriptor> getHandlers() {
        return handlers;
    }

//    public Triples<HandlerDescriptor, String, String> getMiss() {
//        return missTriples;
//    }
//
//    public Triples<String, HandlerDescriptor, String> getFeed() {
//        return feedTriples;
//    }
//
//
//    public Triples<HandlerDescriptor, HandlerDescriptor, String> getTrigger() {
//        return triggerTriples;
//    }

    public boolean isRuntimeExist() {
        return runtimeExist;
    }

    public Set<HandlerDescriptor> getMissHandlers() {
        return missHandlers;
    }

    public HandlersDescriptor getRelHandlersDescriptor() {
        return relHandlersDescriptor;
    }
}