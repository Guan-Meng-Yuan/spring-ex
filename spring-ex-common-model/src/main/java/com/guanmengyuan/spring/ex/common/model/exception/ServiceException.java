package com.guanmengyuan.spring.ex.common.model.exception;

import com.guanmengyuan.spring.ex.common.model.enums.BaseResEnum;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.web.server.ResponseStatusException;

@EqualsAndHashCode(callSuper = true)
public class ServiceException extends ResponseStatusException {

    private BaseResEnum resEnum;
    @Getter
    private final String tips;

    public ServiceException(BaseResEnum resEnum) {
        this(resEnum.getHttpStatusCode(), resEnum.getMessage(), resEnum.getTips());
        this.resEnum = resEnum;
    }

    public ServiceException(HttpStatusCode status, String reason, String tips) {
        super(status, reason);
        this.tips = tips;
    }

    public ServiceException(HttpStatusCode status, String tips) {
        this(status, tips, tips);
    }

    public ServiceException(String tips) {
        this(HttpStatus.INTERNAL_SERVER_ERROR, tips);
    }

    public ServiceException(String tips, String reason) {
        this(HttpStatus.INTERNAL_SERVER_ERROR, tips, reason);
    }


}
