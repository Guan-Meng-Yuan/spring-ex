package com.guanmengyuan.spring.ex.common.model.enums;

import org.springframework.http.HttpStatusCode;

/**
 * 响应enum顶级接口
 *
 * @author guanmengyuan
 */
public interface BaseResEnum {
    /**
     * http状态
     *
     * @return http状态
     */
    HttpStatusCode getHttpStatusCode();

    /**
     * 错误信息
     *
     * @return 错误信息
     */
    String getMessage();

    /**
     * 用户提示
     *
     * @return 用户提示
     */
    String getTips();

    /**
     * 响应码
     *
     * @return 响应码
     */
    String getCode();
}
