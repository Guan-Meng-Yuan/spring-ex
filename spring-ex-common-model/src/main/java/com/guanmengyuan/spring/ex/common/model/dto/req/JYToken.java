package com.guanmengyuan.spring.ex.common.model.dto.req;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 聚英token
 *
 * @author guanmengyuan
 */
@Data
public class JYToken implements Serializable {
    @JsonProperty("accesstoken")
    private String accessToken;
    @JsonProperty("refreshToken")
    private String refreshToken;

    private Long timestamp;

}
