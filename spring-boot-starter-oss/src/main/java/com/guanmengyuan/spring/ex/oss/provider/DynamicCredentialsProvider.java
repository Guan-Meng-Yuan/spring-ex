package com.guanmengyuan.spring.ex.oss.provider;

import com.guanmengyuan.spring.ex.oss.service.CosSecretInfo;
import com.guanmengyuan.spring.ex.oss.service.OssSecretRequestService;

import cn.hutool.core.date.DateUtil;
import lombok.extern.slf4j.Slf4j;
import software.amazon.awssdk.auth.credentials.AwsCredentials;
import software.amazon.awssdk.auth.credentials.AwsCredentialsProvider;
import software.amazon.awssdk.auth.credentials.AwsSessionCredentials;

/**
 * @author guanmengyuan
 */
@Slf4j
public class DynamicCredentialsProvider implements AwsCredentialsProvider {
    private CosSecretInfo cosSecretInfo;
    private static DynamicCredentialsProvider dynamicCredentialsProvider;
    private OssSecretRequestService ossSecretRequestService;

    private DynamicCredentialsProvider(OssSecretRequestService ossSecretRequestService) {
        this.ossSecretRequestService = ossSecretRequestService;
    }

    public static DynamicCredentialsProvider create(OssSecretRequestService ossSecretRequestService) {
        if (null == dynamicCredentialsProvider) {
            dynamicCredentialsProvider = new DynamicCredentialsProvider(ossSecretRequestService);
        }
        return dynamicCredentialsProvider;
    }

    @Override
    public AwsCredentials resolveCredentials() {
        if (null == cosSecretInfo
                || cosSecretInfo.getExpiredTime() < DateUtil.currentSeconds() - 10) {
            log.info("token was expired,try to get new token");
            cosSecretInfo = ossSecretRequestService.requestSecretInfo();
        }
        return AwsSessionCredentials.create(cosSecretInfo.getTmpSecretId(),
                cosSecretInfo.getTmpSecretKey(), cosSecretInfo.getToken());
    }
}
