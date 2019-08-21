package com.hframework.strategy.rule.data;

import com.hframework.strategy.rule.fetch.Fetcher;

/**
 * Created by zhangquanhong on 2017/6/26.
 */
public class EDataField {

    private String code;
    private boolean isKey = false;
    private boolean isFetch = false;
    private Fetcher fetcher;
    private String fetchAttr;

    private EDataSet dataSet;


    public EDataField() {
    }

    public EDataField(String code) {
        this.code = code;
    }

    public EDataField(String code, boolean isKey) {
        this.code = code;
        this.isKey = isKey;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public boolean isKey() {
        return isKey;
    }

    public void setIsKey(boolean isKey) {
        this.isKey = isKey;
    }

    public Fetcher getFetcher() {
        return fetcher;
    }

    public void setFetcher(Fetcher fetcher) {
        this.fetcher = fetcher;
    }

    public boolean isFetch() {
        return isFetch;
    }

    public void setIsFetch(boolean isFetch) {
        this.isFetch = isFetch;
    }

    public EDataSet getDataSet() {
        return dataSet;
    }

    public void setDataSet(EDataSet dataSet) {
        this.dataSet = dataSet;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof EDataField) {
            EDataField eDataField = (EDataField) obj;
            return this.getCode().equals(eDataField.getCode()) /*&&
                    this.isKey == eDataField.isKey() &&
                    ((this.fetcher == null && eDataField.getFetcher() == null)
                            || (this.fetcher != null && this.fetcher.equals(eDataField.getFetcher())))*/;
        }else if (obj instanceof String) {
            return this.getCode().equals(obj);
        }
        return false;
    }

    @Override
    public int hashCode() {
        return code.hashCode() /*+ (isKey?0:1) + fetcher.hashCode()*/;
    }


    public void fetch() {
        dataSet.fetch(this, null);
    }

    public String getFetchAttr() {
        return fetchAttr;
    }

    public void setFetchAttr(String fetchAttr) {
        this.fetchAttr = fetchAttr;
    }
}
