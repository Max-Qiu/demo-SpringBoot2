package com.maxqiu.demo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.serializer.RedisSerializer;

import com.alibaba.fastjson2.support.spring.data.redis.GenericFastJsonRedisSerializer;

/**
 * Session 序列化设置
 *
 * @author Max_Qiu
 */
@Configuration
public class SessionConfig {
    /**
     * 使用 fastjson 序列号
     */
    @Bean
    public RedisSerializer<Object> springSessionDefaultRedisSerializer() {
        return new GenericFastJsonRedisSerializer();
    }
}
