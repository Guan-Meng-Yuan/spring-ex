package com.guanmengyuan.spring.ex.alipay.config;

import com.guanmengyuan.spring.ex.alipay.service.AliPayService;
import com.guanmengyuan.spring.ex.alipay.service.impl.DefaultAliPayServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.util.CollectionUtils;

@EnableConfigurationProperties(AliPayProperties.class)
@AutoConfiguration
@RequiredArgsConstructor
public class AliPayAutoConfiguration {
    private final AliPayProperties aliPayProperties;

    @Bean
    @ConditionalOnMissingBean(AliPayService.class)
    public AliPayService aliPayService() {
        if (CollectionUtils.isEmpty(aliPayProperties.getConfigs())) {
            throw new RuntimeException("alipay config can not be empty");
        }
        AliPayService aliPayService = new DefaultAliPayServiceImpl();
        aliPayService.initFactories(aliPayProperties.getConfigs());
        return aliPayService;
    }
}
