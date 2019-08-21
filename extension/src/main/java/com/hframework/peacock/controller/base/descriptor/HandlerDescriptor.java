package com.hframework.peacock.controller.base.descriptor;

import com.hframework.smartweb.SmartHandlerFactory;
import com.hframework.smartweb.bean.Object2MapHelper;
import com.hframework.smartweb.bean.apiconf.Handler;
import com.hframework.smartweb.bean.apiconf.Parameter;
import com.hframework.smartweb.bean.apiconf.Precheck;
import com.hframework.smartweb.bean.apiconf.Result;
import com.hframework.strategy.rule.ExpressInvoker;
import com.hframework.strategy.rule.ExpressionEngine;
import com.hframework.peacock.controller.base.dc.DC;
import org.apache.commons.lang3.StringUtils;
import org.apache.kafka.common.utils.CopyOnWriteMap;
import org.springframework.core.MethodParameter;

import java.text.ParseException;
import java.util.*;

/**
 * Created by zhangquanhong on 2017/11/15.
 */
public class HandlerDescriptor {

    private String program;

    private Handler handler;
    private ResultDescriptor result;

    private String handlerMark;

    private Map<String, Class> allParameters;
    private Map<String, Object> defaultValues;
    private Set<String> requires;
    private Map<String, String> inputParameters;
    private Set<String> missParameters;

    private Map<String, Parameter> refParameters;

    protected static final Map<String, ExpressInvoker> rules = new HashMap<>();

    private Set<String> results;
    private Set<String> dbQueryFields;


    private SmartHandlerFactory.HandlerInfo single;
    private SmartHandlerFactory.HandlerInfo batch;

    private MethodParameter returnType;

    private boolean manyResult = false;

    public Map<String, String> dependParameters;

    public Map<String, Set<HandlerDescriptor>> dependTo;

    private Set<HandlerDescriptor> triggerTo;

    private Map<String, Precheck> precheckMap;
    private Set<String> precheckVars;

    public HandlerDescriptor(String program, Handler handler) throws ClassNotFoundException {
        this.program = program;
        List<Precheck> precheckList = handler.getPrecheckList();
        if(precheckList != null && precheckList.size() > 0) {
            precheckMap = new HashMap<>();
            for (Precheck precheck : precheckList) {
                String express = null;
                if(precheck.getExpressList() != null && precheck.getExpressList().size() > 0) {
                    express = precheck.getExpressList().get(0);
                }
                String filterExecutor = precheck.getClazz();
                String pass = precheck.getPass();
                String unpass = precheck.getUnpass();
                String unpassResult = precheck.getUnpassResult();
                if(!rules.containsKey(express)) {
                    ExpressInvoker invoker = ExpressionEngine.loadAndExplain(express);
                    rules.put(express, invoker);
                }
                String[] vars = rules.get(express).getVars();
                if(vars != null && vars.length > 0) {
                    precheckVars = new HashSet<>();
                    for (String var : vars) {
                        precheckVars.add(var.substring(1));
                    }
                }
                precheckMap.put(express, precheck);
            }
        }


        this.handler = handler;
        handlerMark = handler.getPath() + "[" + handler.getVersion() + "]";
        result = new ResultDescriptor(handler.getResultList(), handler.getParentPath());

        String handlerClass = handler.getClazz();
        SmartHandlerFactory.HandlerInfo[] handlers = null;
        if(StringUtils.isNoneBlank(handlerClass)) {
            handlers = SmartHandlerFactory.getHandler(Class.forName(handlerClass));
        }else {
            String path = handler.getPath();
            handlers = SmartHandlerFactory.getHandler(program, path);
        }
        single = handlers[0];
        batch = handlers[1];

        if(single != null) {
            allParameters = single.getParameters();
            defaultValues = new HashMap<>(single.getParameterDefaultValues());
            requires = single.getRequiredParameters();
            missParameters = new HashSet<>(requires);
            missParameters.removeAll(new HashSet<Object>(defaultValues.keySet()));

            results = single.getResults();
            if(single.getMethod() != null) {
                returnType = new MethodParameter(single.getMethod(), -1);
                manyResult = Object2MapHelper.isBeanCollectionParameterType(returnType)
                        || Object2MapHelper.isMapCollectionParameterType(returnType)
                        || Object2MapHelper.isBeanArrayParameterType(returnType)
                        || Object2MapHelper.isMapArrayParameterType(returnType);
            }else {
                manyResult =  !"Object".equals(single.getReturnType());
            }

        }

        if(handler.getResultList() != null && handler.getResultList().size() > 0) {
            Map<String, String> dbQueryAliasMap = new HashMap<>();
            results = new LinkedHashSet<>();
            for (Result result : handler.getResultList()) {
                String dbAliasName = parseDBAliasName(result.getName());
                if(StringUtils.isNoneBlank(dbAliasName)) {
                    dbQueryAliasMap.put(dbAliasName, result.getName());//<别名，完整查询段>
                    results.add(dbAliasName);
                }else {
                    results.add(result.getName());
//                    String name = result.getName();
//                    results.add(name.endsWith("[]") ? name.substring(0, name.length() - 2): name);
                }

            }
            if(!dbQueryAliasMap.isEmpty()) {
                dbQueryFields = new LinkedHashSet(results);
                dbQueryFields.removeAll(dbQueryAliasMap.keySet());
                dbQueryFields.addAll(dbQueryAliasMap.values());
            }
        }

        if(handler.getParameterList() != null && handler.getParameterList().size() > 0) {
            for (Parameter parameter : handler.getParameterList()) {
                if(StringUtils.isNoneBlank(parameter.getValue())) {
                    missParameters.remove(parameter.getName());
                    defaultValues.put(parameter.getName(), parameter.getValue());
                }else if(StringUtils.isNoneBlank(parameter.getRef())) {
                    if(refParameters == null) {
                        refParameters = new CopyOnWriteMap<>();
                    }
                    refParameters.put(parameter.getName(), parameter);
                }
            }
            missParameters.removeAll(new HashSet<Object>(defaultValues.keySet()));
        }
    }

    public static String parseDBAliasName(String name) {
        if(StringUtils.isNoneBlank(name)) {
            if(name.toLowerCase().contains(" as ")){
                return name.substring(name.toLowerCase().indexOf(" as ") + 4).trim();
            }
        }
        return null;
    }

    public void calculateDependToAndTrigger(List<HandlerDescriptor> handlers) {
        if(dependParameters == null) return;

        for (String missName : dependParameters.keySet()) {
            String dependName = dependParameters.get(missName);
            if(dependName.startsWith("HANDLER:")){
                String parentPath = dependName.substring("HANDLER:".length());
                for (HandlerDescriptor otherHandler : handlers) {
                    if(parentPath.equals(otherHandler.getHandler().getParentPath())){
                        if(dependTo == null) dependTo = new HashMap<>();
                        if(!dependTo.containsKey(missName)) dependTo.put(missName, new LinkedHashSet<HandlerDescriptor>());
                        dependTo.get(missName).add(otherHandler);
                        otherHandler.triggerTo(this);
                    }
                }
            }else {
                for (HandlerDescriptor otherHandler : handlers) {
                    if(otherHandler != this) {
                        if(otherHandler.containResult(dependName)){
                            if(dependTo == null) dependTo = new HashMap<>();
                            if(!dependTo.containsKey(missName)) dependTo.put(missName, new LinkedHashSet<HandlerDescriptor>());
                            dependTo.get(missName).add(otherHandler);
                            otherHandler.triggerTo(this);
                        }
//                    else if(otherHandler.isRuntimeResult()) {
//                        runtimeExist = true;
//                    }
                    }
                }
            }

        }
    }

    public void initHandlerDependAndTrigger(HandlersDescriptor handlersDescriptor, ParametersDescriptor parameters) {
        setRequest(parameters);
        calculateMissParameter();
        if(handlersDescriptor.getRelHandlersDescriptor() != null && handlersDescriptor.getRelHandlersDescriptor().getHandlers() != null) {
            calculateDependToAndTrigger(handlersDescriptor.getRelHandlersDescriptor().getHandlers());
        }
        calculateDependToAndTrigger(handlersDescriptor.getHandlers());
    }

    public void setRequest(ParametersDescriptor parameters) {
        for (ParameterDescriptor parameterDescriptor : parameters.getParameters()) {
            if(allParameters.containsKey(parameterDescriptor.getName()) ||
                    (precheckVars != null && precheckVars.contains(parameterDescriptor.getName()))) {
                if(inputParameters == null) {
                    inputParameters = new HashMap<>();
                }
                inputParameters.put(parameterDescriptor.getName(), parameterDescriptor.getName());
                missParameters.remove(parameterDescriptor.getName());
            }
            if(refParameters == null) continue;
            for (Map.Entry<String, Parameter> ref : refParameters.entrySet()) {
                if(ref.getValue().getRef().equals(parameterDescriptor.getName()) && ParametersDescriptor.FindScope.fromParameter(ref.getValue().getScope())){
                    if(inputParameters == null) {
                        inputParameters = new HashMap<>();
                    }
                    inputParameters.put(ref.getKey(), parameterDescriptor.getName());
                    refParameters.remove(ref.getKey());
                    missParameters.remove(ref.getKey());
                }
            }
        }
    }

    public Map<String, Class> getAllParameters() {
        return allParameters;
    }

    public Map<String, Object> getDefaultValues() {
        return defaultValues;
    }

    public Set<String> getRequires() {
        return requires;
    }

    public Map<String, String> getInputParameters() {
        return inputParameters;
    }

    public Set<String> getMissParameters() {
        return missParameters;
    }

    public Map<String, Parameter> getRefParameters() {
        return refParameters;
    }

    public boolean isRuntimeResult() {
        return results instanceof Object2MapHelper.RuntimeSet;
    }

    public boolean isOneValueResult() {
        return results instanceof Object2MapHelper.OneValueSet;
    }

    public boolean containResult(String name) {
        return results.contains(name);
    }

    public void triggerTo(HandlerDescriptor nextHandler) {
        if(triggerTo == null) {
            triggerTo = new HashSet<>();
        }
        triggerTo.add(nextHandler);
    }

    public SmartHandlerFactory.HandlerInfo getSingle() {
        return single;
    }

    public SmartHandlerFactory.HandlerInfo getBatch() {
        return batch;
    }

    public Set<String> getResults() {
        return results;
    }



    public void calculateMissParameter() {
        dependParameters = new HashMap<>();
        if(refParameters != null) {
            for (Parameter parameter : refParameters.values()) {
                String ref = parameter.getRef();
                //TODO 需要支持这种形式ref="#P2pIdSmartParser.p2pId"
                dependParameters.put(parameter.getName(), ref);
            }
        }
        if(missParameters != null) {
            for (String parameterName : missParameters) {
                if(!dependParameters.containsKey(parameterName)) {
                    dependParameters.put(parameterName, parameterName);
                }
            }
        }
        if(dependParameters.isEmpty()) {
            dependParameters = null;
        }

    }

    public Map<String, String> getDependParameters() {
        return dependParameters;
    }

    public Map<String, Set<HandlerDescriptor>> getDependTo() {
        return dependTo;
    }

    public void setDependTo(Map<String, Set<HandlerDescriptor>> dependTo) {
        this.dependTo = dependTo;
    }

    public Set<HandlerDescriptor> getTriggerTo() {
        return triggerTo;
    }

    public MethodParameter getReturnType() {
        return returnType;
    }

    public void formatResult(DC responseData) throws Exception {
        result.formatResult(responseData);
    }

    public void removeUnusedField(DC responseData) throws IllegalAccessException, ParseException, InstantiationException {
        result.removeUnusedField(responseData);
    }



    @Override
    public String toString() {
        return "HandlerDescriptor:" + getHandlerMark();
    }

    public boolean check(StringBuffer info) {
        if(dependParameters == null || dependParameters.size() == 0) {
            return true;
        }
        if(dependTo != null && dependParameters.size() == dependTo.size()) {
            return true;
        }

        for (String parameterName : dependParameters.keySet()) {
            if(dependTo == null || !dependTo.containsKey(parameterName)) {
                info.append("attr : ").append(parameterName).append("; depend to :").append(dependParameters.get(parameterName)).append(" not exists !").append("\n");
            }
        }
        return false;
    }

    public String getHandlerMark() {
        return handlerMark;
    }

    public void setHandlerMark(String handlerMark) {
        this.handlerMark = handlerMark;
    }

    public ResultDescriptor getResult() {
        return result;
    }

    public boolean isManyResult() {
        return manyResult;
    }

    public Handler getHandler() {
        return handler;
    }

    public Map<String, Precheck> getPrecheckMap() {
        return precheckMap;
    }

    public static Map<String, ExpressInvoker> getRules() {
        return rules;
    }

    public Set<String> getPrecheckVars() {
        return precheckVars;
    }

    public void setPrecheckVars(Set<String> precheckVars) {
        this.precheckVars = precheckVars;
    }

    public Set<String> getDbQueryFields() {
        if(dbQueryFields != null && !dbQueryFields.isEmpty()) {
            return dbQueryFields;
        }else {
            return results;
        }
    }

    public String getProgram() {
        return program;
    }
}