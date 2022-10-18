package com.maxqiu.demo.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.maxqiu.demo.entity.Permission;

/**
 * 权限表 Mapper 接口
 *
 * @author Max_Qiu
 */
public interface PermissionMapper extends BaseMapper<Permission> {
    List<Permission> getByRoleIds(@Param("roleIds") List<Long> roleIds);
}
