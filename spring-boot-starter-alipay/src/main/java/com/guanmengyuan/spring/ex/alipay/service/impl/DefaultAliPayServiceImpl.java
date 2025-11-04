package com.guanmengyuan.spring.ex.alipay.service.impl;

import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.AlipayConfig;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.internal.util.AlipayEncrypt;
import com.alipay.api.request.AlipaySystemOauthTokenRequest;
import com.alipay.api.response.AlipaySystemOauthTokenResponse;
import com.guanmengyuan.spring.ex.alipay.config.AliPayProperties;
import com.guanmengyuan.spring.ex.alipay.service.AliPayService;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import cn.hutool.v7.core.io.resource.ResourceUtil;
import cn.hutool.v7.core.text.StrUtil;
import cn.hutool.v7.core.util.CharsetUtil;
import cn.hutool.v7.json.JSON;
import cn.hutool.v7.json.JSONUtil;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@RequiredArgsConstructor
public class DefaultAliPayServiceImpl implements AliPayService {
    private static final Map<String, AlipayClient> clients = new ConcurrentHashMap<>();
    private static final Map<String, AlipayConfig> alipayConfigMap = new ConcurrentHashMap<>();

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

            // 设置私钥
            String merchantPrivateKey = ResourceUtil.readStr(appConfig.getMerchantPrivateKeyPath(),
                    StandardCharsets.UTF_8);

            alipayConfig.setPrivateKey(merchantPrivateKey);

            // 设置应用公钥证书文件
            String merchantCertPath = appConfig.getMerchantCertPath();
            alipayConfig.setAppCertContent(ResourceUtil.readUtf8Str(merchantCertPath));

            // 设置支付宝公钥证书文件
            String alipayCertPath = appConfig.getAlipayCertPath();
            alipayConfig.setAlipayPublicCertContent(ResourceUtil.readUtf8Str(alipayCertPath));
            // 支付宝根证书文件路径
            String alipayRootCertPath = appConfig.getAlipayRootCertPath();
            alipayConfig.setRootCertContent(ResourceUtil.readUtf8Str(alipayRootCertPath));
            // 设置
            alipayConfig.setEncryptKey(appConfig.getEncryptKey());

            AlipayClient apiClient;
            try {
                apiClient = new DefaultAlipayClient(alipayConfig);
            } catch (AlipayApiException e) {
                throw new RuntimeException(e);
            }

            alipayConfigMap.put(appId, alipayConfig);
            clients.put(appConfig.getAppId(), apiClient);
        });
    }

    /**
     * 获取openID
     */
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

    /**
     * 获取手机号
     */
    @SneakyThrows
    @Override
    public String getPhoneNumber(String phoneCode, String appId) {
        AlipayConfig config = alipayConfigMap.get(appId);
        JSON response = JSONUtil
                .parse(AlipayEncrypt.decryptContent(phoneCode, config.getEncryptType(),
                        config.getEncryptKey(), CharsetUtil.NAME_UTF_8));
        if (StrUtil.equals(response.getByPath("code", String.class), "10000")
                && StrUtil.equalsIgnoreCase(response.getByPath("msg", String.class), "Success")) {
            return response.getByPath("mobile", String.class);
        }
        throw new AlipayApiException("获取手机号失败");
    }

    @Override
    @SneakyThrows
    public String getUserId(String authCode, String appId) {
        AlipaySystemOauthTokenRequest alipaySystemOauthTokenRequest = new AlipaySystemOauthTokenRequest();
        alipaySystemOauthTokenRequest.setCode(authCode);
        alipaySystemOauthTokenRequest.setGrantType("authorization_code");
        AlipaySystemOauthTokenResponse alipaySystemOauthTokenResponse = clients.get(appId)
                .certificateExecute(alipaySystemOauthTokenRequest);
        if (!alipaySystemOauthTokenResponse.isSuccess()) {
            throw new RuntimeException("获取用户信息失败");
        }
        return alipaySystemOauthTokenResponse.getUserId();
    }

}
