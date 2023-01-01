package com.maxqiu.demo.wrapper;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiPredicate;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.core.toolkit.support.SFunction;
import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.maxqiu.demo.entity.Student;
import com.maxqiu.demo.entity.User;
import com.maxqiu.demo.mapper.StudentMapper;
import com.maxqiu.demo.mapper.UserMapper;

/**
 * @author Max_Qiu
 */
@SpringBootTest
public class TestLambdaQueryWrapper {
    @Autowired
    private UserMapper userMapper;

    // 三种创建lambda查询构造器
    // 1. Wrappers 使用条件构造器（官方推荐）
    LambdaQueryWrapper<User> lambdaQueryWrapper1 = Wrappers.lambdaQuery();
    // 2. 直接 new LambdaQueryWrapper<>(); （官方不推荐，不信看源码）
    LambdaQueryWrapper<User> lambdaQueryWrapper2 = new LambdaQueryWrapper<>();
    // 3. 使用 new QueryWrapper<User>().lambda(); （太繁琐了，不推荐）
    LambdaQueryWrapper<User> lambdaQueryWrapper3 = new QueryWrapper<User>().lambda();

    // groupBy having 见 TestOther

    /**
     * 选择部分字段进行查询
     *
     * 仅 LambdaQueryWrapper 和 QueryWrapper 有 select() 方法
     */
    @Test
    void testSelect1() {
        // SELECT id,username FROM user
        LambdaQueryWrapper<User> queryWrapper = Wrappers.lambdaQuery();
        queryWrapper.select(User::getId, User::getUsername);
        List<User> users = userMapper.selectList(queryWrapper);
        users.forEach(System.out::println);
    }

    /**
     * 排查某些字段，其中，主键ID不可排除
     *
     * 仅 LambdaQueryWrapper 和 QueryWrapper 有 select() 方法
     */
    @Test
    void testSelect2() {
        // SELECT id,age,email FROM user
        LambdaQueryWrapper<User> queryWrapper = Wrappers.lambdaQuery();
        queryWrapper.select(User.class, tableFieldInfo -> !tableFieldInfo.getColumn().equals("username"));
        List<User> users = userMapper.selectList(queryWrapper);
        users.forEach(System.out::println);
    }

    /**
     * 使用 select 查询最大id
     */
    @Test
    public void testSelectMaxId() {
        QueryWrapper<User> wrapper = new QueryWrapper<>();
        wrapper.select("max(id) as id");
        User user = userMapper.selectOne(wrapper);
        System.out.println("maxId=" + user.getId());
    }

    /**
     * boolean condition
     *
     * 是否拼接该查询条件
     */
    @Test
    void testCondition() {
        // SELECT id,username,age,email FROM user WHERE (username LIKE ?)
        condition("m");
        // SELECT id,username,age,email FROM user
        condition("");
    }

    private void condition(String name) {
        LambdaQueryWrapper<User> queryWrapper = Wrappers.lambdaQuery();
        queryWrapper.like(StringUtils.isNotBlank(name), User::getUsername, name);
        List<User> users = userMapper.selectList(queryWrapper);
        users.forEach(System.out::println);
    }

    /**
     * eq 等于 xxx = ?
     *
     * ne 不等于 xxx <> ?
     */
    @Test
    void testEqNe() {
        // SELECT id,username,age,email FROM smp_user WHERE (age = ? AND email <> ?)
        LambdaQueryWrapper<User> queryWrapper = Wrappers.lambdaQuery();
        // 默认 . 之间用 and 连接
        queryWrapper.eq(User::getAge, 100).ne(User::getEmail, "123@123.com");
        List<User> users = userMapper.selectList(queryWrapper);
        users.forEach(System.out::println);
    }

    /**
     * ge 大于 xxx > ?
     *
     * gt 大于等于 xxx >= >
     *
     * lt 小于 xxx < ?
     *
     * le 小于等于 xxx <= ?
     */
    @Test
    void testGtGeLtLe() {
        // SELECT id,username,age,email FROM smp_user WHERE (age > ? AND age >= ? AND age < ? AND age <= ?)
        LambdaQueryWrapper<User> queryWrapper = Wrappers.lambdaQuery();
        queryWrapper.gt(User::getAge, 10).ge(User::getAge, 20).lt(User::getAge, 30).le(User::getAge, 40);
        List<User> users = userMapper.selectList(queryWrapper);
        users.forEach(System.out::println);
    }

    /**
     * between 在AB之间 xxx BETWEEN ? AND ?
     *
     * notBetween 在AB之外 xxx NOT BETWEEN ? AND ?
     */
    @Test
    void testBetweenNotBetween() {
        // SELECT id,username,age,email FROM smp_user WHERE (age BETWEEN ? AND ? AND age NOT BETWEEN ? AND ?)
        LambdaQueryWrapper<User> queryWrapper = Wrappers.lambdaQuery();
        queryWrapper.between(User::getAge, 10, 20).notBetween(User::getAge, 30, 40);
        List<User> users = userMapper.selectList(queryWrapper);
        users.forEach(System.out::println);
    }

    /**
     * like 存在 xxx LIKE ?
     *
     * notLike 不存在 xxx NOT LIKE ?
     *
     * likeLeft 开通匹配 xxx LIKE ?
     *
     * likeRight 结尾匹配 xxx LIKE ?
     *
     * MybatisPlus会自动添加 %
     */
    @Test
    void testLikeNotLikeLikeLeftLikeRight() {
        // SELECT id,username,age,email FROM smp_user
        // WHERE (username LIKE ? AND username NOT LIKE ? AND username LIKE ? AND username LIKE ?)
        // 详细看 Parameters: %m%(String), %m%(String), %m(String), m%(String)
        LambdaQueryWrapper<User> queryWrapper = Wrappers.lambdaQuery();
        queryWrapper.like(User::getUsername, "m").notLike(User::getUsername, "m").likeLeft(User::getUsername, "m").likeRight(User::getUsername, "m");
        List<User> users = userMapper.selectList(queryWrapper);
        users.forEach(System.out::println);
    }

    /**
     * isNull 是空 xxx IS NULL
     *
     * isNotNull 不是空 xxx IS NOT NULL
     */
    @Test
    void testIsNullIsNotNull() {
        // SELECT id,username,age,email FROM smp_user WHERE (age IS NULL AND username IS NOT NULL)
        LambdaQueryWrapper<User> queryWrapper = Wrappers.lambdaQuery();
        queryWrapper.isNull(User::getAge).isNotNull(User::getUsername);
        List<User> users = userMapper.selectList(queryWrapper);
        users.forEach(System.out::println);
    }

    /**
     * in 包含 xxx IN (?,?,?)
     *
     * notIn 不包含 xxx NOT IN (?,?,?)
     */
    @Test
    void testInNotIn() {
        // SELECT id,username,age,email FROM smp_user WHERE (age IN (?,?,?) AND id NOT IN (?,?,?))
        LambdaQueryWrapper<User> queryWrapper = Wrappers.lambdaQuery();
        queryWrapper.in(User::getAge, 18, 19, 20).notIn(User::getId, 1, 2, 3);
        List<User> users = userMapper.selectList(queryWrapper);
        users.forEach(System.out::println);
    }

    /**
     * in 包含 xxx age IN (sql)
     *
     * notIn 不包含 xxx NOT IN (sql)
     */
    @Test
    void testInSqlNotInSql() {
        // SELECT id,username,age,email FROM smp_user WHERE
        // (
        // id IN (select id from smp_user where username like 'q%')
        // AND age NOT IN (select age from smp_user where username like 'xxx%')
        // )
        LambdaQueryWrapper<User> queryWrapper = Wrappers.lambdaQuery();
        queryWrapper.inSql(User::getId, "select id from smp_user where username like 'q%'").notInSql(User::getAge,
            "select age from smp_user where username like 'xxx%'");
        List<User> users = userMapper.selectList(queryWrapper);
        users.forEach(System.out::println);
    }

    @Autowired
    private StudentMapper studentMapper;

    /**
     * exists 存在 EXISTS
     *
     * notExists 不存在 NOT EXISTS
     */
    @Test
    void testExistsNotExists() {
        // SELECT id,`name`,classes_id,create_time FROM smp_student WHERE (
        // EXISTS (SELECT * FROM `smp_classes` WHERE `classes_id` = `id` AND `name` = '一班')
        // AND NOT EXISTS (SELECT * FROM `smp_classes` WHERE `classes_id` = `id` AND `name` = '一班')
        // )
        LambdaQueryWrapper<Student> queryWrapper = Wrappers.lambdaQuery();
        queryWrapper.exists("SELECT * FROM `smp_classes` WHERE `classes_id` = `id` AND `name` = '一班'");
        queryWrapper.notExists("SELECT * FROM `smp_classes` WHERE `classes_id` = `id` AND `name` = '一班'");
        List<Student> users = studentMapper.selectList(queryWrapper);
        users.forEach(System.out::println);
    }

    /**
     * orderBy 排序
     *
     * orderByAsc 正序 xxx ASC
     *
     * orderByDesc 倒序 xxx DESC
     */
    @Test
    void testOrder() {
        // SELECT id,username,age,email FROM smp_user ORDER BY
        // id ASC,
        // age ASC,
        // id ASC,
        // email DESC
        LambdaQueryWrapper<User> queryWrapper = Wrappers.lambdaQuery();
        // 排序（可以多个字段）
        // noinspection unchecked
        queryWrapper.orderBy(true, true, User::getId, User::getAge);
        // 正序（可以多个字段）
        queryWrapper.orderByAsc(User::getId);
        // 倒序（可以多个字段）
        queryWrapper.orderByDesc(User::getEmail);
        List<User> users = userMapper.selectList(queryWrapper);
        users.forEach(System.out::println);
    }

    /**
     * and 且
     *
     * or 或者
     */
    @Test
    void testAndOr() {
        // SELECT id,username,age,email FROM smp_user WHERE (
        // (age = ? AND id = ?)
        // OR (age = ? OR id = ?)
        // OR age = ?
        // AND age = ?
        // )
        LambdaQueryWrapper<User> queryWrapper = Wrappers.lambdaQuery();
        // (age = ? AND id = ?)
        queryWrapper.and(userLambdaQueryWrapper -> userLambdaQueryWrapper.eq(User::getAge, 18).eq(User::getId, 1));
        // OR (age = ? OR id = ?)
        queryWrapper.or(userLambdaQueryWrapper -> userLambdaQueryWrapper.eq(User::getAge, 18).or().eq(User::getId, 1));
        // OR age = ?
        queryWrapper.or().eq(User::getAge, 18);
        // AND age = ?
        queryWrapper.or(false).eq(User::getAge, 18);
        List<User> users = userMapper.selectList(queryWrapper);
        users.forEach(System.out::println);
    }

    /**
     * 拼接SQL
     */
    @Test
    void testLast() {
        // SELECT id,username,age,email FROM user limit 1
        LambdaQueryWrapper<User> queryWrapper = Wrappers.lambdaQuery();
        queryWrapper.last("limit 1");
        List<User> users = userMapper.selectList(queryWrapper);
        users.forEach(System.out::println);
    }

    /**
     * 所有条件使用 = 判断
     */
    @Test
    void testAllEq() {
        // SELECT id,username,age,email FROM smp_user WHERE (username = ? AND age = ?)
        Map<SFunction<User, ?>, Object> map = new HashMap<>();
        map.put(User::getUsername, "max");
        map.put(User::getAge, 1);
        LambdaQueryWrapper<User> queryWrapper = Wrappers.lambdaQuery();
        queryWrapper.allEq(map);
        List<User> users = userMapper.selectList(queryWrapper);
        // 等同selectByMap
        // List<User> users = userMapper.selectByMap(map);
        users.forEach(System.out::println);
    }

    /**
     * 所有条件使用 = 判断
     *
     * 且忽略空值
     */
    @Test
    void testAllEqWithNullCheck() {
        // SELECT id,username,age,email FROM smp_user WHERE (username = ?)
        Map<SFunction<User, ?>, Object> map = new HashMap<>();
        map.put(User::getUsername, "max");
        map.put(User::getAge, null);
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        // 为true则在map的value为null时调用 isNull 方法,为false时则忽略value为null的条件
        queryWrapper.allEq(map, true);
        List<User> users = userMapper.selectList(queryWrapper);
        users.forEach(System.out::println);
    }

    @Test
    void testAllEq2() {
        // SELECT id,username,age,email FROM user WHERE (age IS NULL)
        Map<SFunction<User, ?>, Object> map = new HashMap<>();
        map.put(User::getUsername, "max");
        map.put(User::getAge, null);
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.allEq(new BiPredicate<SFunction<User, ?>, Object>() {
            // TODO SFunction 暂时不会
            @Override
            public boolean test(SFunction<User, ?> s, Object o) {
                return true;
            }
        }, map);
        List<User> users = userMapper.selectList(queryWrapper);
        users.forEach(System.out::println);
    }

    @Test
    void testAllEq3() {
        // SELECT id,username,age,email FROM user WHERE (username = ?)
        Map<SFunction<User, ?>, Object> map = new HashMap<>();
        map.put(User::getUsername, "max");
        map.put(User::getAge, 18);
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        // 注意：value比较时，需要注意不能为空
        queryWrapper.allEq((s, o) -> o.equals("max"), map);
        List<User> users = userMapper.selectList(queryWrapper);
        users.forEach(System.out::println);
    }

    /**
     * 嵌套
     */
    @Test
    public void testNested() {
        // SELECT id,username,age,email FROM smp_user
        // WHERE (
        // (age = ? OR age = ?)
        // AND (age >= ?)
        // )
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.nested(i -> i.eq(User::getAge, 2L).or().eq(User::getAge, 3L));
        queryWrapper.and(i -> i.ge(User::getAge, 20));
        List<User> users = userMapper.selectList(queryWrapper);
        users.forEach(System.out::println);
    }

    /**
     * 自定义SQL
     */
    @Test
    public void testApply() {
        // SELECT id,username,age,email FROM smp_user WHERE (age > ?)
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.apply("age > {0}", "1");
        List<User> users = userMapper.selectList(queryWrapper);
        users.forEach(System.out::println);
    }

    @Test
    public void testFunc() {
        // SELECT id,username,age,email FROM smp_user WHERE (age = ? AND age = ? AND username = ?)
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.func(true, userLambdaQueryWrapper -> userLambdaQueryWrapper.eq(User::getAge, 18));
        queryWrapper.func(true, userLambdaQueryWrapper -> userLambdaQueryWrapper.eq(User::getAge, 18).eq(User::getUsername, "max"));
        List<User> users = userMapper.selectList(queryWrapper);
        users.forEach(System.out::println);
    }

    /**
     * 链式写法，
     */
    @Test
    void testLambdaQueryChainWrapper() {
        // SELECT id,username,age,email FROM smp_user WHERE (age = ?)
        List<User> list = new LambdaQueryChainWrapper<>(userMapper).eq(User::getAge, 18).list();
        list.forEach(System.out::println);

        // SELECT COUNT(*) FROM smp_user WHERE (age = ?)
        // SELECT id,username,age,email,create_time FROM smp_user WHERE (age = ?) LIMIT ?
        Page<User> page = new LambdaQueryChainWrapper<>(userMapper).eq(User::getAge, 18).page(new Page<>(1, 10));
        System.out.println(page.getTotal());

        // SELECT COUNT( * ) FROM smp_user WHERE (age = ?)
        long count = new LambdaQueryChainWrapper<>(userMapper).eq(User::getAge, 18).count();
        System.out.println(count);

        // SELECT id,username,age,email,create_time FROM smp_user WHERE (age = ?)
        List<User> one = new LambdaQueryChainWrapper<>(userMapper).eq(User::getAge, 18).list();
        System.out.println(one);
    }
}
