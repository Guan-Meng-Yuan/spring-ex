package com.guanmengyuan.spring.ex.wx.pay.config;

import com.github.binarywang.wxpay.config.WxPayConfig;
import com.github.binarywang.wxpay.service.WxPayService;
import com.github.binarywang.wxpay.service.impl.WxPayServiceImpl;
import lombok.RequiredArgsConstructor;
import me.chanjar.weixin.common.error.WxRuntimeException;
import org.apache.commons.lang3.StringUtils;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

import java.util.List;
import java.util.stream.Collectors;

@AutoConfiguration
@EnableConfigurationProperties(WxPayProperties.class)
@RequiredArgsConstructor
public class WxPayConfiguration {
    private final WxPayProperties wxPayProperties;

    @Bean
    @ConditionalOnMissingBean(WxPayService.class)
    public WxPayService wxPayService() {
        List<WxPayProperties.Config> configs = wxPayProperties.getConfigs();
        if (configs == null) {
            throw new WxRuntimeException("wx pay config list can not be empty");
        }
        final WxPayService wxPayService = new WxPayServiceImpl();
        wxPayService.setMultiConfig(
                configs.stream().map(
                        a -> {
                            WxPayConfig payConfig = new WxPayConfig();
                            payConfig.setAppId(StringUtils.trimToNull(a.getAppId()));
                            payConfig.setMchId(StringUtils.trimToNull(a.getMchId()));
                            payConfig.setMchKey(StringUtils.trimToNull(a.getMchKey()));
                            payConfig.setSubAppId(StringUtils.trimToNull(a.getSubAppId()));
                            payConfig.setSubMchId(StringUtils.trimToNull(a.getSubMchId()));
                            payConfig.setKeyPath(StringUtils.trimToNull(a.getKeyPath()));
                            //以下是apiV3以及支付分相关
                            payConfig.setServiceId(StringUtils.trimToNull(a.getServiceId()));
                            payConfig.setPayScoreNotifyUrl(StringUtils.trimToNull(a.getPayScoreNotifyUrl()));
                            payConfig.setPrivateKeyPath(StringUtils.trimToNull(a.getPrivateKeyPath()));
                            payConfig.setPrivateCertPath(StringUtils.trimToNull(a.getPrivateCertPath()));
                            payConfig.setCertSerialNo(StringUtils.trimToNull(a.getCertSerialNo()));
                            payConfig.setApiV3Key(StringUtils.trimToNull(a.getApiV3Key()));
                            return payConfig;
                        }).collect(Collectors.toMap(WxPayConfig::getAppId, a -> a, (o, n) -> o)), configs.get(0).getAppId());
        return wxPayService;
    }
}
