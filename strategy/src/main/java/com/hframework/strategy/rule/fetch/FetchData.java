package com.hframework.strategy.rule.fetch;

import com.hframework.strategy.rule.exceptions.DataFetchException;
import org.apache.commons.beanutils.BeanUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by zhangquanhong on 2017/6/27.
 */
public class FetchData<K,O> {
    private Map<K,O> data = new HashMap<K, O>();
    public static <O, K> FetchData swap(List<O> list, KeyMap<O,K> keyMap) {
        FetchData<K, O> fetchData = new FetchData();
        for (O o : list) {
            fetchData.getData().put(keyMap.map(o), o);
        }
        return fetchData;
    }
    public interface KeyMap<O,K>{
        public K map(O t);
    }

    public Map<K, O> getData() {
        return data;
    }

    public FetchData setData(Map<K, O> data) {
        this.data = data;
        return this;
    }

    public boolean containsKey(K key){
        return this.data.containsKey(key);
    }

    public Object getDataByKeyAndAttr(K key, String attrName) {
        O o = this.data.get(key);
        if(o == null) {
            return null;
        }
        if (o instanceof Map) {
            Map map = (Map) o;
            return map.get(attrName);
        }else if(attrName == null) {
            return o;
        }else{
            try {
                return BeanUtils.getProperty(o,attrName);
            } catch (Exception e) {
                throw new DataFetchException("bean " + o + " don't contain " + attrName + "!");
            }
        }
    }
}
