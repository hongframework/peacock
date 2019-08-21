package com.hframework.peacock.controller.base.dc;

public class BreakDC extends NullDC {
    public static final BreakDC SINGLETON = new BreakDC();
    @Override
    public String toString() {
        return "BreakDC{}";
    }
}
