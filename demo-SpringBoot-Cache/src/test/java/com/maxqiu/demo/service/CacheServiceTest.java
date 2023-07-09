package com.maxqiu.demo.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Set;
import java.util.concurrent.CountDownLatch;

import javax.annotation.Resource;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.StringRedisTemplate;

/**
 * @author Max_Qiu
 */
@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class CacheServiceTest {
    @Resource
    private StringRedisTemplate stringRedisTemplate;

    /**
     * 仅用于每个测试用例前清空数据库
     */
    @BeforeEach
    void clean() {
        Set<String> keys = stringRedisTemplate.keys("*");
        if (keys != null && keys.size() != 0) {
            // 清空缓存
            stringRedisTemplate.delete(keys);
        }
    }

    @Resource
    private CacheService service;

    @Test
    @Order(1)
    void customCacheNames1() {
        // 先调用 cache1 返回 1
        assertEquals("1", service.cache1());
        // 再调用 cache3 返回 1
        assertEquals("1", service.cache3());
        // 命中一个缓存则立即返回
    }

    @Test
    @Order(2)
    void customCacheNames2() {
        // 先调用 cache2 返回 2
        assertEquals("2", service.cache2());
        // 再调用 cache3 返回 2
        assertEquals("2", service.cache3());
        // 再调用 cache1 返回 1
        assertEquals("1", service.cache1());
        // 再调用 cache3 返回 1
        assertEquals("1", service.cache3());
        // 多个缓存名称时，写在前面的优先级高
    }

    @Test
    @Order(3)
    void customCacheNames3() {
        // 先调用 cache3 返回 3
        assertEquals("3", service.cache3());
        // 再调用 cache1 或 cache2 均返回 3
        assertEquals("3", service.cache1());
        assertEquals("3", service.cache2());
        // 若多个缓存名称均为空，则多个缓存的值均被写入
    }

    @Test
    @Order(4)
    void customKey() {
        System.out.println(service.key("aaa"));
        String s = stringRedisTemplate.opsForValue().get("key::aaa");
        assertEquals(s, "\"aaa\"");
    }

    @Test
    @Order(5)
    void sync() {
        // 创建并发请求
        CountDownLatch countDownLatch = new CountDownLatch(5);
        for (int i = 1; i <= 5; i++) {
            new Thread(() -> {
                System.out.println(service.sync());
                countDownLatch.countDown();
            }).start();
        }
        try {
            countDownLatch.await();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        // 只有一次“进入了 sync 方法”输出
    }

    @Test
    @Order(6)
    void condition() {
        // 不满足指定条件
        System.out.println(service.condition(1));
        // 不缓存
        String s1 = stringRedisTemplate.opsForValue().get("condition::1");
        assertNull(s1);

        // 满足指定条件
        System.out.println(service.condition(11));
        // 缓存
        String s2 = stringRedisTemplate.opsForValue().get("condition::11");
        assertEquals(s2, "11");
    }

    @Test
    @Order(7)
    void unless() {
        Integer unless;
        do {
            // 获取返回值
            unless = service.unless();
            System.out.println(unless);
            // 当返回值大于0时继续执行，不缓存
        } while (unless > 0);
        String s = stringRedisTemplate.opsForValue().get("unless::SimpleKey []");
        assertEquals(s, "0");
    }

    @Test
    @Order(8)
    void cacheManager() {
        String s = service.cacheManager();
        Long cacheManager = stringRedisTemplate.getExpire("cacheManager::SimpleKey []");
        System.out.println(cacheManager);
        assertNotNull(cacheManager);
        // 理论上为 86400 （1天），防止网络卡顿，所以这里这样判断
        assertTrue(cacheManager.intValue() >= 86399);
    }

    @Test
    @Order(9)
    void cachePut() {
        // 先放入一个缓存
        assertEquals("1", service.cache1());
        // 再强制更新
        assertEquals("success", service.cachePut());
        // 再获取数据返回刷新后的值
        assertEquals("success", service.cache1());
    }

    @Test
    @Order(10)
    void cacheEvict() {
        // 先放入一个缓存
        assertEquals("1", service.cache1());
        // 再执行清空
        service.cacheEvict();
        // 查询该key
        Boolean flag = stringRedisTemplate.hasKey("cache1::SimpleKey []");
        // 显示不存在
        assertNotNull(flag);
        assertFalse(flag);
    }

    @Test
    @Order(11)
    void cacheEvictAllEntries() {
        // 先放入几个值
        service.key("aaa");
        service.key("bbb");
        service.key("ccc");
        // 查询相关key
        Set<String> keysBefore = stringRedisTemplate.keys("key::*");
        // 此时应该有三个
        assertNotNull(keysBefore);
        assertEquals(3, keysBefore.size());
        // 执行清空
        service.cacheEvictAllEntries();
        // 再次查询相关key
        Set<String> keysAfter = stringRedisTemplate.keys("key::*");
        // 此时没有相关
        assertNotNull(keysAfter);
        assertEquals(0, keysAfter.size());
    }

    @Test
    void cacheEvictBeforeInvocation() {
        // 先放值
        service.key("1");
        // 再执行清除
        try {
            service.cacheEvictBeforeInvocation("1");
        } catch (Exception ignored) {

        }
        // 再次查询相关key
        Set<String> keys = stringRedisTemplate.keys("key::*");
        // 此时没有相关
        assertNotNull(keys);
        assertEquals(0, keys.size());
    }
}
