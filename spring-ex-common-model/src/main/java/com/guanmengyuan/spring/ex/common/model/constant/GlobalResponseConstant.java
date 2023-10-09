package com.guanmengyuan.spring.ex.common.model.constant;

import cn.hutool.core.collection.CollUtil;

import java.util.Set;

/**
 * 全局白名单路径常量
 */
public interface GlobalResponseConstant {
    /**
     * 默认白名单路径
     */
    Set<String> DEFAULT_PATH = CollUtil.newHashSet("/v3/api-docs/**", "/webjars/**", "/doc.html",
            "/actuator/**",
            "/swagger-ui.html", "/favicon.ico");
}
