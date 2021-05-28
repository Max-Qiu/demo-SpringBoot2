package com.maxqiu.demo;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.math.BigDecimal;
import java.time.Duration;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

import com.maxqiu.demo.entity.User;

/**
 * String 字符串
 * 
 * @author Max_Qiu
 */
@SpringBootTest
public class StringOperationsTest {
    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    /**
     * 值是String类型的字符串
     *
     * 自定义的RedisTemplate在格式化String时会多出引号
     *
     * 需要使用Spring内置的StringRedisTemplate
     */
    @Resource(name = "stringRedisTemplate")
    private ValueOperations<String, String> stringValueOperations;

    /**
     * 值是对象类型的字符串
     *
     * 使用自定义的RedisTemplate，将值格式化成JSON
     */
    @Resource(name = "redisTemplate")
    private ValueOperations<String, User> userValueOperations;

    /**
     * 值是数字类型的字符串
     *
     * 自定义的RedisTemplate可以格式化Integer
     */
    @Resource(name = "redisTemplate")
    private ValueOperations<String, Integer> integerValueOperations;

    /**
     * GET
     */
    @Test
    @Order(1)
    void get() {
        // 获取不存在的键
        assertNull(stringValueOperations.get("nonexistent"));

        // 获取String类型的数据
        stringValueOperations.set("key", "hello");
        assertEquals("hello", stringValueOperations.get("key"));

        // 获取对象类型的数据
        User user = new User(1, "tom", new BigDecimal(18));
        userValueOperations.set("user", user);
        assertEquals(user, userValueOperations.get("user"));

        integerValueOperations.set("num", 1);
        assertEquals(1, integerValueOperations.get("num"));

        redisTemplate.delete(Arrays.asList("user", "key", "num"));
    }

    /**
     * SET
     * 
     * SETEX
     * 
     * SETNX
     * 
     * GETSET
     */
    @Test
    @Order(2)
    void set() {
        /// SET 设置键与值
        // 简单设置值
        stringValueOperations.set("key1", "hello");
        assertEquals("hello", stringValueOperations.get("key1"));

        // 如果键存在才设置值
        Boolean flag1 = stringValueOperations.setIfPresent("key1", "world");
        assertTrue(flag1);
        assertEquals("world", stringValueOperations.get("key1"));
        Boolean flag2 = stringValueOperations.setIfPresent("key2", "world");
        assertFalse(flag2);
        assertNull(stringValueOperations.get("key2"));

        // 同上，并设置超时时间
        Boolean flag3 = stringValueOperations.setIfPresent("key1", "hello", Duration.ofSeconds(10));
        assertTrue(flag3);
        // stringOperations.setIfPresent("key2", "world", 10, TimeUnit.SECONDS);
        assertEquals(10, redisTemplate.getExpire("key1"));

        // SETEX 设置键与值并设置超时时间
        stringValueOperations.set("key2", "world", Duration.ofSeconds(10));
        // stringOperations.set("key2", "world", 10, TimeUnit.SECONDS);
        assertEquals(10, redisTemplate.getExpire("key2"));

        // SETNX 键不存在则设置键与值
        Boolean flag4 = stringValueOperations.setIfAbsent("key3", "hello");
        assertTrue(flag4);
        assertEquals("hello", stringValueOperations.get("key3"));

        // SET ... NX 键不存在则设置键与值（同时设置时间）
        Boolean flag5 = stringValueOperations.setIfAbsent("key4", "world", Duration.ofSeconds(10));
        // stringOperations.setIfAbsent("key4", "world", 10, TimeUnit.SECONDS);
        assertTrue(flag5);
        assertEquals("world", stringValueOperations.get("key4"));

        // GETSET 获取值并设置一个新值
        String s = stringValueOperations.getAndSet("key4", "redis");
        assertEquals(s, "world");
        assertEquals("redis", stringValueOperations.get("key4"));

        assertEquals(4, redisTemplate.delete(Arrays.asList("key1", "key2", "key3", "key4")));
    }

    @Test
    @Order(3)
    void other() {
        // APPEND 拼接字符串
        Integer append1 = stringValueOperations.append("key", "hello");
        assertEquals(5, append1);
        Integer append2 = stringValueOperations.append("key", " world");
        assertEquals(11, append2);

        // STRLEN 获取字符串长度
        Long size = stringValueOperations.size("key");
        assertEquals(11, size);

        // GETRANGE 截取字符串
        String a1 = stringValueOperations.get("key", 0, 3);
        assertEquals("hell", a1);
        String a2 = stringValueOperations.get("key", -3, -1);
        assertEquals("rld", a2);
        String a3 = stringValueOperations.get("key", 0, -1);
        assertEquals("hello world", a3);
        String a4 = stringValueOperations.get("key", 20, 100);
        assertEquals("", a4);

        // SETRANGE 修改字符串
        stringValueOperations.set("key", "redis", 6);
        assertEquals("hello redis", stringValueOperations.get("key"));

        // MSET 批量设置值
        stringValueOperations.multiSet(Map.of("key1", "v1", "key2", "v2"));
        assertTrue(redisTemplate.hasKey("key1"));

        // MGET 批量获取值
        List<String> list = stringValueOperations.multiGet(Arrays.asList("key1", "key2"));
        assertNotNull(list);
        assertEquals(2, list.size());

        // MSETNX 批量设置值（仅当键不存在）
        Boolean flag = stringValueOperations.multiSetIfAbsent(Map.of("key1", "v1", "key3", "v3"));
        assertFalse(flag);

        integerValueOperations.set("num", 1);

        // INCR 加一
        Long num1 = integerValueOperations.increment("num");
        assertEquals(2, num1);
        // DECR 减一
        Long num2 = integerValueOperations.decrement("num");
        assertEquals(1, num2);

        // INCRBY 加N
        Long num3 = integerValueOperations.increment("num", 15);
        assertEquals(16, num3);
        // DECRBY 减N
        Long num4 = integerValueOperations.decrement("num", 6);
        assertEquals(10, num4);

        redisTemplate.delete(Arrays.asList("key", "key1", "key2", "num"));
    }
}
