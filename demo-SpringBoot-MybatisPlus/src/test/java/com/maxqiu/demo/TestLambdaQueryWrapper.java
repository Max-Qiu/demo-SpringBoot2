package com.maxqiu.demo;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.maxqiu.demo.entity.User;
import com.maxqiu.demo.mapper.UserMapper;

/**
 * @author Max_Qiu
 */
@SpringBootTest
public class TestLambdaQueryWrapper {

    @Autowired
    private UserMapper userMapper;

    // 三种创建lambda查询构造器
    // LambdaQueryWrapper<User> lambdaQueryWrapper1 = new LambdaQueryWrapper<>();
    // LambdaQueryWrapper<User> lambdaQueryWrapper2 = Wrappers.lambdaQuery();
    // LambdaQueryWrapper<User> lambdaQueryWrapper3 = new QueryWrapper<User>().lambda();

    @Test
    public void select1() {
        // SELECT id,username,age,email FROM user WHERE (name LIKE ? AND age < ?)
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.like(User::getUsername, "m").lt(User::getAge, 100);
        List<User> users = userMapper.selectList(queryWrapper);
        users.forEach(System.out::println);
    }

    @Test
    public void select2() {
        // SELECT id,username,age,email FROM user WHERE (username LIKE ? AND age BETWEEN ? AND ? AND email IS NOT NULL)
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.like(User::getUsername, "m").between(User::getAge, 0, 100).isNotNull(User::getEmail);
        List<User> users = userMapper.selectList(queryWrapper);
        users.forEach(System.out::println);
    }

    @Test
    public void select3() {
        // SELECT id,username,age,email FROM user
        // WHERE (username LIKE ? OR age < ?)
        // ORDER BY age DESC,id ASC
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.likeRight(User::getUsername, "m").or().lt(User::getAge, 100).orderByDesc(User::getAge)
            .orderByAsc(User::getId);
        List<User> users = userMapper.selectList(queryWrapper);
        users.forEach(System.out::println);
    }

    @Test
    public void select4() {
        // SELECT id,username,age,email FROM user
        // WHERE (age >= ? AND email IN
        // (select id from user where username like 'q%')
        // )
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.apply("age >= {0}", "1 or true or true").inSql(User::getEmail,
            "select id from user where username like 'q%'");
        List<User> users = userMapper.selectList(queryWrapper);
        users.forEach(System.out::println);
    }

    @Test
    public void select5() {
        // SELECT id,username,age,email FROM user
        // WHERE (username LIKE ? AND
        // (age < ? OR email IS NOT NULL)
        // )
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.likeRight(User::getUsername, "m")
            .and(wrapper -> wrapper.lt(User::getAge, 100).or().isNotNull(User::getEmail));
        List<User> users = userMapper.selectList(queryWrapper);
        users.forEach(System.out::println);
    }

    @Test
    public void select6() {
        // SELECT id,username,age,email FROM user
        // WHERE (username LIKE ? OR
        // (age < ? AND email IS NOT NULL)
        // )
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.likeRight(User::getUsername, "m")
            .or(userQueryWrapper -> userQueryWrapper.lt(User::getAge, 100).isNotNull(User::getEmail));
        List<User> users = userMapper.selectList(queryWrapper);
        users.forEach(System.out::println);
    }

    @Test
    public void select7() {
        // SELECT id,username,age,email FROM user WHERE (age IN (?,?,?))
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.in(User::getAge, 1, 2, 3);
        List<User> users = userMapper.selectList(queryWrapper);
        users.forEach(System.out::println);
    }

    @Test
    public void select8() {
        // SELECT id,username,age,email FROM user limit 1
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.last("limit 1");
        List<User> users = userMapper.selectList(queryWrapper);
        users.forEach(System.out::println);
    }

    @Test
    public void choseSelect() {
        // SELECT id,username FROM user
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.select(User::getId, User::getUsername);
        List<User> users = userMapper.selectList(queryWrapper);
        users.forEach(System.out::println);
    }

    @Test
    public void choseSelect2() {
        // 排查某些字段，其中，主键ID不可排除
        // SELECT id,age,email FROM user
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.select(User.class, tableFieldInfo -> !tableFieldInfo.getColumn().equals("username"));
        List<User> users = userMapper.selectList(queryWrapper);
        users.forEach(System.out::println);
    }

    @Test
    public void testCondition() {
        condition("m");
        condition("");
    }

    private void condition(String name) {
        // SELECT id,username,age,email FROM user WHERE (username LIKE ?)
        // SELECT id,username,age,email FROM user
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.like(StringUtils.isNotBlank(name), User::getUsername, name);
        List<User> users = userMapper.selectList(queryWrapper);
        users.forEach(System.out::println);
    }

    @Test
    public void selectByWrapperAndEntity() {
        // 实例类默认比较条件是相等，需要进行设置
        // SELECT id,username,age,email FROM user
        // WHERE name LIKE CONCAT(?,'%') AND (age < ?)
        User user = new User();
        user.setUsername("m");
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>(user);
        queryWrapper.lt(User::getAge, 100);
        List<User> users = userMapper.selectList(queryWrapper);
        users.forEach(System.out::println);
    }

    @Test
    public void selectByWrapperAllEq() {
        // TODO
        // SELECT id,username,age,email FROM user WHERE (age = ? AND username = ?)
        Map<String, Object> map = new HashMap<>();
        map.put("username", "max");
        map.put("age", 1);
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        // queryWrapper.allEq
        List<User> users = userMapper.selectList(queryWrapper);
        // 等同selectByMap
        // List<User> users = userMapper.selectByMap(map);
        users.forEach(System.out::println);
    }

    @Test
    public void selectByWrapperAllEq2() {
        // SELECT id,username,age,email FROM user WHERE (username = ? AND age IS NULL)
        Map<String, Object> map = new HashMap<>();
        map.put("username", "max");
        map.put("age", null);
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        // queryWrapper.allEq(map);
        List<User> users = userMapper.selectList(queryWrapper);
        users.forEach(System.out::println);
    }

    @Test
    public void selectByWrapperAllEq3() {
        // SELECT id,username,age,email FROM user WHERE (username = ?)
        Map<String, Object> map = new HashMap<>();
        map.put("username", "max");
        map.put("age", null);
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.allEq(map, false);
        List<User> users = userMapper.selectList(queryWrapper);
        users.forEach(System.out::println);
    }

    @Test
    public void selectByWrapperAllEq4() {
        // SELECT id,username,age,email FROM user WHERE (age IS NULL)
        Map<String, Object> map = new HashMap<>();
        map.put("username", "max");
        map.put("age", null);
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.allEq((s, o) -> !s.equals("username"), map);
        List<User> users = userMapper.selectList(queryWrapper);
        users.forEach(System.out::println);
    }

    @Test
    public void selectByWrapperAllEq5() {
        // SELECT id,username,age,email FROM user WHERE (username = ?)
        Map<String, Object> map = new HashMap<>();
        map.put("username", "max");
        map.put("age", 18);
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        // 注意：value比较时，需要注意不能为空
        queryWrapper.allEq((s, o) -> o.equals("max"), map);
        List<User> users = userMapper.selectList(queryWrapper);
        users.forEach(System.out::println);
    }

    @Test
    public void selectMapsByWrapper() {
        // SELECT id,username,age,email FROM user WHERE (username = ?)
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.lt("age", 100);
        List<Map<String, Object>> users = userMapper.selectMaps(queryWrapper);
        users.forEach(System.out::println);
    }

    @Test
    public void selectMapsByWrapper2() {
        // SELECT pid,avg(age) avg_age,min(age) min_age,max(age) max_age
        // FROM user GROUP BY pid HAVING sum(age) < ?
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.select("pid", "avg(age) avg_age", "min(age) min_age", "max(age) max_age").groupBy("pid")
            .having("sum(age) < {0}", 500);
        List<Map<String, Object>> users = userMapper.selectMaps(queryWrapper);
        users.forEach(System.out::println);
    }

    @Test
    public void selectObjsByWrapper() {
        // SELECT id,username,age,email FROM user WHERE (age < ?)
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.lt("age", 100);
        // 仅返回第一列
        List<Object> users = userMapper.selectObjs(queryWrapper);
        users.forEach(System.out::println);
    }

    @Test
    public void selectLambda() {
        // SELECT id,username,age,email FROM user WHERE (name LIKE ?)
        LambdaQueryWrapper<User> lambdaQueryWrapper = Wrappers.lambdaQuery();
        lambdaQueryWrapper.like(User::getUsername, "m");
        List<User> users = userMapper.selectList(lambdaQueryWrapper);
        users.forEach(System.out::println);
    }

    @Test
    public void selectLambda2() {
        // SELECT id,username,age,email FROM user WHERE (name LIKE ?)
        List<User> users = new LambdaQueryChainWrapper<>(userMapper).like(User::getUsername, "m").list();
        users.forEach(System.out::println);
    }
}
