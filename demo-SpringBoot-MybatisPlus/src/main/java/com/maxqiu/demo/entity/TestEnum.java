package com.maxqiu.demo.entity;

import java.io.Serializable;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.maxqiu.demo.enums.GenderEnum;
import com.maxqiu.demo.enums.StateEnum;

/**
 * 
 *
 * @author Max_Qiu
 */
@TableName("smp_test_enum")
public class TestEnum extends Model<TestEnum> {

    private static final long serialVersionUID = 1L;

    /**
     * 主键id
     */
    @TableId("id")
    private Long id;

    /**
     * 性别
     */
    @TableField("gender")
    private GenderEnum gender;

    /**
     * 状态
     */
    @TableField("state")
    private StateEnum state;

    public Long getId() {
        return id;
    }

    public TestEnum setId(Long id) {
        this.id = id;
        return this;
    }

    public GenderEnum getGender() {
        return gender;
    }

    public TestEnum setGender(GenderEnum gender) {
        this.gender = gender;
        return this;
    }

    public StateEnum getState() {
        return state;
    }

    public TestEnum setState(StateEnum state) {
        this.state = state;
        return this;
    }

    @Override
    protected Serializable pkVal() {
        return this.id;
    }

    @Override
    public String toString() {
        return "TestEnum{" + "id=" + id + ", gender=" + gender + ", state=" + state + "} ";
    }
}
