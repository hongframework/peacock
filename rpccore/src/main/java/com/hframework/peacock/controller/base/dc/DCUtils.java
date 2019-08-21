package com.hframework.peacock.controller.base.dc;

import com.google.common.collect.Lists;
import com.hframework.smartweb.exception.SmartHandlerException;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DCUtils {

    public static String toPlantString(DC dc){
        return dc.getClass().getSimpleName() + ":" + toPlantString(dc.getData());
    }

    public static DC warpDC(List<Map<String, Object>> list) {
        if(list.size() == 1) {
            return new MapDC(list.get(0));
        }else if(list.size() > 1){
            return new ListDC(list);
        }else {
            return NullDC.SINGLETON;
        }
    }

    public static DC groupDC2ListDC(DC dc){
        if(isGroupDC(dc)) {
            List<Map<String, Object>> data = new ArrayList<>();
            GroupDC groupDC = (GroupDC) dc;
            for (DC subDC : groupDC.getData()) {
                data.add((Map<String, Object>) subDC.getData());
            }
            ListDC newDc = new ListDC(data);
            newDc.setPrevDc(dc.getPrevDc());
            return newDc;
        }
        return dc;
    }

    public static DC listDC2GroupDC(DC dc){
        if(isListDC(dc)) {
            GroupDC groupDC = new GroupDC();
            ListDC listDC = (ListDC) dc;
            List<Map<String, Object>> data = listDC.getData();
            for (Map<String, Object> datum : data) {
                groupDC.add(new MapDC(datum));
            }
            groupDC.setPrevDc(dc.getPrevDc());
            return groupDC;
        }return dc;
    }

    public static Object getValues(DC dc) {
        Object retObject = null;
        if(isGroupDC(dc)){
            List<Object> objects = new ArrayList<>();
            for (DC itemDC : ((GroupDC) dc).getData()) {
                objects.add(getValues(itemDC));
            }
            retObject = objects;
        }else {
            retObject = dc.getData();
        }

        if(DCUtils.isNullDC(dc.getPrevDc())) {
            return retObject;
        }else {
            final Object prevRetObject = getValues(dc.getPrevDc());
            if(prevRetObject != null && prevRetObject instanceof Map) {
                ((Map) prevRetObject).put("data", retObject);
                return prevRetObject;
            }else {
                final Object finalRetObject = retObject;
                return new HashMap<String, Object>(){{
                    put("meta", prevRetObject);
                    put("data", finalRetObject);

                }};
            }
        }


    }

    public static boolean isMultiDC(DC dc) {
        return isListDC(dc) || isGroupDC(dc);
    }

    public static List<Map<String, Object>> getList(DC dc) {
        if(isListDC(dc)) {
            return (List<Map<String, Object>>) dc.getData();
        }else if(isMapDC(dc)) {
            return Lists.newArrayList((Map<String, Object>)dc.getData());
        }else if(isGroupDC(dc)){
            List<Map<String, Object>> list = new ArrayList<>();
            for (DC dc1 : ((GroupDC) dc).getData()) {
                list.add((Map<String, Object>) dc1.getData());
            }
            return list;
        }else return null;
    }

    public static DC mergeDC(DC dcA, DC dcB, boolean listMergeEnabled) {
        DC resultDc = mergeDCSimple(dcA, dcB, listMergeEnabled);
        DC prevDc = mergeDCSimple(dcA.getPrevDc(), dcB.getPrevDc(), false);
        if(DCUtils.isNullDC(resultDc) && !DCUtils.isNullDC(prevDc)){
            resultDc = new NullDC();
        }
        resultDc.setPrevDc(prevDc);
        return resultDc;
    }

    public static DC mergeDCSimple(DC dcA, DC dcB, boolean listMergeEnabled) {
        if(DCUtils.isNullDC(dcA)) return dcB;
        if(DCUtils.isNullDC(dcB)) return dcA;

        if(DCUtils.isListDC(dcA) && DCUtils.isListDC(dcB)) {
            if(listMergeEnabled){
                ListDC listA = (ListDC) dcA;
                ListDC listB = (ListDC) dcB;
                if(listA.getData().size() != listB.getData().size()) {
                    throw new SmartHandlerException("can't merge list["+dcA+"] to list["+dcB+"], size not match !");
                }else {
                    listA.merge(listB.getData());
                    return listA;
                }
            }else {
                throw new SmartHandlerException("can't merge list["+dcA+"] to list["+dcB+"], on same level !");
            }
        }else if(DCUtils.isGroupDC(dcA) && DCUtils.isGroupDC(dcB)) {
            GroupDC gdcA = (GroupDC) dcA;
            GroupDC gdcB = (GroupDC) dcB;
            if(gdcA.getData().size() != gdcB.getData().size()) {
                throw new SmartHandlerException("can't merge group["+dcA+"] to group["+dcB+"], on same level !");
            }else {
                gdcA.merge(gdcB);
                return gdcA;
            }

        }else if(DCUtils.isMultiDC(dcA) && DCUtils.isMultiDC(dcB)) {
            ListDC listDC = DCUtils.isListDC(dcA) ? (ListDC)dcA : (ListDC)dcB;
            GroupDC groupDC = DCUtils.isGroupDC(dcA) ? (GroupDC)dcA : (GroupDC)dcB;
            if(listDC.getData().size() != groupDC.getData().size()) {
                throw new SmartHandlerException("can't merge list["+listDC+"] to group["+groupDC+"], size not match !");
            }

            //这里是否顺序应该换成listDC.merge(groupDC) return listDC, 防止左右打右手;这里已经出现了场景1，所以先优先用场景1方案解决,如果后续存在问题再进行解决
//            groupDC.merge(listDC);
//            return groupDC;
            /*
                场景1：
                    某API(cust/birthday/list)配置，其中客户列表位于customers下，而客户列表ListDC与客户敏感信息加密扩展GroupDC合并时，
                    如果产生GroupDC，那么将会在上级路径时追加时变成了每行添加了customers路径
                    详见：ResultTreeDescriptor.packaging(Map<HandlerDescriptor, ApiInvokeData.HandleSwapData> handleResult)
                    期望值：customers : [{name:张三,age:17},{name:李四,age:18},{name:王二,age:19}]
                    异常值:[
                            {customers : {name:张三,age:17}},
                            {customers : {{name:李四,age:18}},
                            {customers : {{name:王二,age:19}}
                        ]

             */
            groupDC.merge(listDC);
            return groupDC2ListDC(groupDC);

        }else if(DCUtils.isMapDC(dcB)) {
            dcA.merge(((MapDC)dcB).getData());
            return dcA;
        }else if(DCUtils.isMapDC(dcA)) {
            dcB.merge(((MapDC)dcA).getData());
            return dcB;
        }else {
            dcA.merge(((MapDC)dcB).getData());
            return dcA;
        }
    }


    public static DC valueOf(Object object) {
        if(object == null) {
            return NullDC.SINGLETON;
        }else if(object instanceof DC) {
            return (DC)object;
        }else if(object instanceof Map) {
            return new MapDC((Map<String, Object>) object);
        }else if(object instanceof List) {
            List list = (List) object;
            if(list.isEmpty()){
                return NullDC.SINGLETON;
            }else if(list.get(0) instanceof List) {
                GroupDC dc = new GroupDC();
                for (Object o : (List) object) {
                    dc.add(valueOf(o));
                }
                return dc;
            }else if(list.get(0) instanceof Map) {
                return new ListDC((List<Map<String, Object>>) object);
            }else {
                throw new RuntimeException("TODO");
            }
        }else {
            throw new RuntimeException("TODO");
        }
    }

    public static boolean isNullDC(DC dc) {
        return dc instanceof NullDC;
    }

    public static boolean isMapDC(DC dc) {
        return dc instanceof MapDC;
    }

    public static boolean isListDC(DC dc) {
        return dc instanceof ListDC;
    }
    public static boolean isGroupDC(DC dc) {
        return dc instanceof GroupDC;
    }

    public static boolean isValueDC(DC dc) {
        return dc instanceof ValueDC;
    }

    public static Object getAValue(DC dc, String field) {
        if(isNullDC(dc) || isGroupDC(dc)) return null;

        Map<String, Object> tmp = isListDC(dc)? ((ListDC)dc).getData().get(0) : ((MapDC)dc).getData();
        return tmp.get(field);
    }

    public static String toPlantString(Object value) {
        if (value == null)
            return "null";

        if(value.getClass().isArray()) {
            int iMax = Array.getLength(value) - 1;
            if (iMax == -1)
                return "[]";
            StringBuilder b = new StringBuilder();
            b.append('[');
            for (int i = 0; ; i++) {
                b.append(toPlantString(Array.get(value, i)));
                if (i == iMax)
                    return b.append(']').toString();
                b.append(", ");
            }
        }else if(value instanceof List) {
            List list = (List)value;
            int iMax = list.size() - 1;
            if (iMax == -1)
                return "[]";
            StringBuilder b = new StringBuilder();
            b.append('[');
            for (int i = 0; ; i++) {
                b.append(toPlantString(list.get(i)));
                if (i == iMax)
                    return b.append(']').toString();
                b.append(", ");
            }
        }else if(value instanceof Map) {
            Map map = (Map) value;
            int iMax = map.size() - 1;
            if (iMax == -1)
                return "{}";
            StringBuilder b = new StringBuilder();
            b.append('{');
            int cnt = 0;
            for (Object key : map.keySet()) {
                if(cnt != 0) {
                    b.append("\n");
                    b.append("  ");
                }
                b.append(key).append("=").append(toPlantString(map.get(key)));
                if (cnt++ == iMax)
                    return b.append('}').toString();
                b.append(", ");
            }
        }

        return value.toString();
    }
}
