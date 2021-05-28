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
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SetOperations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.data.redis.core.ZSetOperations;

/**
 * Keys 键相关
 *
 * @author Max_Qiu
 */
@SpringBootTest
public class BaseOperationsTest {
    /**
     * RedisTemplate操作通用对象
     */
    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    /**
     * 操作String类型的对象
     */
    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Test
    void test() {
        // RedisTemplate 可以获取对应数据类型的 XxxOperations
        ValueOperations<String, Object> objectValueOperations = redisTemplate.opsForValue();
        ListOperations<String, Object> objectListOperations = redisTemplate.opsForList();
        SetOperations<String, Object> objectSetOperations = redisTemplate.opsForSet();
        HashOperations<String, String, Object> objectHashOperations = redisTemplate.opsForHash();
        ZSetOperations<String, Object> objectZSetOperations = redisTemplate.opsForZSet();

        // 准备数据
        ValueOperations<String, String> stringValueOperations = stringRedisTemplate.opsForValue();
        stringValueOperations.set("key1", "hello1");
        stringValueOperations.set("key2", "hello2");
        stringValueOperations.set("key3", "hello3");
        stringValueOperations.set("key4", "hello4");

        // KEYS 查找键
        Set<String> keys = redisTemplate.keys("*");
        assertNotNull(keys);
        assertEquals(4, keys.size());

        // EXISTS 键是否存在
        Boolean hasKey = redisTemplate.hasKey("key1");
        assertTrue(hasKey);
        Long existingKeys = redisTemplate.countExistingKeys(Arrays.asList("key1", "key2"));
        assertEquals(2, existingKeys);

        // TYPE 查看键的类型
        assertEquals(DataType.STRING, redisTemplate.type("key1"));

        // DEL 删除键
        Boolean deleteFalse = redisTemplate.delete("key");
        assertFalse(deleteFalse);
        Boolean deleteSuccess = redisTemplate.delete("key1");
        assertTrue(deleteSuccess);
        Long batchDelete = redisTemplate.delete(Arrays.asList("key", "key2"));
        assertEquals(1, batchDelete);

        // UNLINK 删除键（异步）
        Boolean unlink = redisTemplate.unlink("key3");
        assertTrue(unlink);
        Long batchUnlink = redisTemplate.unlink(Arrays.asList("key1", "key2"));
        assertEquals(0, batchUnlink);

        Long expire = redisTemplate.getExpire("key4");
        // TTL 查看键剩余生成秒
        assertEquals(-1, expire);

        // EXPIRE 设置键到期秒
        redisTemplate.expire("key4", 10, TimeUnit.SECONDS);
        redisTemplate.expire("key4", Duration.ofSeconds(10));
        assertEquals(10, redisTemplate.getExpire("key4"));

        // PERSIST key 设置键永不过期
        redisTemplate.persist("key4");
        assertEquals(-1, redisTemplate.getExpire("key4"));

        // 删除数据
        assertTrue(redisTemplate.delete("key4"));
    }
}
