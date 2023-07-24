package com.guanmengyuan.spring.ex.test;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.guanmengyuan.spring.ex.test.mapper")
public class SpringExTestApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringExTestApplication.class, args);
    }

}
