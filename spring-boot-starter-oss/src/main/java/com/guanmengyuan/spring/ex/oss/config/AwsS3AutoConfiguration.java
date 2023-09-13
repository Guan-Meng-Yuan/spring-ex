package com.guanmengyuan.spring.ex.oss.config;

import com.guanmengyuan.spring.ex.oss.client.WxCloudCosClient;
import com.guanmengyuan.spring.ex.oss.dto.CosSecretInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.AwsSessionCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.S3ClientBuilder;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;

import java.net.URI;

@Configuration
@EnableConfigurationProperties(AwsS3Properties.class)
@RequiredArgsConstructor
public class AwsS3AutoConfiguration {
    private final AwsS3Properties awsS3Properties;
    private final WxCloudCosClient wxCloudCosClient;

    @Bean
    public S3Client s3Client() {
        S3ClientBuilder s3ClientBuilder = S3Client.builder().region(getRegion()).endpointOverride(URI.create(getDomain()));
        if (awsS3Properties.getEnableTemModel()) {
            CosSecretInfo cosSecretInfo = wxCloudCosClient.getCosSecretInfo();
            return s3ClientBuilder.credentialsProvider(StaticCredentialsProvider.create(AwsSessionCredentials.create(cosSecretInfo.getTmpSecretId(), cosSecretInfo.getTmpSecretKey(), cosSecretInfo.getToken()))).build();
        }

        return s3ClientBuilder.credentialsProvider(StaticCredentialsProvider.create(AwsBasicCredentials.create(awsS3Properties.getAccessKey(), awsS3Properties.getAccessKeySecret()))).build();
    }

    private Region getRegion() {
        return Region.of(awsS3Properties.getRegion());
    }

    private String getDomain() {
        return awsS3Properties.getEnableHttps() ? "https://" + awsS3Properties.getEndpoint() : "http://" + awsS3Properties.getEndpoint();
    }

    @Bean
    public S3Presigner s3Presigner() {
        S3Presigner.Builder builder = S3Presigner.builder().region(getRegion()).endpointOverride(URI.create(getDomain()));
        if (awsS3Properties.getEnableTemModel()) {
            CosSecretInfo cosSecretInfo = wxCloudCosClient.getCosSecretInfo();
           return builder.credentialsProvider(StaticCredentialsProvider.create(AwsSessionCredentials.create(cosSecretInfo.getTmpSecretId(), cosSecretInfo.getTmpSecretKey(), cosSecretInfo.getToken()))).build();
        }
        return builder.credentialsProvider(StaticCredentialsProvider.create(AwsBasicCredentials.create(awsS3Properties.getAccessKey(), awsS3Properties.getAccessKeySecret()))).build();
    }
}
