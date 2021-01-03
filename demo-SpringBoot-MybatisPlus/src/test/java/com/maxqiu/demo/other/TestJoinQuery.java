package com.maxqiu.demo.other;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.maxqiu.demo.entity.Role;
import com.maxqiu.demo.mapper.RoleMapper;

/**
 * 连表查询
 * 
 * @author Max_Qiu
 */
@SpringBootTest
public class TestJoinQuery {

    @Autowired
    private RoleMapper roleMapper;

    // 不分页情况下连表查询

    /**
     * 使用 GROUP BY + GROUP_CONCAT 查询 role 并将 permission 的 id 合并成一个字段
     */
    @Test
    void list1() {
        // SELECT r.id, r.`name`, r.locked, GROUP_CONCAT(z.permission_id) permission_ids FROM smp_role r
        // LEFT JOIN smp_role_permission z ON r.id = z.role_id
        // GROUP BY r.id, r.`name`, r.locked
        List<Role> list = roleMapper.getRoleAndPermissionIds();
        list.forEach(System.out::println);
    }

    /**
     * 不使用 GROUP BY ，利用自定义的resultMap与collection
     * 
     * 将返回结果封装成 对象内包含集合 的效果
     * 
     * 注意：使用 collection 时建议添加 ORDER BY
     */
    @Test
    void list2() {
        // SELECT r.id, r.`name`, r.locked, p.id 'p_id', p.`name` 'p_name', p.locked 'p_locked' FROM smp_role r
        // LEFT JOIN smp_role_permission z ON r.id = z.role_id
        // LEFT JOIN smp_permission p on p.id = z.permission_id
        // ORDER BY r.id
        List<Role> list = roleMapper.getRoleAndPermissionList();
        list.forEach(System.out::println);
    }

    // 分页情况下连表查询
    // 注：page 仅需要在 mapper 内新建一个重载的重名接口并传入page即可，xml内不需要重复写

    /**
     * 使用 GROUP BY + GROUP_CONCAT 查询 role 并将 permission 的 id 合并成一个字段
     * 
     * 如果是分页查询，SELECT COUNT(*) 不会被优化，不推荐使用
     */
    @Test
    void page1() {
        // SELECT COUNT(*) FROM (
        // SELECT r.id, r.`name`, r.locked, GROUP_CONCAT(z.permission_id) permission_ids FROM smp_role r
        // LEFT JOIN smp_role_permission z ON r.id = z.role_id
        // GROUP BY r.id, r.`name`, r.locked
        // ) TOTAL
        // SELECT r.id, r.`name`, r.locked, GROUP_CONCAT(z.permission_id) permission_ids FROM smp_role r
        // LEFT JOIN smp_role_permission z ON r.id = z.role_id
        // GROUP BY r.id, r.`name`, r.locked LIMIT ?
        Page<Role> page = new Page<>(1, 3);
        page = roleMapper.getRoleAndPermissionIds(page);
        page.getRecords().forEach(System.out::println);
    }

    // 不使用 GROUP BY ，利用自定义的resultMap与collection
    // 将返回结果封装成 对象内包含集合 的效果
    // 此时的分页查询，SELECT COUNT(*)会被优化

    /**
     * 不使用 GROUP BY ，利用自定义的resultMap与collection
     * 
     * 将返回结果封装成 对象内包含集合 的效果
     * 
     * 此时的分页查询，SELECT COUNT(*)会被优化
     * 
     * 但是！返回的结果集数量不正常，所以需要使用子查询
     */
    @Test
    void page2() {
        // SELECT COUNT(*) FROM smp_role r

        // SELECT r.id, r.`name`, r.locked, p.id 'p_id', p.`name` 'p_name', p.locked 'p_locked' FROM smp_role r
        // LEFT JOIN smp_role_permission z ON r.id = z.role_id
        // LEFT JOIN smp_permission p on p.id = z.permission_id
        // ORDER BY r.id LIMIT ?
        Page<Role> page = new Page<>(1, 3);
        page = roleMapper.getRoleAndPermissionList(page);
        page.getRecords().forEach(System.out::println);
    }

    /**
     * 使用子查询
     * 
     * 先分页查询主表
     * 
     * 之后根据主表的每条id继续查询子表
     */
    @Test
    void page3() {
        // SELECT COUNT(*) FROM smp_role r

        // SELECT r.id, r.`name`, r.locked, r.create_time, r.update_time FROM smp_role r LIMIT ?

        // SELECT p.id, p.`name`, p.locked, p.create_time, p.update_time FROM smp_role_permission z
        // LEFT JOIN smp_permission p ON z.permission_id = p.id where z.role_id = ?
        Page<Role> page = new Page<>(1, 3);
        page = roleMapper.getRoleAndPermissionPage(page);
        page.getRecords().forEach(System.out::println);
    }
}
