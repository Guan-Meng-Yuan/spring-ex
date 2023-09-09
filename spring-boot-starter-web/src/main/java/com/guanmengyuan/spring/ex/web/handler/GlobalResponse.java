package com.example.demo.config;

import cn.hutool.core.util.ClassUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.json.JSONUtil;
import com.guanmengyuan.spring.ex.common.model.dto.res.Res;
import lombok.NonNull;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

//@RestControllerAdvice
public class GlobalResponse implements ResponseBodyAdvice<Object> {

    @Override
    public boolean supports(@NonNull MethodParameter returnType, @NonNull Class<? extends HttpMessageConverter<?>> converterType) {
        return true;
    }

    @Override
    public Object beforeBodyWrite(Object body, MethodParameter returnType, @NonNull MediaType selectedContentType, @NonNull Class<? extends HttpMessageConverter<?>> selectedConverterType, @NonNull ServerHttpRequest request, @NonNull ServerHttpResponse response) {
        if (ClassUtil.equals(returnType.getParameterType(), String.class.getName(), false)) {
            response.getHeaders().setContentType(MediaType.APPLICATION_JSON);
            if (null == body) {
                return JSONUtil.toJsonStr(Res.successNoData(IdUtil.nanoId()));
            }
            return JSONUtil.toJsonStr(Res.success(body, IdUtil.nanoId()));
        }
        if (null == body){
            return Res.successNoData(IdUtil.nanoId());
        }
        if (body instanceof Res<?>){
            return body;
        }
        return Res.success(body, IdUtil.nanoId());
    }

}
