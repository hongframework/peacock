package com.hframework.smartweb;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by zhangquanhong on 2017/3/7.
 */
public class LogHelper {

    private static Logger logger = LoggerFactory.getLogger(LogHelper.class);
    private static Map<Thread, RequestInfo> cache = new ConcurrentHashMap<>();

    public static void init(HttpServletRequest request) {
        cache.put(Thread.currentThread(), RequestInfo.valueOf(request));
    }

    public static class  RequestInfo{
        private Thread mainThread;
        private HttpServletRequest request;
        private String url;
        private String method;
        private String parameters;
        private long mainStartTimestamp;

        private List<Thread> subThreads = new ArrayList<>();

        public Thread getMainThread() {
            return mainThread;
        }

        public void setMainThread(Thread mainThread) {
            this.mainThread = mainThread;
        }

        public HttpServletRequest getRequest() {
            return request;
        }

        public void setRequest(HttpServletRequest request) {
            this.request = request;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public String getParameters() {
            return parameters;
        }

        public void setParameters(String parameters) {
            this.parameters = parameters;
        }

        public List<Thread> getSubThreads() {
            return subThreads;
        }

        public void setSubThreads(List<Thread> subThreads) {
            this.subThreads = subThreads;
        }

        public void addSubThread(Thread subThread){
            subThreads.add(subThread);
        }

        public String getMethod() {
            return method;
        }

        public void setMethod(String method) {
            this.method = method;
        }

        public long getMainStartTimestamp() {
            return mainStartTimestamp;
        }

        public void setMainStartTimestamp(long mainStartTimestamp) {
            this.mainStartTimestamp = mainStartTimestamp;
        }

        public static RequestInfo valueOf(HttpServletRequest request) {
            RequestInfo requestInfo = new RequestInfo();
            requestInfo.setRequest(request);
            requestInfo.setMainThread(Thread.currentThread());
            requestInfo.setUrl(request.getRequestURI());
            String paramString = getParamString(request);
            requestInfo.setParameters(paramString);
            requestInfo.setMethod(StringUtils.isNotBlank(paramString) && StringUtils.isBlank(request.getQueryString()) ? "POST" : "GET");
            return requestInfo;
        }
    }

    private static String getParamString(HttpServletRequest request) {

        String queryString = request.getQueryString();
        if(StringUtils.isNotBlank(queryString)) {
            return queryString;
        }else {
            queryString = "";
            Map parameters = request.getParameterMap();//保存request请求参数的临时变量
            Iterator paiter = parameters.keySet().iterator();
            while (paiter.hasNext()) {
                String key = paiter.next().toString();
                String[] values = (String[])parameters.get(key);
                queryString += (key+"="+values[0] + "&");
            }
        }
        return queryString;
    }

    public static enum Type{
        Statistics;
    }
    public static enum Stage{
        Begin, StageBegin, StageEnd, ParallelBegin, ParallelEnd, MainStart, MainEnd, End, ExceptionEnd;
    }

    public static void start(Type type, HttpServletRequest request, Object... args) {
        init(request);
        log(type, Stage.Begin, args);
    }

    public static void end(Type type, Object... args) {
        cache.remove(Thread.currentThread());
        log(type, Stage.End, args);
    }

    public static void stageStart(Type type, Object... args) {
        log(type, Stage.StageBegin, args);
    }

    public static void stageEnd(Type type, Object... args) {
        log(type, Stage.StageEnd, args);
    }

    public static void mainStart(Type type, Object... args) {
        cache.get(Thread.currentThread()).setMainStartTimestamp(System.currentTimeMillis());
    }

    public static void mainEnd(Type type, Object... args) {
        log(type, Stage.MainStart, cache.get(Thread.currentThread()).getMainStartTimestamp(), args);
        log(type, Stage.MainEnd, args);
    }

    public static void stageException(Type type, Object... args) {
        log(type, Stage.ExceptionEnd, args);
    }

    public static void parallelStart(Type type, Thread mainThread, Object... args) {
        String name = Thread.currentThread().getName();
        Thread.currentThread().setName(mainThread.getName() + " > " + name);
//        System.out.println("cache = {" + cache + "}, thread = {" + Thread.currentThread() + "}, request = {" + cache.get(mainThread) + "}");
        cache.put(Thread.currentThread(), cache.get(mainThread));
        log(type, Stage.ParallelBegin, args);

    }

    public static void parallelEnd(Type type, Object... args) {
        log(type, Stage.ParallelEnd, args);
        String name = Thread.currentThread().getName();
        Thread.currentThread().setName(name.substring(name.lastIndexOf(">") + 1).trim());
        cache.remove(Thread.currentThread());
    }

    public static void parallelException(Type type, Object... args) {
        log(type, Stage.ExceptionEnd, args);
    }

    public static void log(Type type,Stage stage, Object... args) {
        log(type, stage, System.currentTimeMillis(), args);
    }

    public static void log(Type type,Stage stage, Long timestamp, Object... args) {
        RequestInfo requestInfo = cache.get(Thread.currentThread());
        StringBuffer sb = new StringBuffer()
                .append(type.name())
                .append("|")
                .append(stage.name())
                .append("|")
                .append(Thread.currentThread().getName())
                .append("|")
                .append(timestamp);
        if(stage.equals(Stage.Begin)) {
            sb.append("|")
                .append(requestInfo.getUrl())
                .append("|")
                .append(requestInfo.getMethod())
                .append("|")
                .append(requestInfo.getParameters());
        }



        if(args != null) {
            for (Object arg : args) {
                if(arg instanceof String && ((String)arg).contains("|")) {
                    arg = (((String) arg).replaceAll("|", "\\\\|"));
                }
                sb.append("|").append(arg);
            }
        }
        logger.debug(sb.toString());
    }

}
