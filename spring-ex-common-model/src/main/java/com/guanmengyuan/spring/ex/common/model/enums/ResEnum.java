package com.guanmengyuan.spring.ex.common.model.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

/**
 * 响应枚举
 */
@AllArgsConstructor
@Getter
public enum ResEnum implements BaseResEnum {
    /**
     * 正常
     */
    SUCCESS(HttpStatus.OK, "ok", "操作成功"),
    /**
     * 未授权
     */
    UNAUTHORIZED(HttpStatus.UNAUTHORIZED, "unauthorized", "未授权"),
    /**
     * 枚举参数不支持
     */
    NOT_AN_ENUMERATION(HttpStatus.NOT_ACCEPTABLE, "header enum not support", "网络异常"),
    /**
     * id不能为空
     */
    ID_CAN_NOT_BE_EMPTY(HttpStatus.BAD_REQUEST, "ID不能为空", "参数错误"),
    /**
     * 参数不能为空
     */
    BODY_CAN_NOT_BE_EMPTY(HttpStatus.BAD_REQUEST, "参数不能为空", "参数错误"),
    /**
     * 网络异常,默认错误响应
     */
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "网络异常", "网络异常"),
    ;
    private final HttpStatusCode httpStatusCode;
    private final String message;
    private final String tips;
}
