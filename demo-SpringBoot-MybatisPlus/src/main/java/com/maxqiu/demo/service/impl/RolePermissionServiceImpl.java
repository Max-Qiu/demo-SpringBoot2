package com.maxqiu.demo.service.impl;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.maxqiu.demo.entity.RolePermission;
import com.maxqiu.demo.mapper.RolePermissionMapper;
import com.maxqiu.demo.service.IRolePermissionService;

/**
 * 角色权限关联表 服务实现类
 *
 * @author Max_Qiu
 */
@Service
public class RolePermissionServiceImpl extends ServiceImpl<RolePermissionMapper, RolePermission> implements IRolePermissionService {

}
