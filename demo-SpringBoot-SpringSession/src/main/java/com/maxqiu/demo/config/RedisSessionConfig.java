package com.maxqiu.demo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.serializer.RedisSerializer;

import com.alibaba.fastjson2.support.spring.data.redis.GenericFastJsonRedisSerializer;

/**
 * Redis共享会话的序列化设置
 *
 * @author Max_Qiu
 */
@Configuration
public class RedisSessionConfig {
    /**
     * 使用 fastjson 序列号
     */
    @Bean
    public RedisSerializer<Object> springSessionDefaultRedisSerializer() {
        return new GenericFastJsonRedisSerializer();
    }
}
