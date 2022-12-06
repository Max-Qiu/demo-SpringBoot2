package com.maxqiu.demo.lock;

import javax.annotation.Resource;

import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import cn.hutool.core.lang.UUID;

/**
 * 分布式锁工厂类
 *
 * @author Max_Qiu
 */
@Component
public class DistributedLockFactory {
    @Resource
    private StringRedisTemplate stringRedisTemplate;

    private static final String UUID_STR = UUID.fastUUID().toString();

    public DistributedRedisLock getRedisLock(String lockName) {
        return new DistributedRedisLock(stringRedisTemplate, lockName, UUID_STR);
    }
}
