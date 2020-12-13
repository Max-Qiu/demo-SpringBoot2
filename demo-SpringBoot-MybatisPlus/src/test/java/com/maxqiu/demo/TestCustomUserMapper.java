package com.maxqiu.demo;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.maxqiu.demo.entity.User;
import com.maxqiu.demo.mapper.UserMapper;

/**
 * @author Max_Qiu
 */
@SpringBootTest
public class TestCustomUserMapper {

    @Autowired
    private UserMapper userMapper;

    @Test
    void customSql() {
        // select * from user WHERE (username LIKE ?)
        LambdaQueryWrapper<User> wrapper = Wrappers.lambdaQuery();
        wrapper.like(User::getUsername, "m");
        List<User> users = userMapper.selectAll(wrapper);
        users.forEach(System.out::println);
    }

    @Test
    void selectMyPage() {
        // SELECT COUNT(*) FROM smp_user WHERE (email LIKE ?)
        // select * from user WHERE (email LIKE ?) LIMIT ?
        LambdaQueryWrapper<User> wrapper = Wrappers.lambdaQuery();
        wrapper.like(User::getEmail, "m");
        Page<User> page = new Page<>(1, 3);
        Page<User> userPage = userMapper.selectUserPage(page, wrapper);
        System.out.println("总页数" + userPage.getPages());
        System.out.println("总记录数" + userPage.getTotal());
        List<User> users = userPage.getRecords();
        users.forEach(System.out::println);
    }
}
