package com.maxqiu.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.session.FlushMode;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;

/**
 * @author Max_Qiu
 */
// 启用Redis存储HTTP的Session
@EnableRedisHttpSession(
    // 设置存储会话的键的命名空间
    redisNamespace = "demo::session",
    // 设置会话超时时间（默认30分钟）单位：秒
    maxInactiveIntervalInSeconds = 1200,
    // Redis会话的刷新模式。
    // ON_SAVE：（默认值）仅在调用SessionRepository.SAVE（Session）时更新备份Redis。在web环境中，发生在HTTP响应提交之前。
    // IMMEDIATE：会话的任何更新都会立即写入Redis实例
    flushMode = FlushMode.IMMEDIATE)
@SpringBootApplication
public class SpringSessionApplication {
    public static void main(String[] args) {
        SpringApplication.run(SpringSessionApplication.class, args);
    }
}
