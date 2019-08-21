package com.hframework.smartweb.bean.handler;

import com.google.common.base.Joiner;
import com.google.common.collect.Lists;
import com.hframework.beans.exceptions.BusinessException;
import com.hframework.common.util.collect.CollectionUtils;
import com.hframework.common.util.collect.bean.Fetcher;
import com.hframework.peacock.controller.base.dc.*;
import com.hframework.smartweb.APIErrorType;
import com.hframework.smartweb.SmartHandlerFactory;
import com.hframework.smartweb.bean.*;
import com.hframework.smartweb.exception.SmartHandlerException;
import com.hframework.springext.argument.SmartParameterResolver;
import com.hframework.tracer.PeacockSpan;
import com.hframework.tracer.TracerFactory;
import com.hframework.peacock.controller.base.dc.*;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;

import java.lang.reflect.*;
import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

/**
 * Created by zhangquanhong on 2017/6/8.
 */
public class HandlerHelper {

    /**
     * 扩展单个map数据对象
     * @param properties 源map数据
     * @return 扩展数据Map
     */
    public static Object handle(SmartHandler smartHandler, Method method, String[] fromProperties, final String[] toProperties, String patternString, Map<String, Object> properties, PeacockSpan span) {
        if(fromProperties == null || fromProperties.length == 0) {
            throw new SmartHandlerException("smart handler's attr properties not exists !");
        }

        if(toProperties == null || toProperties.length == 0) {
            throw new SmartHandlerException("smart handler's newAttr properties not exists !");
        }



        List<Object> fromValues = new ArrayList<>();
        for (int i = 0; i < fromProperties.length; i++) {
            String fromProperty = fromProperties[i];
            if(StringUtils.isBlank(fromProperty)){
                continue;
            }
            Object value = null;
            if(properties.containsKey(fromProperty)) {
                value = properties.get(fromProperty);
            }else if(properties.containsKey(SmartParameterResolver.SMART_WEB_PARAMETER_PREFIX + fromProperty)) {
                value = properties.get(SmartParameterResolver.SMART_WEB_PARAMETER_PREFIX + fromProperty);
            }else {
                throw new SmartHandlerException(APIErrorType.SERVER_ERROR, "while executing expand for " +
                        "" + toProperties[0] + ", from property of " + fromProperty + " not exist !");
            }
            fromValues.add(transformType(value, method.getParameterTypes()[i]));
        }

        if(fromValues != null) {
            try {
                TracerFactory.addTag(span, TracerFactory.TagKey.LOCAL_EXECUTE_PARAMETERS, fromValues, span.hasIORequestTag());
                TracerFactory.addSpanAnnotation(span, TracerFactory.Annotation.PARAMETER_PARSE);
                Object returnObject = handle(smartHandler, method, fromValues.toArray(new Object[0]));
                TracerFactory.addSpanAnnotation(span, TracerFactory.Annotation.HANDLER_INVOKE);
                if(toProperties.length == 1 && toProperties[0].endsWith("[]") && returnObject != null && returnObject instanceof CombineResult){
                    returnObject = ((CombineResult) returnObject).getData();
                }
                if(toProperties.length == 1 && "${RUNTIME.RESULT}".equals(toProperties[0])) {
                    return returnObject;
                }else if(returnObject != null && returnObject instanceof List) {
                    if(toProperties.length == 1 && toProperties[0].endsWith("[]")) {//需要已数组形式返回
                        final String toProperty = toProperties[0];
                        final List<Object> arrays = new ArrayList<>();
                        for (Object o : (List)returnObject) {
                            arrays.add(getPropertyValue(o, toProperty.substring(0, toProperty.length() -2)));
                        }
                        return new HashMap<String, Object>(){{
                            put(toProperty.substring(0, toProperty.length() -2), arrays.toArray(new Object[0]));
                        }};
                    }else {
                        List<Map<String, Object>> result = new ArrayList<>();
                        for (Object o : (List)returnObject) {
                            result.add(filterResultMap(o, toProperties));
                        }
                        return result;
                    }

                }else if(returnObject != null && returnObject instanceof CombineResult) {
                    List<Map<String, Object>> result = new ArrayList<>();
                    CombineResult combineResult = (CombineResult) returnObject;
                    List<Map<String, Object>> data = combineResult.getData();
                    List<String> dataProperties = new ArrayList<>();
                    List<String> metaProperties = new ArrayList<>();
                    for (String toProperty : toProperties) {
                        if(toProperty.startsWith("../")) {
                            metaProperties.add(toProperty.substring(3));
                        }else {
                            dataProperties.add(toProperty);
                        }
                    }
                    String[] toDataProperties = dataProperties.toArray(new String[0]);
                    if(data != null) {
                        for (Map<String, Object> datum : data) {
                            result.add(filterResultMap(datum, toDataProperties));
                        }
                    }
                    DC listDC = DCUtils.valueOf(result);

                    HashMap<String, Object> meta = new HashMap<>();
                    if(metaProperties.contains("offsetA")) {
                        meta.put("offsetA", combineResult.getOffsetA());
                    }
                    if(metaProperties.contains("offsetB")) {
                        meta.put("offsetB", combineResult.getOffsetB());
                    }
                    if(!meta.isEmpty()) {
                        DC metaDc = DCUtils.valueOf(meta);
                        listDC.setPrevDc(metaDc);
                    }
                    return listDC;
                }else {
                    Map<String, Object> result = filterResultMap(returnObject, toProperties);
                    return result.size() == 0 ? null : result;
                }
            }catch (Exception e) {
                if(e instanceof BusinessException) {
                    throw (BusinessException)e;
                }else if(e instanceof InvocationTargetException && ((InvocationTargetException)e).getTargetException() instanceof BusinessException) {
                    throw (BusinessException)((InvocationTargetException)e).getTargetException();
                }else {
                    e.printStackTrace();
                    throw new SmartHandlerException(APIErrorType.SERVER_ERROR, "invoke handler method error ! handler = " + method.getDeclaringClass().getSimpleName() + "; method = " + method.getName() + "; errorInfo = " + e.getMessage() );
                }

            }
        }
        return null;
    }

    /**
     * 扩展批量map数据对象
     * @param properties 源map数据
     * @return 扩展数据Map列表
     * @throws ExecutionException
     * @throws InterruptedException
     */
    public static DC batchHandle(ExecutorService executorService, SmartHandler smartHandler, final Method method, final Method batchMethod,
                                                        final String[] fromProperties, final String[] toProperties, String patternString,
                                                        final DC properties, String[] requestNames, PeacockSpan parentSpan) throws ExecutionException, InterruptedException {


        if(fromProperties == null || fromProperties.length == 0) {
            throw new SmartHandlerException("smart handler's attr properties not exists !");
        }

        if(toProperties == null || toProperties.length == 0) {
            throw new SmartHandlerException("smart handler's newAttr properties not exists !");
        }

        String implicitField = null;
        int implicitParameterLength = 1;
        if(smartHandler instanceof ImplicitSmartHandler && DCUtils.isMapDC(properties)) {
            Map<String, Object> map = (Map<String, Object>) properties.getData();
            for (String field : map.keySet()) {
                Object o = map.get(field);
                if(o != null && o.getClass().isArray()) {//表明这里为二级隐式参数
                    implicitField = field;
                    implicitParameterLength = calcImplicitParametersLength(o);
                }
            }
        }
        //初始化最终返回结果对象列表
        List<Object[]> fromValues = new ArrayList<>();
        List<Object[]> distinctFromValues =null;
        DC batchDC = null;

        if(StringUtils.isNoneBlank(implicitField) && implicitParameterLength > 1 && batchMethod != null) {//隐式存在批量方法，走批量方法
            Object arrayValues = ((MapDC) properties).getData().get(implicitField);//取出隐式业务参数
            int parameterNumbers = Array.getLength(arrayValues);
            for (int i = 0; i < implicitParameterLength; i++) {
                fromValues.add(new Object[parameterNumbers]);
            }

            for (int i = 0; i < parameterNumbers; i++) {//i代表所有参数
                for (int j = 0; j < implicitParameterLength; j++) {//j代表重复参数
                    fromValues.get(j)[i] = Array.get(Array.get(arrayValues, i), j);
                }
            }

            distinctFromValues = fromValues;
            /*
                场景一：某handler进行一次批量查询后，再直接关联SqlQueryOneHandler，那么如果没有去重，后续返回结果与fromValues的记录数并不匹配（见：api/msg/notice/offline/order/change）
                场景二：然而如果直接关联ThirdApiInvokeHandler，由于本身具有一一对应性，返回的结果将会匹配，后续返回结果与fromValues的记录数本身就匹配
                统一逻辑为：
                    优先判断等长性,如果等长（默认有序返回）
                    其次判断不等长，获取返回结果包含入参属性，则进行智能关联处理
                    再次，不等长且返回结果不包含入参属性，直接报错.......
                因此SqlQueryOneHandler等批量处理需要保证等长有序【重要】
             */
            //某handler进行一次批量查询后，再直接关联SqlQueryOneHandler，那么如果没有去重，后续返回结果与fromValues的记录数并不匹配，
            //见api/msg/notice/offline/order/change，所以这里临时去重，等待进一步观察然而直接关联ThirdApiInvokeHandler，由于本身具有一一对应性，返回的结果将会匹配
            //
//            distinctFromValues = distinctFromValues(fromValues);
            batchDC = DCUtils.listDC2GroupDC(doDefaultBatchHandle(
                    executorService, smartHandler, batchMethod, fromProperties, toProperties, patternString, properties, parentSpan));
        }else if(batchMethod == null|| DCUtils.isMapDC(properties)) {//走单个方法(返回MapDC或者ListDC)，批量走单个方法(返回GroupDC)
            return doDefaultBatchHandle(executorService, smartHandler, method, fromProperties, toProperties, patternString, properties, parentSpan);
        }else if(DCUtils.isListDC(properties)){//存在批量方法，走批量方法
            fromValues = CollectionUtils.fetch(((ListDC)properties).getData(), new Fetcher<Map<String, Object>, Object[]>() {
                @Override
                public Object[] fetch(Map<String, Object> objectMap) {
                    List<Object> tmpList = new ArrayList<>();
                    for (int i = 0; i < fromProperties.length; i++) {
                        String fromProperty = fromProperties[i];
                        Object value = null;
                        if(objectMap.containsKey(fromProperty)) {
                            value = objectMap.get(fromProperty);
                        }else if(objectMap.containsKey(SmartParameterResolver.SMART_WEB_PARAMETER_PREFIX + fromProperty)) {
                            value = objectMap.get(SmartParameterResolver.SMART_WEB_PARAMETER_PREFIX + fromProperty);
                        }else {
                            throw new SmartHandlerException(APIErrorType.SERVER_ERROR, "while executing expand for " +
                                    "" + toProperties[0] + ", from property of " + fromProperty + " not exist !");
                        }

                        tmpList.add(transformType(value, method.getParameterTypes()[i]));
                    }
                    return tmpList.toArray(new Object[0]);
                }
            });
            fromValues = convert(method.getParameterTypes(), fromProperties.length, fromValues);
            if(fromValues != null) {
                distinctFromValues = distinctFromValues(fromValues);
                batchDC = batchHandle(smartHandler, batchMethod, distinctFromValues, fromProperties, toProperties, parentSpan);
            }
        }

        //建立多个查询与多个返回的映射关系
        Map<Object[], DC> resultFetchMap = new HashMap<>();
        if(DCUtils.isGroupDC(batchDC)) {
            List<DC> dcList = ((GroupDC) batchDC).getData();
            boolean reqRespNumbersEq = distinctFromValues.size() == dcList.size();
            if(reqRespNumbersEq) {//如果分组数相同，我们任务各批量handler处理器已经按照入参顺序对应处理了，这里直接对应即可
                for (int i = 0; i < distinctFromValues.size(); i++) {
                    resultFetchMap.put(distinctFromValues.get(i), dcList.get(i));
                }
            }else {//否则需要我们智能匹配获得映射关系
                for (int i = 0; i < dcList.size(); i++) {
                    DC childDC = dcList.get(i);//可能为MapDC，也可能为ListDC,还可能是ValueDC
//                Map<String, Object> tempResultMap  = filterResultMap(returnObject, toProperties);
                    String[] keyFields = implicitField != null ? requestNames : fromProperties;
//                String[] fetch = returnObject.fetch(keyFields);

                    if(DCUtils.isValueDC(childDC)) {
                        resultFetchMap.put(distinctFromValues.get(i), childDC);
                    }else {
                        List<Object> propertyValues = new ArrayList<>();
                        for (int j = 0; j < keyFields.length; j++) {
                            String keyProperty = keyFields[j];
                            try {
                                Object value = DCUtils.getAValue(childDC, keyProperty);
                                if(value != null) {
                                    propertyValues.add(value);
                                }else {
                                    propertyValues.add(distinctFromValues.get(0)[j]);
                                }
                            }catch (Exception e) {
                                propertyValues.add(distinctFromValues.get(0)[j]);
                            }
                        }
                        resultFetchMap.put(propertyValues.toArray(new Object[0]), childDC);
                    }
                }
            }
        }
        List removeKeys = new ArrayList();
        removeKeys.addAll(Arrays.asList(fromProperties));
        removeKeys.removeAll(Arrays.asList(toProperties));
        if(!removeKeys.isEmpty()) {
            for (DC dc : resultFetchMap.values()) {
                dc.removeFields(removeKeys);
            }
        }

        GroupDC groupDC = new GroupDC();
        for (Object[] fromValue : fromValues) {
            DC oneDC = NullDC.SINGLETON;
            for (Map.Entry<Object[], DC> mapEntry : resultFetchMap.entrySet()) {
                if(isSameElement(mapEntry.getKey(),fromValue)) {
                    oneDC = mapEntry.getValue();
                    break;//如果命中则不继续下去
                }
            }
            if(!DCUtils.isNullDC(oneDC) && groupDC.getData().contains(oneDC)) {//关联查询的一条结果记录，对应外面的多条记录
                groupDC.add(oneDC.copy());
            }else {
                groupDC.add(oneDC);
            }

        }

        return groupDC;
    }

    public static boolean isSingle(Object o) {
        return !isArray(o);
    }

    public static boolean isArray(Object o) {
        return o != null && o.getClass().isArray();
    }

    public static int getArrayLength(Collection collection) {
        for (Object o : collection) {
            if(o == null || !o.getClass().isArray()) {
                return 1;
            }
            return Array.getLength(o);
        }
        return  0;
    }

    /**
     * @param o
     * @return
     */
    public static Object arrayToString(Object o, String concatChars) {
        if(o == null || !o.getClass().isArray()) {
            return o;
        }

        if("batch_join_by_commas".equals(concatChars)) {
            List<Object> result = new ArrayList<>();
            for (int i = 0; i < Array.getLength(o); i++) {
                Object value = Array.get(o, i);
                result.add(value);
            }
            return Joiner.on(",").join(result);
        }else {
            Set<Object> result = new LinkedHashSet<>();
            for (int i = 0; i < Array.getLength(o); i++) {
                Object value = Array.get(o, i);
                result.add(value);
            }
            if(result.size() == 1) {
                return result.iterator().next();
            }else {
                return o;
            }
        }
    }

    /**
     * 方法某参数是组数，而数组的元素也是数据，计算元素的长度。
     * @param o
     * @return
     */
    private static int calcImplicitParametersLength(Object o) {
        int length = -1;//默认为单个查询数据
        if(Array.getLength(o) > 0){
            //如果第一个元素为数组，则循环每一个元素判定是否相同长度，如果是则返回
            if(Array.get(o, 0).getClass().isArray()) {
                for (int i = 0; i < Array.getLength(o); i++) {
                    if(Array.get(o, i).getClass().isArray()) {//循环每一个参数
                        if(length == -1){
                            length = Array.getLength(Array.get(o, i));
                        }else if(length != Array.getLength(Array.get(o, i))) {
                            /*
                            如果所有的参数长度不一样，说明并不是批量处理,
                            说明：如果单个查询中多个in查询，in的参数传值不能直接用数组，应该用ArrayParam包装
                            将会走如下else分支进行解包(！！！下下下！！！)
                             */
                            return 1;
                        }
                    }else {
                        return 1;
                    }
                }
            }else {
                for (int i = 0; i < Array.getLength(o); i++) {
                    if(Array.get(o, i) instanceof ArrayParam) {//分支进行解包(！！！上上上！！！)
                        Array.set(o, i, ((ArrayParam)(Array.get(o, i))).getO());
                    }
                }
            }
        }
        return length <= 1 ? 1 : length;
    }

    private static GroupDC batchHandle(SmartHandler smartHandler, Method method, List<Object[]> fromValues, String[] fromProperties, String[] toProperties, PeacockSpan parentSpan){
        try {
            TracerFactory.addTag(parentSpan, TracerFactory.TagKey.LOCAL_EXECUTE_CLASS, smartHandler.getClass().getSimpleName());
            TracerFactory.addTag(parentSpan, TracerFactory.TagKey.LOCAL_EXECUTE_METHOD, method.getName());
            TracerFactory.addTag(parentSpan, TracerFactory.TagKey.LOCAL_EXECUTE_PARAMETERS, fromValues, parentSpan.hasIORequestTag());
            TracerFactory.addSpanAnnotation(parentSpan, TracerFactory.Annotation.PARAMETER_PARSE);
            List<Object> result = (List<Object>) handle(smartHandler, method, fromValues.toArray(new Object[0]));
            TracerFactory.addSpanAnnotation(parentSpan, TracerFactory.Annotation.HANDLER_INVOKE);
            ArrayList<Object> fromAndToProperties = Lists.newArrayList();
            fromAndToProperties.addAll(Arrays.asList(fromProperties));
            fromAndToProperties.addAll(Arrays.asList(toProperties));
            String[] tmpProperties = fromAndToProperties.toArray(new String[0]);
            GroupDC groupDC = new GroupDC();
            for (Object single : result) {
                if(single != null && single instanceof List) {
                    List<Map<String, Object>> map = new ArrayList<>();
                    for (Object o : (List)single) {
                        Map<String, Object> tempResultMap  = filterResultMap(o, tmpProperties);
                        map.add(tempResultMap);
                    }
                    groupDC.add(new ListDC(map));
                }else if(single != null && BeanUtils.isSimpleProperty(single.getClass())){
                    groupDC.add(new ValueDC(tmpProperties[0], single));
                }else if(single != null && single instanceof SmartObject){
                    groupDC.add(new ValueDC(tmpProperties[0], ((SmartObject)single).getObject()));
                }else if(single != null && single instanceof SmartMap){
                    groupDC.add(new MapDC(filterResultMap(((SmartMap)single).build(), tmpProperties)));
                }else if(single != null){
                    groupDC.add(new MapDC(filterResultMap(single, tmpProperties)));
                }else {
                    groupDC.add(NullDC.SINGLETON);
                }
            }
            TracerFactory.addSpanAnnotation(parentSpan, TracerFactory.Annotation.RESULT_PACKAGE);
            return groupDC;
        }catch (Exception e) {
            e.printStackTrace();
            throw new SmartHandlerException(APIErrorType.SERVER_ERROR, "invoke handler method error ! handler = " + method.getDeclaringClass().getSimpleName() + "; method = " + method.getName() + "; errorInfo = " + e.getMessage() );

        }

    }

    private static List<Object[]> convert(Class<?>[] parameterTypes, int length, List<Object[]> fromValues) {
        List<Object[]> result = new ArrayList<>();
        for (int i = 0; i < length; i++) {
            result.add((Object[]) Array.newInstance(parameterTypes[i], fromValues.size()));
        }
        for (int i = 0; i < fromValues.size(); i++) {
            Object[] objects = fromValues.get(i);
            for (int j = 0; j < objects.length; j++) {
                result.get(j)[i] = objects[j];
            }
        }
        return result;
    }

    protected static   boolean checkResultIsNone(List<Object> returnObjects){
        return returnObjects != null && returnObjects.size() == 1
                && returnObjects.get(0) != null && returnObjects.get(0) instanceof None;
    }

    protected static List<Object[]> distinctFromValues(List<Object[]> fromValues){
        List<Object[]> result = new ArrayList<>();
        for (Object[] fromValue : fromValues) {
            boolean isContain = false;
            for (Object[] objects : result) {
                if(isSameElement(fromValue, objects)){
                    isContain = true;
                    break;
                };
            }
            if(!isContain){
                result.add(fromValue);
            }

        }
        return result;

    }

    protected static   boolean isSameElement(Object[] fromValue, Object[] toValues){
        if(fromValue.length != toValues.length) return false;

        for (int i = 0; i < fromValue.length; i++) {
            if(fromValue[i] == null && toValues[i] == null) {
                return true;
            }
            if((fromValue[i] == null && toValues[i] != null) ||(fromValue[i] != null && toValues[i] == null)
                    || !String.valueOf(fromValue[i]).equals(String.valueOf(toValues[i]))) {
                return false;
            }
        }
        return true;
    }

    /**
     * 如果批量方法
     * @param properties
     * @param span
     * @return
     * @throws ExecutionException
     * @throws InterruptedException
     */
    protected static DC doDefaultBatchHandle(ExecutorService executorService, final SmartHandler smartHandler,
                                             final Method method, final String[] fromProperties,
                                             final String[] toProperties, final String patternString,
                                             DC properties, final PeacockSpan span) throws ExecutionException, InterruptedException {
        TracerFactory.addTag(span, TracerFactory.TagKey.LOCAL_EXECUTE_CLASS, smartHandler.getClass().getSimpleName());
        TracerFactory.addTag(span, TracerFactory.TagKey.LOCAL_EXECUTE_METHOD, method.getName());
        if(DCUtils.isMapDC(properties)) {//只有一个查询值
            Object result = handle(smartHandler, method, fromProperties, toProperties, patternString, ((MapDC)properties).getData(), span);
            return DCUtils.valueOf(result);
        }else if(DCUtils.isListDC(properties)){
            GroupDC groupResult = new GroupDC();
            List<Future<Object>> futures = new ArrayList<>();
            for (final Map<String, Object> property : ((ListDC)properties).getData()) {
                futures.add(executorService.submit(TracerFactory.wrap(new Callable<Object>() {
                    @Override
                    public Object call() throws Exception {
                        //返回可能是个列表中嵌套的map TODO 难道不能是列表吗？
                        SmartHandlerFactory.HandlerInfo handlerInfo = SmartHandlerFactory.getHandler(smartHandler.getClass())[0];
                        PeacockSpan childSpan = TracerFactory.addChildSpan(handlerInfo.getProgram(), span, handlerInfo.getPath(), handlerInfo.getTitle(), handlerInfo.getPath());
                        Object result = handle(smartHandler, method, fromProperties, toProperties, patternString, property, childSpan);
                        TracerFactory.addTag(childSpan, TracerFactory.TagKey.RESPONSE, result, childSpan.hasHandlerResponseTag());
                        TracerFactory.finishCurrentSpan(childSpan);
                        return result;
                    }
                })));
            }

            for (Future<Object> future : futures) {
                Object value = future.get();
                groupResult.add(DCUtils.valueOf(value));

            }
            return groupResult;
        }else {
            throw new RuntimeException();
        }
    }

    public static Object transformType(Object value, Class<?> parameterType) {
        if(value == null) return value;
        if(parameterType.isAssignableFrom(value.getClass())) {
            return value;
        }else if(String.class.isAssignableFrom(parameterType)){
            return value.toString();
        }else if(Date.class.isAssignableFrom(parameterType)){
            throw new SmartHandlerException(APIErrorType.SERVER_ERROR, "TODO ");
        }else if(BigDecimal.class.isAssignableFrom(parameterType)){
            return new BigDecimal(value.toString());
        }else if(Integer.class.isAssignableFrom(parameterType) || int.class == parameterType){
            return Integer.valueOf(value.toString());
        }else if(Long.class.isAssignableFrom(parameterType) || long.class == parameterType){
            return Long.valueOf(value.toString());
        }else if(Boolean.class.isAssignableFrom(parameterType) || boolean.class == parameterType){
            return Boolean.valueOf(value.toString());
        }else if(parameterType.isArray() && (value == null || value.getClass().isArray())){
            return value;
        }else {
            throw new SmartHandlerException(APIErrorType.SERVER_ERROR, "UNKNOWN TYPE:" + parameterType.getName() + ";VALUE: " + value + ";");
        }
    }

    public static Object handle(SmartHandler smartHandler, Method method, Object[] fromValues) throws InvocationTargetException, IllegalAccessException {
        return method.invoke(smartHandler,fromValues);
    }

    public static Map<String,Object> filterResultMap(Object returnObject, String[] toProperties){
        Map<String, Object> objectMap = new LinkedHashMap<>();
        for (String toProperty : toProperties) {
            if(toProperty.endsWith("[]")) toProperty = toProperty.substring(0, toProperty.length() - 2);
            if(toProperty.contains("->")){
                String key = toProperty.substring(0, toProperty.indexOf("->")).trim();
                String value= toProperty.substring(toProperty.indexOf("->")+2).trim();

                objectMap.put(key, getPropertyValue(returnObject, value));
            }else {
                objectMap.put(toProperty, getPropertyValue(returnObject, toProperty));
            }

        }
        return objectMap;
    }

    protected static Object getPropertyValue(Object returnObject, String property){
        if("*".equals(property)) {
            return returnObject;
        }

        if(returnObject != null && returnObject instanceof Map) {
            return ((Map)returnObject).get(property);
        }else if(returnObject != null && returnObject instanceof SmartMap) {
            return ((SmartMap)returnObject).build().get(property);
        }else if(returnObject != null && returnObject instanceof SmartObject) {
            return ((SmartObject)returnObject).getObject();
        }else if(returnObject != null && !BeanUtils.isSimpleProperty(returnObject.getClass())) {
            return getFieldValue(returnObject, property);
        }else {
            return returnObject;
        }
    }

    private static Field getDeclaredField(Object object, String filedName) {
        for (Class<?> superClass = object.getClass(); superClass != Object.class; superClass = superClass.getSuperclass()) {
            try {
                return superClass.getDeclaredField(filedName);
            } catch (NoSuchFieldException e) {
                // Field 不在当前类定义, 继续向上转型
            }
        }
        return null;
    }

    private static void makeAccessible(Field field) {
        if (!Modifier.isPublic(field.getModifiers())) {
            field.setAccessible(true);
        }
    }

    public static Object getFieldValue(Object object, String fieldName) {
        Field field = getDeclaredField(object, fieldName);
        if (field == null)
            throw new IllegalArgumentException("Could not find field ["
                    + fieldName + "] on target [" + object + "]");

        makeAccessible(field);

        Object result = null;
        try {
            result = field.get(object);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

        return result;
    }

    public static class None{

    }
}
