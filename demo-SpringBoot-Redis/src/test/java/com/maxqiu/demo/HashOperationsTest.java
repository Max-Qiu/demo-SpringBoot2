package com.maxqiu.demo;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;

import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;

/**
 * Hash 哈希散列
 *
 * @author Max_Qiu
 */
@SpringBootTest
public class HashOperationsTest {
    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @Resource(name = "stringRedisTemplate")
    private HashOperations<String, String, String> stringHashOperations;

    @Resource(name = "redisTemplate")
    private HashOperations<String, String, Integer> integerHashOperations;

    /**
     * HSET 给哈希散列添加字段和值
     *
     * HSETNX 给哈希散列添加字段和值（仅字段不存在）
     */
    @Test
    @Order(1)
    void set() {
        // HSET 给哈希散列添加字段和值
        // 单个
        stringHashOperations.put("user", "id", "1");
        stringHashOperations.put("user", "name", "tom");
        // 多个
        stringHashOperations.putAll("user", Map.of("email", "tom@126.com", "age", "18"));

        // HSETNX 给哈希散列添加字段和值（仅字段不存在）
        Boolean flag1 = stringHashOperations.putIfAbsent("user", "address", "jiangsu");
        assertTrue(flag1);
        Boolean flag2 = stringHashOperations.putIfAbsent("user", "address", "jiangsu");
        assertFalse(flag2);

        // 删除数据
        redisTemplate.delete("user");
    }

    /**
     * HGET 获取哈希散列的指定字段的值
     *
     * HMGET 获取哈希散列的多个字段的值
     *
     * HGETALL 获取哈希散列的所有字段与值
     *
     * HKEYS 获取哈希散列的所有字段
     *
     * HVALS 获取哈希散列的所有值
     */
    @Test
    @Order(2)
    void get() {
        // 准备数据
        stringHashOperations.putAll("user", Map.of("id", "1", "name", "tom", "email", "tom@126.com", "age", "18"));

        // HGET 获取哈希散列的指定字段的值
        String name = stringHashOperations.get("user", "name");
        assertEquals("tom", name);

        // HMGET 获取哈希散列的多个字段的值
        List<String> list = stringHashOperations.multiGet("user", Arrays.asList("name", "age", "email", "address"));
        for (String s : list) {
            System.out.println(s);
        }

        // HGETALL 获取哈希散列的所有字段与值
        Map<String, String> map = stringHashOperations.entries("user");
        for (String s : map.keySet()) {
            System.out.println(s + "  " + map.get(s));
        }

        // HKEYS 获取哈希散列的所有字段
        Set<String> keys = stringHashOperations.keys("user");
        for (String key : keys) {
            System.out.println(key);
        }

        // HVALS 获取哈希散列的所有值
        List<String> values = stringHashOperations.values("user");
        for (String value : values) {
            System.out.println(value);
        }

        // 删除数据
        redisTemplate.delete("user");
    }

    @Test
    @Order(3)
    void other() {
        // 准备数据
        stringHashOperations.putAll("user", Map.of("id", "1", "name", "tom", "email", "tom@126.com", "age", "18"));

        // HEXISTS 哈希散列是否存在指定字段
        Boolean hasName = stringHashOperations.hasKey("user", "name");
        assertTrue(hasName);

        // HDEL 删除哈希散列的指定字段
        Long delete = stringHashOperations.delete("user", "id", "email");
        assertEquals(2, delete);

        // HLEN 哈希散列的长度
        Long size = stringHashOperations.size("user");
        assertEquals(2, size);

        // HSTRLEN 哈希散列的字段的长度
        Long length = stringHashOperations.lengthOfValue("user", "name");
        assertEquals(3, length);

        // HINCRBY 增减哈希散列中指定字段的值
        Long increment = integerHashOperations.increment("user", "age", 1);
        assertEquals(19, increment);

        // 删除数据
        redisTemplate.delete("user");
    }
}
