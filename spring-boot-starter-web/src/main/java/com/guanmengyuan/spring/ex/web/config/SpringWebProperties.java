package com.guanmengyuan.spring.ex.web.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.Set;

@ConfigurationProperties(prefix = "spring.mvc")
@Data
public class SpringWebProperties {
    /**
     * 忽略全局响应
     */
    private Set<String> ignores;
}
