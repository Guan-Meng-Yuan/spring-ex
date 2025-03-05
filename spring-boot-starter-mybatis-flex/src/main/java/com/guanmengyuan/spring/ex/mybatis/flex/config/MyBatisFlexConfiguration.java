package com.guanmengyuan.spring.ex.mybatis.flex.config;

import com.guanmengyuan.spring.ex.common.model.domain.BaseDomain;
import com.mybatisflex.core.FlexGlobalConfig;
import com.mybatisflex.core.logicdelete.LogicDeleteProcessor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 自定义mybatis-flex配置
 */
@Configuration
public class MyBatisFlexConfiguration {

    /**
     * 配置全局的插入和修改监听器
     */
    public MyBatisFlexConfiguration() {
        FlexGlobalConfig config = FlexGlobalConfig.getDefaultConfig();
        config.registerInsertListener(new MyInsertListener(), BaseDomain.class);
        config.registerUpdateListener(new MyUpdateListener(), BaseDomain.class);
    }

    /**
     * 自定义逻辑删除处理器
     *
     * @return 逻辑删除处理器
     */
    @Bean
    public LogicDeleteProcessor logicDeleteProcessor() {
        return new MyLogicDeleteProcessor();
    }
}
