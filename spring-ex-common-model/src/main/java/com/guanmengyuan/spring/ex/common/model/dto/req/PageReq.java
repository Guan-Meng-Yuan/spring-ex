package com.guanmengyuan.spring.ex.common.model.dto.req;

import com.mybatisflex.core.paginate.Page;
import lombok.Data;

import java.io.Serializable;

/**
 * 分页参数
 */
@Data
public class PageReq<T> implements Serializable {
    /**
     * 默认构造
     */
    public PageReq() {
    }

    /**
     * 当前页,默认第一页
     */
    private Integer page = 1;
    /**
     * 每页条数,默认10条
     */
    private Integer pageSize = 10;

    /**
     * 查询关键字
     */
    private String keywords;

    /**
     * 转换page对象方法
     *
     * @param pageReq page参数
     * @return page对象
     */
    public static <T> Page<T> of(PageReq<T> pageReq) {
        return Page.of(pageReq.getPage(), pageReq.getPageSize());
    }

    public Page<T> of() {
        return Page.of(this.page, this.pageSize);
    }
}