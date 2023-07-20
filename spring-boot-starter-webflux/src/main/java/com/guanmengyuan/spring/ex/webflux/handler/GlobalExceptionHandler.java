package com.guanmengyuan.spring.ex.webflux.handler;

import com.guanmengyuan.spring.ex.common.model.dto.res.Res;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.bind.support.WebExchangeBindException;
import org.springframework.web.server.ServerWebExchange;

/**
 * 异常处理器
 *
 * @author guanmengyuan
 */
@RestControllerAdvice
@ResponseBody
public class GlobalExceptionHandler {
    @ExceptionHandler(WebExchangeBindException.class)
    public Res<?> exceptionHandler(ServerWebExchange exchange, WebExchangeBindException webExchangeBindException) {
        exchange.getResponse().setStatusCode(webExchangeBindException.getStatusCode());
        return Res.error(webExchangeBindException, exchange);
    }
}
