package com.hframework.peacock.handler.base.field;

import com.hframework.smartweb.annotation.SmartDescription;

public class PaginationInfo {
    @SmartDescription("当前页编号")
    private Long pageNo;
    @SmartDescription("页大小")
    private Long pageSize;
    @SmartDescription("总记录数")
    private Long totalSize;
    @SmartDescription("总页数")
    private Long totalPage;
    @SmartDescription("是否有下一页")
    private boolean hasNext;

    public PaginationInfo(Long pageNo, Long pageSize, Long totalSize, Long totalPage, boolean hasNext) {
        this.pageNo = pageNo;
        this.pageSize = pageSize;
        this.totalSize = totalSize;
        this.totalPage = totalPage;
        this.hasNext = hasNext;
    }

    public PaginationInfo() {
    }

    public Long getPageNo() {
        return pageNo;
    }

    public void setPageNo(Long pageNo) {
        this.pageNo = pageNo;
    }

    public Long getPageSize() {
        return pageSize;
    }

    public void setPageSize(Long pageSize) {
        this.pageSize = pageSize;
    }

    public Long getTotalSize() {
        return totalSize;
    }

    public void setTotalSize(Long totalSize) {
        this.totalSize = totalSize;
    }

    public Long getTotalPage() {
        return totalPage;
    }

    public void setTotalPage(Long totalPage) {
        this.totalPage = totalPage;
    }

    public boolean isHasNext() {
        return hasNext;
    }

    public void setHasNext(boolean hasNext) {
        this.hasNext = hasNext;
    }
}
