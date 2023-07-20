package com.guanmengyuan.spring.ex.webflux.config;

import com.guanmengyuan.spring.ex.webflux.converter.AnyToEnumConverterFactory;
import com.guanmengyuan.spring.ex.webflux.handler.GlobalResponseHandler;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.http.HttpMessageConverters;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.ReactiveAdapterRegistry;
import org.springframework.format.FormatterRegistry;
import org.springframework.http.codec.ServerCodecConfigurer;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.web.reactive.accept.RequestedContentTypeResolver;
import org.springframework.web.reactive.config.WebFluxConfigurer;

import java.util.stream.Collectors;

@Configuration
public class WebFluxConfiguration implements WebFluxConfigurer {

    @Override
    public void configureHttpMessageCodecs(ServerCodecConfigurer configurer) {
        configurer.defaultCodecs().enableLoggingRequestDetails(true);
    }

    @Override
    public void addFormatters(FormatterRegistry registry) {
        registry.addConverterFactory(new AnyToEnumConverterFactory());
    }

    @Bean
    public GlobalResponseHandler responseWrapper(ServerCodecConfigurer serverCodecConfigurer,
                                                 RequestedContentTypeResolver requestedContentTypeResolver,
                                                 ReactiveAdapterRegistry reactiveAdapterRegistry) {
        return new GlobalResponseHandler(serverCodecConfigurer.getWriters(), requestedContentTypeResolver,
                reactiveAdapterRegistry);
    }

    /**
     * 支持openFeign
     */
    @Bean
    @ConditionalOnMissingBean
    public HttpMessageConverters httpMessageConverter(ObjectProvider<HttpMessageConverter<?>> converters) {
        return new HttpMessageConverters(converters.orderedStream().collect(Collectors.toList()));
    }

}
