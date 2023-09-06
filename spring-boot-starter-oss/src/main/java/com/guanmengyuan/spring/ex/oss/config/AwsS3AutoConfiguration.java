package com.guanmengyuan.spring.ex.oss.config;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3AsyncClient;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;

import java.net.URI;

@EnableConfigurationProperties(AwsS3Properties.class)
@RequiredArgsConstructor
public class AwsS3AutoConfiguration {
    private final AwsS3Properties awsS3Properties;
    private S3Client s3Client;

    @ConditionalOnMissingBean(S3AsyncClient.class)
    @Bean
    public S3Client s3Client() {
        if (null == s3Client) {
            s3Client = S3Client.builder().region(getRegion()).endpointOverride(URI.create(getDomain())).credentialsProvider(StaticCredentialsProvider.create(AwsBasicCredentials.create(awsS3Properties.getAccessKey(), awsS3Properties.getAccessKeySecret()))).build();
        }
        return s3Client;
    }

    private Region getRegion() {
        return Region.of(awsS3Properties.getRegine());
    }

    private String getDomain() {
        return awsS3Properties.getEnableHttps() ? "https://" + awsS3Properties.getEndpoint() : "http://" + awsS3Properties.getEndpoint();
    }

    @ConditionalOnMissingBean(S3Presigner.class)
    @Bean
    public S3Presigner s3Presigner() {
        return S3Presigner.builder().region(getRegion()).endpointOverride(URI.create(getDomain())).credentialsProvider(StaticCredentialsProvider.create(AwsBasicCredentials.create(awsS3Properties.getAccessKey(), awsS3Properties.getAccessKeySecret()))).build();
    }
}
