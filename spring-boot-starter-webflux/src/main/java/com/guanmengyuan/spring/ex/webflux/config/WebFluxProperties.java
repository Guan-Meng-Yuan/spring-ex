package com.guanmengyuan.spring.ex.webflux.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.Set;

@ConfigurationProperties(prefix = "spring.webflux")
@Data
public class WebFluxProperties {


    /**
     * 忽略全局响应路径
     */
    private Set<String> ignores;

}
