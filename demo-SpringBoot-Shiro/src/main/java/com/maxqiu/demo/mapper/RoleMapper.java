package com.maxqiu.demo.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.maxqiu.demo.entity.Role;

/**
 * 角色表 Mapper 接口
 *
 * @author Max_Qiu
 */
public interface RoleMapper extends BaseMapper<Role> {
    List<Role> getByUserId(@Param("userId") Long userId);
}
