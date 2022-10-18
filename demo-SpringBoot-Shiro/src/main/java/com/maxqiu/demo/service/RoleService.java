package com.maxqiu.demo.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.maxqiu.demo.entity.Role;
import com.maxqiu.demo.mapper.RoleMapper;

/**
 * 角色表 服务类
 *
 * @author Max_Qiu
 */
@Service
public class RoleService extends ServiceImpl<RoleMapper, Role> {
    public List<Role> getByUserId(Long userId) {
        return baseMapper.getByUserId(userId);
    }
}
