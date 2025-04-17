package com.guanmengyuan.spring.ex.alipay.service;

import com.alipay.api.AlipayClient;
import com.guanmengyuan.spring.ex.alipay.config.AliPayProperties;

import java.util.List;

public interface AliPayService {
    /**
     * 切换appid对应的客户端
     *
     * @param appId appid
     * @return 自定义sdk配置
     */
    AlipayClient switchOverApiClient(String appId);

    /**
     * 初始化支付宝配置
     *
     * @param configs 配置类
     */
    void initFactories(List<AliPayProperties.AliPayConfig> configs);

    /**
     * 获取支付宝用户openId
     *
     * @param authCode 认证code
     * @param appId    小程序id
     * @return 用户id
     */
    String getOpenId(String authCode, String appId);

    String getPhoneNumber(String phoneCode, String appId);

    /**
     * 获取支付宝用户userId
     *
     * @param authCode 认证code
     * @param appId    小程序id
     * @return 用户Id
     */
    String getUserId(String authCode, String appId);

}
