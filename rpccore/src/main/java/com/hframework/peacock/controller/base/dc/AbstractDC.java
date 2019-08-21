package com.hframework.peacock.controller.base.dc;

public abstract class AbstractDC implements DC{
    protected DC prevDc;

    @Override
    public DC getPrevDc() {
        if(prevDc == null) {
            return NullDC.SINGLETON;
        }else {
            return prevDc;
        }
    }

    @Override
    public void setPrevDc(DC prevDc) {
        this.prevDc = prevDc;
    }

    @Override
    public DC copy() {
        return this;
    }
}
