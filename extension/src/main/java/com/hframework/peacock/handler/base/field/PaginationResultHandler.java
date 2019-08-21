package com.hframework.peacock.handler.base.field;

import com.alibaba.fastjson.JSONObject;
import com.hframework.smartweb.annotation.Handler;
import com.hframework.smartweb.annotation.SmartParameter;
import com.hframework.smartweb.bean.SmartHandler;
import com.hframework.smartweb.bean.handler.AbstractSmartHandler;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by zhangquanhong on 2017/2/21.
 */
@Controller
@Handler(path = "/base/pagination_resp_relative")
public class PaginationResultHandler extends AbstractSmartHandler implements SmartHandler {

        @Handler(version = "1.0.0", description = "分页响应(带总数)")
        public PaginationInfo paginationParse(
                @SmartParameter(required = true, description = "当前页", defaultValue = "1") Long pageNo,
                @SmartParameter(required = true, description = "页面大小", defaultValue = "20") Long pageSize,
                @SmartParameter(required = true, description = "总记录数") Long totalSize
                ) {

                Long totalPage = totalSize / pageSize + (totalSize % pageSize > 0 ? 1 : 0);
                return new PaginationInfo(pageNo, pageSize, totalSize, totalPage, totalPage > pageNo);
        }
}
