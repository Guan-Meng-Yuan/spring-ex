package com.guanmengyuan.spring.ex.webflux.handler;

import com.guanmengyuan.spring.ex.webflux.attribute.GlobalErrorAttributes;
import org.springframework.boot.autoconfigure.web.WebProperties;
import org.springframework.boot.autoconfigure.web.reactive.error.AbstractErrorWebExceptionHandler;
import org.springframework.boot.web.error.ErrorAttributeOptions;
import org.springframework.boot.web.reactive.error.ErrorAttributes;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.http.codec.ServerCodecConfigurer;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.*;
import reactor.core.publisher.Mono;

import java.util.Map;

/**
 * 全局异常处理器
 */
@Configuration
@Order(-2)
public class GlobalWebErrorHandler extends AbstractErrorWebExceptionHandler {
    private static final String HTTP_STATUS_FIELD = "httpStatusCode";
    private static final String MESSAGE_FIELD = "message";

    public GlobalWebErrorHandler(GlobalErrorAttributes globalErrorAttributes,
                                 ApplicationContext applicationContext, ServerCodecConfigurer serverCodecConfigurer) {
        super(globalErrorAttributes, new WebProperties().getResources(), applicationContext);
        super.setMessageReaders(serverCodecConfigurer.getReaders());
        super.setMessageWriters(serverCodecConfigurer.getWriters());
    }

    @Override
    protected RouterFunction<ServerResponse> getRoutingFunction(ErrorAttributes errorAttributes) {
        return RouterFunctions.route(RequestPredicates.all(), this::renderErrorResponse);
    }

    private Mono<ServerResponse> renderErrorResponse(ServerRequest request) {

        final Map<String, Object> errorPropertiesMap = getErrorAttributes(request, ErrorAttributeOptions.defaults());
        Object statusCode = errorPropertiesMap.get(HTTP_STATUS_FIELD);
        errorPropertiesMap.remove(HTTP_STATUS_FIELD);
        errorPropertiesMap.remove(MESSAGE_FIELD);
        return ServerResponse.status((HttpStatusCode) statusCode)
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(errorPropertiesMap));
    }
}
