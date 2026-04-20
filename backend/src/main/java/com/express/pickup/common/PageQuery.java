package com.express.pickup.common;

import lombok.Data;

/**
 * 分页查询参数类
 */
@Data
public class PageQuery {

    /**
     * 当前页码
     */
    private Long pageNum = 1L;
    /**
     * 每页数量
     */
    private Long pageSize = 10L;


    /**
     * 获取当前页码
     */
    public Long getPageNum() {return pageNum == null || pageNum < 1 ? 1L : pageNum;
    }

    /**
     * 获取每页数量
     */
    public Long getPageSize() {
        return pageSize == null || pageSize < 1 ? 10L : pageSize;
    }
}
