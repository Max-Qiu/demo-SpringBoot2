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
import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.baomidou.mybatisplus.extension.conditions.update.LambdaUpdateChainWrapper;
import com.maxqiu.demo.entity.User;
import com.maxqiu.demo.mapper.UserMapper;

/**
 * 条件构造器创建方法
 * 
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
        // 如果不使用对象接收直接链式...写法，可以使用下面这种写法设置泛型
        Wrappers.<User>lambdaQuery();
        LambdaQueryWrapper<User> lambdaQueryWrapper3 = Wrappers.lambdaQuery(User.class);
        LambdaQueryWrapper<User> lambdaQueryWrapper4 = Wrappers.lambdaQuery(new User());
        // 创建一个支持Lambda语法且支持链式写法的查询构造器
        LambdaQueryChainWrapper<User> lambdaQueryChainWrapper = new LambdaQueryChainWrapper<>(userMapper);

        UpdateWrapper<User> updateWrapper1 = Wrappers.update();
        UpdateWrapper<User> updateWrapper2 = Wrappers.update(new User());
        LambdaUpdateWrapper<User> lambdaUpdateWrapper1 = Wrappers.lambdaUpdate();
        LambdaUpdateWrapper<User> lambdaUpdateWrapper2 = Wrappers.lambdaUpdate(new User());
        LambdaUpdateWrapper<User> lambdaUpdateWrapper3 = Wrappers.lambdaUpdate(User.class);
        LambdaUpdateChainWrapper<User> lambdaUpdateChainWrapper = new LambdaUpdateChainWrapper<>(userMapper);
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
     * 此时 传入实体 作为查询条件
     */
    @Test
    void queryWithEntity() {
        // 相同字段的条件查询均会添加进SQL
        // 前一个username=?是实体生成的查询条件
        // 后一个username = ?是eq设置的查询条件
        // 实例传入Wrapper时，默认比较条件是相等，实体字段设置 condition = SqlCondition.xxx 可以进行修改
        // 比如：添加 condition = SqlCondition.LIKE 则查询时为 username LIKE CONCAT('%',?,'%')
        // SELECT id,username,age,email FROM smp_user WHERE username=? AND (username = ?)
        User user = new User();
        user.setUsername("max");
        LambdaQueryWrapper<User> lambdaQueryWrapper = Wrappers.lambdaQuery(user);
        lambdaQueryWrapper.eq(User::getUsername, "tom");
        List<User> list = userMapper.selectList(lambdaQueryWrapper);
        System.out.println(list);
    }

    /**
     * 无参 不指定泛型 创建一个 LambdaUpdateWrapper
     *
     * 此时需要手动指定泛型
     */
    @Test
    void update() {
        // UPDATE smp_user SET age=? WHERE (id = ?)
        User user = new User().setAge(18);
        LambdaUpdateWrapper<User> updateWrapper = Wrappers.lambdaUpdate();
        updateWrapper.eq(User::getId, 1);
        int update = userMapper.update(user, updateWrapper);
        System.out.println(update);
    }

    /**
     * 带参 指定泛型 创建一个 LambdaQueryWrapper
     */
    @Test
    void updateWithClass() {
        // UPDATE smp_user SET age=? WHERE (id = ?)
        User user = new User().setAge(18);
        LambdaUpdateWrapper<User> updateWrapper = Wrappers.lambdaUpdate(User.class);
        updateWrapper.eq(User::getId, 1);
        int update = userMapper.update(user, updateWrapper);
        System.out.println(update);
    }

    /**
     * 带参 传入实体 创建一个 LambdaQueryWrapper
     *
     * 此时 传入实体 作为查询条件
     */
    @Test
    void updateWithEntity() {
        // 相同字段的条件查询均会添加进SQL
        // 前一个username=?是实体生成的查询条件
        // 后一个username = ?是eq设置的查询条件
        // 实例传入Wrapper时，默认比较条件是相等，实体字段设置 condition = SqlCondition.xxx 可以进行修改
        // 比如：添加 condition = SqlCondition.LIKE 则查询时为 username LIKE CONCAT('%',?,'%')
        // UPDATE smp_user SET age=? WHERE username=? AND (username = ?)
        LambdaUpdateWrapper<User> updateWrapper = Wrappers.lambdaUpdate(new User().setUsername("max"));
        updateWrapper.eq(User::getUsername, "max");
        // SET 条件通过实体传入
        int update = userMapper.update(new User().setAge(18), updateWrapper);
        System.out.println(update);
    }

}
