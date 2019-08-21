package com.hframework.strategy.rule.component;

import com.hframework.strategy.rule.ExpressInvoker;
import com.hframework.strategy.rule.data.EDataField;
import com.hframework.strategy.rule.data.EDataSet;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

/**
 * Created by zhangquanhong on 2017/6/26.
 */
public class NestingExpress extends Express implements Component{

    private static Logger logger = LoggerFactory.getLogger(NestingExpress.class);

    private String originExpress;
    private Map<String, Express> ifElseExpress;
    private List<Component> components = new ArrayList<Component>();
    private String[] vars;

    public NestingExpress(String express) {
        super(express);
    }

    public NestingExpress(String originExpress, String express, Map<String, Express> ifElseExpress) {
        super(express);
        this.originExpress = originExpress;
        this.ifElseExpress =ifElseExpress;

    }

    @Override
    public String toString() {
        return originExpress;
    }

    public void explain(ExpressInvoker expressInvoker){
        for (Express express1 : ifElseExpress.values()) {
            express1.explain(expressInvoker);
        }
        super.explain(expressInvoker);

    }

    @Override
    public void print() {
        super.print();
    }

    @Override
    public void execute(EDataSet expressionData, Set<Integer> rowIndexScope, Set<Integer> parentUnMatchRowIndex) {
        for (String ifElseId : ifElseExpress.keySet()) {
            Set<Integer> subRowIndexScope = rowIndexScope;
            if(expressionData.containDataFieldCode(ifElseId)) {
                Integer fieldIndex = expressionData.getFieldIndex(ifElseId);
                subRowIndexScope = new HashSet<>();
                for (Integer rowIndex : rowIndexScope) {
                    Object string = expressionData.getData()[rowIndex].get(fieldIndex);
                    if(string == null || StringUtils.isBlank(String.valueOf(string))){
                        subRowIndexScope.add(rowIndex);
                    }
                }
            }
            if(subRowIndexScope.size() != 0) {
                Express express = ifElseExpress.get(ifElseId);
                express.execute(expressionData, subRowIndexScope, parentUnMatchRowIndex);
            }

        }
        super.execute(expressionData, rowIndexScope, parentUnMatchRowIndex);
    }
}