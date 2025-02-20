package com.guanmengyuan.spring.ex.mybatis.flex.config;

import com.mybatisflex.core.FlexGlobalConfig;
import com.mybatisflex.core.logicdelete.LogicDeleteProcessor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MyBatisFlexConfiguration {

    public MyBatisFlexConfiguration() {
        FlexGlobalConfig config = FlexGlobalConfig.getDefaultConfig();
        config.registerInsertListener(new MyInsertListener());
        config.registerUpdateListener(new MyUpdateListener());
    }

    @Bean
    public LogicDeleteProcessor logicDeleteProcessor() {
        return new MyLogicDeleteProcessor();
    }
}
