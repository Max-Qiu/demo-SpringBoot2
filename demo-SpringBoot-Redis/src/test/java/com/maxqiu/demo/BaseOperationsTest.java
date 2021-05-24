package com.maxqiu.demo;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.Duration;
import java.util.Arrays;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.connection.DataType;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.core.ValueOperations;

/**
 * Keys 键相关
 * 
 * @author Max_Qiu
 */
@SpringBootTest
public class BaseOperationsTest {
    /**
     * RedisTemplate 的父类
     */
    @Autowired
    private RedisOperations<String, Object> operations;

    @Test
    void test() {
        // RedisOperations 可以获取对应数据类型的 XxxOperations
        // ListOperations<String, Object> listOperations = template.opsForList();

        // 准备数据
        ValueOperations<String, Object> stringObjectValueOperations = operations.opsForValue();
        stringObjectValueOperations.set("key1", "hello1");
        stringObjectValueOperations.set("key2", "hello2");
        stringObjectValueOperations.set("key3", "hello3");

        // KEYS 查找键
        Set<String> keys = operations.keys("*");
        assertNotNull(keys);
        assertEquals(3, keys.size());

        // EXISTS 键是否存在
        assertTrue(operations.hasKey("key1"));
        assertEquals(2, operations.countExistingKeys(Arrays.asList("key1", "key2")));

        // TYPE 查看键的类型
        assertEquals(DataType.STRING, operations.type("key1"));

        // DEL 删除键
        assertFalse(operations.delete("key"));
        assertTrue(operations.delete("key1"));
        assertEquals(0, operations.delete(Arrays.asList("key", "key1")));

        // UNLINK 删除键（异步）
        assertTrue(operations.unlink("key2"));
        assertEquals(0, operations.unlink(Arrays.asList("key1", "key2")));

        // TTL 查看键剩余生成秒
        assertEquals(-1, operations.getExpire("key3"));

        // EXPIRE 设置键到期秒
        operations.expire("key3", 10, TimeUnit.SECONDS);
        operations.expire("key3", Duration.ofSeconds(10));
        assertEquals(10, operations.getExpire("key3"));

        // PERSIST key 设置键永不过期
        operations.persist("key3");
        assertEquals(-1, operations.getExpire("key3"));

        // 删除数据
        assertTrue(operations.delete("key3"));
    }
}
