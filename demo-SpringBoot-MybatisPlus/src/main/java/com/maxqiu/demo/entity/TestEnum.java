package com.maxqiu.demo.entity;

import java.io.Serializable;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.maxqiu.demo.enums.GenderEnum;
import com.maxqiu.demo.enums.StateEnum;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * 枚举
 *
 * @author Max_Qiu
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@NoArgsConstructor
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

    @Override
    protected Serializable pkVal() {
        return this.id;
    }

    @Override
    public String toString() {
        return JSON.toJSONString(this);
    }
}
