package com.guanmengyuan.spring.ex.alipay.service.impl;

import com.alipay.easysdk.factory.MultipleFactory;
import com.alipay.easysdk.kernel.Config;
import com.alipay.v3.ApiClient;
import com.alipay.v3.ApiException;
import com.alipay.v3.util.model.AlipayConfig;
import com.guanmengyuan.spring.ex.alipay.config.AliPayProperties;
import com.guanmengyuan.spring.ex.alipay.service.AliPayService;
import lombok.SneakyThrows;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class DefaultAliPayServiceImpl implements AliPayService {
    private static final Map<String, ApiClient> clients = new ConcurrentHashMap<>();

    private static final Map<String, MultipleFactory> factories = new ConcurrentHashMap<>();

    private static final Map<String, AliPayProperties.AliPayConfig> alipayConfigMap = new ConcurrentHashMap<>();

    @Override
    public synchronized ApiClient switchOverApiClient(String appId) {
        return clients.get(appId);
    }

    @Override
    public synchronized MultipleFactory switchOverFactory(String appId) {
        return factories.get(appId);
    }


    @Override
    public void initFactories(List<AliPayProperties.AliPayConfig> configs) {
        configs.forEach(config -> {
            String appId = config.getAppId();
            String protocol = config.getProtocol();
            String gatewayHost = config.getGatewayHost();
            String signType = config.getSignType();
            String merchantPrivateKey = config.getMerchantPrivateKey();
            String merchantCertPath = config.getMerchantCertPath();
            String alipayCertPath = config.getAlipayCertPath();
            String alipayRootCertPath = config.getAlipayRootCertPath();
            String encryptKey = config.getEncryptKey();

            ApiClient apiClient = new ApiClient();
            AlipayConfig alipayConfig = new AlipayConfig();
            alipayConfig.setAppId(appId);
            alipayConfig.setPrivateKey(merchantPrivateKey);
            alipayConfig.setAppCertPath(merchantCertPath);
            alipayConfig.setRootCertPath(alipayRootCertPath);
            alipayConfig.setAlipayPublicCertPath(alipayCertPath);
            alipayConfig.setEncryptKey(encryptKey);
//            alipayConfig.setEncryptType(signType);
            alipayConfigMap.put(appId, config);

            MultipleFactory multipleFactory = new MultipleFactory();
            Config aliPayConfig = new Config();
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
            factories.put(appId, multipleFactory);

            try {
                apiClient.setAlipayConfig(alipayConfig);
            } catch (ApiException e) {
                throw new RuntimeException(e);
            }
            clients.put(config.getAppId(), apiClient);
        });
    }

    @Override
    @SneakyThrows
    public  String getUserId(String authCode, String appId) {
        return factories.get(appId).OAuth().getToken(authCode).getUserId();
    }



}
