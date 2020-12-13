package com.maxqiu.demo.base;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.conditions.update.LambdaUpdateChainWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.maxqiu.demo.entity.User;
import com.maxqiu.demo.mapper.UserMapper;

/**
 * @author Max_Qiu
 */
@SpringBootTest
public class TestUserMapper {
    @Autowired
    private UserMapper userMapper;

    @Test
    void insert() {
        // INSERT INTO smp_user ( id, age ) VALUES ( ?, ? )
        User user = new User();
        // 若设置了ID，则会使用指定的ID
        // user.setId(123L);
        user.setAge(18);
        int insert = userMapper.insert(user);
        System.out.println(insert);
    }

    @Test
    void deleteById() {
        // DELETE FROM smp_user WHERE id=?
        int i = userMapper.deleteById(1);
        System.out.println(i);
    }

    @Test
    void deleteBatchIds() {
        // DELETE FROM smp_user WHERE id IN ( ? , ? , ? )
        int i = userMapper.deleteBatchIds(Arrays.asList("1", "2", "3"));
        System.out.println(i);
    }

    @Test
    void deleteByMap() {
        // DELETE FROM smp_user WHERE username = ? AND age = ?
        Map<String, Object> map = new HashMap<>();
        // 注：name为数据库字段名，不是实体变量名
        map.put("username", "max");
        map.put("age", 1);
        int users = userMapper.deleteByMap(map);
        System.out.println(users);
    }

    @Test
    void delete() {
        // DELETE FROM smp_user WHERE (age = ?)
        LambdaQueryWrapper<User> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(User::getAge, 18);
        int delete = userMapper.delete(wrapper);
        System.out.println(delete);
    }

    @Test
    void updateById() {
        // UPDATE smp_user SET username=?, email=? WHERE id=?
        User user = new User();
        user.setId(123L);
        user.setUsername("xxxxxx");
        user.setEmail("1111234@126.com");
        int i = userMapper.updateById(user);
        System.out.println("影响记录数：" + i);
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
        int i = userMapper.update(user, wrapper);
        System.out.println("影响记录数：" + i);

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
        int i = userMapper.update(user, wrapper);
        System.out.println("影响记录数：" + i);
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
        int i = userMapper.update(null, wrapper);
        System.out.println("影响记录数：" + i);
    }

    @Test
    void update4() {
        // UPDATE smp_user SET email=? WHERE (id = ?)
        boolean update = new LambdaUpdateChainWrapper<>(userMapper)
            // 构造WHERE条件
            .eq(User::getId, "123")
            // 构造 SET 值
            .set(User::getEmail, "456@126.com")
            // 执行
            .update();
        System.out.println("更新是否成功" + update);
    }

    @Test
    void selectById() {
        // SELECT id,username,age,email FROM smp_user WHERE id=?
        User user = userMapper.selectById(1);
        System.out.println(user);
    }

    @Test
    void selectBatchIds() {
        // SELECT id,username,age,email FROM smp_user WHERE id IN ( ? , ? )
        List<User> users = userMapper.selectBatchIds(Arrays.asList(1, 2, 3));
        users.forEach(System.out::println);
    }

    @Test
    void selectByMap() {
        // SELECT id,username,age,email FROM smp_user WHERE username = ? AND age = ?
        Map<String, Object> map = new HashMap<>();
        // 注：name为数据库字段名，不是实体变量名
        map.put("username", "max");
        map.put("age", 1);
        List<User> users = userMapper.selectByMap(map);
        users.forEach(System.out::println);
    }

    @Test
    void selectOne() {
        // SELECT id,username,age,email FROM smp_user WHERE (age > ? AND age < ?)
        LambdaQueryWrapper<User> queryWrapper = Wrappers.lambdaQuery();
        queryWrapper.gt(User::getAge, 50).lt(User::getAge, 100);
        // 慎用！！！除非保证查询后的结果只有一条或者为空，否则会抛出异常 Expected one result (or null) to be returned by selectOne(), but found: 5
        User user = userMapper.selectOne(queryWrapper);
        System.out.println(user);
    }

    @Test
    void selectCount() {
        // SELECT COUNT( * ) FROM smp_user WHERE (age > ? AND age < ?)
        LambdaQueryWrapper<User> queryWrapper = Wrappers.lambdaQuery();
        queryWrapper.gt(User::getAge, 0).lt(User::getAge, 100);
        int count = userMapper.selectCount(queryWrapper);
        System.out.println(count);
    }

    @Test
    void selectList() {
        // SELECT id,username,age,email FROM smp_user
        // 1. 如果有条件，则传一个 wrapper ；
        // 2. 如果无条件，可以直接传 null 或者 Wrappers.emptyWrapper()
        // List<User> users = userMapper.selectList(null);
        List<User> users = userMapper.selectList(Wrappers.emptyWrapper());
        users.forEach(System.out::println);
    }

    @Test
    void selectMap() {
        // SELECT id,username,age,email FROM smp_user
        List<Map<String, Object>> users = userMapper.selectMaps(Wrappers.emptyWrapper());
        users.forEach(System.out::println);
    }

    @Test
    void selectObjs() {
        // SELECT id,username,age,email FROM smp_user
        List<Object> users = userMapper.selectObjs(Wrappers.emptyWrapper());
        for (Object user : users) {
            System.out.println(user);
        }
    }

    @Test
    void selectPage() {
        // SELECT COUNT(*) FROM smp_user WHERE (email LIKE ?)
        // SELECT id,username,age,email FROM smp_user WHERE (email LIKE ?) LIMIT ?,?
        LambdaQueryWrapper<User> wrapper = Wrappers.lambdaQuery();
        wrapper.like(User::getEmail, "m");
        Page<User> page = new Page<>(2, 1);
        IPage<User> userPage = userMapper.selectPage(page, wrapper);
        System.out.println("总页数" + userPage.getPages());
        System.out.println("总记录数" + userPage.getTotal());
        List<User> users = userPage.getRecords();
        users.forEach(System.out::println);
    }

    @Test
    void selectMapPage() {
        // SELECT COUNT(*) FROM smp_user WHERE (email LIKE ?)
        // SELECT id,username,age,email FROM smp_user WHERE (email LIKE ?) LIMIT ?,?
        LambdaQueryWrapper<User> wrapper = Wrappers.lambdaQuery();
        wrapper.like(User::getEmail, "m");
        IPage<Map<String, Object>> page = new Page<>(2, 1);
        IPage<Map<String, Object>> userPage = userMapper.selectMapsPage(page, wrapper);
        System.out.println("总页数" + userPage.getPages());
        System.out.println("总记录数" + userPage.getTotal());
        List<Map<String, Object>> users = userPage.getRecords();
        users.forEach(System.out::println);
    }

    @Test
    void selectMapPage2() {
        // SELECT id,username,age,email FROM smp_user WHERE (email LIKE ?) LIMIT ?,?
        LambdaQueryWrapper<User> wrapper = Wrappers.lambdaQuery();
        wrapper.like(User::getEmail, "m");
        IPage<Map<String, Object>> page = new Page<>(2, 1, false);
        IPage<Map<String, Object>> userPage = userMapper.selectMapsPage(page, wrapper);
        System.out.println("总页数" + userPage.getPages());
        System.out.println("总记录数" + userPage.getTotal());
        List<Map<String, Object>> users = userPage.getRecords();
        users.forEach(System.out::println);
    }
}
