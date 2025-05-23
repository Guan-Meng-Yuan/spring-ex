package com.guanmengyuan.spring.ex.wx.pay.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;

/**
 * 微信支付配置类
 */
@Data
@ConfigurationProperties(prefix = "wx.pay")
public class WxPayProperties {

    /**
     * 微信支付配置列表
     */
    private List<Config> configs;

    /**
     * 配置类
     */
    @Data
    public static class Config {
        /**
         * 设置微信公众号或者小程序等的appid.
         */
        private String appId;

        /**
         * 微信支付商户号.
         */
        private String mchId;

        /**
         * 微信支付商户密钥.
         */
        private String mchKey;

        /**
         * 服务商模式下的子商户公众账号ID，普通模式请不要配置，请在配置文件中将对应项删除.
         */
        private String subAppId;

        /**
         * 服务商模式下的子商户号，普通模式请不要配置，最好是请在配置文件中将对应项删除.
         */
        private String subMchId;

        /**
         * apiClient_cert.p12文件的绝对路径，或者如果放在项目中，请以classpath:开头指定.
         */
        private String keyPath;

        /**
         * 微信支付分serviceId
         */
        private String serviceId;

        /**
         * 证书序列号
         */
        private String certSerialNo;

        /**
         * apiV3秘钥
         */
        private String apiV3Key;

        /**
         * 微信支付分回调地址
         */
        private String payScoreNotifyUrl;

        /**
         * apiV3 商户apiClient_key.pem
         */
        private String privateKeyPath;

        /**
         * apiV3 商户apiClient_cert.pem
         */
        private String privateCertPath;
    }


}
