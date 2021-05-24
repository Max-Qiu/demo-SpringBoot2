package com.maxqiu.demo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializer;

/**
 * Redis配置项
 * 
 * @author Max_Qiu
 */
@Configuration
public class RedisConfig {
    /**
     * 自定义RedisTemplate，使用json格式化value
     * 
     * @param redisConnectionFactory
     * @return
     */
    @Bean
    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory redisConnectionFactory) {
        RedisTemplate<String, Object> template = new RedisTemplate<>();
        // 连接工厂
        template.setConnectionFactory(redisConnectionFactory);
        // key序列化
        template.setKeySerializer(RedisSerializer.string());
        // value序列化
        template.setValueSerializer(RedisSerializer.json());
        // hash序列化
        template.setHashKeySerializer(RedisSerializer.string());
        template.setHashValueSerializer(RedisSerializer.json());
        System.out.println("RedisTemplate初始化完成");
        return template;
    }
}
