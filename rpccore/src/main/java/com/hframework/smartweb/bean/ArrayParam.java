package com.hframework.smartweb.bean;

import com.google.common.base.Joiner;

import java.lang.reflect.Array;
import java.util.Arrays;

public class ArrayParam {
    private Object o;

    public ArrayParam(Object o) {
        this.o = o;
    }

    public static ArrayParam valueOf(Object o) {
        return new ArrayParam(o);
    }

    public Object getO() {
        return o;
    }

    @Override
    public String toString() {
        StringBuffer sb = new StringBuffer();
        sb.append("[");
        for (int i = 0; i < Array.getLength(o); i++) {
            Object value = Array.get(this.o, i);
            sb.append(value);
            if(i < Array.getLength(o) - 1) {
                sb.append(",");
            }
        }
        sb.append("]");
        return "ArrayParam:" + sb.toString();
    }
}
