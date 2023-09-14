package com.guanmengyuan.spring.ex.oss.config;

import cn.hutool.core.util.StrUtil;
import cn.hutool.cron.CronUtil;
import cn.hutool.cron.task.Task;
import com.dtflys.forest.Forest;
import com.guanmengyuan.spring.ex.oss.model.OssTemporaryCredentials;
import jakarta.annotation.PostConstruct;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
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
@Slf4j
public class AwsS3AutoConfiguration {
    private final AwsS3Properties awsS3Properties;
    private OssTemporaryCredentials ossTemporaryCredentials;

    private S3ClientBuilder s3ClientBuilder;

    private S3Presigner.Builder s3PresignerBuilder;

    @PostConstruct
    public void init() {
        if (awsS3Properties.getEnableTemporaryCredentialsModel()) {
            ossTemporaryCredentials();
            CronUtil.schedule(awsS3Properties.getCron(), (Task) this::ossTemporaryCredentials);
            CronUtil.setMatchSecond(true);
            CronUtil.start();
        }

    }

    @SneakyThrows
    public void ossTemporaryCredentials() {
        if (awsS3Properties.getEnableTemporaryCredentialsModel()) {
            ossTemporaryCredentials = Forest.get(awsS3Properties.getTemporaryCredentialsUrl()).execute(awsS3Properties.getTemporaryCredentialsResponse());
            Forest.get(awsS3Properties.getTemporaryCredentialsUrl()).execute(awsS3Properties.getTemporaryCredentialsResponse());
            log.info("response,{}", ossTemporaryCredentials);
            s3ClientBuilder.credentialsProvider(StaticCredentialsProvider.create(AwsSessionCredentials.create(ossTemporaryCredentials.temSecretId(), ossTemporaryCredentials.tmpSecretKey(), ossTemporaryCredentials.token())));
            s3PresignerBuilder.credentialsProvider(StaticCredentialsProvider.create(AwsSessionCredentials.create(ossTemporaryCredentials.temSecretId(), ossTemporaryCredentials.tmpSecretKey(), ossTemporaryCredentials.token())));
        }
    }


    public AwsS3AutoConfiguration(AwsS3Properties awsS3Properties) {
        if (awsS3Properties.getEnableTemporaryCredentialsModel()) {
            if (StrUtil.isEmpty(awsS3Properties.getTemporaryCredentialsUrl()) || StrUtil.isEmpty(awsS3Properties.getCron()) || null == awsS3Properties.getTemporaryCredentialsResponse()) {
                throw new RuntimeException("temporary credentials model is enabled,please set the temporaryCredentialsUrl or temporaryCredentialsResponse or cron");
            }
        }
        this.awsS3Properties = awsS3Properties;
    }

    @Bean
    public S3ClientBuilder s3ClientBuilder() {
        s3ClientBuilder = S3Client.builder().region(getRegion()).endpointOverride(URI.create(getDomain()));
        if (awsS3Properties.getEnableTemporaryCredentialsModel()) {
            return s3ClientBuilder.credentialsProvider(StaticCredentialsProvider.create(AwsSessionCredentials.create(ossTemporaryCredentials.temSecretId(), ossTemporaryCredentials.tmpSecretKey(), ossTemporaryCredentials.token())));
        }
        return s3ClientBuilder.credentialsProvider(StaticCredentialsProvider.create(AwsBasicCredentials.create(awsS3Properties.getAccessKey(), awsS3Properties.getAccessKeySecret())));
    }

    private Region getRegion() {
        return Region.of(awsS3Properties.getRegion());
    }

    private String getDomain() {
        return awsS3Properties.getEnableHttps() ? "https://" + awsS3Properties.getEndpoint() : "http://" + awsS3Properties.getEndpoint();
    }

    @Bean
    public S3Presigner.Builder s3PresignerBuilder() {
        s3PresignerBuilder = S3Presigner.builder().region(getRegion()).endpointOverride(URI.create(getDomain()));
        if (awsS3Properties.getEnableTemporaryCredentialsModel()) {
            return s3PresignerBuilder.credentialsProvider(StaticCredentialsProvider.create(AwsSessionCredentials.create(ossTemporaryCredentials.temSecretId(), ossTemporaryCredentials.tmpSecretKey(), ossTemporaryCredentials.token())));
        }
        return s3PresignerBuilder.credentialsProvider(StaticCredentialsProvider.create(AwsBasicCredentials.create(awsS3Properties.getAccessKey(), awsS3Properties.getAccessKeySecret())));
    }


}
