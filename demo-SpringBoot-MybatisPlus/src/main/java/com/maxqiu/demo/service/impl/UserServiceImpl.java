package com.maxqiu.demo.service.impl;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.maxqiu.demo.entity.User;
import com.maxqiu.demo.mapper.UserMapper;
import com.maxqiu.demo.service.IUserService;

/**
 * 用户表 服务实现类
 *
 * @author Max_Qiu
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements IUserService {

}
