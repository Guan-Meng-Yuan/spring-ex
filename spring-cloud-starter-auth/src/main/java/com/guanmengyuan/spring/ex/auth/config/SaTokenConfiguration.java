package com.guanmengyuan.spring.ex.auth.config;

import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;

import com.guanmengyuan.spring.ex.common.model.constant.GlobalResponseConstant;
import com.guanmengyuan.spring.ex.common.model.enums.ResEnum;
import com.guanmengyuan.spring.ex.common.model.exception.ServiceException;

import cn.dev33.satoken.SaManager;
import cn.dev33.satoken.filter.SaServletFilter;
import cn.dev33.satoken.router.SaRouter;
import cn.dev33.satoken.same.SaSameUtil;
import cn.dev33.satoken.stp.StpUtil;
import lombok.RequiredArgsConstructor;

@Configuration
@EnableConfigurationProperties(AuthConfigProperties.class)
@RequiredArgsConstructor
@Order(-100)
public class SaTokenConfiguration {
    private final AuthConfigProperties authConfigProperties;

    @Bean
    @ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.SERVLET)
    public SaServletFilter saServletFilter() {
        authConfigProperties.getWhitelist().addAll(GlobalResponseConstant.DEFAULT_PATH);
        return new SaServletFilter()
                .addInclude("/**")
                .addExclude(authConfigProperties.getWhitelist().toArray(new String[0]))
                .setAuth(obj -> {
                    SaRouter.match("/**", StpUtil::checkLogin);
                    SaRouter.match("/**", () -> {
                        if (SaManager.getConfig().getCheckSameToken()) {
                            SaSameUtil.checkCurrentRequestToken();
                        }
                    });
                }).setError(e -> {
                    throw new ServiceException(ResEnum.UNAUTHORIZED);
                });
    }
}
