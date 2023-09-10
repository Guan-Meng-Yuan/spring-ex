package com.guanmengyuan.spring.ex.oss.util;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.http.HttpUtil;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.ObjectIdentifier;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.GetObjectPresignRequest;

import java.io.File;
import java.time.Duration;
import java.util.Arrays;
import java.util.List;

@Component
@RequiredArgsConstructor
public class OssUtil {
    private final S3Client s3Client;

    private final S3Presigner s3Presigner;

    /**
     * 检测bucket,如果不存在会新建bucket
     *
     * @param bucket 存储桶名称
     */
    private void checkBucket(String bucket) {
        try {
            s3Client.headBucket(builder -> builder.bucket(bucket));
        } catch (Exception e) {
            s3Client.createBucket(builder -> builder.bucket(bucket));
        }
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
        return s3Client
                .putObject(builder -> builder.bucket(bucket).key(key).contentType(HttpUtil.getMimeType(file.getPath())),
                        RequestBody.fromFile(file))
                .sdkHttpResponse().isSuccessful();
    }


    /**
     * 删除文件
     *
     * @param bucket 存储桶名称
     * @param keys   文件key集合
     * @return 删除结果
     */
    public boolean deleteFile(String bucket, String... keys) {
        List<String> keyList = Arrays.asList(keys);
        if (CollUtil.isEmpty(keyList)) {
            return false;
        }
        List<ObjectIdentifier> willDelete = CollUtil.newArrayList();
        keyList.forEach(key -> willDelete.add(ObjectIdentifier.builder().key(key).build()));
        return s3Client
                .deleteObjects(
                        builder -> builder.bucket(bucket).delete(deleteBuilder -> deleteBuilder.objects(willDelete)))
                .hasDeleted();
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
        return s3Client.putObject(
                        builder -> builder.bucket(bucket).key(key)
                                .contentType(multipartFile.getContentType()),
                        RequestBody.fromInputStream(multipartFile.getInputStream(), multipartFile.getSize())).sdkHttpResponse()
                .isSuccessful();
    }


    /**
     * 获取文件临时url
     *
     * @param bucket  存储桶名称
     * @param fileKey 文件名
     * @return 文件url
     */
    public String getUrl(String bucket, String fileKey) {
        return s3Presigner
                .presignGetObject(GetObjectPresignRequest.builder().signatureDuration(Duration.ofMinutes(60))
                        .getObjectRequest(GetObjectRequest.builder().bucket(bucket).key(fileKey).build()).build())
                .url().toString();
    }
}
