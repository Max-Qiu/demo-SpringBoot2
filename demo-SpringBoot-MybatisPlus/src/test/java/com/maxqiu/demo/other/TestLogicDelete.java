package com.maxqiu.demo.other;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.maxqiu.demo.entity.LogicDelete;
import com.maxqiu.demo.mapper.LogicDeleteMapper;

/**
 * 测试逻辑删除
 *
 * 需要在 application.yml 中开启全局逻辑删除
 *
 * 或者为实体的指定字段添加 @TableLogic 注解
 *
 * @author Max_Qiu
 */
@SpringBootTest
public class TestLogicDelete {
    @Autowired
    private LogicDeleteMapper deleteMapper;

    /**
     * 测试查询
     *
     * 自动带上 WHERE deleted=0
     */
    @Test
    void select() {
        // SELECT id,username,deleted FROM smp_test_delete WHERE deleted=0
        List<LogicDelete> list = deleteMapper.selectList(Wrappers.emptyWrapper());
        list.forEach(System.out::println);
    }

    /**
     * 测试插入
     *
     * 对于逻辑删除字段，需要在数据库中设置默认值，或者手动赋值，或者使用自动填充
     */
    @Test
    void insert() {
        // INSERT INTO smp_test_delete ( username ) VALUES ( ? )
        LogicDelete delete = new LogicDelete().setUsername("max");
        // 若数据库未设置默认值，则需要手动赋值
        // delete.setDeleted(false);
        boolean insert = delete.insert();
        System.out.println(insert);
    }

    /**
     * 测试修改
     *
     * 修改时，默认添加 WHERE deleted=0
     */
    @Test
    void update() {
        // 对已删除的进行修改，返回 false
        // UPDATE smp_test_delete SET username=? WHERE id=? AND deleted=0
        // Updates: 0
        LogicDelete delete = new LogicDelete().setId(2L).setUsername("tom");
        boolean b = delete.updateById();
        System.out.println(b);

        // 对未删除的进行修改，返回 true
        // UPDATE smp_test_delete SET username=? WHERE id=? AND deleted=0
        // Updates: 1
        delete.setId(3L);
        boolean b1 = delete.updateById();
        System.out.println(b1);
    }

    /**
     * 测试删除
     *
     * 删除语句会变更为更新语句，并将逻辑删除字段修改为1，同时会带上WHERE deleted=0保证不操作到已经逻辑删除的数据
     */
    @Test
    void delete() {
        // UPDATE smp_test_delete SET deleted=1 WHERE deleted=0 AND (username = ?)
        LambdaUpdateWrapper<LogicDelete> wrapper = Wrappers.lambdaUpdate();
        wrapper.eq(LogicDelete::getUsername, "max");
        int delete = deleteMapper.delete(wrapper);
        System.out.println(delete);
    }
}
