package com.maxqiu.demo.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.maxqiu.demo.entity.User;

/**
 * 用户表 Mapper 接口
 *
 * @author Max_Qiu
 */
public interface UserMapper extends BaseMapper<User> {
    // 自定义查询语句
    // @Select("select * from user ${ew.customSqlSegment}")
    // List<User> selectAll(@Param(Constants.WRAPPER) Wrapper<User> wrapper);

    // 使用自定义XML配置文件
    List<User> selectAll(@Param(Constants.WRAPPER) Wrapper<User> wrapper);

    Page<User> selectUserPage(Page<User> page, @Param(Constants.WRAPPER) Wrapper<User> wrapper);
}
