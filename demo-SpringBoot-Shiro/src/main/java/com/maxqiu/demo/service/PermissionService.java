package com.maxqiu.demo.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.maxqiu.demo.entity.Permission;
import com.maxqiu.demo.mapper.PermissionMapper;

/**
 * 权限表 服务类
 *
 * @author Max_Qiu
 */
@Service
public class PermissionService extends ServiceImpl<PermissionMapper, Permission> {
    public List<Permission> getByRoleIds(List<Long> roleIds) {
        if (roleIds.size() == 0) {
            return new ArrayList<>();
        }
        return baseMapper.getByRoleIds(roleIds);
    }
}
