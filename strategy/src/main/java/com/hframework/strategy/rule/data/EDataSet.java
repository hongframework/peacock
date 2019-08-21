package com.hframework.strategy.rule.data;

import com.google.common.collect.Lists;
import com.hframework.common.util.StringUtils;
import com.hframework.strategy.index.repository.indexs.Mysql;
import com.hframework.strategy.rule.exceptions.DataFetchException;
import com.hframework.strategy.rule.fetch.FetchData;
import com.hframework.strategy.rule.fetch.Fetcher;
import com.hframework.strategy.rule.fetch.mysql.MysqlFetcher;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.*;

/**
 * Created by zhangquanhong on 2017/6/26.
 */
public class EDataSet {

    public List<EDataField> meta = new ArrayList<EDataField>();
    public List<Object>[] data = null;
    public String[] label = null;
    public String[] lastStepLabel = null;
    public String[] curStageLabel = null;

    private boolean keyFieldTypeUnMatch = false;
    private Class keyFieldType;
    private Long createTimeMillis = System.currentTimeMillis();

    private int keyIndex = -1;
    private Map<Fetcher, List<String>> fetcherOverview;

    private Map<String, List<String>> descriptions;

    public EDataSet(List<String> metaData, List<List<Object>> data, int keyIndex) {
        for (int i = 0; i < metaData.size(); i++) {
            if(i == keyIndex){
                addDataField(metaData.get(i), true);
            }else {
                addDataField(metaData.get(i), false);
            }
        }
        this.data = data.toArray(new List[0]);
    }

    public EDataSet(Object[][] data, int keyIndex) {
        this(getStringArrayByObjectArray(data[0]) ,Arrays.copyOfRange(data,1, data.length), keyIndex);
    }

    private static String[] getStringArrayByObjectArray(Object[] data) {
        String[] meta = new String[data.length];
        for (int i = 0; i < data.length; i++) {
            meta[i] = String.valueOf(data[i]);
        }
        return meta;
    }

    ;

    public EDataSet(String[] metaData, Object[][] data, int keyIndex) {
        for (int i = 0; i < metaData.length; i++) {
            if(i == keyIndex){
                addDataField(metaData[i], true);
            }else {
                addDataField(metaData[i], false);
            }
        }
        List<Object>[] datas = new List[data.length];
        for (int i = 0; i < data.length; i++) {
            datas[i] = new ArrayList(Arrays.asList(data[i]));
        }
        this.data = datas;
    }
    public EDataSet(String[] metaData, Object[][] data){
        this(metaData, data, 0);
    }
    public EDataSet(List<String> metaData, List<List<Object>> data) {
        this(metaData, data, 0);
    }

    public EDataSet(String meta, String data) {
        addDataField(meta,true);
        this.data = new List[]{Lists.newArrayList((Object) data)};
    }
    public EDataSet(String meta, List<String> datas) {
        addDataField(meta, true);
        List<Object>[] data = new List[datas.size()];
        for (int i = 0; i < datas.size(); i++) {
            data[i] = Lists.newArrayList((Object) data);
        }
        this.data = data;
    }

    public EDataSet(Object object) {
        this.data = new List[]{getRowDataFromObject(object)};
    }

    public static Map<String, Object> parsePropertiesMap(Object obj) {
        Map<String, Object> parmMap = new LinkedHashMap<String, Object>();
        // 属性的名称及类型
        Field[] fileds = obj.getClass().getDeclaredFields();
        try {
            for (Field field : fileds) {
                // 属性名称
                String filedName = field.getName();
                field.setAccessible(true);
                ;
                parmMap.put(filedName,field.get(obj) != null ? field.get(obj) : null);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return parmMap;
    }

    private List<Object> getRowDataFromObject(Object object){
        List<Object> listObject = new ArrayList<Object>();
        Map<String, Object> objectMap = null;
        if(object instanceof Map) {
            objectMap = (Map) object;
        }else {
            objectMap = parsePropertiesMap(object);
        }
        for (Map.Entry<String, Object> entry : objectMap.entrySet()) {
            String key = entry.getKey();
            Object value = entry.getValue();
            if(!containDataFieldCode(key)){
                addDataField(key, meta.isEmpty() ?true : false);
            }
            listObject.add(value);
        }
        return listObject;
    }


    public EDataSet(List objects) {
        this.data = new List[objects.size()];
        for (int i = 0; i < objects.size(); i++) {
            this.data[i] = getRowDataFromObject(objects.get(i));
        }
    }

    public List<EDataField> getMeta() {
        return meta;
    }

    public Integer[] getFieldIndexs(String[] fieldCodes) {
        Integer[] result = new Integer[fieldCodes.length];
        for (int i = 0; i < fieldCodes.length; i++) {
            Integer fieldIndex = getFieldIndex(fieldCodes[i]);
            if(fieldIndex < 0) throw new DataFetchException("data set 's field " + fieldCodes[i] + " is not exists !");
            result[i] = fieldIndex;
        }
        return result;

    }
    public Integer getFieldIndex(String fieldCode) {
        for (int i = 0; i < meta.size(); i++) {
            if(fieldCode.equals(meta.get(i).getCode())){
                return i;
            }
        }
        return -1;
    }

    public void setMeta(List<EDataField> meta) {
        this.meta = meta;
    }

    public String getKeyDataFieldCode(){
        for (EDataField eDataField : meta) {
            if(eDataField.isKey()) {
                return eDataField.getCode();
            }
        }
        return null;
    }

    public boolean containDataFieldCode(String code) {
        for (EDataField eDataField : meta) {
            if(eDataField.getCode().equals(code)) return true;
        }
        return false;
    }

    public EDataField addDataField(String code, boolean isKey){
        EDataField eDataField = new EDataField(code, isKey);
        eDataField.setDataSet(this);
        this.meta.add(eDataField);
        if(isKey) keyIndex = this.meta.indexOf(eDataField);
        return eDataField;
    };

    public EDataField addDataFieldAndFillNull(String code){
        EDataField eDataField = addDataField(code, false);
        Integer fieldIndex = getFieldIndex(code);
        for (List<Object> row : getData()) {
            if(fieldIndex == row.size()) {
                row.add(null);
            }else if(fieldIndex < row.size()){
                row.set(fieldIndex, null);
            }else {
                throw new DataFetchException("data extend out of expect !");
            }
        }
        return eDataField;
    };

    public void fetch(Fetcher fetcher, List<EDataField> eDataFields, Set<Integer> rowIndexScope) {
        if(getKeyIndex() == -1){
            throw new DataFetchException("data set 's key field not exists !");
        }
        if(keyFieldType == null) {
            if(fetcher.getClass().getGenericInterfaces().length == 0) {
                keyFieldType = (Class)((ParameterizedType) fetcher.getClass().getSuperclass().getGenericInterfaces()[0]).getActualTypeArguments()[0];
            }else {
                keyFieldType = (Class)((ParameterizedType) fetcher.getClass().getGenericInterfaces()[0]).getActualTypeArguments()[0];
            }
        }

        String[] parameters = null;
        if(MysqlFetcher.class.isAssignableFrom(fetcher.getClass())){
            parameters = ((MysqlFetcher) fetcher).sqlParameters();
        }


        Set tmpKeySet = getKeySet(rowIndexScope);


        Set<Object[]> aliasKeySet = getAliasKeySet(rowIndexScope, parameters);
        if(parameters != null && parameters.length == 1){
            tmpKeySet = new HashSet();
            for (Object[] o : aliasKeySet) {
                tmpKeySet.add(o[0]);
            }
        }

        Set keySet = tmpKeySet;

        Object next = tmpKeySet.iterator().next();
        if(!next.getClass().isAssignableFrom(keyFieldType)) {
            keySet = new HashSet();
            for (Object o : tmpKeySet) {
                if(keyFieldType.equals(Integer.class)) {
                    keySet.add(Integer.valueOf(String.valueOf(o)));
                }else if(keyFieldType.equals(Long.class)) {
                    keySet.add(Long.valueOf(String.valueOf(o)));
                }else if(keyFieldType.equals(String.class)) {
                    keySet.add(String.valueOf(o));
                }else {
                    throw new DataFetchException("key field type mismatch [require:" + keyFieldType.getSimpleName() + ", input:" + next.getClass().getSimpleName() + "] !");
                }
                keyFieldTypeUnMatch = true;
            }
        }





        FetchData fetchData = null;
        try {
            if(MysqlFetcher.class.isAssignableFrom(fetcher.getClass())){
                fetchData = ((MysqlFetcher)fetcher).fetch(keySet, aliasKeySet);
            }else {
                fetchData = fetcher.fetch(keySet);
            }

        } catch (Exception e) {
            e.printStackTrace();
            throw new DataFetchException("fetch data error [" + e.getMessage() + "] !");
        }
        fullDataByFetchData(fetchData, eDataFields);
    }

    private Set<Object[]> getAliasKeySet(Set<Integer> rowIndexScope, String[] parameters) {
        if(parameters != null && parameters.length > 0) {
            Integer[] fieldIndexs = this.getFieldIndexs(parameters);
            return  getFieldValueSet(rowIndexScope, fieldIndexs);
        }else {
            return null;
        }
    }

    public void fetch(EDataField eDataField, Set<Integer> rowIndexScope) {
        fetch(eDataField.getFetcher(), Lists.newArrayList(eDataField), rowIndexScope);
    }

    private void fullDataByFetchData(FetchData fetchData, List<EDataField> eDataFields){
        for (List<Object> row : data) {
            Object key = row.get(keyIndex);
            Object value = null;
            if(keyFieldTypeUnMatch){
                if(keyFieldType.equals(Integer.class)) {
                    key = Integer.valueOf(String.valueOf(key));
                }else if(keyFieldType.equals(Long.class)) {
                    key = Long.valueOf(String.valueOf(key));
                }else if(keyFieldType.equals(String.class)) {
                    key = String.valueOf(key);
                }
            }

            for (EDataField eDataField : eDataFields) {
                if(fetchData.containsKey(key)) {
                    value = fetchData.getDataByKeyAndAttr(key, eDataField.getFetchAttr());
                    if(value == null) {
                        value = 0;
                    }
                }

                Integer fieldIndex = getFieldIndex(eDataField.getCode());
                if(fieldIndex == row.size()) {
                    row.add(value);
                }else if(fieldIndex < row.size()){
                    row.set(fieldIndex, value);
                }else {
                    throw new DataFetchException("data extend out of expect !");
                }
            }
        }
    }

    /**
     * 仅获取还没有结果的Key的集合
     * @return
     */
    private Set getKeySet(Set<Integer> rowIndexScope) {
        Set<Object> keySet = new HashSet<Object>();
        for (int i = 0; i < data.length; i++) {
            if(rowIndexScope != null && !rowIndexScope.contains(i)) continue;
            //这里使用label作为判断，原因只要没有出结果的数据都需要扩展查询一下，因为同样的数据别的分支可能还用到，所以不用curStageLabel
            if( label.length <= i || label[i] == null) {
                keySet.add(data[i].get(keyIndex));
            }

        }
        return keySet;
    }

    /**
     * 仅获取还没有结果的Key的集合
     * @return
     */
    private Set getFieldValueSet(Set<Integer> rowIndexScope, Integer[] indexs) {
        Set<Object[]> keySet = new HashSet<Object[]>();
        for (int i = 0; i < data.length; i++) {
            if(rowIndexScope != null && !rowIndexScope.contains(i)) continue;
            //这里使用label作为判断，原因只要没有出结果的数据都需要扩展查询一下，因为同样的数据别的分支可能还用到，所以不用curStageLabel
            if( label.length <= i || label[i] == null) {
                Object[] objects = new Object[indexs.length];
                for (int j = 0; j < indexs.length; j++) {
                    objects[j] = data[i].get(indexs[j]);
                }
                keySet.add(objects);
            }

        }
        return keySet;
    }

    public int getKeyIndex() {
        return keyIndex;
    }

    public void setKeyIndex(int keyIndex) {
        this.keyIndex = keyIndex;
    }

    public List<Object>[] getData() {
        return data;
    }

    public void setData(List<Object>[] data) {
        this.data = data;
    }

    public String[] getLabel() {
        return label;
    }

    public void setLabel(String[] label) {
        this.label = label;
    }

    public String[] getLastStepLabel() {
        return lastStepLabel;
    }

    public void setLastStepLabel(String[] lastStepLabel) {
        this.lastStepLabel = lastStepLabel;
    }

    public String[] getCurStageLabel() {
        return curStageLabel;
    }

    public void setCurStageLabel(String[] curStageLabel) {
        this.curStageLabel = curStageLabel;
    }

    public void flushStageLabel() {
        for (int i = 0; i < lastStepLabel.length; i++) {
            if(lastStepLabel[i] != null) {
                curStageLabel[i] = lastStepLabel[i];
            }
        }
    }
    public void flushLabel() {
        for (int i = 0; i < curStageLabel.length; i++) {
            if(curStageLabel[i] != null) {
                label[i] = curStageLabel[i];
            }
        }
    }


    public void clearLabel(int i) {
        label[i] = null;
    }

    @Override
    public String toString() {
        List<String> result = new ArrayList<>();
        StringBuffer tmp = new StringBuffer();
        tmp.append("- ");
        for (EDataField eDataField : meta) {
            tmp.append(eDataField.getCode()).append(", ");
        }
        tmp.append("[ RESULT ]");
        String tmpString = tmp.toString();
        result.add(tmpString);
        int maxLength = tmpString.length();

        for (int i = 0; i < data.length; i++) {
            tmp = new StringBuffer();
            tmp.append("- ");
            for (Object object : data[i]) {
                tmp.append(object).append(", ");
            }

            tmp.append("[ " + ((label == null || label.length <= i )? "UNKNOWN" : label[i]) + " ]");
            tmpString = tmp.toString();
            result.add(tmpString);
            maxLength = tmpString.length() > maxLength ? tmpString.length() : maxLength;
        }
        char[] chars = new char[maxLength + 2];
        Arrays.fill(chars, '-');

        String string = "";
        for (String row : result) {
            string = string + StringUtils.rightPad(row,maxLength) + " -\n";
        }
        String description = "";
        if(descriptions != null) {
            for (String fieldCode : descriptions.keySet()) {

                description += "- " + fieldCode + " : ";
                for (String desc : descriptions.get(fieldCode)) {
                    description += desc + "; ";
                }
                description += "\n";
            }
        }

        return String.valueOf(chars) + "\n" +
                string +
                String.valueOf(chars) + "\n" + description;
    }

    public String info(){
        long curTimeMillis = System.currentTimeMillis();
        StringBuffer sb = new StringBuffer();
        sb.append("total : " + data.length);
        sb.append(" (time : " + (curTimeMillis - createTimeMillis) / 1000 + "." + (curTimeMillis - createTimeMillis) % 1000 + "s)");
        sb.append("\n");

        Map<String, Integer> map = new HashMap<>();
        for (String result : label) {
            if(map.containsKey(result)){
                map.put(result, map.get(result) + 1);
            }else {
                map.put(result,1);
            }
        }

        for (Map.Entry<String, Integer> stringIntegerEntry : map.entrySet()) {
            sb.append("- " + stringIntegerEntry.getKey() + " : " + stringIntegerEntry.getValue() + " ( " + stringIntegerEntry.getValue() *100/label.length + "." + stringIntegerEntry.getValue()% label.length + "% )");
            sb.append("\n");
        }

        return sb.toString();
    }



    public void setFetcherOverview(Map<Fetcher, List<String>> fetcherOverview) {
        this.fetcherOverview = fetcherOverview;
    }

    public Map<Fetcher, List<String>> getFetcherOverview() {
        return fetcherOverview;
    }


    public void addDescription(String fieldCode, String description) {
        if(descriptions == null) {
            descriptions = new LinkedHashMap<>();
        }
        if(!descriptions.containsKey(fieldCode)) {
            descriptions.put(fieldCode, new ArrayList<String>());
        }
        descriptions.get(fieldCode).add(description);

    }


}
