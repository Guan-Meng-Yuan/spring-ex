package com.guanmengyuan.spring.ex.common.model.enums;

import org.springframework.http.HttpStatusCode;

/**
 * 响应enum顶级接口
 *
 * @author guanmengyuan
 */
public interface BaseResEnum {
    HttpStatusCode getHttpStatusCode();

    String getMessage();

    String getTips();
}
