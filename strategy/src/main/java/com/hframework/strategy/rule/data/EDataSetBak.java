package com.hframework.strategy.rule.data;

import com.google.common.collect.Lists;
import com.hframework.common.util.StringUtils;
import com.hframework.strategy.rule.exceptions.DataFetchException;
import com.hframework.strategy.rule.fetch.FetchData;
import com.hframework.strategy.rule.fetch.Fetcher;

import java.lang.reflect.Field;
import java.util.*;

/**
 * Created by zhangquanhong on 2017/6/26.
 */
public class EDataSetBak {

    public List<EDataField> meta = new ArrayList<EDataField>();
    public List<List<Object>> data = new ArrayList<List<Object>>();
//    public List<Object>[] data = null;
    public List<String> label = new ArrayList<String>();
    public List<String> lastStepLabel = new ArrayList<String>();
    public List<String> curStageLabel = new ArrayList<String>();

    private Long createTimeMillis = System.currentTimeMillis();

    private int keyIndex = -1;

    public EDataSetBak(List<String> metaData, List<List<Object>> data, int keyIndex) {
        for (int i = 0; i < metaData.size(); i++) {
            if(i == keyIndex){
                addDataField(metaData.get(i), true);
            }else {
                addDataField(metaData.get(i), false);
            }
        }
        this.data = data;
    }

    public EDataSetBak(Object[][] data, int keyIndex) {
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

    public EDataSetBak(String[] metaData, Object[][] data, int keyIndex) {
        for (int i = 0; i < metaData.length; i++) {
            if(i == keyIndex){
                addDataField(metaData[i], true);
            }else {
                addDataField(metaData[i], false);
            }
        }
        List<List<Object>> dataList = new ArrayList<>();
        for (Object[] objects : data) {
            dataList.add(new ArrayList(Arrays.asList(objects)));
        }
        this.data = dataList;
    }
    public EDataSetBak(String[] metaData, Object[][] data){
        this(metaData, data, 0);
    }
    public EDataSetBak(List<String> metaData, List<List<Object>> data) {
        this(metaData, data, 0);
    }

    public EDataSetBak(String meta, String data) {
        addDataField(meta,true);
        this.data.add(Lists.newArrayList((Object) data));
    }
    public EDataSetBak(String meta, List<String> datas) {
        addDataField(meta, true);
        for (String data : datas) {
            this.data.add(Lists.newArrayList((Object)data));
        }
    }

    public EDataSetBak(Object object) {
        addObjectToData(object);
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

    private void addObjectToData(Object object){
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
        data.add(listObject);
    }


    public EDataSetBak(List objects) {
        for (Object object : objects) {
            addObjectToData(object);
        }
    }

    public List<EDataField> getMeta() {
        return meta;
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

    public void setData(List<List<Object>> data) {
        this.data = data;
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
        eDataField.setDataSet(null);//TODO
        this.meta.add(eDataField);
        if(isKey) keyIndex = this.meta.indexOf(eDataField);
        return eDataField;
    };

    public void fetch(EDataField eDataField, Set<Integer> rowIndexScope) {
        if(getKeyIndex() == -1){
            throw new DataFetchException("data set 's key field not exists !");
        }
        Fetcher fetcher = eDataField.getFetcher();
        FetchData fetchData = null;
        try {
            fetchData = fetcher.fetch(getKeySet(rowIndexScope));
        } catch (Exception e) {
            e.printStackTrace();
            throw new DataFetchException("fetch data error [" + e.getMessage() + "] !");

        }
        fullDataByFetchData(fetchData, eDataField);
    }

    private void fullDataByFetchData(FetchData fetchData, EDataField eDataField){
        for (List<Object> row : data) {
            Object key = row.get(keyIndex);
            Object value = null;
            if(fetchData.containsKey(key)) {
                 value = fetchData.getDataByKeyAndAttr(key, eDataField.getFetchAttr());
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

    /**
     * 仅获取还没有结果的Key的集合
     * @return
     */
    private Set getKeySet(Set<Integer> rowIndexScope) {
        Set<Object> keySet = new HashSet<Object>();
        for (int i = 0; i < data.size(); i++) {
            if(rowIndexScope != null && !rowIndexScope.contains(i)) continue;
            //这里使用label作为判断，原因只要没有出结果的数据都需要扩展查询一下，因为同样的数据别的分支可能还用到，所以不用curStageLabel
            if( label.size() <= i || label.get(i) == null) {
                keySet.add(data.get(i).get(keyIndex));
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

    public List<List<Object>> getData() {
        return data;
    }

    public List<String> getLabel() {
        return label;
    }

    public void setLabel(List<String> label) {
        this.label = label;
    }

    public List<String> getLastStepLabel() {
        return lastStepLabel;
    }

    public void setLastStepLabel(List<String> lastStepLabel) {
        this.lastStepLabel = lastStepLabel;
    }

    public List<String> getCurStageLabel() {
        return curStageLabel;
    }

    public void setCurStageLabel(List<String> curStageLabel) {
        this.curStageLabel = curStageLabel;
    }

    public void flushStageLabel() {
        for (int i = 0; i < lastStepLabel.size(); i++) {
            if(lastStepLabel.get(i) != null) {
                curStageLabel.set(i, lastStepLabel.get(i));
            }
        }
    }
    public void flushLabel() {
        for (int i = 0; i < curStageLabel.size(); i++) {
            if(curStageLabel.get(i) != null) {
                label.set(i, curStageLabel.get(i));
            }
        }
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

        for (int i = 0; i < data.size(); i++) {
            tmp = new StringBuffer();
            tmp.append("- ");
            for (Object object : data.get(i)) {
                tmp.append(object).append(", ");
            }

            tmp.append("[ " + ((label == null || label.size() <= i )? "UNKNOWN" : label.get(i)) + " ]");
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
        return String.valueOf(chars) + "\n" +
                string +
                String.valueOf(chars) + "\n";
    }

    public String info(){
        long curTimeMillis = System.currentTimeMillis();
        StringBuffer sb = new StringBuffer();
        sb.append("total : " + data.size());
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
            sb.append("- " + stringIntegerEntry.getKey() + " : " + stringIntegerEntry.getValue() + " ( " + stringIntegerEntry.getValue() *100/label.size() + "." + stringIntegerEntry.getValue()% label.size()  + "% )");
            sb.append("\n");
        }

        return sb.toString();
    }

}
