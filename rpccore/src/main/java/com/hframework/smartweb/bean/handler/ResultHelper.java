package com.hframework.smartweb.bean.handler;

import com.hframework.common.util.DateUtils;
import com.hframework.common.util.RegexUtils;
import com.hframework.common.util.SpelExpressionUtils;
import com.hframework.smartweb.bean.SmartFormatter;
import com.hframework.smartweb.bean.SmartPattern;
import org.apache.commons.lang3.StringUtils;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.common.TemplateParserContext;
import org.springframework.expression.spel.standard.SpelExpressionParser;

import java.lang.reflect.Array;
import java.lang.reflect.ParameterizedType;
import java.math.BigDecimal;
import java.text.ParseException;
import java.util.*;

/**
 * Created by zhangquanhong on 2017/11/27.
 */
public class ResultHelper {

    public static String expressFunctionInvoke(String expr){
        String[] functions = RegexUtils.find(expr, "[a-z]+\\(\\)");
        for (String function : functions) {
            Calendar instance = Calendar.getInstance();
            if("year()".equals(function)) {
                expr = expr.replace("year()", String.valueOf(instance.get(Calendar.YEAR)));
            }else if("month()".equals(function)) {
                expr = expr.replace("month()", String.valueOf(instance.get(Calendar.MONTH) + 1));
            }
        }
        return expr;
    }

    /**
     * 单个对象单个属性的格式化与别名处理
     * @param objectMap
     * @param attr
     * @param alias
     * @param keepAttr
     * @param defaultValue
     * @param formatter
     * @param pattern
     * @param newAttrSet    @throws IllegalAccessException
     * @throws InstantiationException
     */
    public static Object doSmartAliasAndFormat(Map<String, Object> objectMap, String attr, String alias,
                                       boolean keepAttr, String defaultValue, Class<? extends SmartFormatter> formatter, String pattern,
                                       Set<String> newAttrSet) throws Exception {
        if(objectMap == null) {
            return null;
        }
        Object value = objectMap.get(attr);

        if(StringUtils.isNotBlank(defaultValue)) {
            if(value == null || (value instanceof String && StringUtils.isBlank((String)value))){
                if(defaultValue.trim().matches("\\$\\{[^{}]+\\}")) {
                    String expr = expressFunctionInvoke(defaultValue.trim().substring(2, defaultValue.trim().length() - 1));
                    ExpressionParser parser = new SpelExpressionParser();
                    value = parser.parseExpression("#{" + expr + "}",
                            new TemplateParserContext()).getValue();
                }else {
                    value = defaultValue;
                }

            }
        }

        if(formatter != null && !formatter.equals(SmartFormatter.class)) {
            Class actualTypeClass = null;
            if(formatter.getGenericInterfaces().length != 0 && formatter.getGenericInterfaces()[0] instanceof ParameterizedType) {
                actualTypeClass =  (Class)(((ParameterizedType)formatter.getGenericInterfaces()[0]).getActualTypeArguments()[0]);
            }else if(formatter.getGenericSuperclass() != null
                    && formatter.getGenericSuperclass() instanceof ParameterizedType
                    && ((ParameterizedType)formatter.getGenericSuperclass()).getActualTypeArguments().length != 0){
                actualTypeClass =  (Class)(((ParameterizedType)formatter.getGenericSuperclass()).getActualTypeArguments()[0]);
            }

            if(value != null && !(actualTypeClass.isAssignableFrom(value.getClass()))) {
                if(actualTypeClass.equals(BigDecimal.class)) {
                    value =new BigDecimal(String.valueOf(value));
                }else if(actualTypeClass.equals(Date.class)) {
                    value = DateUtils.parseYYYYMMDDHHMMSS((String) value);
                }else if(actualTypeClass.equals(Integer.class)) {
                    value = Integer.valueOf(String.valueOf(value));
                }else if(actualTypeClass.equals(Long.class)) {
                    value = Long.valueOf(String.valueOf(value));
                }
            }

            if(value != null && value.getClass().isArray()) {
                List listValue = new ArrayList();
                for (int i = 0; i < Array.getLength(value); i++) {
                    listValue.add(Array.get(value, i));
                }
                value = listValue;
            }

            if(StringUtils.isBlank(pattern)) {
                value = formatter.newInstance().format(value);
            }else {
                value = formatter.newInstance().format(value, pattern);
            }
        }
        if(objectMap.containsKey(attr)) {
            String newKey = StringUtils.isNotBlank(alias) ? alias : attr;
            if(!keepAttr) {
                objectMap.remove(attr);
            }
            objectMap.put(newKey, value);
            newAttrSet.add(newKey);
        }else if(value != null){
            String newKey = StringUtils.isNotBlank(alias) ? alias : attr;
            objectMap.put(newKey, value);
        }

        return value;
    }
}
