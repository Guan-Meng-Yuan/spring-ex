package com.guanmengyuan.spring.ex.auth.config;

import cn.hutool.core.collection.CollUtil;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.Set;

@Data
@ConfigurationProperties(prefix = "sa-token")
public class AuthConfigProperties {
    /**
     * 白名单
     */
    private Set<String> whitelist = CollUtil.newHashSet();
}
