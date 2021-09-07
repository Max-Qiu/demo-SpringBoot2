package com.maxqiu.demo.base;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.baomidou.mybatisplus.extension.conditions.query.QueryChainWrapper;
import com.baomidou.mybatisplus.extension.conditions.update.LambdaUpdateChainWrapper;
import com.baomidou.mybatisplus.extension.conditions.update.UpdateChainWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.maxqiu.demo.entity.User;
import com.maxqiu.demo.service.IUserService;

/**
 * 测试 Service 层
 *
 * @author Max_Qiu
 */
@SpringBootTest
public class TestUserService {
    @Autowired
    private IUserService userService;

    /**
     * 获取 BaseMapper 对象
     */
    @Test
    void getBaseMapper() {
        BaseMapper<User> baseMapper = userService.getBaseMapper();
        LambdaQueryWrapper<User> wrapper = Wrappers.lambdaQuery();
        List<User> userList = baseMapper.selectList(wrapper);
        System.out.println(userList);
    }

    /**
     * 获取 QueryChainWrapper 对象
     */
    @Test
    void query() {
        QueryChainWrapper<User> query = userService.query();
        List<User> list = query.list();
        list.forEach(System.out::println);
    }

    /**
     * 获取 LambdaQueryChainWrapper 对象
     */
    @Test
    void lambdaQuery() {
        LambdaQueryChainWrapper<User> query = userService.lambdaQuery();
        List<User> list = query.list();
        list.forEach(System.out::println);
    }

    /**
     * 获取 UpdateChainWrapper 对象
     */
    @Test
    void update() {
        UpdateChainWrapper<User> wrapper = userService.update();
        wrapper.eq("username", "max");
        boolean update = wrapper.update(new User().setAge(100));
        System.out.println(update);
    }

    /**
     * 获取 LambdaUpdateChainWrapper 对象
     */
    @Test
    void lambdaUpdate() {
        LambdaUpdateChainWrapper<User> update = userService.lambdaUpdate();
        boolean b = update.eq(User::getAge, 100).update(new User().setAge(88));
        System.out.println(b);
    }

    /**
     * 单条对象保存
     *
     * 如果设置ID，则存在相同ID时抛出异常
     */
    @Test
    void save() {
        // INSERT INTO smp_user ( id, username, age ) VALUES ( ?, ?, ? )
        boolean save1 = userService.save(new User().setUsername("Amy").setAge(11));
        System.out.println(save1);
    }

    /**
     * 更新或保存
     *
     * 如果没有ID，则直接保存
     *
     * 如果有ID，则会先检查，再决定执行 INSERT 或 UPDATE
     *
     * TODO 为啥这个会使用事务
     */
    @Test
    void saveOrUpdate() {
        // INSERT INTO smp_user ( id, username, age ) VALUES ( ?, ?, ? )
        boolean save1 = userService.saveOrUpdate(new User().setUsername("Amy").setAge(11));
        System.out.println(save1);
        // SELECT id,username,age,email FROM smp_user WHERE id=?
        // INSERT INTO smp_user ( id, username, age ) VALUES ( ?, ?, ? )
        boolean save2 = userService.saveOrUpdate(new User().setId(1L).setUsername("Amy").setAge(11));
        System.out.println(save2);
        // SELECT id,username,age,email FROM smp_user WHERE id=?
        // UPDATE smp_user SET username=?, age=? WHERE id=?
        boolean save3 = userService.saveOrUpdate(new User().setId(1L).setUsername("Amy").setAge(11));
        System.out.println(save3);
    }

    /**
     * 更新或保存，带条件查询
     */
    @Test
    void saveOrUpdateWithWrapper() {
        // update(entity, updateWrapper) || saveOrUpdate(entity);
        // 先执行 update ，根据update结果执行 saveOrUpdate

        // UPDATE smp_user SET age=? WHERE (age = ?)
        // SELECT id,username,age,email FROM smp_user WHERE id=?
        // INSERT INTO smp_user ( id, age ) VALUES ( ?, ? )
        boolean save1 = userService.saveOrUpdate(new User().setId(2L).setAge(11),
            new LambdaUpdateWrapper<User>().eq(User::getAge, 18));
        System.out.println(save1);
        // UPDATE smp_user SET age=? WHERE (age = ?)
        boolean save2 = userService.saveOrUpdate(new User().setId(2L).setAge(18),
            new LambdaUpdateWrapper<User>().eq(User::getAge, 11));
        System.out.println(save2);
        // UPDATE smp_user SET username=?, age=? WHERE (age = ?)
        // SELECT id,username,age,email FROM smp_user WHERE id=?
        // UPDATE smp_user SET age=? WHERE id=?
        boolean save3 = userService.saveOrUpdate(new User().setId(2L).setAge(18),
            new LambdaUpdateWrapper<User>().eq(User::getAge, 1));
        System.out.println(save3);
    }

    /**
     * 单条SQL批量保存
     *
     * 遇到相同ID抛出异常
     */
    @Test
    void saveBatch() {
        // INSERT INTO smp_user ( id, username, age ) VALUES ( ?, ?, ? )
        List<User> userList = new ArrayList<>();
        userList.add(new User().setUsername("max").setAge(12));
        userList.add(new User().setUsername("Tom").setAge(13));
        userList.add(new User().setId(3L).setUsername("张三").setAge(13));
        boolean b = userService.saveBatch(userList);
        System.out.println(b);
    }

    /**
     * 单条SQL批量保存
     *
     * 并限制每次最大数量
     */
    @Test
    void saveBatchAndSetBatchSize() {
        // INSERT INTO smp_user ( id, username, age ) VALUES ( ?, ?, ? )
        // INSERT INTO smp_user ( id, username, age ) VALUES ( ?, ?, ? )
        // INSERT INTO smp_user ( id, username, age ) VALUES ( ?, ?, ? )
        List<User> userList = new ArrayList<>();
        userList.add(new User().setUsername("max").setAge(12));
        userList.add(new User().setUsername("Tom").setAge(13));
        userList.add(new User().setId(4L).setUsername("张三").setAge(13));
        // 批量插入时，限制单次最大数量
        boolean b = userService.saveBatch(userList, 1);
        System.out.println(b);
    }

    /**
     * 批量保存或更新
     *
     * 有ID和没ID会自动分段
     *
     * 如果没有ID，则直接保存
     *
     * 如果有ID，则会先检查，再决定执行 INSERT 或 UPDATE
     */
    @Test
    void saveOrUpdateBatch() {
        List<User> userList = new ArrayList<>();
        // INSERT INTO smp_user ( id, username, age ) VALUES ( ?, ?, ? )
        userList.add(new User().setUsername("max").setAge(12));
        userList.add(new User().setUsername("Vicky").setAge(10));
        // SELECT id,username,age,email FROM smp_user WHERE id=?
        // INSERT INTO smp_user ( id, username, age ) VALUES ( ?, ?, ? )
        userList.add(new User().setId(5L).setUsername("Tom").setAge(13));
        // SELECT id,username,age,email FROM smp_user WHERE id=?
        // UPDATE smp_user SET username=?, age=? WHERE id=?
        userList.add(new User().setId(5L).setUsername("Jerry").setAge(16));
        // INSERT INTO smp_user ( id, username, age ) VALUES ( ?, ?, ? )
        userList.add(new User().setUsername("张三").setAge(16));
        userService.saveOrUpdateBatch(userList);
    }

    /**
     * 同上，且限制数量
     */
    @Test
    void saveOrUpdateBatchAndSetBatchSize() {
        List<User> userList = new ArrayList<>();
        // INSERT INTO smp_user ( id, username, age ) VALUES ( ?, ?, ? )
        userList.add(new User().setUsername("max").setAge(12));
        userList.add(new User().setUsername("Vicky").setAge(10));
        // SELECT id,username,age,email FROM smp_user WHERE id=?
        // INSERT INTO smp_user ( id, username, age ) VALUES ( ?, ?, ? )
        userList.add(new User().setId(5L).setUsername("Tom").setAge(13));
        // SELECT id,username,age,email FROM smp_user WHERE id=?
        // UPDATE smp_user SET username=?, age=? WHERE id=?
        userList.add(new User().setId(5L).setUsername("Jerry").setAge(16));
        // INSERT INTO smp_user ( id, username, age ) VALUES ( ?, ?, ? )
        userList.add(new User().setUsername("张三").setAge(16));
        // 批量插入时，限制单次最大数量
        userService.saveOrUpdateBatch(userList, 3);
    }

    /**
     * 更新2
     */
    @Test
    void updateByWrapper() {
        // UPDATE smp_user SET username=? WHERE (age = ?)
        boolean update =
            userService.update(new User().setUsername("max"), new LambdaUpdateWrapper<User>().eq(User::getAge, "101"));
        System.out.println(update);
    }

    /**
     * 根据ID更新
     */
    @Test
    void updateById() {
        // UPDATE smp_user SET username=? WHERE id=?
        boolean b = userService.updateById(new User().setId(8L).setUsername("123"));
        System.out.println(b);
    }

    /**
     * 批量更新ID更新
     */
    @Test
    void updateBatchById1() {
        // UPDATE smp_user SET username=?, age=? WHERE id=?
        List<User> userList = new ArrayList<>();
        userList.add(new User().setId(11L).setUsername("max").setAge(12));
        userList.add(new User().setId(12L).setUsername("Tom").setAge(13));
        userList.add(new User().setId(3L).setUsername("张三").setAge(15));
        boolean b = userService.updateBatchById(userList);
        System.out.println(b);
    }

    /**
     * 批量更新ID更新
     */
    @Test
    void updateBatchById2() {
        List<User> userList = new ArrayList<>();
        // UPDATE smp_user SET username=?, age=? WHERE id=?
        userList.add(new User().setId(11L).setUsername("max").setAge(12));
        userList.add(new User().setId(12L).setUsername("Tom").setAge(13));
        // UPDATE smp_user SET username=?, age=? WHERE id=?
        userList.add(new User().setId(3L).setUsername("张三").setAge(15));
        boolean b = userService.updateBatchById(userList, 2);
        System.out.println(b);
    }

    /**
     * 根据ID删除
     */
    @Test
    void removeById() {
        // DELETE FROM smp_user WHERE id=?
        boolean b = userService.removeById(1);
        // 无结果返回 false
        System.out.println(b);
    }

    /**
     * 根据ID批量删除
     */
    @Test
    void removeByIds() {
        // DELETE FROM smp_user WHERE id IN ( ? , ? , ? )
        ArrayList<Integer> integers = new ArrayList<>();
        integers.add(1);
        integers.add(2);
        integers.add(3);
        boolean b = userService.removeByIds(integers);
        // 无结果返回 false
        System.out.println(b);
    }

    /**
     * 条件删除
     */
    @Test
    void remove() {
        // DELETE FROM smp_user WHERE (age = ?)
        boolean remove = userService.remove(new LambdaQueryWrapper<User>().eq(User::getAge, 14));
        System.out.println(remove);
    }

    /**
     * 条件删除（Map）
     */
    @Test
    void removeByMap() {
        // DELETE FROM smp_user WHERE username = ?
        HashMap<String, Object> map = new HashMap<>();
        map.put("username", "mmm");
        boolean b = userService.removeByMap(map);
        // 无结果返回 false
        System.out.println(b);
    }

    /**
     * 根据ID获取
     */
    @Test
    void getById() {
        // SELECT id,username,age,email FROM smp_user WHERE id=?
        User one = userService.getById(1);
        System.out.println(one);
    }

    /**
     * 条件查询获取第一个，并设置是否抛出异常
     */
    @Test
    void getOne1() {
        // SELECT id,name,age,email,pid,createtime FROM user WHERE (age > ?)
        // false 查出多个时，仅返回第一个，并日志记录警告
        User one = userService.getOne(Wrappers.<User>lambdaQuery().eq(User::getAge, 24), false);
        System.out.println(one);
    }

    /**
     * 条件查询获取第一个，多个时抛出异常，即上一个方法的第二个参数为true
     */
    @Test
    void getOne2() {
        // SELECT id,username,age,email FROM smp_user WHERE (age > ?)
        // 查出多个时，抛出异常
        User one = userService.getOne(Wrappers.<User>lambdaQuery().eq(User::getAge, 1000));
        System.out.println(one);
    }

    /**
     * 条件查询 Map (仅返回第一条数据)
     */
    @Test
    void getMap() {
        // SELECT id,username,age,email FROM smp_user
        Map<String, Object> user = userService.getMap(Wrappers.emptyWrapper());
        System.out.println(user);
    }

    /**
     * 获取ID
     */
    @Test
    void getObj() {
        Long id = userService.getObj(Wrappers.emptyWrapper(), o -> (Long)o);
        System.out.println(id);
    }

    /**
     * 统计
     */
    @Test
    void count() {
        // SELECT COUNT( * ) FROM smp_user
        long count = userService.count();
        System.out.println(count);
    }

    /**
     * 条件统计
     */
    @Test
    void countByWrapper() {
        // SELECT COUNT( * ) FROM smp_user WHERE (username = ?)
        long count = userService.count(new LambdaQueryWrapper<User>().eq(User::getUsername, "m"));
        System.out.println(count);
    }

    /**
     * 无条件查询全部
     */
    @Test
    void list() {
        // SELECT id,username,age,email FROM smp_user
        List<User> list = userService.list();
        list.forEach(System.out::println);
    }

    /**
     * 有条件查询列表
     */
    @Test
    void listByWrapper() {
        // SELECT id,username,age,email FROM smp_user WHERE (age = ?)
        List<User> list = userService.list(new LambdaQueryWrapper<User>().eq(User::getAge, 18));
        list.forEach(System.out::println);
    }

    /**
     * 根据多个id查询列表
     */
    @Test
    void listByIds() {
        // SELECT id,username,age,email FROM smp_user WHERE id IN ( ? , ? , ? )
        ArrayList<Integer> integers = new ArrayList<>();
        integers.add(1);
        integers.add(2);
        integers.add(3);
        List<User> list = userService.listByIds(integers);
        list.forEach(System.out::println);
    }

    /**
     * 根据map对象条件查询列表
     */
    @Test
    void listByMap() {
        // SELECT id,username,age,email FROM smp_user WHERE username = ?
        HashMap<String, Object> map = new HashMap<>();
        map.put("username", "mmm");
        List<User> list = userService.listByMap(map);
        list.forEach(System.out::println);
    }

    /**
     * 无条件查询，返回 List<Map<String, Object>>
     */
    @Test
    void listMaps() {
        // SELECT id,username,age,email FROM smp_user
        List<Map<String, Object>> list = userService.listMaps();
        list.forEach(System.out::println);
    }

    /**
     * 有条件查询，返回 List<Map<String, Object>>
     */
    @Test
    void listMapsByWrapper() {
        // SELECT id,username,age,email FROM smp_user WHERE (email LIKE ?)
        List<Map<String, Object>> list = userService.listMaps(new LambdaQueryWrapper<User>().like(User::getEmail, "m"));
        list.forEach(System.out::println);
    }

    /**
     * 无条件查询，返回 List<Object>
     */
    @Test
    void listObjs() {
        // SELECT id,username,age,email FROM smp_user
        List<Object> list = userService.listObjs();
        list.forEach(System.out::println);
    }

    /**
     * 有条件查询，返回 List<Object>
     */
    @Test
    void listObjsByWrapper() {
        // SELECT id,username,age,email FROM smp_user WHERE (email LIKE ?)
        List<Object> list = userService.listObjs(new LambdaQueryWrapper<User>().like(User::getEmail, "m"));
        list.forEach(System.out::println);
    }

    /**
     * 无条件查询，返回id集合
     */
    @Test
    void listObjs2() {
        List<Long> list = userService.listObjs(o -> (Long)o);
        list.forEach(System.out::println);
    }

    /**
     * 有条件查询，返回id集合
     */
    @Test
    void listObjs2ByWrapper() {
        // SELECT id,username,age,email FROM smp_user WHERE (email LIKE ?)
        List<Long> list = userService.listObjs(new LambdaQueryWrapper<User>().like(User::getEmail, "m"), o -> (Long)o);
        list.forEach(System.out::println);
    }

    /**
     * 分页
     */
    @Test
    void page() {
        // SELECT COUNT(*) FROM smp_user
        // SELECT id,username,age,email FROM smp_user LIMIT ?
        Page<User> page = userService.page(new Page<>(1, 2));
        System.out.println("总页数" + page.getPages());
        System.out.println("总记录数" + page.getTotal());
        List<User> users = page.getRecords();
        users.forEach(System.out::println);
    }

    /**
     * 条件分页
     */
    @Test
    void pageByWrapper() {
        // SELECT COUNT(*) FROM smp_user WHERE (email LIKE ?)
        // SELECT id,username,age,email FROM smp_user WHERE (email LIKE ?) LIMIT ?
        Page<User> page = userService.page(new Page<>(1, 2), new LambdaQueryWrapper<User>().like(User::getEmail, "m"));
        System.out.println("总页数" + page.getPages());
        System.out.println("总记录数" + page.getTotal());
        List<User> users = page.getRecords();
        users.forEach(System.out::println);
    }

    /**
     * 分页 返回 map
     */
    @Test
    void pageMaps() {
        // SELECT COUNT(*) FROM smp_user
        // SELECT id,username,age,email FROM smp_user LIMIT ?
        Page<Map<String, Object>> page = userService.pageMaps(new Page<>(1, 2));
        System.out.println("总页数" + page.getPages());
        System.out.println("总记录数" + page.getTotal());
        List<Map<String, Object>> users = page.getRecords();
        users.forEach(System.out::println);
    }

    /**
     * 条件分页 返回 map
     */
    @Test
    void pageMapsByWrapper() {
        // SELECT COUNT(*) FROM smp_user WHERE (email LIKE ?)
        // SELECT id,username,age,email FROM smp_user WHERE (email LIKE ?) LIMIT ?
        Page<Map<String, Object>> page =
            userService.pageMaps(new Page<>(1, 2), new LambdaQueryWrapper<User>().like(User::getEmail, "m"));
        System.out.println("总页数" + page.getPages());
        System.out.println("总记录数" + page.getTotal());
        List<Map<String, Object>> users = page.getRecords();
        users.forEach(System.out::println);
    }
}
