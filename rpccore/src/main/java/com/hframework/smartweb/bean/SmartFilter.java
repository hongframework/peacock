package com.hframework.smartweb.bean;

import org.springframework.web.context.request.WebRequest;

import java.util.List;
import java.util.Map;

/**
 * 扩展处理接口
 * Created by zhangquanhong on 2017/2/21.
 */
public interface SmartFilter {

    public boolean filter(String path, String version, String key, List<Map<String, Object>> returnObjects, Object attrObject, String[] when, Map<String, String> parameters) throws Exception;
}
