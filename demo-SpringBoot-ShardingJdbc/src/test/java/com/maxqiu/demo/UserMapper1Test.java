package com.maxqiu.demo;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.maxqiu.demo.entity.User;
import com.maxqiu.demo.mapper.UserMapper;

/**
 * 单库多表 水平分表
 *
 * @author Max_Qiu
 */
@SpringBootTest
public class UserMapper1Test {
    @Autowired
    private UserMapper userMapper;

    @Test
    void addUser() {
        for (int i = 0; i < 10; i++) {
            User user = new User();
            user.setUsername("TOM" + i);
            user.setEmail(i + "xxx@xxx.com");
            userMapper.insert(user);
        }
    }

    @Test
    void getUser() {
        User user = userMapper.selectById(1522866999970672643L);
        System.out.println(user);
    }
}
