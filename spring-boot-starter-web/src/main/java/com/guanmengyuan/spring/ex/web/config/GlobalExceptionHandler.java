package com.guanmengyuan.spring.ex.web.config;

import com.guanmengyuan.spring.ex.common.model.dto.res.Res;
import com.guanmengyuan.spring.ex.common.model.exception.ServiceException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(Throwable.class)
    public Res<?> handlerThrowable(Throwable throwable, HttpServletRequest servletRequest, HttpServletResponse response) {
        String requestURI = servletRequest.getRequestURI();
        log.error("request error:{}", requestURI, throwable);
        if (throwable instanceof ServiceException serviceException) {
            response.setStatus(serviceException.getStatusCode().value());
        }
        return Res.error(throwable);
    }
}
