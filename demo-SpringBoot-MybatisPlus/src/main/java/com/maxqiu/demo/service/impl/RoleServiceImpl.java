package com.maxqiu.demo.service.impl;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.maxqiu.demo.entity.Role;
import com.maxqiu.demo.mapper.RoleMapper;
import com.maxqiu.demo.service.IRoleService;

/**
 * 角色表 服务实现类
 *
 * @author Max_Qiu
 */
@Service
public class RoleServiceImpl extends ServiceImpl<RoleMapper, Role> implements IRoleService {

}
