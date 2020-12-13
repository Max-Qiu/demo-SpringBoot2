package com.maxqiu.demo.wrapper;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.maxqiu.demo.entity.User;
import com.maxqiu.demo.mapper.UserMapper;

/**
 * @author Max_Qiu
 */
@SpringBootTest
public class TestWrappers {

    @Autowired
    private UserMapper userMapper;

    @Test
    void testWrapper() {
        // 创建一个空条件的查询构造器（其实用不到，条件为空的话可以直接传null，比如 userMapper.selectList(null)）
        QueryWrapper<User> emptyWrapper = Wrappers.emptyWrapper();

        // 创建一个查询构造器（推荐用Lambda，毕竟queryWrapper.eq("username", "max")这种写String的方式看起来不咋地）
        QueryWrapper<User> queryWrapper1 = Wrappers.query();
        QueryWrapper<User> queryWrapper2 = Wrappers.query(new User());
        // 创建一个支持Lambda语法查询构造器（推荐，详细区别见下方代码）
        LambdaQueryWrapper<User> lambdaQueryWrapper1 = Wrappers.lambdaQuery();
        LambdaQueryWrapper<User> lambdaQueryWrapper3 = Wrappers.lambdaQuery(User.class);
        LambdaQueryWrapper<User> lambdaQueryWrapper2 = Wrappers.lambdaQuery(new User());

        UpdateWrapper<User> updateWrapper1 = Wrappers.update();
        UpdateWrapper<User> updateWrapper2 = Wrappers.update(new User());
        LambdaUpdateWrapper<User> lambdaUpdateWrapper1 = Wrappers.lambdaUpdate();
        LambdaUpdateWrapper<User> lambdaUpdateWrapper2 = Wrappers.lambdaUpdate(new User());
        LambdaUpdateWrapper<User> lambdaUpdateWrapper3 = Wrappers.lambdaUpdate(User.class);
    }

    /**
     * 无参 不指定泛型 创建一个 LambdaQueryWrapper
     * 
     * 此时需要手动指定泛型
     */
    @Test
    void query() {
        LambdaQueryWrapper<User> lambdaQueryWrapper = Wrappers.lambdaQuery();
        lambdaQueryWrapper.eq(User::getAge, 18);
        List<User> list = userMapper.selectList(lambdaQueryWrapper);
        System.out.println(list);
    }

    /**
     * 带参 指定泛型 创建一个 LambdaQueryWrapper
     */
    @Test
    void queryWithClass() {
        LambdaQueryWrapper<User> lambdaQueryWrapper = Wrappers.lambdaQuery(User.class);
        lambdaQueryWrapper.eq(User::getAge, 18);
        List<User> list = userMapper.selectList(lambdaQueryWrapper);
        System.out.println(list);
    }

    /**
     * 带参 传入实体 创建一个 LambdaQueryWrapper
     * 
     * 此时实体作为查询条件
     */
    @Test
    void queryWithEntity1() {
        // 相同字段的条件查询均会添加进SQL
        // 前一个username=?是实体生成的查询条件
        // 后一个username = ?是eq设置的查询条件
        // 实例类默认比较条件是相等，字段设置 condition = SqlCondition.xxx 可以进行修改
        // 比如：添加 condition = SqlCondition.LIKE 则查询时为 username LIKE CONCAT('%',?,'%')
        // SELECT id,username,age,email FROM smp_user WHERE username=? AND (username = ?)
        User user = new User();
        user.setUsername("max");
        LambdaQueryWrapper<User> lambdaQueryWrapper = Wrappers.lambdaQuery(user);
        lambdaQueryWrapper.eq(User::getUsername, "tom");
        List<User> list = userMapper.selectList(lambdaQueryWrapper);
        System.out.println(list);
    }

}
