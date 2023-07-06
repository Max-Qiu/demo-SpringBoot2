package com.maxqiu.demo.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.maxqiu.demo.entity.Role;

/**
 * 角色表 Mapper 接口
 *
 * @author Max_Qiu
 */
public interface RoleMapper extends BaseMapper<Role> {
    List<Role> getRoleAndPermissionIds();

    List<Role> getRoleAndPermissionList();

    Page<Role> getRoleAndPermissionIds(@Param("page") Page<Role> page);

    Page<Role> getRoleAndPermissionList(@Param("page") Page<Role> page);

    Page<Role> getRoleAndPermissionPage(@Param("page") Page<Role> page);
}
