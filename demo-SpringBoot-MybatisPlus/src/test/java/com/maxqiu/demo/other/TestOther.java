package com.maxqiu.demo.other;

import java.time.Duration;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.mybatis.spring.MyBatisSystemException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.maxqiu.demo.entity.AutoFill;
import com.maxqiu.demo.entity.TestEnum;
import com.maxqiu.demo.entity.User;
import com.maxqiu.demo.enums.GenderEnum;
import com.maxqiu.demo.enums.StateEnum;
import com.maxqiu.demo.mapper.AutoFillMapper;
import com.maxqiu.demo.mapper.TestEnumMapper;
import com.maxqiu.demo.mapper.UserMapper;

/**
 * @author Max_Qiu
 */
@SpringBootTest
public class TestOther {
    @Autowired
    private UserMapper userMapper;

    /**
     * 防止操作全表
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

    @Autowired
    private AutoFillMapper autoFillMapper;

    /**
     * 测试自动填充
     *
     * 比较好用的是针对 create_time update_time 字段，在插入和修改自动填充值（即数据库中未设置 默认值 或 根据当前时间戳更新）
     *
     * 使用时需要实现 MetaObjectHandler 例如：TimeMetaObjectHandler
     *
     * 并在对应实体字段添加 @TableField(value = "xxx", fill = FieldFill.xxx) 例如 AutoFill
     */
    @Test
    void testAutoFill() {
        // 新建一条数据，并执行插入
        AutoFill autoFill = new AutoFill().setName("name1");
        // INSERT INTO smp_auto_fill ( id, `name`, create_time ) VALUES ( ?, ?, ? )
        autoFillMapper.insert(autoFill);
        // 插入后，实体内的字段也有值
        System.out.println("创建时间：" + autoFill.getCreateTime());

        // 根据id查询当前数据
        // SELECT id,username,age,email FROM smp_user WHERE id=?
        AutoFill insert = autoFillMapper.selectById(autoFill.getId());
        // 从数据库中查询的实体存在创建时间
        System.out.println(insert);

        // 注：
        // Java中LocalDateTime时间精度为系统默认精度，即小数点后7位
        // MySQL中datetime类型时间精度根据长度确定小数点后有几位
        // 所以会出现插入时的实体和再查询出的实体时间不相等的情况，例如：
        System.out.println(Duration.between(autoFill.getCreateTime(), insert.getCreateTime()));
        // 解决方案见 TimeMetaObjectHandler

        // 执行更新
        // UPDATE smp_auto_fill SET `name`=?, create_time=?, update_time=? WHERE id=?
        autoFill.setName("name2");
        autoFillMapper.updateById(autoFill);
        // 插入后，实体内的字段也有值
        System.out.println("修改时间：" + autoFill.getUpdateTime());

        // 根据id查询当前数据
        // SELECT id,`name`,create_time,update_time FROM smp_auto_fill WHERE id=?
        AutoFill update = autoFillMapper.selectById(autoFill.getId());
        // 从数据库中查询的实体存在修改时间
        System.out.println(update);
    }

    @Autowired
    private TestEnumMapper enumMapper;

    /**
     * 测试枚举
     *
     * 1. 新建枚举<br>
     * 1.1 新建枚举，使用 @EnumValue 标记数据库存的值 参考 GenderEnum<br>
     * 1.2 新建枚举，实现 IEnum 接口
     *
     * 2. 修改实体，对应字段类型修改为枚举类型
     *
     * CRUD如下：
     */
    @Test
    void testEnum() {
        TestEnum testEnum = new TestEnum().setGender(GenderEnum.MALE).setState(StateEnum.NORMAL);
        // INSERT INTO smp_test_enum ( id, gender, state ) VALUES ( ?, ?, ? )
        enumMapper.insert(testEnum);

        TestEnum select = enumMapper.selectById(testEnum.getId());
        // SELECT id,gender,state FROM smp_test_enum WHERE id=?
        System.out.println(select);

        testEnum.setGender(GenderEnum.FEMALE);
        // UPDATE smp_test_enum SET gender=?, state=? WHERE id=?
        enumMapper.updateById(testEnum);

        LambdaUpdateWrapper<TestEnum> updateWrapper = Wrappers.lambdaUpdate();
        updateWrapper.eq(TestEnum::getGender, GenderEnum.FEMALE);
        List<TestEnum> list = enumMapper.selectList(updateWrapper);
        // SELECT id,gender,state FROM smp_test_enum WHERE (gender = ?)
        list.forEach(System.out::println);

        // 删除
        LambdaUpdateWrapper<TestEnum> deleteWrapper = Wrappers.lambdaUpdate();
        deleteWrapper.eq(TestEnum::getState, StateEnum.NORMAL);
        // DELETE FROM smp_test_enum WHERE (state = ?)
        enumMapper.delete(deleteWrapper);
    }
}
