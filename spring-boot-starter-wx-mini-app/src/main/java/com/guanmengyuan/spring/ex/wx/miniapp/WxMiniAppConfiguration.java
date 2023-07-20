package com.guanmengyuan.spring.ex.wx.miniapp;

import cn.binarywang.wx.miniapp.api.WxMaService;
import cn.binarywang.wx.miniapp.api.impl.WxMaServiceImpl;
import cn.binarywang.wx.miniapp.config.impl.WxMaDefaultConfigImpl;
import lombok.RequiredArgsConstructor;
import me.chanjar.weixin.common.error.WxRuntimeException;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

import java.util.List;
import java.util.stream.Collectors;

@EnableConfigurationProperties(WxMiniAppProperties.class)
@AutoConfiguration
@RequiredArgsConstructor
public class WxMiniAppConfiguration {
    private final WxMiniAppProperties wxMiniAppProperties;

    @Bean
    @ConditionalOnMissingBean(WxMaService.class)
    public WxMaService wxMaService() {
        List<WxMiniAppProperties.Config> configs = this.wxMiniAppProperties.getConfigs();
        if (configs == null) {
            throw new WxRuntimeException("wx mini app config list can not be empty,for more information please see https://github.com/binarywang/weixin-java-miniapp-demo/tree/master ");
        }
        WxMaService maService = new WxMaServiceImpl();
        maService.setMultiConfigs(
                configs.stream()
                        .map(a -> {
                            WxMaDefaultConfigImpl config = new WxMaDefaultConfigImpl();
//                WxMaDefaultConfigImpl config = new WxMaRedisConfigImpl(new JedisPool());
                            // 使用上面的配置时，需要同时引入jedis-lock的依赖，否则会报类无法找到的异常
                            config.setAppid(a.getAppid());
                            config.setSecret(a.getSecret());
                            config.setToken(a.getToken());
                            config.setAesKey(a.getAesKey());
                            config.setMsgDataFormat(a.getMsgDataFormat());
                            return config;
                        }).collect(Collectors.toMap(WxMaDefaultConfigImpl::getAppid, a -> a, (o, n) -> o)),configs.get(0).getAppid());
        return maService;
    }
}
