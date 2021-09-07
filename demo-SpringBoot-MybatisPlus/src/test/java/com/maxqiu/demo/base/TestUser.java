package com.maxqiu.demo.base;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.maxqiu.demo.entity.User;

/**
 * 测试 Entity 层 / 测试 AR 模式
 *
 * @author Max_Qiu
 */
@SpringBootTest
public class TestUser {
    @Test
    void insert() {
        // INSERT INTO smp_user ( id, username, age ) VALUES ( ?, ?, ? )
        User user = new User();
        // 若设置了ID，则会使用指定的ID
        // user.setId(123L);
        user.setUsername("sssssssss");
        user.setAge(188);
        boolean insert = user.insert();
        System.out.println(insert);
    }

    @Test
    void insertOrUpdate() {
        // 先查询有没有，没有则插入
        // SELECT id,username,age,email FROM smp_user WHERE id=?
        // INSERT INTO smp_user ( id, username ) VALUES ( ?, ? )
        // 先查询有没有，有则更新
        // SELECT id,username,age,email FROM smp_user WHERE id=?
        // UPDATE smp_user SET username=? WHERE id=?
        User user = new User();
        user.setId(1113L);
        user.setUsername("你好啊");
        boolean b = user.insertOrUpdate();
        System.out.println(b);
    }

    @Test
    void deleteById1() {
        // DELETE FROM smp_user WHERE id=?
        boolean b = new User().deleteById(123L);
        System.out.println(b);
    }

    @Test
    void deleteById2() {
        // DELETE FROM smp_user WHERE id=?
        User user = new User();
        user.setId(123L);
        boolean b = user.deleteById();
        System.out.println(b);
    }

    @Test
    void delete() {
        // DELETE FROM smp_user WHERE (username = ?)
        LambdaQueryWrapper<User> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(User::getUsername, "max");
        boolean delete = new User().delete(wrapper);
        System.out.println(delete);
    }

    @Test
    void updateById() {
        // UPDATE smp_user SET username=? WHERE id=?
        User user = new User();
        user.setId(123L);
        user.setUsername("11111111111");
        boolean b = user.updateById();
        System.out.println(b);
    }

    @Test
    void update1() {
        // UPDATE smp_user SET email=? WHERE (username = ?)
        // 构造WHERE条件
        LambdaUpdateWrapper<User> wrapper = new LambdaUpdateWrapper<>();
        wrapper.eq(User::getUsername, "xxxxxx");
        // 构造 SET 值
        User user = new User();
        user.setEmail("123@126.com");
        boolean i = user.update(wrapper);
        System.out.println("更新结果：" + i);

    }

    @Test
    void update2() {
        // UPDATE smp_user SET email=? WHERE username=? AND (username = ?)
        // 构造WHERE条件
        User whereUser = new User();
        whereUser.setUsername("xxxxxx");
        LambdaUpdateWrapper<User> wrapper = new LambdaUpdateWrapper<>(whereUser);
        wrapper.eq(User::getUsername, "xxxxxx");
        // 构造 SET 值
        User user = new User();
        user.setEmail("123@126.com");
        boolean i = user.update(wrapper);
        System.out.println("更新结果：" + i);
    }

    @Test
    void update3() {
        // UPDATE smp_user SET age=? WHERE (username = ?)
        LambdaUpdateWrapper<User> wrapper = new LambdaUpdateWrapper<>();
        wrapper
            // 构造WHERE条件
            .eq(User::getUsername, "xxxxxx")
            // 构造 SET 值
            .set(User::getAge, 88);
        boolean i = new User().update(wrapper);
        System.out.println("更新结果：" + i);
    }

    @Test
    void selectAll() {
        // SELECT id,username,age,email FROM smp_user
        List<User> users = new User().selectAll();
        users.forEach(System.out::println);
    }

    @Test
    void selectById1() {
        // SELECT id,username,age,email FROM smp_user WHERE id=?
        User user = new User();
        user.setId(123L);
        User newUser = user.selectById();
        System.out.println(newUser);
        System.out.println(user.equals(newUser));
    }

    @Test
    void selectById2() {
        // SELECT id,username,age,email FROM smp_user WHERE id=?
        User user = new User();
        User newUser = user.selectById(123L);
        System.out.println(newUser);
        System.out.println(user.equals(newUser));
    }

    @Test
    void selectOne() {
        // SELECT id,username,age,email FROM smp_user WHERE (age > ? AND age < ?)
        LambdaQueryWrapper<User> queryWrapper = Wrappers.lambdaQuery();
        queryWrapper.gt(User::getAge, 50).lt(User::getAge, 100);
        // 慎用！！！除非保证查询后的结果只有一条或者为空，否则会抛出异常 Expected one result (or null) to be returned by selectOne(), but found: 5
        User user = new User().selectOne(queryWrapper);
        System.out.println(user);
    }

    @Test
    void selectCount() {
        // SELECT COUNT( * ) FROM smp_user WHERE (age > ? AND age < ?)
        LambdaQueryWrapper<User> queryWrapper = Wrappers.lambdaQuery();
        queryWrapper.gt(User::getAge, 0).lt(User::getAge, 100);
        long count = new User().selectCount(queryWrapper);
        System.out.println(count);
    }

    @Test
    void selectList() {
        // SELECT id,username,age,email FROM smp_user
        // 1. 如果有条件，则传一个 wrapper ；
        // 2. 如果无条件，可以直接传 null 或者 Wrappers.emptyWrapper()
        // List<User> users = userMapper.selectList(null);
        List<User> users = new User().selectList(Wrappers.emptyWrapper());
        users.forEach(System.out::println);
    }

    @Test
    void selectPage() {
        // SELECT COUNT(*) FROM smp_user WHERE (email LIKE ?)
        // SELECT id,username,age,email FROM smp_user WHERE (email LIKE ?) LIMIT ?,?
        LambdaQueryWrapper<User> wrapper = Wrappers.lambdaQuery();
        wrapper.like(User::getEmail, "m");
        Page<User> page = new Page<>(2, 1);
        IPage<User> userPage = new User().selectPage(page, wrapper);
        System.out.println("总页数" + userPage.getPages());
        System.out.println("总记录数" + userPage.getTotal());
        List<User> users = userPage.getRecords();
        users.forEach(System.out::println);
    }
}
