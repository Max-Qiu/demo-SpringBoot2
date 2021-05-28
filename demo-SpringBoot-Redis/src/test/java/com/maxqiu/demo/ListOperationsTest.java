package com.maxqiu.demo;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import javax.annotation.Resource;

import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.RedisTemplate;

import com.maxqiu.demo.entity.User;

/**
 * List 列表
 * 
 * @author Max_Qiu
 */
@SpringBootTest
public class ListOperationsTest {
    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    /**
     * 值是String类型的列表
     * 
     * 自定义的RedisTemplate在格式化String时会多出引号
     * 
     * 需要使用Spring内置的StringRedisTemplate
     */
    @Resource(name = "stringRedisTemplate")
    private ListOperations<String, String> stringListOperations;

    /**
     * 值是对象类型的列表
     * 
     * 使用自定义的RedisTemplate，将值格式化成JSON
     */
    @Resource(name = "redisTemplate")
    private ListOperations<String, User> userListOperations;

    /**
     * 值是数字类型的列表
     *
     * 自定义的RedisTemplate可以格式化Integer
     */
    @Resource(name = "redisTemplate")
    private ListOperations<String, Integer> integerListOperations;

    /**
     * LRANGE 按指定范围查看列表的值
     */
    @Test
    @Order(1)
    void range() {
        // 插入元素
        stringListOperations.rightPushAll("list", "a", "b", "c", "d");

        List<String> list = stringListOperations.range("list", 0, -1);
        assertNotNull(list);
        assertEquals(4, list.size());
        assertEquals("a", list.get(0));

        redisTemplate.delete("list");
    }

    /**
     * RPUSH 在列表右侧插值
     * 
     * RPUSHX 仅key存在时在列表右侧插值
     * 
     * LPUSH 在列表左侧插值
     * 
     * LPUSHX 仅key存在时在列表左侧插值
     */
    @Test
    @Order(2)
    void push() {
        // RPUSH 在列表右侧插入单个元素
        stringListOperations.rightPush("list1", "a");
        assertEquals(1, stringListOperations.size("list1"));
        // RPUSH 在列表右侧插入多个元素
        stringListOperations.rightPushAll("list1", "b", "c", "d");
        // stringListOperations.rightPushAll("list1", Arrays.asList("b", "c", "d"));
        assertEquals(4, stringListOperations.size("list1"));

        // RPUSHX 仅key存在时在列表右侧插值
        stringListOperations.rightPushIfPresent("list1", "e");
        assertEquals(5, stringListOperations.size("list1"));

        // LPUSH 在列表左侧插入单个元素
        stringListOperations.leftPush("list2", "a");
        assertEquals(1, stringListOperations.size("list2"));
        // LPUSH 在列表左侧插入多个元素
        stringListOperations.leftPushAll("list2", "b", "c");
        // stringListOperations.leftPushAll("list2", Arrays.asList("d", "e"));
        assertEquals(3, stringListOperations.size("list2"));

        // LPUSHX 仅key存在时在列表左侧插值
        stringListOperations.leftPushIfPresent("list2", "d");
        assertEquals(4, stringListOperations.size("list2"));

        // LINSERT 插入元素
        // 在指定元素左侧插入
        stringListOperations.leftPush("list1", "b", "hello");
        assertEquals("hello", stringListOperations.index("list1", 1));
        // 在指定元素右侧插入
        stringListOperations.rightPush("list2", "c", "world");
        assertEquals("world", stringListOperations.index("list2", 2));

        // 插入的类型需要相同，对应类型使用对应的Operations
        // 例如：int类型
        integerListOperations.rightPushAll("nums", 1, 2, 3);
        assertEquals(3, integerListOperations.size("nums"));

        // 例如：User类型
        User user = new User(1, "tom", new BigDecimal("165.3"));
        userListOperations.rightPush("users", user);
        User user2 = userListOperations.rightPop("users");
        assertEquals(user, user2);

        // 清空数据
        redisTemplate.delete(Arrays.asList("list1", "list2", "nums", "users"));
    }

    /**
     * RPOP 从列表末尾获取元素并删除
     * 
     * LPOP 从列表头部获取元素并删除
     */
    @Test
    @Order(3)
    void pop() {
        stringListOperations.rightPushAll("list", "a", "b", "c", "d");

        // RPOP 从列表末尾获取元素并删除
        String s1 = stringListOperations.rightPop("list");
        assertEquals("d", s1);

        // LPOP 从列表头部获取元素并删除
        String s2 = stringListOperations.leftPop("list");
        assertEquals("a", s2);

        // 清空数据
        redisTemplate.delete("list");
    }

    @Test
    @Order(4)
    void other() {
        Long size1 = stringListOperations.rightPushAll("list", "a", "b", "c", "d", "e");

        // LLEN 列表长度
        Long size2 = stringListOperations.size("list");
        assertEquals(size1, size2);

        // LINDEX 获取指定索引的值
        String first = stringListOperations.index("list", 0);
        assertEquals("a", first);

        // LSET 修改指定索引的值
        stringListOperations.set("list", 2, "world");
        assertEquals("world", stringListOperations.index("list", 2));

        // LREM 删除指定元素
        Long remove = stringListOperations.remove("list", 1, "b");
        assertEquals(1, remove);

        // LTRIM 裁剪列表
        stringListOperations.trim("list", 1, 2);
        assertEquals(2, stringListOperations.size("list"));

        // RPOPLPUSH 从source列表右边吐出一个值，插到destination列表左边
        String move = stringListOperations.rightPopAndLeftPush("list", "list2");
        assertEquals("d", move);
        assertEquals(1, stringListOperations.size("list"));
        assertEquals(1, stringListOperations.size("list2"));

        // 清空数据
        redisTemplate.delete(Arrays.asList("list", "list2"));
    }
}
