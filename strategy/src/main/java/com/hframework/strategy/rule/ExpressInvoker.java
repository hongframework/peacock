package com.hframework.strategy.rule;

import com.hframework.strategy.rule.component.Express;
import com.hframework.strategy.rule.component.IfThenMapping;
import com.hframework.strategy.rule.data.DataSetFiller;
import com.hframework.strategy.rule.data.EDataSet;
import com.hframework.common.util.RegexUtils;
import com.hframework.common.util.StringUtils;
import com.hframework.strategy.rule.exceptions.RuleInitializeException;
import com.hframework.strategy.rule.repository.SyntaxParseUtils;

import java.util.*;

/**
 * Created by zhangquanhong on 2017/6/26.
 */
public class ExpressInvoker<T>{
    private boolean loadAvailable = true;
    private boolean loadLazy = true;
    private String rule;
    private List<IfThenMapping> ifThenMappings = new ArrayList<IfThenMapping>();
    private String[] vars;

    private List<String> nestingIfThen = new ArrayList<>();

    private ExpressInvoker(String info) {
        this.rule = info;
    }
    private ExpressInvoker(String info, boolean loadLazy) {
        this.rule = info;
        this.loadLazy = loadLazy;
    }
    private ExpressInvoker(String info, boolean loadAvailable, boolean loadLazy) {
        this.rule = info;
        this.loadAvailable = loadAvailable;
        this.loadLazy = loadLazy;
    }

    public boolean isLoadAvailable() {
        return loadAvailable;
    }

    public void setLoadAvailable(boolean loadAvailable) {
        this.loadAvailable = loadAvailable;
    }

    public boolean isLoadLazy() {
        return loadLazy;
    }

    public void setLoadLazy(boolean loadLazy) {
        this.loadLazy = loadLazy;
    }

    @Override
    public String toString() {
        return rule;
    }

    public static ExpressInvoker load(String rule) {
        ExpressInvoker expressInvoker = new ExpressInvoker(rule.trim());
        return expressInvoker;
    }

    public static ExpressInvoker load(String rule, boolean loadLazy) {
        ExpressInvoker expressInvoker = new ExpressInvoker(rule.trim(),loadLazy);
        return expressInvoker;
    }

    public static ExpressInvoker load(String rule, boolean loadAvailable, boolean loadLazy) {
        ExpressInvoker expressInvoker = new ExpressInvoker(rule.trim(),loadAvailable, loadLazy);
        return expressInvoker;
    }

    public void print(){
        for (IfThenMapping ifThenMapping : ifThenMappings) {
            System.out.println(ifThenMapping);
            ifThenMapping.print();
        }
    }

    public List<String> thenExpresses(){
        List<String> result = new ArrayList<>();
        for (IfThenMapping ifThenMapping : ifThenMappings) {
            result.add(String.valueOf(Express.SpelExpressionUtils.execute(ifThenMapping.getThenExpress().toString())));
        }
        return result;
    }

    public void explain() {

        String compressRule = rule.replaceAll("[\"\']+[ ]+[\"']+", "#nbsp#").replaceAll("\\s", "").replaceAll("#nbsp#", "\" \"");
        if(compressRule.startsWith("if") || compressRule.startsWith("IF")) {
//                String[] parts = compressRule.substring(2).split("((else(if)?)|(ELSE(IF)?))");
            ifThenMappings.addAll(parseIfThenMappings(compressRule));

        }else {
            ifThenMappings.add(new IfThenMapping("true", compressRule));
        }

        for (IfThenMapping ifThenMapping : ifThenMappings) {
            ifThenMapping.explain(this);
        }
        //初始化变量列表，当loadLazy = true时，将对变量列表所有变量值进行检查
        vars = Express.initVars(compressRule);

    }


    public static List<IfThenMapping> parseIfThenMappings(String compressRule) {
        try{
            List<IfThenMapping> ifThenMappings = new ArrayList<IfThenMapping>();
            List<String> ifThenMappingString = parseIfThenMappingString(compressRule);
            for (String ifThenMappingStr : ifThenMappingString) {
                String condition = null;
                String result = null;
                if(ifThenMappingStr.contains("{") && ifThenMappingStr.contains("}")) {//TODO 关于嵌套问题，需要考虑条件中嵌套{时，那么这样解析就存在问题
                    condition = ifThenMappingStr.substring(0, ifThenMappingStr.indexOf("{"));
                    result = ifThenMappingStr.substring(ifThenMappingStr.indexOf("{") + 1, ifThenMappingStr.lastIndexOf("}"));
                }else if(ifThenMappingStr.contains(":")){
                    int ifThenSplitIndex = SyntaxParseUtils.findIfThenSplitIndex(ifThenMappingStr);
                    condition = ifThenMappingStr.substring(0, ifThenSplitIndex);
                    result = ifThenMappingStr.substring(ifThenSplitIndex + 1);

                }else {
                    result = ifThenMappingStr;
                }
                if(StringUtils.isBlank(condition)) {
                    condition = "true";
                }
                condition = SyntaxParseUtils.trimQuoteInPairs(condition);
                result = SyntaxParseUtils.trimQuoteInPairs(result);

    //            if(condition.startsWith("(") && condition.endsWith(")")){
    //                condition = condition.substring(1, condition.length() - 1);
    //            }
    //            if(result.startsWith("(") && result.endsWith(")")){
    //                result = result.substring(1, result.length() - 1);
    //            }
                ifThenMappings.add(new IfThenMapping(condition, result));
            }
            return ifThenMappings;
        }catch (Exception e) {
            System.out.println(compressRule);
            throw e;
        }
    }

    public static List<String> parseIfThenMappingString(String compressRule) {
        Map<Integer, String> indexKeywordMapping = SyntaxParseUtils.getMatchIndexMapping(compressRule, "((else(if)?)|(ELSE(IF)?)|(IF)|(if))");
        return parseIfThenMappingString(compressRule, indexKeywordMapping);
    }

    public static List<String> parseIfElseString(String compressRule) {
        Map<Integer, String> indexKeywordMapping = SyntaxParseUtils.getMatchIndexMapping(compressRule, "((else(if)?)|(ELSE(IF)?)|(IF)|(if))");
        return parseIfElseString(compressRule, indexKeywordMapping);
    }

    public static List<String> parseIfElseString(String compressRule, Map<Integer, String> indexKeywordMapping) {
        List<String> result = new ArrayList<String>();
        Integer count = 0;
        Integer ifStartIndex = 0;
        for (Map.Entry<Integer, String> indexKeywordMap : indexKeywordMapping.entrySet()) {
            Integer index = indexKeywordMap.getKey();
            String keyword = indexKeywordMap.getValue();
            if("if".equals(keyword.toLowerCase())) {
                if(count++ == 0) {
                    ifStartIndex = index;
                }
            }else if ("else".equals(keyword.toLowerCase())) {
                if(--count == 0) {
                    String elsePart = compressRule.substring(index + 4);
                    elsePart = SyntaxParseUtils.removeInvalidEndOfQuote(elsePart);
                    int ifEndIndex = index + 4 + elsePart.length();
                    result.add(compressRule.substring(ifStartIndex, ifEndIndex));
                }
            }
        }
        if(count != 0) {
            throw new RuleInitializeException("if-else struct [" + compressRule + "] is not closed !");
        }

        if(result.size() == 0) {
            throw new RuleInitializeException("if-else struct [" + compressRule + "] is not exists !");
        }

        return result;
    }


    public static List<String> parseIfThenMappingString(String compressRule, Map<Integer, String> indexKeywordMapping) {
        List<String> result = new ArrayList<String>();
        Integer count = 0;
        Integer nextPartIndex = 0;
        for (Map.Entry<Integer, String> indexKeywordMap : indexKeywordMapping.entrySet()) {
            Integer index = indexKeywordMap.getKey();
            String keyword = indexKeywordMap.getValue();
            if("if".equals(keyword.toLowerCase())) {
                if(count++ == 0) {
                    nextPartIndex +=2 + index;
                }
            }else if ("else".equals(keyword.toLowerCase())) {
                if(--count == 0) {
                    String elsePart = compressRule.substring(index + 4);
                    elsePart = SyntaxParseUtils.removeInvalidEndOfQuote(elsePart);
                    result.add(compressRule.substring(nextPartIndex, index));
                    result.add(elsePart);
                    break;//一次只解析对一个if-else分支，因为else分支中还存在if-else结构，所以这里直接break，否则接下来的循环又将获取else中的if-then分支
                }
            }else if("elseif".equals(keyword.toLowerCase())) {
                if(--count == 0) {
                    result.add(compressRule.substring(nextPartIndex, index));
                    nextPartIndex = index + 6;
                }
                count ++;
            }
        }
        if(count != 0) {
            throw new RuleInitializeException("if-else  struct [" + compressRule + "] is not closed !");
        }

        return result;
    }


    public EDataSet invoke(EDataSet expressionData){
        expressionData.setLabel(new String[expressionData.getData().length]);
        expressionData.setLastStepLabel(new String[expressionData.getData().length]);
        expressionData.setCurStageLabel(new String[expressionData.getData().length]);

        DataSetFiller.setFetcherOverview(expressionData, vars);
        if(!isLoadLazy()){
            DataSetFiller.autoFullDataSet(expressionData, vars, null);
        }
        Set<Integer> unMatchRowIndex = new LinkedHashSet<Integer>();
        for (int i = 0; i < expressionData.getData().length; i++) {
            unMatchRowIndex.add(i);
        }

        for (IfThenMapping ifThenMapping : ifThenMappings) {
            Set<Integer> curMatchRowIndex = ifThenMapping.match(expressionData, unMatchRowIndex);
            if(!curMatchRowIndex.isEmpty()) {
//                ifThenMapping.output(expressionData, curMatchRowIndex);
                ifThenMapping.output(expressionData,curMatchRowIndex, unMatchRowIndex);//因为结果中使用的变量可能下面的分支数据依然要获取，所以这里就全部获取
                unMatchRowIndex.removeAll(curMatchRowIndex);
            }

        }
        return expressionData;
    }

    public String[] getVars() {
        return vars;
    }

    public int getGlobalNestingIfId(String ifThenString) {
        if(!nestingIfThen.contains(ifThenString)) {
            nestingIfThen.add(ifThenString);
        }
        return nestingIfThen.indexOf(ifThenString);
    }
}