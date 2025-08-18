package com.guanmengyuan.spring.ex.common.model.dto.req;

import com.mybatisflex.core.paginate.Page;
import lombok.Data;

import java.io.Serializable;

/**
 * 分页参数 - 兼容多种分页参数命名
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
     * 当前页,默认第一页 (兼容current)
     */
    private Integer current = 1;

    /**
     * 当前页,默认第一页 (兼容pageNumber)
     */
    private Integer pageNumber = 1;

    /**
     * 当前页,默认第一页 (兼容pageNum)
     */
    private Integer pageNum = 1;

    /**
     * 当前页,默认第一页 (兼容pageIndex)
     */
    private Integer pageIndex = 1;

    /**
     * 每页条数,默认10条
     */
    private Integer pageSize = 10;

    /**
     * 每页条数,默认10条 (兼容size)
     */
    private Integer size = 10;

    /**
     * 每页条数,默认10条 (兼容limit)
     */
    private Integer limit = 10;

    /**
     * 每页条数,默认10条 (兼容pageCount)
     */
    private Integer pageCount = 10;

    /**
     * 查询关键字
     */
    private String keywords;

    /**
     * 查询关键字 (兼容keyword)
     */
    private String keyword;

    /**
     * 查询关键字 (兼容search)
     */
    private String search;

    /**
     * 查询关键字 (兼容q)
     */
    private String q;

    /**
     * 排序字段
     */
    private String sortBy;

    /**
     * 排序方向 (asc/desc)
     */
    private String sortOrder;

    /**
     * 获取当前页码 (优先使用page，其次current，最后pageNumber)
     */
    public Integer getCurrentPage() {
        if (page != null && page >= 1) {
            return page;
        }
        if (current != null && current >= 1) {
            return current;
        }
        if (pageNumber != null && pageNumber >= 1) {
            return pageNumber;
        }
        if (pageNum != null && pageNum >= 1) {
            return pageNum;
        }
        if (pageIndex != null && pageIndex >= 1) {
            return pageIndex;
        }
        return 1;
    }

    /**
     * 获取每页条数 (优先使用pageSize，其次size，最后limit)
     */
    public Integer getCurrentPageSize() {
        if (pageSize != null && pageSize >= 1) {
            return pageSize;
        }
        if (size != null && size >= 1) {
            return size;
        }
        if (limit != null && limit >= 1) {
            return limit;
        }
        if (pageCount != null && pageCount >= 1) {
            return pageCount;
        }
        return 10;
    }

    /**
     * 获取查询关键字 (优先使用keywords，其次keyword，最后search)
     */
    public String getSearchKeywords() {
        if (keywords != null && !keywords.trim().isEmpty()) {
            return keywords.trim();
        }
        if (keyword != null && !keyword.trim().isEmpty()) {
            return keyword.trim();
        }
        if (search != null && !search.trim().isEmpty()) {
            return search.trim();
        }
        if (q != null && !q.trim().isEmpty()) {
            return q.trim();
        }
        return null;
    }

    /**
     * 转换page对象方法 (兼容旧版本)
     *
     * @param pageReq page参数
     * @return page对象
     */
    public static <T> Page<T> of(PageReq<T> pageReq) {
        return Page.of(pageReq.getCurrentPage(), pageReq.getCurrentPageSize());
    }

    /**
     * 转换page对象方法 (推荐使用)
     */
    public Page<T> toPage() {
        return Page.of(getCurrentPage(), getCurrentPageSize());
    }

    /**
     * 兼容旧版本的of方法
     */
    public Page<T> of() {
        return toPage();
    }

    /**
     * 获取偏移量 (用于数据库查询)
     */
    public Integer getOffset() {
        return (getCurrentPage() - 1) * getCurrentPageSize();
    }

    /**
     * 获取总页数 (根据总数计算)
     */
    public Integer getTotalPages(Integer totalCount) {
        if (totalCount == null || totalCount <= 0) {
            return 0;
        }
        return (int) Math.ceil((double) totalCount / getCurrentPageSize());
    }
}