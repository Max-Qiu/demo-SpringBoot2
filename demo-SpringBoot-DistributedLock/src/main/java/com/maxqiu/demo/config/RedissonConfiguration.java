package com.maxqiu.demo.config;

import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Redisson 配置类
 *
 * @author Max_Qiu
 */
@Configuration
public class RedissonConfiguration {
    @Bean
    public RedissonClient redissonClient() {
        Config config = new Config();
        config.useSingleServer().setAddress("redis://redis:6379").setPassword("123");
        return Redisson.create(config);
    }
}
