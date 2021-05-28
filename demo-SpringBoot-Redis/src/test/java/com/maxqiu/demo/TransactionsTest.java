package com.maxqiu.demo;

import java.util.Arrays;
import java.util.List;

import javax.annotation.Resource;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SessionCallback;
import org.springframework.data.redis.core.ValueOperations;

/**
 * 事务
 * 
 * @author Max_Qiu
 */
@SpringBootTest
public class TransactionsTest {
    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    /**
     * 值是数字类型的字符串
     *
     * 自定义的RedisTemplate可以格式化Integer
     */
    @Resource(name = "redisTemplate")
    private ValueOperations<String, Integer> valueOperations;

    @Test
    void test() {
        valueOperations.set("a", 100);
        valueOperations.set("b", 100);
        // 乐观锁
        // redisOperations.watch("a");
        List<Long> execute = redisTemplate.execute(new SessionCallback<>() {
            @Override
            public List<Long> execute(RedisOperations operations) throws DataAccessException {
                operations.multi();
                ValueOperations<String, Long> valueOperations = operations.opsForValue();
                valueOperations.increment("a", 10);
                valueOperations.decrement("b", 10);
                return operations.exec();
            }
        });
        Assertions.assertEquals(110, execute.get(0));
        Assertions.assertEquals(90, execute.get(1));

        // 删除数据
        redisTemplate.delete(Arrays.asList("a", "b"));
    }
}
