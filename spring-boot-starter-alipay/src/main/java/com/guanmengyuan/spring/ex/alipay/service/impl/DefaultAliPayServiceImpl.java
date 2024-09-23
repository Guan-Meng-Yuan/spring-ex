package com.guanmengyuan.spring.ex.alipay.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.AlipayConfig;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.request.AlipaySystemOauthTokenRequest;
import com.alipay.api.response.AlipaySystemOauthTokenResponse;
import com.alipay.easysdk.factory.MultipleFactory;
import com.alipay.easysdk.kernel.Config;
import com.guanmengyuan.spring.ex.alipay.config.AliPayProperties;
import com.guanmengyuan.spring.ex.alipay.service.AliPayService;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.dromara.hutool.core.io.resource.ResourceUtil;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@RequiredArgsConstructor
public class DefaultAliPayServiceImpl implements AliPayService {
    private static final Map<String, AlipayClient> clients = new ConcurrentHashMap<>();
    private static final Map<String, AlipayConfig> alipayConfigMap = new ConcurrentHashMap<>();
    private static final Map<String, MultipleFactory> multipleFactoryMap = new ConcurrentHashMap<>();

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

            // 设置私钥
            String merchantPrivateKey = ResourceUtil.readStr(appConfig.getMerchantPrivateKeyPath(),
                    StandardCharsets.UTF_8);

            config.merchantPrivateKey = merchantPrivateKey;
            alipayConfig.setPrivateKey(merchantPrivateKey);

            // 设置应用公钥证书文件
            String merchantCertPath = appConfig.getMerchantCertPath();
            alipayConfig.setAppCertContent(ResourceUtil.readUtf8Str(merchantCertPath));
            config.merchantCertPath = merchantCertPath;

            // 设置支付宝公钥证书文件
            String alipayCertPath = appConfig.getAlipayCertPath();
            config.alipayCertPath = alipayCertPath;
            alipayConfig.setAlipayPublicCertContent(ResourceUtil.readUtf8Str(alipayCertPath));
            // 支付宝根证书文件路径
            String alipayRootCertPath = appConfig.getAlipayRootCertPath();
            config.alipayRootCertPath = alipayRootCertPath;
            alipayConfig.setRootCertContent(ResourceUtil.readUtf8Str(alipayRootCertPath));
            // 设置
            alipayConfig.setEncryptKey(appConfig.getEncryptKey());

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
        AlipaySystemOauthTokenResponse alipaySystemOauthTokenResponse = clients.get(appId)
                .certificateExecute(alipaySystemOauthTokenRequest);
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
