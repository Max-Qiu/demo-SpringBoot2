package com.maxqiu.demo.entity;

import java.io.Serializable;
import java.time.LocalDateTime;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

/**
 * 学生表
 *
 * @author Max_Qiu
 */
@Getter
@Setter
@NoArgsConstructor
@ToString
@Accessors(chain = true)
@TableName("smp_student")
public class Student extends Model<Student> {
    private static final long serialVersionUID = 1L;

    /**
     * 主键id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 学生姓名
     */
    @TableField("`name`")
    private String name;

    /**
     * 班级id
     */
    @TableField("classes_id")
    private Long classesId;

    /**
     * 创建时间
     */
    @TableField("create_time")
    private LocalDateTime createTime;

    @TableField(exist = false)
    private Long count;

    @Override
    public Serializable pkVal() {
        return this.id;
    }
}
