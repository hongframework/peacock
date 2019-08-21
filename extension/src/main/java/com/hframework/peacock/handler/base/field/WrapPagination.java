package com.hframework.peacock.handler.base.field;

import com.hframework.smartweb.annotation.SmartDescription;

public class WrapPagination {
    @SmartDescription("分页偏移量")
    private Long pageOffset;

    @SmartDescription("记录数加一")
    private Long pageSizePlus;

    public WrapPagination(Long pageOffset, Long pageSizePlus) {
        this.pageOffset = pageOffset;
        this.pageSizePlus = pageSizePlus;
    }

    public Long getPageOffset() {
        return pageOffset;
    }

    public void setPageOffset(Long pageOffset) {
        this.pageOffset = pageOffset;
    }


    public Long getPageSizePlus() {
        return pageSizePlus;
    }

    public void setPageSizePlus(Long pageSizePlus) {
        this.pageSizePlus = pageSizePlus;
    }
}
