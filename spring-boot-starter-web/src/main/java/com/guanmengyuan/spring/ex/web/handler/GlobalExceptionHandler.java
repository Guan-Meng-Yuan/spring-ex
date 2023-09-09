package com.example.demo.config;

import com.guanmengyuan.spring.ex.common.model.dto.res.Res;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {
    @ExceptionHandler(Throwable.class)
    public ResponseEntity<Res<?>> res(Throwable throwable, HttpServletRequest request, ServletResponse httpServletResponse) {
        String requestId = request.getRequestId();
        String requestURI = request.getRequestURI();
        log.error("requestPath:{},requestId:{}", requestURI, requestId, throwable);
        return ResponseEntity.internalServerError().body(Res.error(throwable,requestId));
    }
}
