package com.guanmengyuan.spring.ex.web.config;

import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.autoconfigure.web.ServerProperties;
import org.springframework.boot.autoconfigure.web.servlet.error.BasicErrorController;
import org.springframework.boot.autoconfigure.web.servlet.error.ErrorViewResolver;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.servlet.error.ErrorAttributes;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.annotation.Order;
import org.springframework.format.FormatterRegistry;
import org.springframework.lang.NonNull;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.guanmengyuan.spring.ex.common.model.converter.AnyToEnumConverterFactory;

@Configuration
@EnableConfigurationProperties(SpringWebProperties.class)
@ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.SERVLET)
@Order(-100)
@Primary
public class SpringWebAutoConfiguration implements WebMvcConfigurer {
    private final ServerProperties serverProperties;

    public SpringWebAutoConfiguration(ServerProperties serverProperties) {
        this.serverProperties = serverProperties;
    }

    @Override
    public void addFormatters(@NonNull FormatterRegistry registry) {
        registry.addConverterFactory(new AnyToEnumConverterFactory());
    }

    @Primary
    @Bean
    public BasicErrorController basicErrorController(ErrorAttributes errorAttributes,
            ObjectProvider<ErrorViewResolver> errorViewResolvers) {
        return new GlobalErrorController(serverProperties);
    }

}
