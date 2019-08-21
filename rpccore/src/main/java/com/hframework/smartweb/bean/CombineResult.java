package com.hframework.smartweb.bean;

import com.hframework.smartweb.annotation.SmartDescription;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class CombineResult {
    @SmartDescription(name = "../offsetA", value = "集合A新偏移量")
    private Long offsetA;
    @SmartDescription(name = "../offsetB", value = "集合B新偏移量")
    private Long offsetB;
    @SmartDescription(main = true, value = "合并后数据集合")
    private List<Map<String, Object>> data = new ArrayList<>();

    public CombineResult(Long offsetA, Long offsetB, List<Map<String, Object>> data) {
        this.offsetA = offsetA;
        this.offsetB = offsetB;
        this.data = data;
    }

    public Long getOffsetA() {
        return offsetA;
    }

    public void setOffsetA(Long offsetA) {
        this.offsetA = offsetA;
    }

    public Long getOffsetB() {
        return offsetB;
    }

    public void setOffsetB(Long offsetB) {
        this.offsetB = offsetB;
    }

    public List<Map<String, Object>> getData() {
        return data;
    }

    public void setData(List<Map<String, Object>> data) {
        this.data = data;
    }
}
