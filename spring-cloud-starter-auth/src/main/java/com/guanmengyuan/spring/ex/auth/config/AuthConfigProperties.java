package com.guanmengyuan.spring.ex.auth.config;

import lombok.Data;
import org.dromara.hutool.core.collection.set.SetUtil;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.Set;

@Data
@ConfigurationProperties(prefix = "sa-token")
public class AuthConfigProperties {
    /**
     * 白名单
     */
    private Set<String> whitelist= SetUtil.of();
}
