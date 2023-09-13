package com.guanmengyuan.spring.ex.oss.dto;

import lombok.Data;

import java.io.Serializable;
import java.time.Duration;

/**
 * 微信云托管临时密钥
 */
@Data
public class CosSecretInfo implements Serializable {
    private String TmpSecretId;
    private String TmpSecretKey;
    private Duration ExpiredTime;
    private String Token;
}
