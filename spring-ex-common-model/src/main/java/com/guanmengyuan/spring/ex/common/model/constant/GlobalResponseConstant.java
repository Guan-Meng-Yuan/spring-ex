package com.guanmengyuan.spring.ex.common.model.constant;

import org.dromara.hutool.core.collection.set.SetUtil;

import java.util.Set;

/**
 * 全局白名单路径常量
 */
public interface GlobalResponseConstant {
    /**
     * 默认白名单路径
     */
    Set<String> DEFAULT_PATH = SetUtil.of("/v3/api-docs/**", "/webjars/**", "/doc.html",
            "/actuator/**",
            "/swagger-ui.html", "/favicon.ico");
}
