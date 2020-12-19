package com.maxqiu.demo.other;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.RowBounds;
import org.assertj.core.util.Maps;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.maxqiu.demo.entity.User;
import com.maxqiu.demo.mapper.UserMapper;

/**
 * 测试 自定义SQL
 * 
 * @author Max_Qiu
 */
@SpringBootTest
public class TestCustomUserMapper {

    @Autowired
    private UserMapper userMapper;

    @Test
    void rowBoundsTest() {
        // TODO 待完善
        // select * from smp_user WHERE username like concat('%',?,'%')
        RowBounds rowBounds = new RowBounds(1, 5);
        List<User> users = userMapper.rowBoundList(rowBounds, Maps.newHashMap("username", "%"));
        users.forEach(System.out::println);
    }

    /**
     * 自己实现的 selectByMap
     */
    @Test
    void selectMap() {
        // select * from smp_user WHERE username like concat('%',?,'%')
        Map<String, Object> name = Maps.newHashMap("username", "%a");
        userMapper.selectMap(name).forEach(System.out::println);
    }

    /**
     * 使用 @Select 注解编写SQL
     */
    @Test
    void selectByAtSelect() {
        // select * from smp_user WHERE (username LIKE ?)
        LambdaQueryWrapper<User> wrapper = Wrappers.lambdaQuery();
        wrapper.like(User::getUsername, "m");
        List<User> users = userMapper.selectByAtSelect(wrapper);
        users.forEach(System.out::println);
    }

    /**
     * 自定义 xml 传入 wrapper
     */
    @Test
    void selectListByXml() {
        // select * from smp_user WHERE (username LIKE ?)
        LambdaQueryWrapper<User> wrapper = Wrappers.lambdaQuery();
        wrapper.like(User::getUsername, "m");
        List<User> users = userMapper.selectListByXml(wrapper);
        users.forEach(System.out::println);
    }

    /**
     * 自定义 xml 传入 wrapper 且分页
     */
    @Test
    void selectPageByXml() {
        // SELECT COUNT(*) FROM smp_user WHERE (email LIKE ?)
        // select * from smp_user WHERE (email LIKE ?) LIMIT ?
        LambdaQueryWrapper<User> wrapper = Wrappers.lambdaQuery();
        wrapper.like(User::getEmail, "m");
        Page<User> page = new Page<>(1, 3);
        Page<User> userPage = userMapper.selectPageByXml(page, wrapper);
        System.out.println("总页数" + userPage.getPages());
        System.out.println("总记录数" + userPage.getTotal());
        List<User> users = userPage.getRecords();
        users.forEach(System.out::println);
    }
}
