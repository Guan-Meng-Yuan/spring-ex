package com.guanmengyuan.spring.ex.mybatis.flex.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.Data;

/**
 * mybatis flex 配置项
 */
@Data
@ConfigurationProperties(prefix = "mybatis-flex")
public class MyBatisFlexProperties {
    /**
     * 是否开启sql审计
     */
    private Boolean auditEnable = false;
}
