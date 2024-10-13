package com.guanmengyuan.spring.ex.common.model.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.web.server.ResponseStatusException;

import com.guanmengyuan.spring.ex.common.model.enums.BaseResEnum;

import lombok.EqualsAndHashCode;
import lombok.Getter;

/**
 * 业务异常
 */
@EqualsAndHashCode(callSuper = true)
public class ServiceException extends ResponseStatusException {
    /**
     * 用户提示
     */
    @Getter
    private final String tips;
    /**
     * 响应枚举
     */
    private BaseResEnum resEnum;

    /**
     * 业务异常
     *
     * @param resEnum 响应枚举
     */
    public ServiceException(BaseResEnum resEnum) {
        this(resEnum.getHttpStatusCode(), resEnum.getMessage(), resEnum.getTips());
        this.resEnum = resEnum;
    }

    /**
     * 业务异常
     *
     * @param status http状态
     * @param reason 错误信息
     * @param tips   用户提示
     */
    public ServiceException(HttpStatusCode status, String reason, String tips) {
        super(status, reason);
        this.tips = tips;
    }

    /**
     * 业务异常
     *
     * @param status http状态
     * @param tips   用户提示
     */
    public ServiceException(HttpStatusCode status, String tips) {
        this(status, tips, tips);
    }

    /**
     * 业务异常
     *
     * @param tips 用户提示
     */
    public ServiceException(String tips) {
        this(HttpStatus.INTERNAL_SERVER_ERROR, tips);
    }

    /**
     * 业务异常
     *
     * @param tips   用户提示
     * @param reason 错误信息
     */
    public ServiceException(String tips, String reason) {
        this(HttpStatus.INTERNAL_SERVER_ERROR, reason, tips);
    }

}
