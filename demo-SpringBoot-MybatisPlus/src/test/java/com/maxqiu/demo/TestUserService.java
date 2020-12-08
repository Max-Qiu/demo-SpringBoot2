package com.maxqiu.demo;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.maxqiu.demo.entity.User;
import com.maxqiu.demo.service.IUserService;

/**
 * @author Max_Qiu
 */
@SpringBootTest
public class TestUserService {

    @Autowired
    private IUserService userService;

    @Test
    public void getOne() {
        // SELECT id,name,age,email,pid,createtime FROM user WHERE (age > ?)
        // false 查出多个时，仅返回第一个，并日志记录警告
        User one = userService.getOne(Wrappers.<User>lambdaQuery().gt(User::getAge, 25), false);
        System.out.println(one);
    }
}
