package com.hframework.peacock.handler.base.field;

import com.hframework.smartweb.annotation.Handler;
import com.hframework.smartweb.annotation.SmartParameter;
import com.hframework.smartweb.bean.SmartHandler;
import com.hframework.smartweb.bean.handler.AbstractSmartHandler;
import org.springframework.stereotype.Controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by zhangquanhong on 2017/2/21.
 */
@Controller
@Handler(path = "/base/pagination_warp")
public class WrapPaginationHandler extends AbstractSmartHandler implements SmartHandler {

        @Handler(version = "1.0.0", description = "分页请求扩展")
        public WrapPagination warp(
                @SmartParameter(required = true, description = "当前页", defaultValue = "1") Long pageNo,
                @SmartParameter(required = true, description = "页面大小", defaultValue = "20") Long pageSize){
                return new WrapPagination((pageNo -1) * pageSize, pageSize + 1);

        }
}
