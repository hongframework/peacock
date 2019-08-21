package com.hframework.peacock.handler.base.field;

import com.hframework.smartweb.annotation.Handler;
import com.hframework.smartweb.annotation.SmartParameter;
import com.hframework.smartweb.bean.SmartHandler;
import com.hframework.smartweb.bean.handler.AbstractSmartHandler;
import org.springframework.stereotype.Controller;

import java.util.List;
import java.util.Map;

/**
 * Created by zhangquanhong on 2017/2/21.
 */
@Controller
@Handler(path = "/base/pagination_resp_absolute")
public class PaginationSmartHandler extends AbstractSmartHandler implements SmartHandler {

        @Handler(version = "1.0.0", description = "分页响应(不带总数)")
        public PaginationInfo paginationParse(
                @SmartParameter(required = true, description = "当前页", defaultValue = "1") Long pageNo,
                @SmartParameter(required = true, description = "页面大小", defaultValue = "20") Long pageSize,
                @SmartParameter(required = true, description = "数据集合") List<Map<String, Object>> list) {

                boolean hasNext = false;
                Long totalSize = (pageNo - 1) * pageSize + (list == null ? 0 : list.size()) ;
                if(list != null && list.size() > pageSize) {
//                        list.remove(list.size() - 1);
                        list.get(list.size() - 1).put("NEXT_PAGINATION_FLAT", true);
                        // 那么后续进行ListDC与GroupDC merge出现(21vs20条)报错，所以这里不直接删除，只是标记删除
                        //如果一次查询的返回list会进行关联查询，比如SqlQueryOneHandler关联，如果这里直接删除，
                        hasNext = true;
                }

                return new PaginationInfo(pageNo, pageSize, totalSize, hasNext? (pageNo + 1): pageNo, hasNext);
        }
}
