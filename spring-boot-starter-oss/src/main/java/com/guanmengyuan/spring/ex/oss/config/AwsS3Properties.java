package com.guanmengyuan.spring.ex.oss.config;

import com.guanmengyuan.spring.ex.oss.model.OssTemporaryCredentials;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

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
    private boolean enableHttps = false;
    /**
     * 是否使用临时凭证
     */
    private Boolean enableTemporaryCredentialsModel = false;
    /**
     * 获取临时凭证的api地址
     */
    private String temporaryCredentialsUrl;

    /**
     * 临时凭证响应类
     */
    private Class<? extends OssTemporaryCredentials> temporaryCredentialsResponse;
    /**
     * 重新请求凭证的cron表达式
     */
    private String cron;
}
