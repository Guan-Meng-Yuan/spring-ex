package com.guanmengyuan.spring.ex.oss.util;

import java.io.File;
import java.time.Duration;
import java.util.Arrays;
import java.util.List;

import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import com.dtflys.forest.Forest;
import com.guanmengyuan.spring.ex.oss.config.AwsS3Properties;
import com.guanmengyuan.spring.ex.oss.model.OssTemporaryCredentials;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.cron.CronUtil;
import cn.hutool.cron.task.Task;
import cn.hutool.http.HttpUtil;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.AwsSessionCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.S3ClientBuilder;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.ObjectIdentifier;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.GetObjectPresignRequest;

@Component
@RequiredArgsConstructor
@Slf4j
public class OssUtil {
    private final S3ClientBuilder s3ClientBuilder;

    private final S3Presigner.Builder s3PresignerBuilder;
    private final AwsS3Properties awsS3Properties;

    private OssTemporaryCredentials ossTemporaryCredentials;

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
            ossTemporaryCredentials = Forest.get(awsS3Properties.getTemporaryCredentialsUrl())
                    .execute(awsS3Properties.getTemporaryCredentialsResponse());
            log.info("response,{}", ossTemporaryCredentials);
            s3ClientBuilder.credentialsProvider(
                    StaticCredentialsProvider.create(AwsSessionCredentials.create(ossTemporaryCredentials.temSecretId(),
                            ossTemporaryCredentials.tmpSecretKey(), ossTemporaryCredentials.token())));
            s3PresignerBuilder.credentialsProvider(
                    StaticCredentialsProvider.create(AwsSessionCredentials.create(ossTemporaryCredentials.temSecretId(),
                            ossTemporaryCredentials.tmpSecretKey(), ossTemporaryCredentials.token())));
        }
    }

    public S3Presigner s3Presigner() {
        if (!awsS3Properties.getEnableTemporaryCredentialsModel()) {
            return s3PresignerBuilder.credentialsProvider(StaticCredentialsProvider.create(
                    AwsBasicCredentials.create(awsS3Properties.getAccessKey(), awsS3Properties.getAccessKeySecret())))
                    .build();
        }
        return s3PresignerBuilder.build();
    }

    public S3Client s3Client() {
        if (!awsS3Properties.getEnableTemporaryCredentialsModel()) {
            return s3ClientBuilder.credentialsProvider(
                    StaticCredentialsProvider.create(AwsSessionCredentials.create(ossTemporaryCredentials.temSecretId(),
                            ossTemporaryCredentials.tmpSecretKey(), ossTemporaryCredentials.token())))
                    .build();
        }
        return s3ClientBuilder.build();
    }

    /**
     * 检测bucket,如果不存在会新建bucket
     *
     * @param bucket 存储桶名称
     */
    private void checkBucket(String bucket) {
        S3Client s3Client = s3Client();
        try {
            s3Client.headBucket(builder -> builder.bucket(bucket));
        } catch (Exception e) {
            s3Client.createBucket(builder -> builder.bucket(bucket));
        }
        s3Client.close();
    }

    /**
     * 上传文件
     *
     * @param bucket 存储桶名称
     * @param key    文件key
     * @param file   文件
     * @return 上传结果
     */
    public boolean upload(String bucket, String key, File file) {
        checkBucket(bucket);
        S3Client s3Client = s3Client();
        boolean successful = s3Client
                .putObject(builder -> builder.bucket(bucket).key(key).contentType(HttpUtil.getMimeType(file.getPath())),
                        RequestBody.fromFile(file))
                .sdkHttpResponse().isSuccessful();
        s3Client.close();
        return successful;
    }

    /**
     * 删除文件
     *
     * @param bucket 存储桶名称
     * @param keys   文件key集合
     * @return 删除结果
     */
    public boolean deleteFile(String bucket, String... keys) {
        S3Client s3Client = s3Client();
        List<String> keyList = Arrays.asList(keys);
        if (CollUtil.isEmpty(keyList)) {
            return false;
        }
        List<ObjectIdentifier> willDelete = CollUtil.newArrayList();
        keyList.forEach(key -> willDelete.add(ObjectIdentifier.builder().key(key).build()));
        boolean hadDeleted = s3Client
                .deleteObjects(
                        builder -> builder.bucket(bucket).delete(deleteBuilder -> deleteBuilder.objects(willDelete)))
                .hasDeleted();
        s3Client.close();
        return hadDeleted;
    }

    /**
     * 上传文件
     *
     * @param bucket        存储桶名称
     * @param multipartFile 文件
     * @return 上传结果
     */
    @SneakyThrows
    public boolean upload(String bucket, MultipartFile multipartFile) {
        return upload(bucket, multipartFile.getOriginalFilename(), multipartFile);
    }

    /**
     * 上传文件
     *
     * @param bucket        存储桶名称
     * @param key           文件名称
     * @param multipartFile 文件
     * @return 上传结果
     */
    @SneakyThrows
    public boolean upload(String bucket, String key, MultipartFile multipartFile) {
        checkBucket(bucket);
        S3Client s3Client = s3Client();
        boolean successful = s3Client.putObject(
                builder -> builder.bucket(bucket).key(key)
                        .contentType(multipartFile.getContentType()),
                RequestBody.fromInputStream(multipartFile.getInputStream(), multipartFile.getSize())).sdkHttpResponse()
                .isSuccessful();
        s3Client.close();
        return successful;
    }

    /**
     * 获取文件临时url
     *
     * @param bucket  存储桶名称
     * @param fileKey 文件名
     * @return 文件url
     */
    public String getUrl(String bucket, String fileKey) {
        S3Presigner s3Presigner = s3Presigner();
        String url = s3Presigner
                .presignGetObject(GetObjectPresignRequest.builder().signatureDuration(Duration.ofMinutes(60))
                        .getObjectRequest(GetObjectRequest.builder().bucket(bucket).key(fileKey).build()).build())
                .url().toString();
        s3Presigner.close();
        return url;
    }
}
