package com.guanmengyuan.spring.ex.oss.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.Data;

@ConfigurationProperties(prefix = "oss")
@Data
public class AwsS3Properties {
    /**
     * 存储域名
     */
    private String endpoint;

    /**
     * 区域
     */
    private String region;
    /**
     * 密钥keyId
     */
    private String accessKey;
    /**
     * 密钥KeySecret
     */
    private String accessKeySecret;
    /**
     * 是否开启https
     */
    private Boolean enableHttps = false;

    private Boolean enableTemModel = false;

}
