package com.guanmengyuan.spring.ex.common.model.dto.req;

import java.io.Serializable;

import lombok.Data;

/**
 * 分页参数
 */
@Data
public class PageReq implements Serializable {
    /**
     * 当前页,默认第一页
     */
    private Integer currentPage = 1;
    /**
     * 每页条数,默认10条
     */
    private Integer pageSize = 10;

    /**
     * 查询关键字
     */
    private String keywords;
}
