package com.guanmengyuan.spring.cloud.example.provider;

import org.apache.dubbo.config.spring.context.annotation.EnableDubbo;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@EnableDubbo
@MapperScan("com.guanmengyuan.spring.cloud.example.provider.mapper")
public class UserProviderApplication {
    public static void main(String[] args) {
        SpringApplication.run(UserProviderApplication.class, args);
    }
}
