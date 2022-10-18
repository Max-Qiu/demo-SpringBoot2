package com.maxqiu.demo.service;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.maxqiu.demo.entity.User;
import com.maxqiu.demo.mapper.UserMapper;

/**
 * 用户表 服务类
 *
 * @author Max_Qiu
 */
@Service
public class UserService extends ServiceImpl<UserMapper, User> {
    /**
     * 根据名称查找用户
     */
    public User getByName(String name) {
        LambdaQueryWrapper<User> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(User::getName, name);
        return getOne(wrapper);
    }
}
