package com.maxqiu.demo.config;

import java.nio.charset.StandardCharsets;
import java.time.Duration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.StringRedisSerializer;

/**
 * Redis缓存配置
 *
 * @author Max_Qiu
 */
@Configuration
public class RedisCacheConfig {
    /**
     * 默认Redis全局配置。（30分钟超时）
     *
     * @param redisConnectionFactory
     * @return
     */
    @Primary
    @Bean
    public RedisCacheManager cacheManager(RedisConnectionFactory redisConnectionFactory) {
        return RedisCacheManager.builder(redisConnectionFactory).cacheDefaults(instanceConfig(30L)).build();
    }

    /**
     * 永不超时
     */
    @Bean
    public RedisCacheManager noExpire(RedisConnectionFactory connectionFactory) {
        return RedisCacheManager.builder(connectionFactory).cacheDefaults(instanceConfig(0L)).build();
    }

    /**
     * 2小时超时
     */
    @Bean
    public RedisCacheManager expire2h(RedisConnectionFactory connectionFactory) {
        return RedisCacheManager.builder(connectionFactory).cacheDefaults(instanceConfig(60 * 2L)).build();
    }

    /**
     * 一天超时
     */
    @Bean
    public RedisCacheManager expire1day(RedisConnectionFactory connectionFactory) {
        return RedisCacheManager.builder(connectionFactory).cacheDefaults(instanceConfig(60 * 24L)).build();
    }

    /**
     * 通用配置
     *
     * @param ttl
     *            超时时间（分钟）
     */
    private RedisCacheConfiguration instanceConfig(Long ttl) {
        // 缓存配置对象
        return RedisCacheConfiguration.defaultCacheConfig()
            // 设置缓存的默认超时时间
            .entryTtl(Duration.ofMinutes(ttl))
            // 如果是空值，不缓存（不建议设置，防止缓存穿透）
            // .disableCachingNullValues()
            // 设置key序列化器
            .serializeKeysWith(RedisSerializationContext.SerializationPair.fromSerializer(new StringRedisSerializer(StandardCharsets.UTF_8)))
            // 设置value序列化器（这里使用阿里巴巴的Fastjson格式化工具）
            .serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(new GenericJackson2JsonRedisSerializer()));
    }
}
