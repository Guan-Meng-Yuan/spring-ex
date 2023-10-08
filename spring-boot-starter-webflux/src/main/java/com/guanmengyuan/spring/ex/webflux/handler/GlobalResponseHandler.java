package com.guanmengyuan.spring.ex.webflux.handler;

import cn.hutool.core.collection.CollUtil;
import com.guanmengyuan.spring.ex.common.model.dto.res.Res;
import com.guanmengyuan.spring.ex.webflux.config.WebFluxProperties;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.BeanInitializationException;
import org.springframework.core.MethodParameter;
import org.springframework.core.ReactiveAdapterRegistry;
import org.springframework.http.codec.HttpMessageWriter;
import org.springframework.http.server.RequestPath;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.PathMatcher;
import org.springframework.web.reactive.HandlerResult;
import org.springframework.web.reactive.accept.RequestedContentTypeResolver;
import org.springframework.web.reactive.result.method.annotation.ResponseBodyResultHandler;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Set;

import static com.guanmengyuan.spring.ex.common.model.constant.GlobalResponseConstant.DEFAULT_PATH;

@Slf4j
public class GlobalResponseHandler extends ResponseBodyResultHandler {

    private static PathMatcher pathMatcher;
    private static final MethodParameter METHOD_PARAMETER;
    private final WebFluxProperties webFluxProperties;

    static {
        try {

            METHOD_PARAMETER = new MethodParameter(GlobalResponseHandler.class.getDeclaredMethod("methodForParams"),
                    -1);
        } catch (NoSuchMethodException | SecurityException e) {
            log.error("methodForParams method not found , {}", e.getMessage(), e);
            throw new BeanInitializationException(e.getMessage());
        }
    }

    public GlobalResponseHandler(List<HttpMessageWriter<?>> writers, RequestedContentTypeResolver resolver,
            ReactiveAdapterRegistry registry, WebFluxProperties webFluxProperties) {
        super(writers, resolver, registry);
        this.webFluxProperties = webFluxProperties;
        setOrder(-1);
    }

    @SuppressWarnings({ "unused", "SameReturnValue" })
    private static Res<?> methodForParams() {
        return null;
    }

    private static Object wrapCommonResult(Object body, String traceId) {
        if (body instanceof Res<?> res) {
            res.setTraceId(traceId);
            return res;
        }
        return Res.success(body, traceId);
    }

    private static PathMatcher getPathMatcher() {
        if (null == pathMatcher) {
            pathMatcher = new AntPathMatcher();
        }
        return pathMatcher;
    }

    private static Set<String> getPattern(Set<String> ignores) {
        if (CollUtil.isEmpty(ignores)) {
            return DEFAULT_PATH;
        }
        if (!CollUtil.containsAll(ignores, DEFAULT_PATH)) {
            ignores.addAll(DEFAULT_PATH);
        }
        return ignores;
    }

    @Override
    @NonNull
    public Mono<Void> handleResult(ServerWebExchange exchange, HandlerResult result) {
        Object returnValue = result.getReturnValue();
        String traceId = exchange.getRequest().getId();
        RequestPath path = exchange.getRequest().getPath();
        Set<String> ignores = getPattern(webFluxProperties.getIgnores());
        if (CollUtil.isNotEmpty(ignores)
                && ignores.stream().anyMatch(pattern -> getPathMatcher().match(pattern, path.value()))) {
            return super.handleResult(exchange, result);
        }
        if (returnValue instanceof Mono<?> mono) {
            return writeBody(mono.map(body -> GlobalResponseHandler.wrapCommonResult(body, traceId))
                    .defaultIfEmpty(Res.successNoData(traceId)), METHOD_PARAMETER, exchange);
        }
        if (returnValue instanceof Flux<?> flux) {
            return writeBody(flux.collectList().map(body -> GlobalResponseHandler.wrapCommonResult(body, traceId))
                    .defaultIfEmpty(Res.successNoData(traceId)), METHOD_PARAMETER, exchange);
        }
        return writeBody(wrapCommonResult(returnValue, traceId), METHOD_PARAMETER, exchange);
    }

}
