package com.guanmengyuan.spring.ex.mybatis.flex.config;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import com.guanmengyuan.spring.ex.common.model.domain.BaseDomain;
import com.guanmengyuan.spring.ex.common.mybatis.flex.listener.BaseInsertListener;
import com.mybatisflex.core.FlexGlobalConfig;
import com.mybatisflex.core.audit.AuditManager;
import com.mybatisflex.core.logicdelete.LogicDeleteManager;
import com.mybatisflex.core.logicdelete.impl.BooleanLogicDeleteProcessor;
import com.mybatisflex.spring.boot.MyBatisFlexCustomizer;

@Configuration
@EnableConfigurationProperties(MybatisFlexProperties.class)
public class MybatisFlexConfiguration implements MyBatisFlexCustomizer {
    public MybatisFlexConfiguration(MybatisFlexProperties myBatisFlexProperties) {
        AuditManager.setAuditEnable(myBatisFlexProperties.getAuditEnable());
        LogicDeleteManager.setProcessor(new BooleanLogicDeleteProcessor());
    }

    @Override
    public void customize(FlexGlobalConfig globalConfig) {
        globalConfig.registerInsertListener(new BaseInsertListener(), BaseDomain.class);
    }
}
