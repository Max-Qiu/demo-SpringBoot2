package com.maxqiu.demo.other;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.mybatis.spring.MyBatisSystemException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.maxqiu.demo.entity.User;
import com.maxqiu.demo.mapper.UserMapper;

/**
 * @author Max_Qiu
 */
@SpringBootTest
public class TestOther {
    @Autowired
    private UserMapper userMapper;

    /**
     * 操作全表
     * 
     * 插件内添加 interceptor.addInnerInterceptor(new BlockAttackInnerInterceptor());
     * 
     * 可在遇到全表操作时，抛出异常
     */
    @Test
    void testOperationAllTable() {
        userMapper.selectList(new QueryWrapper<>());
        userMapper.deleteById(1L);
        userMapper.insert(new User().setAge(18).setEmail("13"));
        User user = new User();
        user.setUsername("test_update");
        userMapper.update(user, new QueryWrapper<User>().eq("id", 1L));
        try {
            userMapper.update(new User().setAge(18), new QueryWrapper<>());
        } catch (MyBatisSystemException e) {
            System.out.println("发现全表更新");
        }
        try {
            userMapper.delete(new QueryWrapper<>());
        } catch (MyBatisSystemException e) {
            System.out.println("发现全表删除");
        }
        List<User> list = userMapper.selectList(new QueryWrapper<>());
        if (list.size() == 0) {
            System.out.println("数据都被删掉了");
        } else {
            System.out.println("数据还在");
        }
    }
}
