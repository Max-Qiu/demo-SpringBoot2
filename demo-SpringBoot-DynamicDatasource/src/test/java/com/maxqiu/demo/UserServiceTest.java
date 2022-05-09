package com.maxqiu.demo;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.baomidou.dynamic.datasource.toolkit.DynamicDataSourceContextHolder;
import com.maxqiu.demo.normal.service.UserService;

/**
 * @author Max_Qiu
 */
@SpringBootTest
public class UserServiceTest {
    @Autowired
    private UserService userService;

    @Test
    void list() {
        DynamicDataSourceContextHolder.push("a");
        userService.list().forEach(System.out::println);
        DynamicDataSourceContextHolder.push("b");
        userService.list().forEach(System.out::println);

        DynamicDataSourceContextHolder.clear();
    }
}
