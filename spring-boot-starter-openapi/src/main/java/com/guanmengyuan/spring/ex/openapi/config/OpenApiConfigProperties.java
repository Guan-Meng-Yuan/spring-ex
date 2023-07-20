package com.guanmengyuan.spring.ex.openapi.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = "springdoc")
public class OpenApiConfigProperties {
    /**
     * openapi标题
     */
    private String title;
    /**
     * 联系人email
     */
    private String email;
    /**
     * 文档描述
     */
    private String description;

    /**
     * 文档版本
     */
    private String version;
    /**
     * 联系人名称
     */
    private String name;
}
