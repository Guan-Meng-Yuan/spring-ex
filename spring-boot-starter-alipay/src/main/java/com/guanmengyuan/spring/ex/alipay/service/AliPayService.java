package com.guanmengyuan.spring.ex.alipay.service;

import java.util.List;

import com.alipay.easysdk.factory.MultipleFactory;
import com.alipay.v3.ApiClient;
import com.guanmengyuan.spring.ex.alipay.config.AliPayProperties;

public interface AliPayService {
    /**
     * 切换appid对应的客户端
     *
     * @param appId appid
     * @return 自定义sdk配置
     */
    ApiClient switchOverApiClient(String appId);

    /**
     * 切换appid对应的客户端 easy sdk
     *
     * @param appId appid
     * @return 自定义sdk配置
     */
    MultipleFactory switchOverFactory(String appId);

    /**
     * 初始化支付宝配置
     *
     * @param configs 配置类
     */
    void initFactories(List<AliPayProperties.AliPayConfig> configs);

    /**
     * 获取支付宝用户id
     *
     * @param authCode 认证code
     * @param appId    小程序id
     * @return 用户id
     */
    String getUserId(String authCode, String appId);

}
