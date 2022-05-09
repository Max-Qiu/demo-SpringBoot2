package com.maxqiu.demo;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.maxqiu.demo.entity.User;
import com.maxqiu.demo.mapper.UserMapper;

/**
 * 多库多表 水平分表
 *
 * @author Max_Qiu
 */
@SpringBootTest
public class UserMapper2Test {
    @Autowired
    private UserMapper userMapper;

    @Test
    void addUser() {
        for (int i = 0; i < 10; i++) {
            User user = new User();
            user.setUsername("TOM" + i);
            user.setGroupId(i % 2);
            user.setEmail(i + "xxx@xxx.com");
            userMapper.insert(user);
        }
    }

    @Test
    void getUser1() {
        // 直接根据id查找，会在所有库中执行查找，直至找到
        User user = userMapper.selectById(1522869889913196547L);
        System.out.println(user);
    }

    @Test
    void getUser2() {
        LambdaQueryWrapper<User> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(User::getId, 1522869889913196547L);
        // 设置组之后，会在指定库中查找
        wrapper.eq(User::getGroupId, 1);
        User user1 = userMapper.selectOne(wrapper);
        System.out.println(user1);
    }
}
