package com.maxqiu.demo.other;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.maxqiu.demo.entity.OptimisticLocker;
import com.maxqiu.demo.mapper.OptimisticLockerMapper;

/**
 * 乐观锁
 *
 * 需要在插件内添加 interceptor.addInnerInterceptor(new OptimisticLockerInnerInterceptor());
 *
 * 然后在对应实体的版本控制字段添加 @Version
 *
 * @author Max_Qiu
 */
@SpringBootTest
public class TestOptimisticLocker {
    @Autowired
    private OptimisticLockerMapper mapper;

    /**
     * 新建一条数据并插入和修改
     */
    @Test
    void testUpdateByIdSuccess() {
        // 插入一条数据，并设置版本为 1
        OptimisticLocker locker = new OptimisticLocker();
        locker.setName("name");
        locker.setVersion(1L);
        // INSERT INTO smp_optimistic_locker ( id, `name`, version ) VALUES ( ?, ?, ? )
        mapper.insert(locker);
        Long id = locker.getId();

        // 更新这条数据，且这条数据的版本为 1
        OptimisticLocker lockerUpdate = new OptimisticLocker();
        lockerUpdate.setId(id);
        lockerUpdate.setName("newName");
        lockerUpdate.setVersion(1L);
        // UPDATE smp_optimistic_locker SET `name`=?, version=? WHERE id=? AND version=?
        int i = mapper.updateById(lockerUpdate);
        // 执行更新，修改了 1 条数据
        System.out.println(i);
        // 执行后的实体版本更新为 2
        System.out.println(lockerUpdate.getVersion());
    }

    /**
     * 从数据库获取一条数据并更新
     */
    @Test
    void testUpdateByIdSuccessFromDb() {
        // 从数据库中根据id获取一条数据，并获取版本
        // SELECT id,`name`,version FROM smp_optimistic_locker WHERE id=?
        OptimisticLocker locker = mapper.selectById(1);
        long oldVersion = locker.getVersion();
        // UPDATE smp_optimistic_locker SET `name` = ?, version = ? WHERE id = ? AND version = ?
        int i = mapper.updateById(locker);
        // 执行更新，修改了 1 条数据
        System.out.println(i);
        // 执行后的实体版本比原版本大 1
        System.out.println(oldVersion + 1 == locker.getVersion());
    }

    /**
     * 测试更新失败
     */
    @Test
    public void testUpdateByIdFail() {
        // 新建一条数据并插入
        OptimisticLocker locker = new OptimisticLocker();
        locker.setName("username");
        locker.setVersion(1L);
        // INSERT INTO smp_optimistic_locker ( id, `name`, version ) VALUES ( ?, ?, ? )
        mapper.insert(locker);
        Long id = locker.getId();

        // 更新这条数据，且设置版本和数据库不一样
        OptimisticLocker lockerUpdate = new OptimisticLocker();
        lockerUpdate.setId(id);
        lockerUpdate.setVersion(0L);
        // UPDATE smp_optimistic_locker SET version=? WHERE id=? AND version=?
        int i = mapper.updateById(lockerUpdate);
        // 执行更新，修改了 0 条数据
        System.out.println(i);
    }

    /**
     * 测试更新时不设置版本
     */
    @Test
    public void testUpdateByIdSuccessWithNoVersion() {
        // 新建一条数据并指定id
        OptimisticLocker user = new OptimisticLocker();
        user.setName("name");
        user.setVersion(1L);
        // INSERT INTO smp_optimistic_locker ( id, `name`, version ) VALUES ( ?, ?, ? )
        mapper.insert(user);
        Long id = user.getId();

        // 不设置id进行更新
        OptimisticLocker userUpdate = new OptimisticLocker();
        userUpdate.setId(id);
        userUpdate.setName("newName");
        userUpdate.setVersion(null);
        // UPDATE smp_optimistic_locker SET `name`=? WHERE id=?
        int i = mapper.updateById(userUpdate);
        // 执行更新，修改了 1 条数据
        System.out.println(i);

        // 重新从数据库获取该条数据
        // SELECT id,`name`,version FROM smp_optimistic_locker WHERE id=?
        OptimisticLocker updated = mapper.selectById(id);
        // 输出数据库中的版本，显示为 1 ，即未改变
        System.out.println(updated.getVersion().intValue());
        // 输出数据库中的自动，值已改变
        System.out.println(updated.getName());
    }

    /**
     * 批量更新带乐观锁
     */
    @Test
    public void testUpdateByEntitySucc() {
        // 查询版本为 1 的数据数量
        LambdaQueryWrapper<OptimisticLocker> ew = Wrappers.lambdaQuery();
        ew.eq(OptimisticLocker::getVersion, 1);
        // SELECT COUNT( * ) FROM smp_optimistic_locker WHERE (version = ?)
        long count = mapper.selectCount(ew);

        // 设置一个查询条件，且需要更新的版本为 1
        OptimisticLocker entity = new OptimisticLocker();
        entity.setName("new name");
        entity.setVersion(1L);

        // 执行更新
        // UPDATE smp_optimistic_locker SET `name`=?, version=? WHERE (version = ?)
        int update = mapper.update(entity, null);

        // 更新的数量应当和版本为 1 的数量相同
        System.out.println(count == update);

        // 再设置一个查询条件，且需要更新的版本为 1
        LambdaQueryWrapper<OptimisticLocker> ew1 = Wrappers.lambdaQuery();
        ew1.eq(OptimisticLocker::getVersion, 1);
        // SELECT COUNT( * ) FROM smp_optimistic_locker WHERE (version = ?)
        long count1 = mapper.selectCount(ew1);
        // 应当查询不到版本为 1 的数据
        System.out.println(count1);

        // 再设置一个查询条件，且需要更新的版本为 2
        LambdaQueryWrapper<OptimisticLocker> ew2 = Wrappers.lambdaQuery();
        ew2.eq(OptimisticLocker::getVersion, 2);
        // SELECT COUNT( * ) FROM smp_optimistic_locker WHERE (version = ?)
        long count2 = mapper.selectCount(ew);
        System.out.println(count2);
    }
}
