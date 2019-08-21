package com.hframework.tracer;

import brave.Tracing;
import brave.propagation.CurrentTraceContext;
import brave.sampler.Sampler;
import com.hframework.common.springext.properties.PropertyConfigurerUtils;
import com.hframework.peacock.controller.base.dc.DC;
import com.hframework.peacock.controller.base.dc.DCUtils;
import org.apache.commons.lang3.StringUtils;
import zipkin2.Span;
import zipkin2.reporter.AsyncReporter;
import zipkin2.reporter.okhttp3.OkHttpSender;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;

public class TracerFactory {

    public enum Annotation{
        PARAMETER_PARSE,
        PRE_HANDLER_INVOKE,
        PRE_CHECK,
        HANDLER_INVOKE,
        RESULT_PACKAGE,
        sr,
        ss,
    }

    public enum TagKey{
        REQUEST,
        RESPONSE,
        PRE_CHECK,
        LOCAL_EXECUTE_CLASS,
        LOCAL_EXECUTE_METHOD,
        LOCAL_EXECUTE_PARAMETERS,

    }

    private static final OkHttpSender sender;
    private static AsyncReporter<Span> spanReporter;

    private static Map<String, Tracing> tracingCache;

    private static Map<Long, List<PeacockSampler.Build>> peacockSamplers = new HashMap<>();

    static {
        //创建一个Reporter，通过http的方式直接上报给zipkin server
        sender = OkHttpSender.create(PropertyConfigurerUtils.getProperty("zipkin.url"));
        spanReporter = AsyncReporter.create(sender);
        tracingCache = new HashMap<>();
        Runtime.getRuntime().addShutdownHook(new Thread() {
            public void run() {
               shutdownHook();
            }
        });
    }

    private  static synchronized void shutdownHook(){
        for (Tracing tracing : tracingCache.values()) {
            tracing.close();
        }
        spanReporter.close();
        sender.close();
    }


    public static Tracing getTrance(String program, String serverName){
        if(!tracingCache.containsKey(serverName)){
            synchronized (TracerFactory.class) {
                if(!tracingCache.containsKey(serverName)){
                    final Tracing tracing = Tracing.newBuilder()
                            .localServiceName(serverName)
                            .spanReporter(spanReporter)
//                            .sampler(RateLimitingSampler.create(100))
                            .sampler(getPeacockSample(program, serverName))
                            .build();
                    tracingCache.put(serverName, tracing);
                }
            }
        }
        return tracingCache.get(serverName);
    }


    public static PeacockSampler getPeacockSample(String program, String serverName){
        if(StringUtils.isNotBlank(program) && peacockSamplers.containsKey(Long.valueOf(program))) {
            List<PeacockSampler.Build> builds = peacockSamplers.get(Long.valueOf(program));
            for (PeacockSampler.Build build : builds) {
                if(build.match(Long.valueOf(program), serverName)) {
                    return build.build();
                }
            }
        }
        return new PeacockSampler(new HashMap<String, Object>(), Sampler.NEVER_SAMPLE);
    }

    public static synchronized void clearAllTrance(Map<Long, List<PeacockSampler.Build>> newPeacockSamplers){
        peacockSamplers = newPeacockSamplers;
        tracingCache.clear();
    }


    /**
     * peacock由于一个进程中多个Tracing，因此Tracing.current()并不适用，需要通过PeacockSpan透传
     */
    @Deprecated
    public static CurrentTraceContext.Scope maybeScope(brave.Span currentSpan) {
        return Tracing.current().currentTraceContext().maybeScope(TracerFactory.getCurrentSpanIfNull(currentSpan).context());
    }


    /**
     * peacock由于一个进程中多个Tracing，因此Tracing.current()并不适用，需要通过PeacockSpan透传
     */
    @Deprecated
     public static <C> Callable<C> wrap(Callable<C> task) {
        return Tracing.current().currentTraceContext().wrap(task);
    }
    /**
     * 添加子Span
     * @param parent
     * @param serviceName
     * @param name
     * @param path
     * @return
     */
    public static PeacockSpan addChildSpan(String program, PeacockSpan parent, String serviceName, String name, String path){
        brave.Span span =  TracerFactory.getTrance(program, serviceName).tracer().newChild(getCurrentSpanIfNull(parent.getSpan()).context())
                .kind(brave.Span.Kind.SERVER)
                .name(name)
                .tag("path", path)
                .tag("name", name)
                .tag("peacock.version", "1.0.1")
                .start();
//        Tracing.current().currentTraceContext().newScope(span.context());
        return new PeacockSpan(span, parent.getSampler());
    }

    public static PeacockSpan srSpanOrStartNewTrace(String program, String serviceName, String name, String path) {

        if(Tracing.currentTracer() == null || Tracing.currentTracer().currentSpan() == null) {
            return startNewTrace(program, serviceName, name, path);
        }else {
            return new PeacockSpan(addSpanAnnotation(getCurrentSpanIfNull(null), Annotation.sr), null);//是否应该显示传入currentSpan TODO
        }
    }

    public static PeacockSpan startNewTrace(String program, String serviceName, String name, String path) {
        Tracing trance = TracerFactory.getTrance(program, serviceName);
        brave.Span span = trance.tracer().newTrace()
                .kind(brave.Span.Kind.SERVER)
                .name(name)
                .tag("path", path)
                .tag("name", name)
                .tag("peacock.version", "1.0.1")
                .start();

//        Tracing.current().currentTraceContext().newScope(span.context());

        return new PeacockSpan(span, trance.sampler());
    }


    public static brave.Span addSpanAnnotation(PeacockSpan span, Annotation annotation){
        return addSpanAnnotation(span.getSpan(), annotation);
    }

    public static brave.Span addSpanAnnotation(brave.Span span, Annotation annotation){
        return getCurrentSpanIfNull(span).annotate(annotation.toString().toLowerCase());
    }

    public static brave.Span addTag(PeacockSpan span, TagKey tag, Object value, boolean canAdd){
        if(canAdd) {
            return getCurrentSpanIfNull(span.getSpan()).tag(tag.toString().toLowerCase(), value.toString());
        }else {
            return getCurrentSpanIfNull(span.getSpan());
        }
    }

    public static brave.Span addTag(PeacockSpan span, TagKey tag, Object value){
        if(value instanceof DC) {
            value = DCUtils.toPlantString(value);
        }
        return getCurrentSpanIfNull(span.getSpan()).tag(tag.toString().toLowerCase(), value.toString());
    }

    public static void finishCurrentSpan(PeacockSpan span){
        getCurrentSpanIfNull(span.getSpan()).finish();
    }

    public static void errorCurrentSpan(PeacockSpan span, Throwable throwable){
        getCurrentSpanIfNull(span.getSpan()).error(throwable);
    }



    public static brave.Span getCurrentSpanIfNull(brave.Span span) {
        if(span == null) {
            return Tracing.currentTracer().currentSpan();
        }else {
            return span;
        }
    }
}
