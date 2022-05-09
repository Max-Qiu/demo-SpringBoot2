package com.maxqiu.demo.normal.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.maxqiu.demo.normal.entity.User;
import com.maxqiu.demo.normal.service.UserService;

/**
 * 用户表 前端控制器
 *
 * @author Max_Qiu
 */
@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserService userService;

    /**
     * 根据id获取用户
     */
    @GetMapping("get/{id}")
    public User get(@PathVariable Integer id) {
        return userService.getById(id);
    }
}
