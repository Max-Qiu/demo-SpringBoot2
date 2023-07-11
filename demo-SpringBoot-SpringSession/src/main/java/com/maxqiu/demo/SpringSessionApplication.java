package com.maxqiu.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;

/**
 * @author Max_Qiu
 */
// 启用Redis存储HTTP的Session
@EnableRedisHttpSession(
    // 会话超时时间，单位：秒（默认 1800 秒）
    redisNamespace = "demo::session",
    // 用于存储会话的键的命名空间（默认 spring::session）
    maxInactiveIntervalInSeconds = 3600)
@SpringBootApplication
public class SpringSessionApplication {
    public static void main(String[] args) {
        SpringApplication.run(SpringSessionApplication.class, args);
    }
}
