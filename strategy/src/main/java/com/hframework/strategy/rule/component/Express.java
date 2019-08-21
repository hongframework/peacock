package com.hframework.strategy.rule.component;

import com.google.common.base.Joiner;
import com.hframework.strategy.rule.ExpressInvoker;
import com.hframework.strategy.rule.component.function.DateFunction;
import com.hframework.strategy.rule.component.function.IFunction;
import com.hframework.strategy.rule.component.function.InFunction;
import com.hframework.strategy.rule.data.DataSetFiller;
import com.hframework.strategy.rule.data.EDataSet;
import com.hframework.strategy.rule.exceptions.ExpressExecuteException;
import com.hframework.common.util.RegexUtils;
import com.hframework.common.util.StringUtils;
import com.hframework.strategy.rule.repository.SyntaxParseUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.common.TemplateParserContext;
import org.springframework.expression.spel.standard.SpelExpressionParser;

import java.util.*;

/**
 * Created by zhangquanhong on 2017/6/26.
 */
public class Express implements Component{

    private static List<IFunction> functions = new ArrayList<>();
    static {
        functions.add(new DateFunction());
        functions.add(new InFunction());
    }

    private static Logger logger = LoggerFactory.getLogger(Express.class);
    private static final And AND = new And();
    private static final Or OR = new Or();
    private static final Non NON = new Non();


    protected String express;
    private String tmpColumnName;
    private List<Component> components = new ArrayList<Component>();
    private String[] vars;

    public Express(String express) {
        this.express = express;
    }
    public Express(String express, String tmpColumnName) {
        this.express = express;
        this.tmpColumnName = tmpColumnName;
    }


    @Override
    public String toString() {
        return express;
    }

    public static String[]  initVars(String express) {
        String tmpExpress = express;
        for (IFunction function : functions) {
            tmpExpress = function.removeTemp(tmpExpress);
        }
        String[] vars = RegexUtils.find(tmpExpress, "\\$[{]?[a-zA-Z]+[a-zA-Z0-9._]*[}]?");
        vars = new HashSet<String>(Arrays.asList(vars)).toArray(new String[0]);
        Arrays.sort(vars, new Comparator<String>() {
            public int compare(String o1, String o2) {
                return o1.contains(o2) ? -1 : 1;
            }
        });
        return vars;
    }

    public void explain(ExpressInvoker expressInvoker){
        vars = initVars(express);

        List<IfThenMapping> ifThenMappings = new ArrayList<>();
        if(StringUtils.isNotBlank(express) && express.toLowerCase().startsWith("if")){
            ifThenMappings = ExpressInvoker.parseIfThenMappings(express);
        }

        if(!ifThenMappings.isEmpty()) {
            components.addAll(ifThenMappings);
            for (IfThenMapping ifThenMapping : ifThenMappings) {
                ifThenMapping.explain(expressInvoker);
            }
        }else {
            String trimQuoteExpress = express;//trimQuote(express);
            List<String> componentStrings  = null;
            if(!trimQuoteExpress.contains("|") && !trimQuoteExpress.contains("&&")  && !trimQuoteExpress.contains("if(")) {
                return ;
            }
            while (true) {
                Map<Integer, String> matchIndexMapping = SyntaxParseUtils.getMatchIndexMapping(trimQuoteExpress, "[\\(\\)]+|[|&]+");
                componentStrings = new ArrayList<String>();
                int count = 0;
                int nextStartIndex = 0;
                for (Map.Entry<Integer, String> matchInfo : matchIndexMapping.entrySet()) {
                    Integer index = matchInfo.getKey();
                    String match = matchInfo.getValue();
                    if('(' == match.charAt(0)) {
                        for (char ca : match.toCharArray()) {
                            if('(' == ca) {
                                count ++;
                            }else if(')' == ca) {
                                count --;
                            }
                        }
                    }else if(')' == match.charAt(0)) {
                        count -= match.length();
                    }else if('|' == match.charAt(0) || '&' == match.charAt(0)) {
                        if(count == 0) {
                            componentStrings.add(trimQuoteExpress.substring(nextStartIndex, index));
                            componentStrings.add(match);
                            nextStartIndex = index + match.length();
                        }
                    }
                }
                if(nextStartIndex != trimQuoteExpress.length()) {
                    componentStrings.add(trimQuoteExpress.substring(nextStartIndex));
                }
                if(componentStrings.size() == 1 && componentStrings.get(0).startsWith("(")
                        && componentStrings.get(0).endsWith(")")) {
                    String tmpTrimQuoteExpress = SyntaxParseUtils.trimQuoteInPairs(trimQuoteExpress);
                    if(trimQuoteExpress.equals(tmpTrimQuoteExpress)) {//case:（a+b)+(b+c)
                        break;
                    }
                    trimQuoteExpress = tmpTrimQuoteExpress;
                    continue;
                }
                break;
            }

            //表明该表达式不包含&&，||逻辑运算符，但是表达式存在if语句的运算,比如表达式（if(a>b): 1 else: 2) * 1.2
            if(componentStrings.size() == 1 && express.equals(componentStrings.get(0))) {
                List<String> strings = ExpressInvoker.parseIfElseString(express);
                String executeExpress = express;
                Map<String, Express> ifElseExpress = new LinkedHashMap<>();
                for (String ifThenString : strings) {
                    int ifIndex = expressInvoker.getGlobalNestingIfId(ifThenString);
                    String ifVarName = "$ifSentence" + ifIndex;
                    String ifName = "ifSentence" + ifIndex;
                    ifElseExpress.put(ifName, new Express(ifThenString, ifName));
                    executeExpress = executeExpress.replace(ifThenString, ifVarName);
                }
//                for (int i = 0; i < strings.size(); i++) {
//                    String ifThenString = strings.get(i);
//                    int globalNestingIfId = expressInvoker.getGlobalNestingIfId(ifThenString);
//                    String ifVarName = "$ifSentence" + i;
//                    String ifName = "ifSentence" + i;
//                    ifElseExpress.put(ifVarName, new Express(strings.get(i), ifName));
//                    executeExpress = executeExpress.replace(strings.get(i), ifVarName);
//                }
                executeExpress = SyntaxParseUtils.trimQuoteOnVars(executeExpress);
                System.out.println(executeExpress);
                NestingExpress nestingExpress = new NestingExpress(express, executeExpress, ifElseExpress);
                components.add(nestingExpress);
                nestingExpress.explain(expressInvoker);
            }else {
                for (String componentString : componentStrings) {
                    if("&&".equals(componentString)) {
                        components.add(AND);
                    }else if("||".equals(componentString)) {
                        components.add(OR);
                    }else {
                        Express express = new Express(componentString);
                        components.add(express);
                        express.explain(expressInvoker);
                    }
                }
            }


        }
    }

    public void print() {
        for (Component component : components) {
            System.out.print(component);
            System.out.print(", ");
        }
        System.out.println();
        System.out.print("==> ");
        for (Component component : components) {
            if (component instanceof Express) {
                Express component1 = (Express) component;
                component1.print();
            }
        }
    }

    public void execute(EDataSet expressionData, Set<Integer> rowIndexScope, Set<Integer> parentUnMatchRowIndex) {
        if(rowIndexScope.isEmpty()){
            return;
        }
        if(StringUtils.isNotBlank(tmpColumnName) && !expressionData.containDataFieldCode(tmpColumnName)) {
            expressionData.addDataFieldAndFillNull(tmpColumnName);
            expressionData.addDescription(tmpColumnName, "temp result => " + express);
        }
        Integer tmpFieldIndex = StringUtils.isNotBlank(tmpColumnName) ? expressionData.getFieldIndex(tmpColumnName) : -1;
        if(components.isEmpty()) {
            DataSetFiller.autoFullDataSet(expressionData, vars, parentUnMatchRowIndex);
            Map<String, Integer> varIndexMap = new LinkedHashMap<String, Integer>();
            for (String varString : vars) {
                 String fieldCode = varString.replaceAll("[${}]+", "");
                Integer index = expressionData.getFieldIndex(fieldCode);
                if(index < 0) {
                    throw new ExpressExecuteException("data for var " + varString + " not exists!");
                }
                varIndexMap.put(varString, index);
            }
            List<Object>[] data = expressionData.getData();
            for (int i = 0; i < data.length; i++) {
                //如果已经计算出结果，则不再进行表达式运算
                if(!rowIndexScope.contains(i) || expressionData.getCurStageLabel()[i] != null) continue;
                List<Object> row = data[i];
                String tmpExpress = express;
                for (Map.Entry<String, Integer> varIndex : varIndexMap.entrySet()) {
                    String varString = varIndex.getKey();
                    Integer index = varIndex.getValue();
                    String value = String.valueOf(row.get(index));
                    if(RegexUtils.find(String.valueOf(row.get(index)),"[^0-9\\.\\-]+").length > 0) {
                        value = "'" + value + "'";
                    }

                    if(StringUtils.isBlank(value)) {
                        value = "null";
                    }

                    tmpExpress = tmpExpress.replaceAll("\\" + varString, value);
                }

                for (IFunction function : functions) {
                    tmpExpress = function.execute(tmpExpress);
                }

//                if(tmpExpress.contains("now(") || tmpExpress.contains("month(") || tmpExpress.contains("week(")||tmpExpress.contains("day(") || tmpExpress.contains("hour(")) {
//                    String[] timeInfos = RegexUtils.find(tmpExpress, "((now)|(month)|(week)|(day)|(hour))\\(\\d*\\)");
//                    for (String timeInfo : timeInfos) {
//                        String keyword = timeInfo.substring(0, timeInfo.indexOf("("));
//                        String number = timeInfo.substring(timeInfo.indexOf("(") + 1, timeInfo.indexOf(")"));
//                        if(StringUtils.isBlank(number)) number = "1";
//                        Long target = 0L;
//                        if("now".equals(keyword)) {
//                            target = System.currentTimeMillis()/1000;
//                        }else if("month".equals(keyword)) {
//                            target = 30 * 24 * 60 * 60 * Long.valueOf(number);
//                        }else if("week".equals(keyword)) {
//                            target = 7 * 24 * 60 * 60 * Long.valueOf(number);
//                        }else if("day".equals(keyword)) {
//                            target = 24 * 60 * 60 * Long.valueOf(number);
//                        }else if("hour".equals(keyword)) {
//                            target = 60 * 60 * Long.valueOf(number);
//                        }
//                        tmpExpress = tmpExpress.replaceAll(timeInfo.replace("(","\\(").replace(")","\\)"), String.valueOf(target));
//                    }
////                    System.out.println("=========>" + tmpExpress);
//                }
                try {
                    if(tmpExpress.contains(",") && express.contains(",")) {
                        List<Object> result = new ArrayList<>();
                        String[] tmpExpresses = tmpExpress.split(",");
                        for (String express : tmpExpresses) {
                            result.add(SpelExpressionUtils.executeObject(express));
                        }
                        expressionData.getLastStepLabel()[i] = Joiner.on(",").join(result);
                    }else {
                        String result = String.valueOf(SpelExpressionUtils.execute(tmpExpress));
                        logger.debug("execute: {} | {} => {}", express, tmpExpress, result);
                        expressionData.getLastStepLabel()[i] = result;
                    }

                }catch (Exception e) {
                    System.out.println(expressionData);
                    throw new ExpressExecuteException("执行表达式[ " + tmpExpress + " ]失败!");
                }
            }
        }else {
            Set<Integer> unMatchRowIndex = new LinkedHashSet<Integer>(rowIndexScope);
            for (Component component : components) {
                if(component instanceof Express) {
                    ((Express) component).execute(expressionData, rowIndexScope, parentUnMatchRowIndex);
                }else if(component instanceof IfThenMapping) {
                    IfThenMapping ifThenMapping = (IfThenMapping) component;
                    Set<Integer> curMatchRowIndex = ifThenMapping.match(expressionData, unMatchRowIndex);
                    if(!curMatchRowIndex.isEmpty()) {
                        ifThenMapping.output(expressionData, curMatchRowIndex, curMatchRowIndex);
                        unMatchRowIndex.removeAll(curMatchRowIndex);
                    }
                }else if(component instanceof And) {
                    boolean isOk = true;
                    String[] label = expressionData.getLastStepLabel();
                    for (int i = 0; i < label.length; i++) {
                        if(label[i] == null) continue;
                        if(Boolean.valueOf(label[i]) == false){
                            expressionData.getCurStageLabel()[i] = String.valueOf(false);
                        }else{
                            isOk = false;
                        }
                    }
                    expressionData.setLastStepLabel(new String[expressionData.getData().length]);
                    if(isOk) return;
                }else if(component instanceof Or) {
                    boolean isOk = true;
                    String[] label = expressionData.getLastStepLabel();
                    for (int i = 0; i < label.length; i++) {
                        if(label[i] == null) continue;
                        if(Boolean.valueOf(label[i]) == true){
                            expressionData.getCurStageLabel()[i] = String.valueOf(true);
                        }else{
                            isOk = false;
                        }
                    }
                    expressionData.setLastStepLabel(new String[expressionData.getData().length]);
                    if(isOk) return;
                }
            }
        }

        if(tmpFieldIndex > 0) {
            for (Integer i : rowIndexScope) {//如果别名为if代码块，由于if内部条件命中和，lastStepLabel转化为label
                if(expressionData.getLabel()[i] != null ) {
                    expressionData.getData()[i].set(tmpFieldIndex, expressionData.getLabel()[i]);
                    expressionData.clearLabel(i);
                }else {
                    //TODO， 是否存在这种场景？
                    expressionData.getData()[i].set(tmpFieldIndex, expressionData.getLastStepLabel()[i]);
                }
            }
        }
    }

    public static class SpelExpressionUtils {

        private static final Logger logger = LoggerFactory.getLogger(SpelExpressionUtils.class);

        public static boolean check(String express, Map<String, Object> data) throws Exception {
            return Boolean.parseBoolean(String.valueOf(execute(express, data)));
        }

        public static Object executeSenior(String express, Map<String, Object> data) throws Exception {
            String[] vars = RegexUtils.find(express, "\\$[a-zA-Z][a-zA-Z0-9_]*");
            for (String var : vars) {
                Object value = data.get(var.substring(1));
                express = express.replace(var, String.valueOf(value));
            }

            String[] exprs = RegexUtils.find(express, "\\$\\{[^{}]+\\}");
            for (String expr : exprs) {
                Object value = executeSenior(expr.substring(2, expr.length() - 1), data);
                express = express.replace(expr, String.valueOf(value));
            }

            return execute(express, data);
        }


        public static Object execute(String express, Map<String, Object> data) throws Exception {
            if(StringUtils.isNotBlank(express)) {
                //去掉字符常量，同时必须以字母开头的才会是变量
                String[] vars = RegexUtils.find(express
                                .replaceAll("'[^']*'", "")
                                .replaceAll(".substring\\(", "(")
                                .replaceAll(".contains\\(", "(")
                                .replaceAll(".split\\(", "(")
                                .replaceAll(".parseInt\\(", "(")
                                .replaceAll(".length\\(", "(")
                                .replaceAll(".trim\\(", "(")
                                .replaceAll("T\\([a-zA-Z.]+\\)", "")
                        , "[a-zA-Z]+[a-zA-Z0-9._]*");
                Arrays.sort(vars, new Comparator<String>() {
                    public int compare(String o1, String o2) {
                        return o1.contains(o2) ? -1 : 1;
                    }
                });
                return execute(data, express, vars);
            }

            return null;
        }
        private static Object execute(Map<String, Object> data, String filterExp, String[] vars) throws Exception {
            String resultExp = filterExp;
            try{
                if(StringUtils.isBlank(filterExp)) {
                    return null;
                }

                if(vars != null && vars.length > 0) {
                    for (String var : vars) {
                        resultExp = resultExp.replaceAll(var,getValValueFromData(var, data));
                    }
                }

                return execute(resultExp);
            }catch (Exception e) {
                logger.error("SPEL execute failed, [filterExp = {}, resultExp = {}]", filterExp, resultExp);
                throw new Exception(resultExp);
            }
        }

        public static Object execute(String express){
            ExpressionParser parser = new SpelExpressionParser();
//            String value = parser.parseExpression("#{" + express + "}",
//                    new TemplateParserContext()).getValue(String.class);
            Object value = parser.parseExpression("#{" + express + "}",
                    new TemplateParserContext()).getValue();
//            logger.debug("execute: {} => {}", express, value);
            return value;
        }

        public static Object executeObject(String express){
            ExpressionParser parser = new SpelExpressionParser();
            return parser.parseExpression("#{" + express + "}",
                    new TemplateParserContext()).getValue();
        }

        private static String getValValueFromData(String var, Map<String, Object> data) {
            if(data.get(var) != null) {
                if (data.get(var) instanceof String) {
                    return "'" + String.valueOf(data.get(var)) + "'" ;
                }
            }
            return String.valueOf(data.get(var));
        }

        public static void main(String[] args) throws Exception {
            String express = "add_time != orig.add_time &&  !(1>2) && '2015-12-12 12:12:21' > '2015-12-12 12:12:12' ";


//        express = "(1>2) && '2015-12-12 12:12:21' > '2015-12-12 12:12:12' || '2015-12-12 12:12:22' != '2015-12-12 12:12:12'";

            System.out.println(check(express, new HashMap<String, Object>() {{
                put("add_time", "2015-12-12 12:12:12");
                put("orig.add_time", "2015-12-12 12:12:11");
            }}));

            System.out.println(execute(express, new HashMap<String, Object>() {{
                put("add_time", "2015-12-12 12:12:11");
                put("orig.add_time", "2015-12-12 12:12:11");
            }}));

            System.out.println(execute("count * 3", new HashMap<String, Object>() {{
                put("count", 10);
            }}));
        }
    }

}