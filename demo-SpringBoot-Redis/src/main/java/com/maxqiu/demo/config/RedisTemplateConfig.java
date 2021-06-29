package com.maxqiu.demo.config;

import java.nio.charset.StandardCharsets;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import com.alibaba.fastjson.support.spring.GenericFastJsonRedisSerializer;

/**
 * Redis操作配置
 * 
 * @author Max_Qiu
 */
@Configuration
public class RedisTemplateConfig {
    /**
     * 自定义RedisTemplate，使用fastjson格式化value
     */
    @Bean
    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory redisConnectionFactory) {
        RedisTemplate<String, Object> template = new RedisTemplate<>();
        // 连接工厂
        template.setConnectionFactory(redisConnectionFactory);
        // key序列化
        template.setKeySerializer(new StringRedisSerializer(StandardCharsets.UTF_8));
        // value序列化（这里使用阿里巴巴的Fastjson格式化工具）
        template.setValueSerializer(new GenericFastJsonRedisSerializer());
        // hash序列化
        template.setHashKeySerializer(new StringRedisSerializer(StandardCharsets.UTF_8));
        template.setHashValueSerializer(new GenericFastJsonRedisSerializer());
        // 启用事务支持
        template.setEnableTransactionSupport(true);
        return template;
    }
}
