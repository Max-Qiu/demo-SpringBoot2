package com.maxqiu.demo;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author Max_Qiu
 */
@SpringBootApplication
@MapperScan("com.maxqiu.demo.mapper")
public class ShardingSphereJdbcApplication {
    public static void main(String[] args) {
        SpringApplication.run(ShardingSphereJdbcApplication.class, args);
    }
}
