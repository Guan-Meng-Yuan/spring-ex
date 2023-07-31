package com.guanmengyuan.spring.ex.webflux.handler;

import com.guanmengyuan.spring.ex.common.model.dto.res.Res;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.dromara.hutool.core.text.StrUtil;
import org.springframework.beans.factory.BeanInitializationException;
import org.springframework.core.MethodParameter;
import org.springframework.core.ReactiveAdapterRegistry;
import org.springframework.http.codec.HttpMessageWriter;
import org.springframework.web.reactive.HandlerResult;
import org.springframework.web.reactive.accept.RequestedContentTypeResolver;
import org.springframework.web.reactive.result.method.annotation.ResponseBodyResultHandler;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

@Slf4j
public class GlobalResponseHandler extends ResponseBodyResultHandler {

    private static final MethodParameter METHOD_PARAMETER;

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
                                 ReactiveAdapterRegistry registry) {
        super(writers, resolver, registry);
        setOrder(-1);
    }

    @SuppressWarnings({"unused", "SameReturnValue"})
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

    @Override
    @NonNull
    public Mono<Void> handleResult(ServerWebExchange exchange, HandlerResult result) {
        Object returnValue = result.getReturnValue();
        String traceId = exchange.getRequest().getId();
        if (StrUtil.startWithAny(exchange.getRequest().getPath().value(), "/v3/api-docs", "/webjars", "/doc.html",
                "/actuator",
                "/swagger-ui.html")) {
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
