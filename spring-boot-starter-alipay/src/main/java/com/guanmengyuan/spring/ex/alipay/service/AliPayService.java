package com.guanmengyuan.spring.ex.alipay.service;

import com.alipay.easysdk.factory.MultipleFactory;
import com.guanmengyuan.spring.ex.alipay.config.AliPayProperties;

import java.util.List;

public interface AliPayService {
    /**
     * 切换appid对应的客户端
     *
     * @param appId appid
     * @return 自定义sdk配置
     */
    @SuppressWarnings("unused")
    MultipleFactory switchOverTo(String appId);

    /**
     * 初始化支付宝配置
     *
     * @param configs 配置类
     */
    void initFactories(List<AliPayProperties.AliPayConfig> configs);
}
