package com.guanmengyuan.spring.ex.common.model.constant;

import cn.hutool.core.collection.CollUtil;

import java.util.Set;

public interface GlobalResponseConstant {
    Set<String> DEFAULT_PATH = CollUtil.newHashSet("/v3/api-docs/**", "/webjars/**", "/doc.html",
            "/actuator/**",
            "/swagger-ui.html", "/favicon.ico");
}
