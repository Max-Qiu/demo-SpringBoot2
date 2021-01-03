package com.maxqiu.demo.service.impl;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.maxqiu.demo.entity.Permission;
import com.maxqiu.demo.mapper.PermissionMapper;
import com.maxqiu.demo.service.IPermissionService;

/**
 * 权限表 服务实现类
 *
 * @author Max_Qiu
 */
@Service
public class PermissionServiceImpl extends ServiceImpl<PermissionMapper, Permission> implements IPermissionService {

}
