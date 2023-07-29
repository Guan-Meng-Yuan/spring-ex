package com.guanmengyuan.spring.ex.alipay.config;


import com.alipay.easysdk.kernel.util.AntCertificationUtil;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;

/**
 * 支付宝配置
 */
@Data
@ConfigurationProperties(prefix = "alipay")
public class AliPayProperties {
    /**
     * 支付宝配置类
     */
    private List<AliPayConfig> configs;

    /**
     * 支付宝配置类
     */
    @Data
    public static class AliPayConfig {
        /**
         * 支付宝appid
         */
        private String appId;

        /**
         * 协议 默认 https://
         */
        private String protocol = "https";
        /**
         * 地址 默认 openapi.alipay.com
         */
        private String gatewayHost = "openapi.alipay.com";

        /**
         * 加密方式 默认RSA2
         */
        private String signType = "RSA2";

        /**
         * 商户私钥
         */
        private String merchantPrivateKey;

        /**
         * 商户私钥位置
         */
        private String merchantPrivateKeyPath;

        /**
         * 商户证书位置
         */
        private String merchantCertPath;

        /**
         * 支付宝证书位置
         */
        private String alipayCertPath;

        /**
         * 支付宝根证书位置
         */
        private String alipayRootCertPath;


        public void setMerchantPrivateKeyPath(String merchantPrivateKeyPath) {
            this.merchantPrivateKeyPath = merchantPrivateKeyPath;
            this.merchantPrivateKey = AntCertificationUtil.readCertContent(merchantPrivateKeyPath);
        }

        /**
         * AES秘钥 可用于加密解密
         */
        private String encryptKey;
    }
}