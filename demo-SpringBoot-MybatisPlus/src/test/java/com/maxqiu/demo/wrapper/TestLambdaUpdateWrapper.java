package com.maxqiu.demo.wrapper;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.conditions.update.LambdaUpdateChainWrapper;
import com.maxqiu.demo.entity.User;
import com.maxqiu.demo.mapper.UserMapper;

/**
 * @author Max_Qiu
 */
@SpringBootTest
public class TestLambdaUpdateWrapper {

    @Autowired
    private UserMapper userMapper;

    // 三种创建lambda查询构造器
    // 1. Wrappers 使用条件构造器（官方推荐）
    LambdaUpdateWrapper<User> lambdaQueryWrapper1 = Wrappers.lambdaUpdate();
    // 2. 直接 new LambdaQueryWrapper<>(); （官方不推荐，不信看源码）
    LambdaUpdateWrapper<User> lambdaQueryWrapper2 = new LambdaUpdateWrapper<>();
    // 3. 使用 new QueryWrapper<User>().lambda(); （太繁琐了，不推荐）
    LambdaUpdateWrapper<User> lambdaQueryWrapper3 = new UpdateWrapper<User>().lambda();

    /**
     * LambdaUpdateWrapper 仅多出 set 和 setSql 方法
     */
    @Test
    void testSetSetSql() {
        // UPDATE smp_user SET email=?, age=?,username = 'max' WHERE (id = ?)
        LambdaUpdateWrapper<User> updateWrapper = Wrappers.lambdaUpdate();
        // .set 方式
        updateWrapper.set(User::getAge, 18);
        // .setSql 方式
        updateWrapper.setSql("username = 'max'");
        // 设置 where条件
        updateWrapper.eq(User::getId, 1);
        // 实体对象内的属性也会被作为 set 条件
        User user = new User().setEmail("123@123.com");
        // 当 updateWrapper 有 set 时，也可以 userMapper.update(null,updateWrapper) 即生成的SQL中必须有 SET
        int update = userMapper.update(user, updateWrapper);
        System.out.println(update);
    }

    /**
     * 链式写法，
     */
    @Test
    void testLambdaQueryChainWrapper() {
        // UPDATE smp_user SET email=? WHERE (age = ?)
        boolean update1 = new LambdaUpdateChainWrapper<>(userMapper)
            // 构造WHERE条件
            .eq(User::getAge, 18)
            // 执行 同时 传入 SET 值
            .update(new User().setEmail("123@123.com"));
        System.out.println(update1);

        // UPDATE smp_user SET email=? WHERE (id = ?)
        boolean update2 = new LambdaUpdateChainWrapper<>(userMapper)
            // 构造WHERE条件
            .eq(User::getId, "123")
            // 构造 SET 值
            .set(User::getEmail, "456@126.com")
            // 执行
            .update();
        System.out.println(update2);

        // DELETE FROM smp_user WHERE (age = ?)
        boolean remove = new LambdaUpdateChainWrapper<>(userMapper).eq(User::getAge, 18).remove();
        System.out.println(remove);
    }
}
