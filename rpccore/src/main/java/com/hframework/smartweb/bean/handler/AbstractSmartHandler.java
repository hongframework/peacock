package com.hframework.smartweb.bean.handler;

import com.hframework.beans.exceptions.BusinessException;
import com.hframework.common.util.RegexUtils;
import com.hframework.common.util.SpelExpressionUtils;
import com.hframework.smartweb.bean.SmartHandler;
import com.hframework.smartweb.annotation.Handler;
import com.hframework.smartweb.SmartHandlerFactory;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.beans.factory.InitializingBean;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.*;

/**
 * 扩展处理抽象实现
 * Created by zhangquanhong on 2017/2/21.
 */
public abstract class AbstractSmartHandler implements InitializingBean, SmartHandler {


    public void afterPropertiesSet() throws Exception {
//        Type genType = this.getClass().getGenericSuperclass();
//        Type[] params = ((ParameterizedType) genType).getActualTypeArguments();

        Class entryClass = this.getClass();

        //避免同一个类重复加载
        if(!SmartHandler.class.isAssignableFrom(entryClass) || SmartHandlerFactory.contain(entryClass)){
            return ;
        }

        Class handlerClass = this.getClass();
        Annotation classAnnotation = handlerClass.getAnnotation(Handler.class);
        Method[] declaredMethods = handlerClass.getDeclaredMethods();
        for (Method method : declaredMethods) {
            Annotation[] annotations = method.getAnnotations();
            for (Annotation annotation : annotations) {
                if (annotation instanceof Handler) {

                    SmartHandlerFactory.addHandler(entryClass,  method, (Handler)classAnnotation, (Handler)annotation);
                }
            }
        }

    }

    public static Pair<String, Object[]> recombinationSqlAndParameters(String sql, String[] queryFields, Object... parameterValues) {
        String[] vars = RegexUtils.find(sql, "\\$\\{[^}]+\\}");
        Object[] newParameterValues = null;
        if(vars != null && vars.length > 0) {
            List<String> tempList = Arrays.asList(queryFields);
            sql = sql.replaceAll("\\$?\\$\\{[^}]+\\}", "?");
//            List<String> distinctVars = Lists.newArrayList(Sets.newLinkedHashSet(Lists.newArrayList(vars)));
//            if(distinctVars.size() != parameterValues.length) {
//                throw new BusinessException("db query more , parameters not matches !");
//            }
            newParameterValues = new Object[vars.length];
            for (int i = 0; i < vars.length; i++) {
                String valueName = vars[i].substring(vars[i].indexOf("{") +1, vars[i].indexOf("}"));
                if(tempList.contains(valueName)) {
                    newParameterValues[i] = parameterValues[tempList.indexOf(valueName)];
                }else {
                    Map<String, Object> tempMap = new HashMap<>();
                    for (int j = 0; j < queryFields.length; j++) {
                        tempMap.put(queryFields[j], parameterValues[j]);
                    }
                    try {
                        valueName = ResultHelper.expressFunctionInvoke(valueName);
                        Long result = Long.valueOf(SpelExpressionUtils.execute(valueName, tempMap));
                        newParameterValues[i] = result;
                    } catch (Exception e) {
                        e.printStackTrace();
                        throw new BusinessException("db query more , parameters not matches !");
                    }
                }

            }
        }else {
            int paramLength = RegexUtils.find(sql, "\\?").length;
            if(paramLength == parameterValues.length) {
                newParameterValues = parameterValues;
            }else if(paramLength < parameterValues.length) {
                newParameterValues = Arrays.copyOf(parameterValues, paramLength);
            }else {
                throw new RuntimeException("parameter count not equal !");
            }

        }
        return new ImmutablePair<>(sql, newParameterValues);
    }

//    public static <T> T parseParameter(Object value, Class<T> t) {
//        if(value == null) {
//            return null;
//        }else if(value.getClass().isAssignableFrom(t)){
//            return (T) value;
//        }else if(t.isAssignableFrom(Integer.class)){
//            return (T) Integer.valueOf(String.valueOf(value));
//        }else if(t.isAssignableFrom(Long.class)){
//            return (T) Long.valueOf(String.valueOf(value));
//        }else if(t.isAssignableFrom(Double.class)){
//            return (T) Double.valueOf(String.valueOf(value));
//        }else if(t.isAssignableFrom(Float.class)){
//            return (T) Float.valueOf(String.valueOf(value));
//        }else if(t.isAssignableFrom(String.class)){
//            return (T) String.valueOf(value);
//        }else {
//            throw new SmartHandlerException("smart handler's parameter type transform not support, from " + value.getClass().getSimpleName()+ " to " + t.getSimpleName() + "!");
//        }
//    }

}
