package com.guanmengyuan.spring.ex.web.config;

import static com.guanmengyuan.spring.ex.common.model.constant.GlobalResponseConstant.DEFAULT_PATH;

import java.util.Set;

import org.dromara.hutool.core.collection.CollUtil;
import org.dromara.hutool.core.collection.set.SetUtil;
import org.dromara.hutool.core.text.StrUtil;
import org.dromara.hutool.json.JSONUtil;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.lang.Nullable;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import com.guanmengyuan.spring.ex.common.model.dto.res.Res;

import lombok.NonNull;

@RestControllerAdvice
public class GlobalResponseAdvice implements ResponseBodyAdvice<Object> {

    private final Set<String> ignores;
    private final SpringWebProperties springWebProperties;
    private final AntPathMatcher antPathMatcher;

    public GlobalResponseAdvice(SpringWebProperties springWebProperties) {
        this.springWebProperties = springWebProperties;

        ignores = SetUtil.of();
        if (CollUtil.isNotEmpty(springWebProperties.getIgnores())) {
            ignores.addAll(springWebProperties.getIgnores());
        }
        ignores.addAll(DEFAULT_PATH);
        this.antPathMatcher = new AntPathMatcher();
    }

    @Override
    public boolean supports(@NonNull MethodParameter returnType,
            @NonNull Class<? extends HttpMessageConverter<?>> converterType) {
        return true;
    }

    @Override
    public Object beforeBodyWrite(@Nullable Object body, @NonNull MethodParameter returnType,
            @NonNull MediaType selectedContentType,
            @NonNull Class<? extends HttpMessageConverter<?>> selectedConverterType, @NonNull ServerHttpRequest request,
            @NonNull ServerHttpResponse response) {
        String path = request.getURI().getPath();
        if (!springWebProperties.getEnableGlobalRes() || StrUtil.equals(path, "/error") || body instanceof Res<?>
                || ignores.stream().anyMatch(ignore -> antPathMatcher.match(ignore, path))) {
            return body;
        }
        return wrapperBody(body, returnType, response);
    }

    private Object wrapperBody(Object body, MethodParameter returnType, @NonNull ServerHttpResponse response) {
        if (returnType.getParameterType().isNestmateOf(String.class)) {
            response.getHeaders().setContentType(MediaType.APPLICATION_JSON);
            return JSONUtil.toJsonStr(Res.success(body));
        }
        return Res.success(body);
    }
}
