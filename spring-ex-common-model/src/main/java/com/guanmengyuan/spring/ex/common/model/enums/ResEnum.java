package com.guanmengyuan.spring.ex.common.model.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

@AllArgsConstructor
@Getter
public enum ResEnum implements BaseResEnum {
    SUCCESS(HttpStatus.OK, "ok", "操作成功"),
    UNAUTHORIZED(HttpStatus.UNAUTHORIZED, "unauthorized", "未授权"),
    NOT_AN_ENUMERATION(HttpStatus.NOT_ACCEPTABLE, "header enum not support", "网络异常"),
    ID_CAN_NOT_BE_EMPTY(HttpStatus.BAD_REQUEST, "ID不能为空", "参数错误"),
    BODY_CAN_NOT_BE_EMPTY(HttpStatus.BAD_REQUEST, "参数不能为空", "参数错误"),
    ;
    private final HttpStatusCode httpStatusCode;
    private final String message;
    private final String tips;
}
