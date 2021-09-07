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
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.maxqiu.demo.entity.User;
import com.maxqiu.demo.mapper.UserMapper;

/**
 * 测试 DAO 层
 *
 * @author Max_Qiu
 */
@SpringBootTest
public class TestUserMapper {
    @Autowired
    private UserMapper userMapper;

    /**
     * 插入
     */
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

    /**
     * 根据id删除
     */
    @Test
    void deleteById() {
        // DELETE FROM smp_user WHERE id=?
        int i = userMapper.deleteById(1);
        System.out.println(i);
    }

    /**
     * 根据id批量删除
     */
    @Test
    void deleteBatchIds() {
        // DELETE FROM smp_user WHERE id IN ( ? , ? , ? )
        int i = userMapper.deleteBatchIds(Arrays.asList("1", "2", "3"));
        System.out.println(i);
    }

    /**
     * 条件删除
     */
    @Test
    void delete() {
        // DELETE FROM smp_user WHERE (age = ?)
        LambdaQueryWrapper<User> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(User::getAge, 18);
        int delete = userMapper.delete(wrapper);
        System.out.println(delete);
    }

    /**
     * 条件删除（条件为map对象）
     */
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

    /**
     * 根据id更新
     */
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

    /**
     * 条件更新
     */
    @Test
    void update() {
        // UPDATE smp_user SET email=? WHERE (username = ?)
        // 构造WHERE条件
        LambdaUpdateWrapper<User> wrapper = new LambdaUpdateWrapper<>();
        wrapper.eq(User::getUsername, "xxxxxx");
        // 构造 SET 值
        User user = new User().setEmail("123@126.com");
        int i = userMapper.update(user, wrapper);
        System.out.println("影响记录数：" + i);

    }

    /**
     * 根据id查找一个
     */
    @Test
    void selectById() {
        // SELECT id,username,age,email FROM smp_user WHERE id=?
        User user = userMapper.selectById(1);
        System.out.println(user);
    }

    /**
     * 根据条件查找一个
     */
    @Test
    void selectOne() {
        // SELECT id,username,age,email FROM smp_user WHERE (age > ? AND age < ?)
        LambdaQueryWrapper<User> queryWrapper = Wrappers.lambdaQuery();
        queryWrapper.gt(User::getAge, 50).lt(User::getAge, 100);
        // 慎用！！！除非保证查询后的结果只有一条或者为空，否则会抛出异常 Expected one result (or null) to be returned by selectOne(), but found: 5
        User user = userMapper.selectOne(queryWrapper);
        System.out.println(user);
    }

    /**
     * 根据id批量查找
     */
    @Test
    void selectBatchIds() {
        // SELECT id,username,age,email FROM smp_user WHERE id IN ( ? , ? )
        List<User> users = userMapper.selectBatchIds(Arrays.asList(1, 2, 3));
        users.forEach(System.out::println);
    }

    /**
     * 条件查询
     */
    @Test
    void selectList() {
        // SELECT id,username,age,email FROM smp_user
        // 1. 如果有条件，则传一个 wrapper ；
        // 2. 如果无条件，可以直接传 null 或者 Wrappers.emptyWrapper()
        // List<User> users = userMapper.selectList(null);
        List<User> users = userMapper.selectList(Wrappers.emptyWrapper());
        users.forEach(System.out::println);
    }

    /**
     * 条件查询（条件为map）
     */
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

    /**
     * 统计查询
     */
    @Test
    void selectCount() {
        // SELECT COUNT( * ) FROM smp_user WHERE (age > ? AND age < ?)
        LambdaQueryWrapper<User> queryWrapper = Wrappers.lambdaQuery();
        queryWrapper.gt(User::getAge, 0).lt(User::getAge, 100);
        long count = userMapper.selectCount(queryWrapper);
        System.out.println(count);
    }

    /**
     * 条件查询（返回为map）
     */
    @Test
    void selectMap() {
        // SELECT id,username,age,email FROM smp_user
        List<Map<String, Object>> users = userMapper.selectMaps(Wrappers.emptyWrapper());
        users.forEach(System.out::println);
    }

    /**
     * 条件查询（返回为Object）
     */
    @Test
    void selectObjs() {
        // SELECT id,username,age,email FROM smp_user
        List<Object> users = userMapper.selectObjs(Wrappers.emptyWrapper());
        for (Object user : users) {
            System.out.println(user);
        }
    }

    /**
     * 分页查询
     *
     * 更多分页示例参照 TestPage
     */
    @Test
    void selectPage() {
        // SELECT COUNT(*) FROM smp_user WHERE (email LIKE ?)
        // SELECT id,username,age,email FROM smp_user WHERE (email LIKE ?) LIMIT ?,?
        LambdaQueryWrapper<User> wrapper = Wrappers.lambdaQuery();
        wrapper.like(User::getEmail, "m");
        Page<User> page = new Page<>(2, 2);
        // 直接传入，无需接收返回值，返回值自动存入 page
        userMapper.selectPage(page, wrapper);
        System.out.println("总页数" + page.getPages());
        System.out.println("总记录数" + page.getTotal());
        List<User> users = page.getRecords();
        users.forEach(System.out::println);
    }

    /**
     * 分页查询（返回map）
     */
    @Test
    void selectMapPage() {
        // SELECT COUNT(*) FROM smp_user WHERE (email LIKE ?)
        // SELECT id,username,age,email FROM smp_user WHERE (email LIKE ?) LIMIT ?,?
        LambdaQueryWrapper<User> wrapper = Wrappers.lambdaQuery();
        wrapper.like(User::getEmail, "m");
        IPage<Map<String, Object>> page = new Page<>(1, 2);
        userMapper.selectMapsPage(page, wrapper);
        System.out.println("总页数" + page.getPages());
        System.out.println("总记录数" + page.getTotal());
        List<Map<String, Object>> users = page.getRecords();
        users.forEach(System.out::println);
    }
}
