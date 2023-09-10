package com.guanmengyuan.spring.ex.web.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.Set;

@Data
@ConfigurationProperties(prefix = "spring.mvc")
public class SpringWebProperties {
    private Set<String> ignores;

    private Boolean enableGlobalRes = false;
}
