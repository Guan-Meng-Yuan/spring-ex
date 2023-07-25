package com.guanmengyuan.spring.ex.alipay.service.impl;

import com.alipay.easysdk.factory.MultipleFactory;
import com.alipay.easysdk.kernel.Config;
import com.guanmengyuan.spring.ex.alipay.config.AliPayProperties;
import com.guanmengyuan.spring.ex.alipay.service.AliPayService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DefaultAliPayServiceImpl implements AliPayService {
    private static final Map<String, MultipleFactory> factories = new HashMap<>();

    @Override
    public MultipleFactory switchOverTo(String appId) {
        return factories.get(appId);
    }

    @Override
    public void initFactories(List<AliPayProperties.AliPayConfig> configs) {
        configs.forEach(config -> {
            MultipleFactory multipleFactory = new MultipleFactory();
            Config aliPayConfig = new Config();

            String appId = config.getAppId();
            String protocol = config.getProtocol();
            String gatewayHost = config.getGatewayHost();
            String signType = config.getSignType();
            String merchantPrivateKey = config.getMerchantPrivateKey();
            String merchantCertPath = config.getMerchantCertPath();
            String alipayCertPath = config.getAlipayCertPath();
            String alipayRootCertPath = config.getAlipayRootCertPath();
            String encryptKey = config.getEncryptKey();

            aliPayConfig.appId = appId;
            aliPayConfig.protocol = protocol;
            aliPayConfig.gatewayHost = gatewayHost;
            aliPayConfig.signType = signType;
            aliPayConfig.merchantPrivateKey = merchantPrivateKey;
            aliPayConfig.merchantCertPath = merchantCertPath;
            aliPayConfig.alipayRootCertPath = alipayRootCertPath;
            aliPayConfig.alipayCertPath = alipayCertPath;
            aliPayConfig.encryptKey = encryptKey;

            multipleFactory.setOptions(aliPayConfig);
            factories.put(config.getAppId(), multipleFactory);
        });
    }
}
