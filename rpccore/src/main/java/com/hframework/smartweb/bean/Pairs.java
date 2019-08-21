package com.hframework.smartweb.bean;

import org.apache.commons.lang3.tuple.Pair;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhangquanhong on 2017/6/5.
 */
public class Pairs<L, R> {

    private List<Pair<L, R>> pairs = new ArrayList<>();

    public Pairs push(final L left) {
        pairs.add(Pair.<L, R>of(left,null));
        return this;
    }

    public Pairs push(final L left, final R right) {
        pairs.add(Pair.of(left,right));
        return this;
    }

    public static Pairs instance(){
        return new Pairs();
    }
}
