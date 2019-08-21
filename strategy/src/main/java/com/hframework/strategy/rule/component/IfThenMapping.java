package com.hframework.strategy.rule.component;

import com.hframework.strategy.rule.ExpressInvoker;
import com.hframework.strategy.rule.data.EDataSet;

import java.util.LinkedHashSet;
import java.util.Set;

/**
 * Created by zhangquanhong on 2017/6/26.
 */
public class IfThenMapping implements Component{//Condition
    private Express ifExpress;
    private Express thenExpress;

    public IfThenMapping(String ifExp, String thenExp) {
        ifExpress = new Express(ifExp);
        thenExpress = new Express(thenExp);
    }
    @Override
    public String toString() {
        return "if " + ifExpress.toString() + ": then " + thenExpress.toString() + ";";
    }


    public void explain(ExpressInvoker expressInvoker) {
        ifExpress.explain(expressInvoker);
        thenExpress.explain(expressInvoker);
    }
    public void print(){
        System.out.println("ifExpress =>" + ifExpress);
        ifExpress.print();
        System.out.println("thenExpress =>" + thenExpress);
        thenExpress.print();

    }

    public Set<Integer> match(EDataSet expressionData, Set<Integer> rowIndexScope) {
        if(rowIndexScope.isEmpty()){
            return new LinkedHashSet<Integer>();
        }
        ifExpress.execute(expressionData, rowIndexScope, rowIndexScope);
        expressionData.flushStageLabel();
        Set<Integer> matchRowIndex = new LinkedHashSet<Integer>();
        for (int i = 0; i < expressionData.getCurStageLabel().length; i++) {
            if(Boolean.valueOf(expressionData.getCurStageLabel()[i])){
                matchRowIndex.add(i);
            }
        }
        expressionData.setLastStepLabel(new String[expressionData.getData().length]);
        expressionData.setCurStageLabel(new String[expressionData.getData().length]);
        return matchRowIndex;
    }

    public String output(EDataSet expressionData, Set<Integer> curMatchRowIndex, Set<Integer> parentUnMatchRowIndex)  {
         thenExpress.execute(expressionData, curMatchRowIndex, parentUnMatchRowIndex);
        expressionData.flushStageLabel();
        expressionData.flushLabel();
        expressionData.setLastStepLabel(new String[expressionData.getData().length]);
        expressionData.setCurStageLabel(new String[expressionData.getData().length]);
        return null;
    }

    public Express getThenExpress() {
        return thenExpress;
    }

}