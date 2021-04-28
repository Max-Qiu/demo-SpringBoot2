package com.maxqiu.demo.other;

import java.util.List;
import java.util.Map;

import org.assertj.core.util.Maps;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.maxqiu.demo.entity.User;
import com.maxqiu.demo.mapper.ClassesMapper;
import com.maxqiu.demo.mapper.UserMapper;
import com.maxqiu.demo.model.ClassesStudent;
import com.maxqiu.demo.model.MyPage;
import com.maxqiu.demo.model.ParamSome;

/**
 * @author Max_Qiu
 */
@SpringBootTest
public class TestPage {
    @Autowired
    private UserMapper userMapper;

    /**
     * 分页查询 并设置排序
     */
    @Test
    void selectPage() {
        // SELECT COUNT(*) FROM smp_user WHERE (email LIKE ?)
        // SELECT id, username, age, email FROM smp_user WHERE (email LIKE ?) ORDER BY age DESC, age ASC LIMIT ?,?
        LambdaQueryWrapper<User> wrapper = Wrappers.lambdaQuery();
        wrapper.like(User::getEmail, "m");
        wrapper.orderByDesc(User::getAge);
        Page<User> page = new Page<>(2, 2);
        // page 也可以设置排序，但是 wrapper 的优先级更高
        page.addOrder(OrderItem.asc("age"));
        // 直接传入，无需接收返回值，返回值自动存入 page
        userMapper.selectPage(page, wrapper);
        System.out.println("总页数" + page.getPages());
        System.out.println("总记录数" + page.getTotal());
        List<User> users = page.getRecords();
        users.forEach(System.out::println);
    }

    /**
     * 分页查询（仅分页，不进行 count 查询）
     */
    @Test
    void selectPageWithoutCount() {
        // SELECT id,username,age,email FROM smp_user WHERE (email LIKE ?) LIMIT ?,?
        LambdaQueryWrapper<User> wrapper = Wrappers.lambdaQuery();
        wrapper.like(User::getEmail, "m");
        // false 参数，设置不进行统计
        Page<User> page = new Page<>(2, 2, false);
        userMapper.selectPage(page, wrapper);
        System.out.println("总页数" + page.getPages());
        System.out.println("总记录数" + page.getTotal());
        List<User> users = page.getRecords();
        users.forEach(System.out::println);
    }

    /**
     * 分页查询
     */
    @Test
    void selectPageWithoutTotal() {
        // SELECT id,username,age,email FROM smp_user WHERE (email LIKE ?) LIMIT ?,?
        LambdaQueryWrapper<User> wrapper = Wrappers.lambdaQuery();
        wrapper.like(User::getEmail, "m");
        // TODO 不知道这个 total 参数干嘛的
        Page<User> page = new Page<>(1, 2, 1000);
        userMapper.selectPage(page, wrapper);
        System.out.println("总页数" + page.getPages());
        System.out.println("总记录数" + page.getTotal());
        List<User> users = page.getRecords();
        users.forEach(System.out::println);
    }

    /**
     * new Page 自定义分页对象
     * 
     * 相当于在 Page 对象中传入了参数
     */
    @Test
    void newPage() {
        // SELECT COUNT(*) FROM smp_user WHERE username LIKE ?
        // select * from smp_user WHERE username like ? LIMIT ?
        // new Page<User> 中的 User 泛型不能省略
        Page<User> page = new Page<User>(1, 5) {
            private static final long serialVersionUID = -9106255080131670213L;
            private String username = "%";

            public String getUsername() {
                return username;
            }

            public void setUsername(String username) {
                this.username = username;
            }
        };

        List<User> list = userMapper.myPage(page);
        System.out.println("list.size=" + list.size());
        System.out.println("page.total=" + page.getTotal());
    }

    /**
     * MyPage 继续 Page
     * 
     * 实现自定义分页
     * 
     * 使用 MyPage 对象传参
     */
    @Test
    void myPage() {
        // SELECT COUNT(*) FROM smp_user WHERE username LIKE concat('%', ?, '%')
        // select * from smp_user WHERE username like concat('%',?,'%') LIMIT ?
        MyPage<User> page = new MyPage<>(1, 5);
        page.setUsername("a");
        userMapper.myPage(page).forEach(System.out::println);
    }

    /**
     * MyPage 继续 Page
     * 
     * 实现自定义分页
     * 
     * 使用 map 对象传参
     */
    @Test
    void testMyPageMap() {
        // SELECT COUNT(*) FROM smp_user WHERE username LIKE concat('%', ?, '%')
        // select * from smp_user WHERE username like concat('%',?,'%') LIMIT ?
        MyPage<User> page = new MyPage<>(1, 5);
        Map<String, Object> map = Maps.newHashMap("username", "%a");
        userMapper.myPageMap(page, map);
        page.getRecords().forEach(System.out::println);
    }

    /**
     * 自定义 page 对象
     */
    @Test
    void testMyPage() {
        // SELECT COUNT(*) FROM smp_user WHERE (age = ? AND username = ?) OR (age = ? AND username = ?)
        // select * from smp_user where (age = ? and username = ?) or (age = ? and username = ?) LIMIT ?
        MyPage<User> myPage = new MyPage<>(1, 5);
        myPage.setSelectInt(20);
        myPage.setSelectStr("Jack");
        ParamSome paramSome = new ParamSome(20, "Jack");
        MyPage<User> userMyPage = userMapper.mySelectPage(myPage, paramSome);
        System.out.println(userMyPage.getTotal());
    }

    @Autowired
    private ClassesMapper classesMapper;

    /**
     * 分页优化
     * 
     * 生成 countSql 会在 left join 的表不参与 where 条件的情况下,把 left join 优化掉
     * 
     * 所以建议任何带有 left join 的sql,都写标准sql既给于表一个别名,字段也要 别名.字段
     */
    @Test
    void countOptimize() {
        // 下面的 left join 不会对 count 进行优化,因为 where 条件里有 join 的表的条件

        // SELECT COUNT(*) FROM smp_classes c LEFT JOIN smp_student s ON c.id = s.classes_id
        // WHERE c.`name` = ? AND s.`name` = ?

        // SELECT c.id, c.`name`, c.create_time, s.id 's_id',
        // s.`name` 's_name', s.create_time 's_create_time', s.classes_id 's_classes_id'
        // FROM smp_classes c
        // LEFT JOIN smp_student s ON c.id = s.classes_id
        // WHERE c.`name` = ? AND s.`name` = ? LIMIT ?
        Page<ClassesStudent> page1 = new Page<>(1, 2);
        Map<String, Object> map1 = Maps.newHashMap("classesName", "一班");
        map1.put("studentName", "张三");
        classesMapper.classesStudentPage(page1, map1);
        page1.getRecords().forEach(System.out::println);

        // 下面的 left join 会对 count 进行优化,因为 where 条件里没有 join 的表的条件

        // SELECT COUNT(*) FROM smp_classes c WHERE c.`name` = ?

        // SELECT c.id, c.`name`, c.create_time,
        // s.id 's_id', s.`name` 's_name', s.create_time 's_create_time', s.classes_id 's_classes_id'
        // FROM smp_classes c LEFT JOIN smp_student s ON c.id = s.classes_id
        // WHERE c.`name` = ? LIMIT ?
        Page<ClassesStudent> page2 = new Page<>(1, 2);
        Map<String, Object> map2 = Maps.newHashMap("classesName", "一班");
        classesMapper.classesStudentPage(page2, map2);
        page2.getRecords().forEach(System.out::println);
    }
}
