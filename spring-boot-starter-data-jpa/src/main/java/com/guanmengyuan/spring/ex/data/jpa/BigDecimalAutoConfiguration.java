package com.guanmengyuan.spring.ex.data.jpa;

import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernatePropertiesCustomizer;
import org.springframework.context.annotation.Bean;

import java.util.Map;

@AutoConfiguration
public class BigDecimalAutoConfiguration {

    @Bean
    public HibernatePropertiesCustomizer hibernatePropertiesCustomizer() {
        return (Map<String, Object> hibernateProperties) -> {
            // 自动指定使用我们的自定义 Dialect
            hibernateProperties.put("hibernate.dialect", DefaultSQLDialect.class.getName());
        };
    }
}