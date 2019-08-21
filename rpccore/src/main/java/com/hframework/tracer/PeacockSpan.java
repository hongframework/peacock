package com.hframework.tracer;

import brave.Span;
import brave.Tracing;
import brave.sampler.Sampler;

public class PeacockSpan {

    private Sampler sampler;

    private Span span;

    public PeacockSpan(Span span, Sampler sampler) {
        this.sampler = sampler;
        this.span = span;
    }

    public Sampler getSampler() {
        return sampler;
    }

    public Span getSpan() {
        return span;
    }

    public  PeacockSampler getPeacockSampler(){
        if(!(sampler instanceof PeacockSampler)) {
            throw new RuntimeException(sampler + " is not instance of PeacockSampler !");
        }
        return (PeacockSampler) sampler;
    }


    public boolean hasApiRequestTag() {
        return getPeacockSampler().hasApiRequestTag();
    }

    public boolean hasApiResponseTag() {
        return getPeacockSampler().hasApiResponseTag();
    }

    public boolean hasHandlerRequestTag() {
        return getPeacockSampler().hasHandlerRequestTag();
    }

    public boolean hasHandlerResponseTag() {
        return getPeacockSampler().hasHandlerResponseTag();
    }

    public boolean hasIOResponseTag() {
        return getPeacockSampler().hasIOResponseTag();
    }

    public boolean hasIORequestTag() {
        return getPeacockSampler().hasIORequestTag();
    }
}
