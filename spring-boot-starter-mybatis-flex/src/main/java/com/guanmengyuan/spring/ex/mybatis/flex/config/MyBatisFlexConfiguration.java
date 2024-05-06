package com.guanmengyuan.spring.ex.mybatis.flex.config;

import com.mybatisflex.core.logicdelete.LogicDeleteProcessor;
import com.mybatisflex.core.logicdelete.impl.BooleanLogicDeleteProcessor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MyBatisFlexConfiguration {

    @Bean
    public LogicDeleteProcessor logicDeleteProcessor() {
        return new BooleanLogicDeleteProcessor();
    }
}
