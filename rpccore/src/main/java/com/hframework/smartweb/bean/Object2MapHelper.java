package com.hframework.smartweb.bean;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.hframework.common.util.ReflectUtils;
import com.hframework.common.util.collect.CollectionUtils;
import com.hframework.common.util.collect.bean.Fetcher;
import com.hframework.smartweb.SmartHandlerFactory;
import com.hframework.smartweb.annotation.SmartDescription;
import com.hframework.smartweb.annotation.SmartIdentity;
import com.hframework.smartweb.bean.SmartMap;
import com.hframework.smartweb.bean.SmartObject;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.core.GenericCollectionTypeResolver;
import org.springframework.core.MethodParameter;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.util.*;

/**
 * Created by zhangquanhong on 2017/11/16.
 */
public class Object2MapHelper {

    public static class RuntimeSet<T> extends HashSet implements Set{
        public RuntimeSet(){}
        public RuntimeSet(Set<T> elements){
            if(elements != null);
            for (T element : elements) {
                this.add(element);
            }
        }
    }

    public static class OneValueSet<T> extends HashSet implements Set{
        public OneValueSet(){}
        public OneValueSet(Set<T> elements){
            if(elements != null);
            for (T element : elements) {
                this.add(element);
            }
        }
    }

    public static Set<String> allResults(Map<String, SmartHandlerFactory.ResultInfo> resultInfo, MethodParameter returnType) {
        Set<String> result = new HashSet<>();
        Class beanType = null;
        Class mapType = null;
        boolean isBatch = false;
        if(SmartMap.class.isAssignableFrom(returnType.getParameterType())) {
            mapType = returnType.getParameterType();
            //TODO
        }else if(SmartObject.class.isAssignableFrom(returnType.getParameterType())) {
            mapType = returnType.getParameterType();
            //TODO
        }else if(isBeanCollectionParameterType(returnType)) {
            beanType = getCollectionParameterType(returnType);
            isBatch = true;
        }else if(isBeanArrayParameterType(returnType)) {
            beanType = getArrayParameterType(returnType);
            isBatch = true;
        }else if(isBeanParameterType(returnType)){
            beanType = returnType.getParameterType();
        }else if(isMapCollectionParameterType(returnType)){
            mapType = getCollectionParameterType(returnType);
            isBatch = true;
        }else if(isMapArrayParameterType(returnType)){
            mapType = getArrayParameterType(returnType);
            isBatch = true;
        }else if(Map.class.isAssignableFrom(returnType.getParameterType())) {
            mapType = returnType.getParameterType();
        }
        if(beanType != null && beanType.getPackage().getName().startsWith("com.")) {
            System.out.println("package:" + beanType.getPackage() + "; clazz:" + beanType);
            // 得到所有的属性
            Field[] fields = beanType.getDeclaredFields();
            Set<String> names = new LinkedHashSet<>();
            for (Field field : fields) {
                SmartDescription annotation = field.getAnnotation(SmartDescription.class);
                if(annotation == null || !annotation.main()) {
                    String name  = annotation != null && StringUtils.isNotBlank(annotation.name()) ? annotation.name() : field.getName();
                    String desc = annotation != null && StringUtils.isNotBlank(annotation.value()) ? annotation.value() : name;
                    resultInfo.put(name, new SmartHandlerFactory.ResultInfo(desc));
                    names.add(name);
                }else {

                }

            }
            return names;
        }

        SmartIdentity identity = returnType.getMethodAnnotation(SmartIdentity.class);
        if(identity != null) {
            final String[] value = identity.value();
            if(value != null && value.length > 0) {
                if(mapType != null) {
                    return new RuntimeSet(Sets.newHashSet(value));
                }else {
                    return new OneValueSet(Sets.newHashSet(value));
                }
            }
        }

        if(mapType != null) {
            return new RuntimeSet<String>();
        }else {
            return new OneValueSet<String>();
        }
    }

    public static List<Map<String, Object>> transformAllToMapStruts(MethodParameter returnType, Object returnValue) {
        //将Bean对象转化为Map对象
        List<Map<String, Object>> returnObjects = new ArrayList<>();
        if(isBeanCollectionParameterType(returnType)) {
            returnObjects = CollectionUtils.fetch(Lists.newArrayList((Iterable) returnValue), new Fetcher() {
                @Override
                public Map<String, Object> fetch(Object o) {
                    return com.hframework.common.util.BeanUtils.parsePropertiesMap(o);
                }
            });
        }else if(isBeanArrayParameterType(returnType)) {
            returnObjects = CollectionUtils.fetch(Arrays.asList((Map<String, Object>[]) returnValue), new Fetcher() {
                @Override
                public Map<String, Object> fetch(Object o) {
                    return com.hframework.common.util.BeanUtils.parsePropertiesMap(o);
                }
            });
        }else if(isMapCollectionParameterType(returnType)){
            returnObjects = Lists.newArrayList((Iterable) returnValue);
        }else if(isMapArrayParameterType(returnType)){
            returnObjects = Arrays.asList((Map<String, Object>[]) returnValue);
        }else if(Map.class.isAssignableFrom(returnType.getParameterType())) {
            returnObjects.add((Map<String, Object>) returnValue);
        }else if(SmartMap.class.isAssignableFrom(returnType.getParameterType())) {
            returnObjects.add((Map<String, Object>) ((SmartMap)returnValue).build());
        }else if(SmartObject.class.isAssignableFrom(returnType.getParameterType())) {
            returnObjects.add(((SmartObject)returnValue).getMap());
        }else if(isBeanParameterType(returnType)){
            returnObjects.add(com.hframework.common.util.BeanUtils.parsePropertiesMap(returnValue));
        }
        return returnObjects;
    }

    public static List<Map<String, Object>> transformAllToMapStruts(Object returnValue) {
        //将Bean对象转化为Map对象
        List<Map<String, Object>> returnObjects = new ArrayList<>();
        if(returnValue == null) return null;

        if(returnValue instanceof List && ((List) returnValue).size() == 0) {
            return null;
        }else if(returnValue.getClass().isArray() && Array.getLength(returnValue) == 0) {
            return null;
        }else if(returnValue instanceof List && ((List) returnValue).size() > 0) {
            if(!(((List) returnValue).get(0) instanceof Map)) {
                returnObjects = CollectionUtils.fetch(Lists.newArrayList((Iterable) returnValue), new Fetcher() {
                    @Override
                    public Map<String, Object> fetch(Object o) {
                        return com.hframework.common.util.BeanUtils.parsePropertiesMap(o);
                    }
                });
            }else {
                returnObjects = Lists.newArrayList((Iterable) returnValue);
            }
        }else if(returnValue.getClass().isArray() && Array.getLength(returnValue) > 0) {
            if(!(Array.get(returnValue, 0) instanceof Map)) {
                returnObjects = CollectionUtils.fetch(Arrays.asList((Map<String, Object>[]) returnValue), new Fetcher() {
                    @Override
                    public Map<String, Object> fetch(Object o) {
                        return com.hframework.common.util.BeanUtils.parsePropertiesMap(o);
                    }
                });
            }else {
                returnObjects = Arrays.asList((Map<String, Object>[]) returnValue);
            }
        }else if(!BeanUtils.isSimpleProperty(returnValue.getClass())){
            if(!(returnValue instanceof Map)){
                returnObjects.add(com.hframework.common.util.BeanUtils.parsePropertiesMap(returnValue));
            }else {
                returnObjects.add((Map<String, Object>) returnValue);
            }
        }
        return returnObjects;
    }

    /**
     * 判断是否为【集合类】存放【Map对象】
     * @param parameter
     * @return
     */
    public static  boolean isMapCollectionParameterType(MethodParameter parameter) {
        Class<?> collectionType = getCollectionParameterType(parameter);
        return ((collectionType != null) && Map.class.isAssignableFrom(collectionType));
    }

    /**
     * 判断是否为【数组】存放【Map对象】
     * @param parameter
     * @return
     */
    public static  boolean isMapArrayParameterType(MethodParameter parameter) {
        Class<?> collectionType = getArrayParameterType(parameter);
        return ((collectionType != null) && Map.class.isAssignableFrom(collectionType));
    }

    /**
     * 判断是否为【集合类】存放【Bean对象】
     * @param parameter
     * @return
     */
    public static  boolean isBeanCollectionParameterType(MethodParameter parameter) {
        Class<?> collectionType = getCollectionParameterType(parameter);
        return ((collectionType != null) && !BeanUtils.isSimpleProperty(collectionType)
                && !Collection.class.equals(collectionType) && !List.class.isAssignableFrom(collectionType));
    }

    /**
     * 判断是否为【数组】存放【Bean对象】
     * @param parameter
     * @return
     */
    public static  boolean isBeanArrayParameterType(MethodParameter parameter) {
        Class<?> collectionType = getArrayParameterType(parameter);
        return ((collectionType != null) && !BeanUtils.isSimpleProperty(collectionType)
                && !Collection.class.equals(collectionType) && !List.class.isAssignableFrom(collectionType));
    }

    /**
     * 判断是否为【Bean对象】
     * @param returnType
     * @return
     */
    public static  boolean isBeanParameterType(MethodParameter returnType) {
        return !Iterator.class.isAssignableFrom(returnType.getParameterType())
                && !BeanUtils.isSimpleProperty(returnType.getParameterType()) && !Map.class.isAssignableFrom(returnType.getParameterType());
    }


    /**
     * 判断是否为集合类型
     * @param parameter
     * @return
     */
    public static  Class<?> getCollectionParameterType(MethodParameter parameter) {
        Class<?> paramType = parameter.getParameterType();
        if (Collection.class.equals(paramType) || List.class.isAssignableFrom(paramType)){
            Class<?> valueType = GenericCollectionTypeResolver.getCollectionParameterType(parameter);
            if (valueType != null) {
                return valueType;
            }
        }
        return null;
    }

    /**
     * 判断是否为数组类型
     * @param parameter
     * @return
     */
    public static  Class<?> getArrayParameterType(MethodParameter parameter) {
        Class<?> paramType = parameter.getParameterType().getComponentType();
        return paramType;
    }
}
