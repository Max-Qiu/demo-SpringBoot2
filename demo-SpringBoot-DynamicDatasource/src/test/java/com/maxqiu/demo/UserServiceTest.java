package com.maxqiu.demo;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.baomidou.dynamic.datasource.toolkit.DynamicDataSourceContextHolder;
import com.maxqiu.demo.normal.service.UserService;
import com.maxqiu.demo.system.service.DbInfoService;

/**
 * @author Max_Qiu
 */
@SpringBootTest
public class UserServiceTest {
    @Autowired
    private UserService userService;

    @Autowired
    private DbInfoService dbInfoService;

    @Test
    void list() {
        // 动态源 a
        DynamicDataSourceContextHolder.push("a");
        userService.list().forEach(System.out::println);
        userService.list().forEach(System.out::println);
        userService.list().forEach(System.out::println);

        // 静态源
        dbInfoService.list().forEach(System.out::println);

        // 动态源 b
        DynamicDataSourceContextHolder.push("b");
        userService.list().forEach(System.out::println);
        userService.list().forEach(System.out::println);
        userService.list().forEach(System.out::println);

        // 结束清空当前线程
        DynamicDataSourceContextHolder.clear();
    }
}
