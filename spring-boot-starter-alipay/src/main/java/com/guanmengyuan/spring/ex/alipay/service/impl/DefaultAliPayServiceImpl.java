package com.guanmengyuan.spring.ex.alipay.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.AlipayConfig;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.internal.util.file.FileUtils;
import com.alipay.api.request.AlipaySystemOauthTokenRequest;
import com.alipay.api.response.AlipaySystemOauthTokenResponse;
import com.alipay.easysdk.factory.MultipleFactory;
import com.alipay.easysdk.kernel.Config;
import com.guanmengyuan.spring.ex.alipay.config.AliPayProperties;
import com.guanmengyuan.spring.ex.alipay.service.AliPayService;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.core.io.ResourceLoader;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@RequiredArgsConstructor
public class DefaultAliPayServiceImpl implements AliPayService {
    private static final Map<String, AlipayClient> clients = new ConcurrentHashMap<>();
    private static final Map<String, AlipayConfig> alipayConfigMap = new ConcurrentHashMap<>();
    private static final Map<String, MultipleFactory> multipleFactoryMap = new ConcurrentHashMap<>();

    private final ResourceLoader resourceLoader;

    @Override
    public AlipayClient switchOverApiClient(String appId) {
        return clients.get(appId);
    }

    @Override
    @SneakyThrows
    public void initFactories(List<AliPayProperties.AliPayConfig> configs) {
        configs.forEach(appConfig -> {
            String appId = appConfig.getAppId();
            AlipayConfig alipayConfig = new AlipayConfig();
            alipayConfig.setEncryptKey(appConfig.getEncryptKey());
            alipayConfig.setAppId(appId);
            Config config = new Config();
            config.appId = appId;
            config.protocol = appConfig.getProtocol();
            config.gatewayHost = appConfig.getGatewayHost();
            config.signType = appConfig.getSignType();
            config.encryptKey = appConfig.getEncryptKey();
            try {
                String merchantPrivateKey = FileUtils.readFileToString(
                        resourceLoader.getResource(appConfig.getMerchantPrivateKeyPath()).getFile(),
                        StandardCharsets.UTF_8);
                config.merchantPrivateKey = merchantPrivateKey;
                // 设置私钥
                alipayConfig.setPrivateKey(merchantPrivateKey);
                String merchantCertPath = resourceLoader.getResource(appConfig.getMerchantCertPath()).getFile()
                        .getAbsolutePath();
                // 设置应用公钥证书文件
                alipayConfig.setAppCertPath(merchantCertPath);
                config.merchantCertPath = merchantCertPath;
                // 设置支付宝公钥证书文件
                String alipayCertPath = resourceLoader.getResource(appConfig.getAlipayCertPath()).getFile()
                        .getAbsolutePath();
                config.alipayCertPath = alipayCertPath;
                alipayConfig.setAlipayPublicCertPath(alipayCertPath);
                // 支付宝根证书文件路径
                String alipayRootCertPath = resourceLoader.getResource(appConfig.getAlipayRootCertPath()).getFile()
                        .getAbsolutePath();
                config.alipayRootCertPath = alipayRootCertPath;
                alipayConfig.setRootCertPath(alipayRootCertPath);
                // 设置
                alipayConfig.setEncryptKey(appConfig.getEncryptKey());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            AlipayClient apiClient;
            try {
                apiClient = new DefaultAlipayClient(alipayConfig);
            } catch (AlipayApiException e) {
                throw new RuntimeException(e);
            }
            MultipleFactory multipleFactory = new MultipleFactory();
            multipleFactory.setOptions(config);
            multipleFactoryMap.put(appId, multipleFactory);
            alipayConfigMap.put(appId, alipayConfig);
            clients.put(appConfig.getAppId(), apiClient);
        });
    }

    @Override
    @SneakyThrows
    public String getOpenId(String authCode, String appId) {
        AlipaySystemOauthTokenRequest alipaySystemOauthTokenRequest = new AlipaySystemOauthTokenRequest();
        alipaySystemOauthTokenRequest.setCode(authCode);
        alipaySystemOauthTokenRequest.setGrantType("authorization_code");
        AlipaySystemOauthTokenResponse alipaySystemOauthTokenResponse = clients.get(appId).certificateExecute(alipaySystemOauthTokenRequest);
        if (!alipaySystemOauthTokenResponse.isSuccess()) {
            throw new RuntimeException("获取用户信息失败");
        }
        return alipaySystemOauthTokenResponse.getOpenId();
    }

    @SneakyThrows
    @Override
    public String getPhoneNumber(String phoneCode, String appId) {
        String decrypted = multipleFactoryMap.get(appId).AES().decrypt(phoneCode);
        JSONObject jsonObject = JSON.parseObject(decrypted);
        if (!"10000".equals(jsonObject.get("code"))) {
            throw new RuntimeException("数据解析失败");
        }
        Object mobile = jsonObject.get("mobile");
        return mobile.toString();
    }

}
