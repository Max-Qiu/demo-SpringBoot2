package com.maxqiu.demo.config;

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
 * @author Max_Qiu
 */
@Configuration
public class RedisConfig {
    /**
     * 默认Redis全局配置。（用不超时）
     * 
     * @param redisConnectionFactory
     * @return
     */
    @Primary
    @Bean
    public RedisCacheManager cacheManager(RedisConnectionFactory redisConnectionFactory) {
        return RedisCacheManager.builder(redisConnectionFactory).cacheDefaults(instanceConfig(0L)).build();
    }

    /**
     * 1分钟超时
     */
    @Bean
    public RedisCacheManager expire1min(RedisConnectionFactory connectionFactory) {
        return RedisCacheManager.builder(connectionFactory).cacheDefaults(instanceConfig(60 * 60L)).build();
    }

    /**
     * 2小时超时
     */
    @Bean
    public RedisCacheManager expire2h(RedisConnectionFactory connectionFactory) {
        return RedisCacheManager.builder(connectionFactory).cacheDefaults(instanceConfig(60 * 60 * 2L)).build();
    }

    /**
     * 一天超时
     */
    @Bean
    public RedisCacheManager expire1day(RedisConnectionFactory connectionFactory) {
        return RedisCacheManager.builder(connectionFactory).cacheDefaults(instanceConfig(60 * 60 * 24L)).build();
    }

    /**
     * 通用配置
     * 
     * @param ttl
     *            超时时间（秒）
     */
    private RedisCacheConfiguration instanceConfig(Long ttl) {
        // 缓存配置对象
        return RedisCacheConfiguration.defaultCacheConfig()
            // 设置缓存的默认超时时间
            .entryTtl(Duration.ofSeconds(ttl))
            // 如果是空值，不缓存
            .disableCachingNullValues()
            // 设置key序列化器
            .serializeKeysWith(RedisSerializationContext.SerializationPair.fromSerializer(new StringRedisSerializer()))
            // 设置value序列化器
            .serializeValuesWith(
                RedisSerializationContext.SerializationPair.fromSerializer(new GenericJackson2JsonRedisSerializer()));
    }
}