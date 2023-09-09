package com.guanmengyuan.spring.ex.web.handler;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ClassUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.json.JSONUtil;
import com.guanmengyuan.spring.ex.common.model.dto.res.Res;
import com.guanmengyuan.spring.ex.web.config.SpringWebProperties;
import jakarta.annotation.Resource;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.PathMatcher;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import java.util.Set;

@RestControllerAdvice
@RequiredArgsConstructor
public class GlobalResponse implements ResponseBodyAdvice<Object> {
    @Resource
    private SpringWebProperties springWebProperties;
    public static final Set<String> DEFAULT_PATH = CollUtil.newHashSet("/v3/api-docs/**", "/webjars/**", "/doc.html",
            "/actuator/**",
            "/swagger-ui.html", "/favicon.ico");

    private static Set<String> getPattern(Set<String> ignores) {
        if (CollUtil.isEmpty(ignores)) {
            return DEFAULT_PATH;
        }
        if (!CollUtil.containsAll(ignores, DEFAULT_PATH)) {
            ignores.addAll(DEFAULT_PATH);
        }
        return ignores;
    }

    private static PathMatcher pathMatcher;

    private static PathMatcher getPathMatcher() {
        if (null == pathMatcher) {
            pathMatcher = new AntPathMatcher();
        }
        return pathMatcher;
    }

    @Override
    public boolean supports(@NonNull MethodParameter returnType, @NonNull Class<? extends HttpMessageConverter<?>> converterType) {
        return true;
    }

    @Override
    public Object beforeBodyWrite(Object body, @NonNull MethodParameter returnType, @NonNull MediaType selectedContentType, @NonNull Class<? extends HttpMessageConverter<?>> selectedConverterType, @NonNull ServerHttpRequest request, @NonNull ServerHttpResponse response) {
        String path = request.getURI().getPath();
        Set<String> ignores = getPattern(springWebProperties.getIgnores());
        if (CollUtil.isNotEmpty(ignores)
                && ignores.stream().anyMatch(pattern -> getPathMatcher().match(pattern, path))) {
            return body;
        }
        if (ClassUtil.equals(returnType.getParameterType(), String.class.getName(), false)) {
            response.getHeaders().setContentType(MediaType.APPLICATION_JSON);
            if (null == body) {
                return JSONUtil.toJsonStr(Res.successNoData(IdUtil.nanoId()));
            }
            return JSONUtil.toJsonStr(Res.success(body, IdUtil.nanoId()));
        }
        if (null == body) {
            return Res.successNoData(IdUtil.nanoId());
        }
        if (body instanceof Res<?>) {
            return body;
        }
        return Res.success(body, IdUtil.nanoId());
    }

}
