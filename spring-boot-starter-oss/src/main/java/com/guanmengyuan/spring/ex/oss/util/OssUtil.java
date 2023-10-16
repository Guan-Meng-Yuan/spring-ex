package com.guanmengyuan.spring.ex.oss.util;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import com.guanmengyuan.spring.ex.oss.config.OssConfigProperties;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.ObjectIdentifier;

import java.io.File;
import java.util.List;

/**
 * @author guanmengyuan
 */
@Component
@RequiredArgsConstructor
@SuppressWarnings("unused")
public class OssUtil {
    private final S3Client s3Client;
    private final OssConfigProperties ossConfigProperties;

    /**
     * 删除对象
     *
     * @param bucket 存储桶名称
     * @param keys   文件列表
     * @return 删除结果
     */
    public Boolean deleteObjects(String bucket, List<String> keys) {
        if (CollUtil.isEmpty(keys)) {
            throw new RuntimeException("文件列表为空");
        }

        List<ObjectIdentifier> willDelete = ListUtil.of();
        for (String key : keys) {
            willDelete.add(ObjectIdentifier.builder().key(key).build());
        }
        return s3Client.deleteObjects(builder -> builder.bucket(bucket).delete(delete -> delete.objects(willDelete)))
                .hasDeleted();
    }

    /**
     * 删除对象
     *
     * @param keys 文件列表
     * @return 删除结果
     */
    public Boolean deleteObjects(String... keys) {
        return deleteObjects(ossConfigProperties.getDefaultBucket(), ListUtil.of(keys));
    }

    /**
     * 上传文件
     *
     * @param file   文件
     * @param bucket 存储桶名称
     * @param key    文件key
     * @return 上传后的key
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
     * @return 上传后的key
     */
    @SneakyThrows
    public String upload(File file, String bucket, String key) {
        String fileKey = IdUtil.getSnowflakeNextIdStr() + StrUtil.DASHED + key;

        s3Client.putObject(
                builder -> builder.bucket(bucket).key(fileKey)
                        .contentType(FileUtil.getMimeType(file.getName())),
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
