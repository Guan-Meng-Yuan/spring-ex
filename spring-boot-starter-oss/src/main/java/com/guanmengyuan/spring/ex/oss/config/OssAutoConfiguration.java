package com.guanmengyuan.spring.ex.oss.config;

import cn.hutool.core.util.StrUtil;
import com.guanmengyuan.spring.ex.oss.enums.SecretMode;
import com.guanmengyuan.spring.ex.oss.provider.DynamicCredentialsProvider;
import com.guanmengyuan.spring.ex.oss.service.OssSecretRequestService;
import org.springframework.beans.factory.BeanInitializationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;

import java.net.URI;
import java.util.Objects;

/**
 * @author guanmengyuan
 */
@Configuration
@EnableConfigurationProperties(OssConfigProperties.class)
public class OssAutoConfiguration {
    private final OssConfigProperties ossConfigProperties;

    @Autowired(required = false)
    private OssSecretRequestService ossSecretRequestService;

    public OssAutoConfiguration(OssConfigProperties ossConfigProperties) {
        if (Objects.requireNonNull(ossConfigProperties.getSecretMode()) == SecretMode.USUALLY) {
            if (!StrUtil.isAllNotEmpty(ossConfigProperties.getAccessKey(), ossConfigProperties.getAccessSecret(),
                    ossConfigProperties.getRegion(), ossConfigProperties.getEndpoint(),
                    ossConfigProperties.getDefaultBucket())) {
                throw new BeanInitializationException(
                        "oss config properties bind error,please check the oss properties");
            }
        }
        this.ossConfigProperties = ossConfigProperties;
    }

    @Bean
    @ConditionalOnProperty(prefix = "oss", name = "secret-mode", havingValue = "usually", matchIfMissing = true)
    public S3Client s3Client() {
        return S3Client.builder()
                .region(Region.of(ossConfigProperties.getRegion()))
                .endpointOverride(
                        URI.create(ossConfigProperties.getEnableHttps() ? "https://" + ossConfigProperties.getEndpoint()
                                : "http://" + ossConfigProperties.getEndpoint()))
                .credentialsProvider(StaticCredentialsProvider.create(AwsBasicCredentials
                        .create(ossConfigProperties.getAccessKey(), ossConfigProperties.getAccessSecret())))
                .build();
    }

    @Bean
    @ConditionalOnProperty(prefix = "oss", name = "secret-mode", havingValue = "temporary")
    public S3Client tempModeS3Client() {
        if (null == ossSecretRequestService) {
            throw new BeanInitializationException("oss secret request service must not be null,please implements");
        }
        return S3Client.builder()
                .region(Region.of(ossConfigProperties.getRegion()))
                .endpointOverride(
                        URI.create(ossConfigProperties.getEnableHttps() ? "https://" + ossConfigProperties.getEndpoint()
                                : "http://" + ossConfigProperties.getEndpoint()))
                .credentialsProvider(DynamicCredentialsProvider.create(ossSecretRequestService)).build();

    }

}
