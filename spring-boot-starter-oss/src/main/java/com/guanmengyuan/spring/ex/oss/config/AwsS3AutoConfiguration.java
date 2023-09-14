package com.guanmengyuan.spring.ex.oss.config;

import java.net.URI;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import cn.hutool.core.util.StrUtil;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.S3ClientBuilder;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;

@Configuration
@EnableConfigurationProperties(AwsS3Properties.class)
public class AwsS3AutoConfiguration {
    private final AwsS3Properties awsS3Properties;

    public AwsS3AutoConfiguration(AwsS3Properties awsS3Properties) {
        if (awsS3Properties.getEnableTemporaryCredentialsModel()) {
            if (StrUtil.isEmpty(awsS3Properties.getTemporaryCredentialsUrl())
                    || StrUtil.isEmpty(awsS3Properties.getCron())
                    || null == awsS3Properties.getTemporaryCredentialsResponse()) {
                throw new RuntimeException(
                        "temporary credentials model is enabled,please set the temporaryCredentialsUrl or temporaryCredentialsResponse or cron");
            }
        }
        this.awsS3Properties = awsS3Properties;
    }

    @Bean
    public S3ClientBuilder s3ClientBuilder() {
        return S3Client.builder().region(getRegion()).endpointOverride(URI.create(getDomain()));

    }

    private Region getRegion() {
        return Region.of(awsS3Properties.getRegion());
    }

    private String getDomain() {
        return awsS3Properties.isEnableHttps() ? "https://" + awsS3Properties.getEndpoint()
                : "http://" + awsS3Properties.getEndpoint();
    }

    @Bean
    public S3Presigner.Builder s3PresignerBuilder() {
        return S3Presigner.builder().region(getRegion())
                .endpointOverride(URI.create(getDomain()));
    }

}
