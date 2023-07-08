package com.maxqiu.demo;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

import javax.annotation.Resource;

import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SetOperations;

import com.maxqiu.demo.entity.User;

/**
 * Set 集合
 *
 * @author Max_Qiu
 */
@SpringBootTest
public class SetOperationsTest {
    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    /**
     * 值是String类型的集合
     *
     * 自定义的RedisTemplate在格式化String时会多出引号
     *
     * 需要使用Spring内置的StringRedisTemplate
     */
    @Resource(name = "stringRedisTemplate")
    private SetOperations<String, String> stringSetOperations;

    /**
     * 值是对象类型的集合
     *
     * 使用自定义的RedisTemplate，将值格式化成JSON
     */
    @Resource(name = "redisTemplate")
    private SetOperations<String, User> userSetOperations;

    /**
     * 值是数字类型的集合
     *
     * 自定义的RedisTemplate可以格式化Integer
     */
    @Resource(name = "redisTemplate")
    private SetOperations<String, Integer> integerSetOperations;

    /**
     * 测试基本操作
     */
    @Test
    @Order(1)
    void normal() {
        // SADD 添加元素到集合中
        Long add = stringSetOperations.add("set", "a", "b", "c", "d", "e");
        assertEquals(5, add);

        // SCARD 集合大小
        Long size = stringSetOperations.size("set");
        assertEquals(add, size);

        // SMEMBERS 列出集合中的元素
        Set<String> set = stringSetOperations.members("set");
        assertNotNull(set);
        assertEquals(5, set.size());

        // SISMEMBER 存在指定的元素
        Boolean member = stringSetOperations.isMember("set", "a");
        assertTrue(member);

        // SREM 删除指定元素
        Long remove = stringSetOperations.remove("set", "a");
        assertEquals(1, remove);

        // SMOVE 移动指定元素
        Boolean move = stringSetOperations.move("set", "b", "set2");
        assertTrue(move);

        // SRANDMEMBER 随机取出一个或多个元素
        // 取出一个元素
        String set1 = stringSetOperations.randomMember("set");
        System.out.println(set1);
        // 取出多个元素且不重复
        Set<String> set3 = stringSetOperations.distinctRandomMembers("set", 2);
        assertEquals(2, set3.size());
        // 取出多个元素但可能有重复
        List<String> list = stringSetOperations.randomMembers("set", 2);
        assertEquals(2, list.size());

        // SPOP 随机取出一个或多个元素并删除
        String set2 = stringSetOperations.pop("set");
        System.out.println(set2);
        List<String> list2 = stringSetOperations.pop("set", 2);
        assertNotNull(list2);
        assertEquals(2, list2.size());

        // 删除数据
        assertFalse(redisTemplate.hasKey("set"));
        redisTemplate.delete("set2");
    }

    /**
     * 测试不同类型
     */
    @Test
    @Order(2)
    void type() {
        // 对象类型
        Long add = userSetOperations.add("users", new User(1, "tom", new BigDecimal(185)), new User(2, "tom", new BigDecimal(183)));
        Long size = userSetOperations.size("users");
        assertEquals(add, size);

        // 数字类型
        Long nums = integerSetOperations.add("nums", 1, 2, 3, 3);
        assertEquals(3, nums);

        // 删除数据
        redisTemplate.delete(Arrays.asList("users", "nums"));
    }

    /**
     * SINTER 交集
     *
     * SUNION 并集
     *
     * SDIFF 差集
     */
    @Test
    @Order(3)
    void aggregate() {
        // 准备数据
        stringSetOperations.add("set1", "a", "b", "c");
        stringSetOperations.add("set2", "a", "d", "f");

        // SINTER 交集
        Set<String> intersect = stringSetOperations.intersect("set1", "set2");
        // stringSetOperations.intersect(Arrays.asList("set1", "set2"));
        // stringSetOperations.intersect("set1", Arrays.asList("set2"));
        assertEquals(1, intersect.size());
        assertTrue(intersect.contains("a"));

        // SINTERSTORE 计算交集并存入目标集合
        Long intersectAndStore = stringSetOperations.intersectAndStore("set1", "set2", "set3");
        assertEquals(1, intersectAndStore);

        // SUNION 并集
        Set<String> union = stringSetOperations.union("set1", "set2");
        assertEquals(5, union.size());

        // SUNIONSTORE 计算并集并存入目标集合
        Long unionAndStore = stringSetOperations.unionAndStore("set1", "set2", "set4");
        assertEquals(5, unionAndStore);

        // SDIFF 差集
        Set<String> difference = stringSetOperations.difference("set1", "set2");
        assertEquals(2, difference.size());
        assertTrue(difference.contains("b"));

        // SDIFFSTORE 计算差集并存入目标集合
        Long differenceAndStore = stringSetOperations.differenceAndStore("set1", "set2", "set5");
        assertEquals(2, differenceAndStore);

        // 删除数据
        redisTemplate.delete(Arrays.asList("set1", "set2", "set3", "set4", "set5"));
    }
}
