package com.guanmengyuan.spring.ex.common.model.dto.req;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 * 聚英电子操作请求
 *
 * @author guanmengyuan
 */
@Data
public class JYOpr {
    /**
     * 设备编号
     */
    @JsonProperty("unid")
    private String unId;
}
