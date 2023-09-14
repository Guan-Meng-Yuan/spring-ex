package com.guanmengyuan.spring.ex.oss.model;

import java.time.Duration;

public interface OssTemporaryCredentials {
    /**
     * 获取临时凭证ID
     *
     * @return 临时凭证ID
     */
    String temSecretId();

    /**
     * 获取临时凭证密钥
     *
     * @return 临时凭证密钥
     */
    String tmpSecretKey();

    /**
     * 临时凭证过期时间
     *
     * @return 临时凭证过期时间
     */
    Duration expiredTime();

    /**
     * 临时凭证token
     */
    String token();
}
