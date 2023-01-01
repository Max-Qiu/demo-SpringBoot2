package com.maxqiu.demo.entity;

import java.io.Serializable;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.maxqiu.demo.enums.GenderEnum;
import com.maxqiu.demo.enums.StateEnum;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

/**
 * 枚举
 *
 * @author Max_Qiu
 */
@Getter
@Setter
@NoArgsConstructor
@ToString
@Accessors(chain = true)
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
    public Serializable pkVal() {
        return this.id;
    }
}
