package com.guanmengyuan.spring.ex.oss.service;

import java.io.Serializable;

/**
 * 临时密钥接口
 */
public interface CosSecretInfo extends Serializable {

    String getTmpSecretId();

    String getTmpSecretKey();

    String getToken();

    Long getExpiredTime();

}
