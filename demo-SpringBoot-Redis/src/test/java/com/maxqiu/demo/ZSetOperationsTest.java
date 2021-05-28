package com.maxqiu.demo;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.HashSet;
import java.util.Set;

import javax.annotation.Resource;

import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.DefaultTypedTuple;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.core.ZSetOperations;

/**
 * ZSet 有序集合（Sorted Sets）
 * 
 * @author Max_Qiu
 */
@SpringBootTest
public class ZSetOperationsTest {
    @Autowired
    private RedisOperations<String, Object> redisOperations;

    /**
     * 值是String类型的有序集合
     *
     * 自定义的RedisTemplate在格式化String时会多出引号
     *
     * 需要使用Spring内置的StringRedisTemplate
     */
    @Resource(name = "stringRedisTemplate")
    private ZSetOperations<String, String> stringZSetOperations;

    /**
     * ZADD 将一个或多个member及source添加到有序集合中
     */
    @Test
    @Order(1)
    void add() {
        Boolean flag = stringZSetOperations.add("zset", "maths", 99);
        assertTrue(flag);

        Set<ZSetOperations.TypedTuple<String>> set = new HashSet<>();
        set.add(new DefaultTypedTuple<>("chinese", 90.0));
        set.add(new DefaultTypedTuple<>("english", 60.0));
        Long add = stringZSetOperations.add("zset", set);
        assertEquals(2, add);

        // 删除数据
        redisOperations.delete("zset");
    }

    /**
     * 根据指定规则排序取出
     */
    @Test
    @Order(2)
    void get() {
        // 准备数据
        Set<ZSetOperations.TypedTuple<String>> set = new HashSet<>();
        set.add(new DefaultTypedTuple<>("chinese", 115.0));
        set.add(new DefaultTypedTuple<>("maths", 135.5));
        set.add(new DefaultTypedTuple<>("english", 110.5));
        set.add(new DefaultTypedTuple<>("physics", 88.0));
        set.add(new DefaultTypedTuple<>("chemistry", 65.0));
        stringZSetOperations.add("zset", set);

        /// 正序
        // ZRANGE 返回排序集合中的指定范围的元素（正序，从小到大）
        Set<String> zset = stringZSetOperations.range("zset", 0, -1);
        System.out.println(zset);

        // ZRANGEBYSCORE 返回指定分数内的元素（正序，从小到大）
        Set<String> zset3 = stringZSetOperations.rangeByScore("zset", 10, 100);
        System.out.println(zset3);

        // ZRANGEBYSCORE 返回指定分数内的元素（正序，从小到大）（LIMIT 分页）
        Set<String> zset5 = stringZSetOperations.rangeByScore("zset", 0, 200, 1, 2);
        System.out.println(zset5);

        // ZRANGE xxxWithScores 返回时包含分数（正序，从小到大）其他同
        Set<ZSetOperations.TypedTuple<String>> zset2 = stringZSetOperations.rangeWithScores("zset", 0, -1);
        if (zset2 != null) {
            for (ZSetOperations.TypedTuple<String> stringTypedTuple : zset2) {
                System.out.println(stringTypedTuple.getValue() + " " + stringTypedTuple.getScore());
            }
        }

        /// 倒序
        // ZREVRANGE 返回排序集合中的指定范围的元素（倒序，从大到小）
        stringZSetOperations.reverseRange("zset", 0, -1);
        // ZREVRANGEBYSCORE 返回指定分数内的元素（倒序，从大到小）
        stringZSetOperations.reverseRangeByScore("zset", 10, 100);
        // ZREVRANGEBYSCORE 返回指定分数内的元素（倒序，从大到小）（LIMIT 分页）
        stringZSetOperations.reverseRangeByScore("zset", 0, 200, 1, 2);
        // ZREVRANGE xxxWithScores 返回时包含分数（倒序，从大到小）其他同
        stringZSetOperations.reverseRangeWithScores("zset", 0, -1);

        // 删除数据
        redisOperations.delete("zset");
    }

    @Test
    @Order(3)
    void other() {
        // 准备数据
        Set<ZSetOperations.TypedTuple<String>> set = new HashSet<>();
        set.add(new DefaultTypedTuple<>("chinese", 115.0));
        set.add(new DefaultTypedTuple<>("maths", 135.5));
        set.add(new DefaultTypedTuple<>("english", 110.5));
        set.add(new DefaultTypedTuple<>("physics", 88.0));
        set.add(new DefaultTypedTuple<>("chemistry", 65.0));
        stringZSetOperations.add("zset", set);

        // ZSCORE 返回排序集中元素的分数
        Double score = stringZSetOperations.score("zset", "chinese");
        assertEquals(115.0, score);

        // 查询元素在有序集合中的排名
        // ZRANK
        Long rank = stringZSetOperations.rank("zset", "chinese");
        assertEquals(3, rank);
        // ZREVRANK
        Long reverseRank = stringZSetOperations.reverseRank("zset", "chinese");
        assertEquals(1, reverseRank);

        // ZCARD key 返回排序集合的元素数量
        Long size = stringZSetOperations.size("zset");
        assertEquals(5, size);

        // ZCOUNT 统计指定范围内的元素数量
        Long count = stringZSetOperations.count("zset", 0, 100);
        assertEquals(2, count);

        // ZINCRBY 给指定元素增减分数
        Double incrementScore = stringZSetOperations.incrementScore("zset", "english", 18.5);
        assertEquals(129.0, incrementScore);

        // ZREM 删除元素
        Long remove = stringZSetOperations.remove("zset", "chinese", "english");
        assertEquals(2, remove);

        // 删除数据
        redisOperations.delete("zset");
    }
}
