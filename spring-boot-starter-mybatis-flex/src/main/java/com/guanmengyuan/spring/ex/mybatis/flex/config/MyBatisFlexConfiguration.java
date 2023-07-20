package com.guanmengyuan.spring.ex.mybatis.flex.config;

import com.mybatisflex.core.audit.AuditManager;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(MyBatisFlexProperties.class)
public class MyBatisFlexConfiguration {
    public MyBatisFlexConfiguration(MyBatisFlexProperties myBatisFlexProperties) {
        AuditManager.setAuditEnable(myBatisFlexProperties.getAuditEnable());
    }
}
