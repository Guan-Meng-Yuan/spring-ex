package com.guanmengyuan.spring.ex.oss.util;

import java.io.File;
import java.nio.ByteBuffer;
import java.time.Duration;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.http.HttpUtil;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import reactor.core.publisher.Flux;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.ObjectIdentifier;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.GetObjectPresignRequest;

@Component
@RequiredArgsConstructor
public class OssUtil {
    private final S3Client s3Client;

    private final S3Presigner s3Presigner;

    private void checkBucket(String bucket) {
        try {
            s3Client.headBucket(builder -> builder.bucket(bucket));
        } catch (Exception e) {
            s3Client.createBucket(builder -> builder.bucket(bucket));
        }
    }

    public boolean upload(String bucket, String key, File file) {
        checkBucket(bucket);
        return s3Client
                .putObject(builder -> builder.bucket(bucket).key(key).contentType(HttpUtil.getMimeType(file.getPath())),
                        RequestBody.fromFile(file))
                .sdkHttpResponse().isSuccessful();
    }

    public boolean upload(String bucket, String key, ByteBuffer byteBuffer, String contentType) {
        checkBucket(bucket);
        return s3Client.putObject(builder -> builder.bucket(bucket).key(key).contentType(contentType),
                RequestBody.fromByteBuffer(byteBuffer)).sdkHttpResponse().isSuccessful();

    }

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

    public boolean upload(String bucket, FilePart filePart) {
        return upload(bucket, filePart.filename(), filePart);
    }

    @SneakyThrows
    public boolean upload(String bucket, MultipartFile multipartFile) {
        return upload(bucket, multipartFile.getOriginalFilename(), multipartFile);
    }

    @SneakyThrows
    public boolean upload(String bucket, String key, MultipartFile multipartFile) {
        checkBucket(bucket);
        return s3Client.putObject(
                builder -> builder.bucket(bucket).key(key)
                        .contentType(multipartFile.getContentType()),
                RequestBody.fromInputStream(multipartFile.getInputStream(), multipartFile.getSize())).sdkHttpResponse()
                .isSuccessful();
    }

    public boolean upload(String bucket, String key, FilePart filePart) {
        Flux<DataBuffer> dataBufferFlux = filePart.content();
        AtomicReference<ByteBuffer> byteBuffer = new AtomicReference<>();
        dataBufferFlux.reduce(DataBuffer::write).map(dataBuffer -> {
            byte[] bytes = new byte[dataBuffer.readableByteCount()];
            dataBuffer.read(bytes);
            DataBufferUtils.release(dataBuffer);
            byteBuffer.set(ByteBuffer.wrap(bytes));
            return byteBuffer;
        }).subscribe();
        String mimeType = HttpUtil.getMimeType(filePart.filename());

        return upload(bucket, key, byteBuffer.get(), mimeType);
    }

    public String getUrl(String bucket, String fileKey) {
        return s3Presigner
                .presignGetObject(GetObjectPresignRequest.builder().signatureDuration(Duration.ofMinutes(60))
                        .getObjectRequest(GetObjectRequest.builder().bucket(bucket).key(fileKey).build()).build())
                .url().toString();
    }
}
