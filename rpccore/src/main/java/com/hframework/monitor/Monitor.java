package com.hframework.monitor;

/**
 * Created by zhangquanhong on 2016/9/26.
 */
public interface Monitor<T> {

    public void addListener(MonitorListener listener);

    public void notifyListener() throws Exception;
    /**启动*/
    public void start() throws Exception;
    /**重新加载*/
    public void reload();
    /**销毁*/
    public void destroy();
    public  T getObject() ;

}
