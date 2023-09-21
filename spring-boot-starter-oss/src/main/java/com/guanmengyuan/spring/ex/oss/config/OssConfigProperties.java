package com.guanmengyuan.spring.ex.oss.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

import com.guanmengyuan.spring.ex.oss.enums.SecretMode;

import lombok.Data;

/**
 * @author guanmengyuan
 */
@Data
@ConfigurationProperties(prefix = "oss")
public class OssConfigProperties {
    /**
     * 访问endpoint
     */
    private String endpoint;
    /**
     * 默认存储桶名称
     */
    private String defaultBucket;
    /**
     * 是否开启https
     */
    private Boolean enableHttps = false;
    /**
     * 存储key
     */
    private String accessKey;
    /**
     * 存储密钥
     */
    private String accessSecret;
    /**
     * 密钥模式
     */
    private SecretMode secretMode = SecretMode.USUALLY;
    /**
     * 区域
     */
    private String region;

}
