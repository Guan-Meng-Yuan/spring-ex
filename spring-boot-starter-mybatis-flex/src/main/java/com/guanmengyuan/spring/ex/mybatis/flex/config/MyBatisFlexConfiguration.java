package com.guanmengyuan.spring.ex.mybatis.flex.config;

import com.guanmengyuan.spring.ex.common.model.domain.BaseDomain;
import com.mybatisflex.core.FlexGlobalConfig;
import com.mybatisflex.core.logicdelete.LogicDeleteProcessor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MyBatisFlexConfiguration {

    public MyBatisFlexConfiguration() {
        FlexGlobalConfig config = FlexGlobalConfig.getDefaultConfig();
        config.registerInsertListener(new MyInsertListener(), BaseDomain.class);
        config.registerUpdateListener(new MyUpdateListener(), BaseDomain.class);
    }

    @Bean
    public LogicDeleteProcessor logicDeleteProcessor() {
        return new MyLogicDeleteProcessor();
    }
}
