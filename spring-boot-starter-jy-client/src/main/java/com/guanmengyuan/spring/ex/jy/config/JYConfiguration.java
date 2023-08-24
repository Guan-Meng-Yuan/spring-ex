package com.guanmengyuan.spring.ex.jy.config;

import com.github.lianjiatech.retrofit.spring.boot.core.RetrofitScan;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * 聚英电子配置
 *
 * @author guanmengyuan
 */


 
@EnableConfigurationProperties(JYProperties.class)
@Configuration
@Import(cn.hutool.extra.spring.SpringUtil.class)
@RetrofitScan("com.guanmengyuan.config.client")
public class JYConfiguration {
}
