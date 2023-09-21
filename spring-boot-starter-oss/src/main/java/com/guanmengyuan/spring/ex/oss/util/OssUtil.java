package com.guanmengyuan.spring.ex.oss.util;

import java.io.File;
import java.util.List;

import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import com.guanmengyuan.spring.ex.oss.config.OssConfigProperties;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpUtil;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.ObjectIdentifier;

/**
 * @author guanmengyuan
 */
@Component
@RequiredArgsConstructor
public class OssUtil {
    private final S3Client s3Client;
    private final OssConfigProperties ossConfigProperties;

    /**
     * 删除对象
     * 
     * @param bucket
     * @param keys
     * @return
     */
    public Boolean deleteObjects(String bucket, List<String> keys) {
        if (CollUtil.isEmpty(keys)) {
            throw new RuntimeException("文件列表为空");
        }
        List<ObjectIdentifier> willDelete = CollUtil.newArrayList();
        for (String key : keys) {
            willDelete.add(ObjectIdentifier.builder().key(key).build());
        }
        return s3Client.deleteObjects(builder -> builder.bucket(bucket).delete(delete -> delete.objects(willDelete)))
                .hasDeleted();
    }

    /**
     * 删除对象
     * 
     * @param keys
     * @return
     */
    public Boolean deleteObjects(String... keys) {
        return deleteObjects(ossConfigProperties.getDefaultBucket(), CollUtil.toList(keys));
    }

    /**
     * 上传文件
     * 
     * @param file   文件
     * @param bucket 存储桶名称
     * @param key    文件key
     * @return
     */
    @SneakyThrows
    public String upload(MultipartFile file, String bucket, String key) {
        String fileKey = IdUtil.getSnowflakeNextIdStr() + StrUtil.DASHED + key;
        s3Client.putObject(
                builder -> builder.bucket(bucket).key(fileKey).contentType(file.getContentType()),
                RequestBody.fromBytes(file.getBytes())).sdkHttpResponse().isSuccessful();
        return fileKey;
    }

    @SneakyThrows
    public String upload(MultipartFile file, String bucket) {
        String fileKey = IdUtil.getSnowflakeNextIdStr() + StrUtil.DASHED + file.getOriginalFilename();
        return upload(file, bucket, fileKey);
    }

    @SneakyThrows
    public String upload(MultipartFile file) {
        return upload(file, ossConfigProperties.getDefaultBucket());
    }

    /**
     * 上传文件
     * 
     * @param file   文件
     * @param bucket 存储桶名称
     * @param key    文件key
     * @return
     */
    @SneakyThrows
    public String upload(File file, String bucket, String key) {
        String fileKey = IdUtil.getSnowflakeNextIdStr() + StrUtil.DASHED + key;
        s3Client.putObject(
                builder -> builder.bucket(bucket).key(fileKey)
                        .contentType(HttpUtil.getMimeType(file.getName())),
                RequestBody.fromFile(file)).sdkHttpResponse().isSuccessful();
        return fileKey;
    }

    @SneakyThrows
    public String upload(File file, String bucket) {
        String fileKey = IdUtil.getSnowflakeNextIdStr() + StrUtil.DASHED + file.getName();
        return upload(file, bucket, fileKey);
    }

    @SneakyThrows
    public String upload(File file) {
        return upload(file, ossConfigProperties.getDefaultBucket());
    }

}
