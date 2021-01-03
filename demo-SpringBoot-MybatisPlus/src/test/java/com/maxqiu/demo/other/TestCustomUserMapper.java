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

    /**
     * MyBatis 自带分页（逻辑分页）
     */
    @Test
    void rowBoundsTest() {
        // 设置分页条件，offset 代表第 i+1 条数据，limit 代表取几条。如下示例中，取第二条数据，共取 5 条
        RowBounds rowBounds = new RowBounds(1, 5);
        // select * from smp_user WHERE username like concat('%',?,'%')
        List<User> users = userMapper.rowBoundList(rowBounds, Maps.newHashMap("username", "%"));
        // 虽然 select * 查出了 2 条数据，但是集合中仅一条数据，因为分页设置从第二条数据开始取
        System.out.println(users.size());
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
