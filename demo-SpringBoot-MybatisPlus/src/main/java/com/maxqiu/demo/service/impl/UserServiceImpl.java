package com.maxqiu.demo.service.impl;

import com.maxqiu.demo.entity.User;
import com.maxqiu.demo.mapper.UserMapper;
import com.maxqiu.demo.service.IUserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author Max_Qiu
 * @since 2020-12-08
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements IUserService {

}
