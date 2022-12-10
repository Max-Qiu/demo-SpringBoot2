package com.maxqiu.demo.service;

import javax.annotation.Resource;

import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

/**
 * 库存（Redisson） 服务类
 *
 * @author Max_Qiu
 */
@Service
public class Stock4Service {
    @Resource
    private RedisTemplate<String, Integer> redisTemplate;

    @Resource
    private RedissonClient redissonClient;

    public void lock() {
        RLock lock = redissonClient.getLock("lock");
        lock.lock();
        try {
            Integer stock = redisTemplate.opsForValue().get("stock");
            if (stock != null && stock > 0) {
                redisTemplate.opsForValue().set("stock", stock - 1);
            }
            // test();
        } finally {
            lock.unlock();
        }
    }
}
