package com.guanmengyuan.spring.ex.web.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.Set;

@Data
@ConfigurationProperties(prefix = "spring.mvc")
public class SpringWebProperties {
    /**
     * 忽略全局响应路径
     */
    private Set<String> ignores;
    /**
     * 是否开启全局响应
     */
    private Boolean enableGlobalRes = false;
}
