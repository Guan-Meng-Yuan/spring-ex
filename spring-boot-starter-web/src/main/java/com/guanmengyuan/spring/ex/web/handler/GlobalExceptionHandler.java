package com.guanmengyuan.spring.ex.web.handler;

import cn.hutool.core.io.IoUtil;
import cn.hutool.core.util.ArrayUtil;
import com.guanmengyuan.spring.ex.common.model.dto.res.Res;
import jakarta.servlet.http.HttpServletRequest;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.io.BufferedReader;
import java.util.Enumeration;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {
    @SneakyThrows
    @ExceptionHandler(Throwable.class)
    public ResponseEntity<Res<?>> res(Throwable throwable, HttpServletRequest request) {
        String requestId = request.getRequestId();
        String requestURI = request.getRequestURI();
        Enumeration<String> headerNames = request.getHeaderNames();
        StringBuffer headerContent = new StringBuffer("\n");
        while (headerNames.hasMoreElements()) {
            String headerName = headerNames.nextElement();
            // 使用头名称获取头值
            String headerValue = request.getHeader(headerName);
            headerContent.append(headerName).append(" : ").append(headerValue).append("\n");
        }
        String method = request.getMethod();
        BufferedReader reader = request.getReader();
        Enumeration<String> parameterNames = request.getParameterNames();
        StringBuffer paramContent = new StringBuffer("\n");
        while (parameterNames.hasMoreElements()) {
            String paramKey = parameterNames.nextElement();
            String[] parameterValues = request.getParameterValues(paramKey);
            paramContent.append(paramKey).append(" : ").append(ArrayUtil.toString(parameterValues)).append("\n");
        }
        String body = IoUtil.read(reader);
        log.error("\nrequestPath:{}\nrequestId:{}\nrequestHeader:{}\nrequestMethod:{}\nrequestBody:{}\nrequestParam:{}", requestURI, requestId, headerContent, method, body, paramContent, throwable);
        return ResponseEntity.internalServerError().body(Res.error(throwable, requestId));
    }
}
