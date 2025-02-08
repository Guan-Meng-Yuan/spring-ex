package com.guanmengyuan.spring.ex.web.config;

import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import com.guanmengyuan.spring.ex.common.model.dto.res.R;
import com.guanmengyuan.spring.ex.common.model.exception.ServiceException;

import cn.dev33.satoken.exception.NotLoginException;
import cn.dev33.satoken.exception.SaTokenException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

@RestControllerAdvice
@Slf4j
@Order(-1)
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public R<?> handleMethodArgumentNotValidException(MethodArgumentNotValidException e,
                                                      HttpServletRequest servletRequest, HttpServletResponse response) {
        String requestURI = servletRequest.getRequestURI();
        log.error("request error:{}", requestURI, e);
        response.setStatus(HttpStatus.BAD_REQUEST.value());
        return R.error(e);
    }

    @ExceptionHandler(Throwable.class)
    public R<?> handlerThrowable(Throwable throwable, HttpServletRequest servletRequest,
                                 HttpServletResponse response) {
        String requestURI = servletRequest.getRequestURI();
        log.error("request error:{}", requestURI, throwable);
        response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
        if (throwable instanceof ServiceException serviceException) {
            response.setStatus(serviceException.getStatusCode().value());
        }
        return R.error(throwable);
    }

    @ExceptionHandler(NoResourceFoundException.class)
    public R<?> handlerNoResourceFoundException(NoResourceFoundException throwable,
                                                HttpServletRequest servletRequest, HttpServletResponse response) {
        String requestURI = servletRequest.getRequestURI();
        log.error("request error:{},没有资源,status:{}", requestURI, HttpStatus.NOT_FOUND.value());
        response.setStatus(HttpStatus.NOT_FOUND.value());
        return R.notFound(throwable);
    }

    @ExceptionHandler({ SaTokenException.class })
    public R<?> handlerSaToKen(SaTokenException saTokenException, HttpServletRequest servletRequest,
                               HttpServletResponse response) {
        String requestURI = servletRequest.getRequestURI();
        log.error("request error:{}", requestURI, saTokenException);
        if (saTokenException instanceof NotLoginException) {
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
        } else {
            response.setStatus(HttpStatus.NOT_ACCEPTABLE.value());
        }
        return R.error(saTokenException);
    }
}
