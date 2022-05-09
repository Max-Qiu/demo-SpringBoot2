package com.maxqiu.demo.normal.service;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.maxqiu.demo.normal.entity.User;
import com.maxqiu.demo.normal.mapper.UserMapper;

/**
 * 用户表 服务类
 *
 * @author Max_Qiu
 */
@Service
public class UserService extends ServiceImpl<UserMapper, User> {

}
