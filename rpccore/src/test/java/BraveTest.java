import brave.Span;
import brave.Tracing;
import brave.propagation.ExtraFieldPropagation;
import com.hframework.smartweb.SmartThreadPoolExecutorFactory;
import com.hframework.tracer.TracerFactory;
import org.junit.Test;
import zipkin2.storage.InMemoryStorage;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;

public class BraveTest {

    private InMemoryStorage storage;

    private ExecutorService executorService = SmartThreadPoolExecutorFactory.getExecutorServiceObject();

    @Test
    public void  simple_span() throws InterruptedException {
        brave.Span root = TracerFactory.getTrance("1", "client").tracer().newTrace().name("service_1").start();
        root.tag("path", "/cust/get_cust_lists");
        root.tag("name", "获取客户列表");
        root.annotate("step1");
        root.annotate("step2");
        root.annotate("step3");
        root.kind(brave.Span.Kind.CLIENT);
        //do your business...
        ExtraFieldPropagation.set("country-code", "FO");
        root.customizer().name("111").tag("222","3333");
        root.finish();

        brave.Span span1 = TracerFactory.getTrance("1","service").tracer().newChild(root.context()).name("service_1.1").start();
        span1.tag("handler", "true");
        span1.finish();

        TracerFactory.getTrance("1","service1").tracer().newChild(root.context()).name("service_1.2").start().finish();
        TracerFactory.getTrance("1","service2").tracer().newChild(root.context()).name("service_1.3").start().finish();

        TracerFactory.getTrance("1","service3").tracer().newChild(span1.context()).name("service_1.1.1").start().finish();
        TracerFactory.getTrance("1","service4").tracer().newChild(span1.context()).name("service_1.1.2").start().finish();
    }


    @Test
    public void  test_execute_service() throws InterruptedException {

        Span root = TracerFactory.startNewTrace("1","root", "root", "root").getSpan();
        for (int i = 0; i < 10; i++) {
            final String serviceName = "service-" + i;
            executorService.submit(Tracing.current().currentTraceContext().wrap(new Callable<Object>() {
                @Override
                public Object call() throws Exception {
                    TracerFactory.addChildSpan("1",null, serviceName, serviceName,serviceName).getSpan().finish();
                    Thread.sleep(1000L);
                    return null;
                }
            }));
        }
        root.finish();
        Thread.sleep(1000L);

    }
}
