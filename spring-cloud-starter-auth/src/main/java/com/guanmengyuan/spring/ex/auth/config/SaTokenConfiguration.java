package com.guanmengyuan.spring.ex.auth.config;

import cn.dev33.satoken.SaManager;
import cn.dev33.satoken.reactor.filter.SaReactorFilter;
import cn.dev33.satoken.router.SaRouter;
import cn.dev33.satoken.same.SaSameUtil;
import cn.dev33.satoken.stp.StpUtil;
import com.guanmengyuan.spring.ex.common.model.enums.ResEnum;
import com.guanmengyuan.spring.ex.common.model.exception.ServiceException;
import com.guanmengyuan.spring.ex.webflux.handler.GlobalResponseHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;

@Configuration
@EnableConfigurationProperties(AuthConfigProperties.class)
@RequiredArgsConstructor
@Order(-100)
public class SaTokenConfiguration {
    private final AuthConfigProperties authConfigProperties;


    @Bean
    public SaReactorFilter saReactorFilter() {
        authConfigProperties.getWhitelist().addAll(GlobalResponseHandler.DEFAULT_PATH);
        return new SaReactorFilter()
                .addInclude("/**")
                .addExclude(authConfigProperties.getWhitelist().toArray(new String[0]))
                .setAuth(obj -> {
                    SaRouter.match("/**", StpUtil::checkLogin);
                    SaRouter.match("/**", () -> {
                        if (SaManager.getConfig().getCheckSameToken()) {
                            SaSameUtil.checkCurrentRequestToken();
                        }
                    });
                })
                .setError(e -> {
                    throw new ServiceException(ResEnum.UNAUTHORIZED);
                });
    }
}
