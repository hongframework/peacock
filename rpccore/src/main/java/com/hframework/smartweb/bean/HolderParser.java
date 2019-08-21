package com.hframework.smartweb.bean;

import org.apache.commons.lang3.tuple.Pair;
import org.springframework.web.context.request.WebRequest;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * Created by zhangquanhong on 2017/2/21.
 */
public interface HolderParser<T> {

    public T parse(String path, String version, String key, Map<String, String> parameters, WebRequest request);

}