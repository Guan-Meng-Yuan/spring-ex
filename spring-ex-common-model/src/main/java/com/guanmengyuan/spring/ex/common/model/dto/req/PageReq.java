package com.guanmengyuan.spring.ex.common.model.dto.req;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.io.Serializable;

/**
 * 分页请求体
 */
@Data
public class PageReq implements Serializable {
    /**
     * 当前页码
     */
    @NotNull(message = "页码不能为空")
    private Integer pageNumber;
    /**
     * 每页条数
     */
    @NotNull(message = "条数不能为空")
    private Integer pageSize;

}
