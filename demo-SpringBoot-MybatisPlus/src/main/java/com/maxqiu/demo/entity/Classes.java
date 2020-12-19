package com.maxqiu.demo.entity;

import java.io.Serializable;
import java.time.LocalDateTime;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;

/**
 * 
 *
 * @author Max_Qiu
 */
@TableName("smp_classes")
public class Classes extends Model<Classes> {

    private static final long serialVersionUID = 1L;

    /**
     * 主键id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 班级名称
     */
    @TableField("`name`")
    private String name;

    /**
     * 创建时间
     */
    @TableField("create_time")
    private LocalDateTime createTime;

    public Long getId() {
        return id;
    }

    public Classes setId(Long id) {
        this.id = id;
        return this;
    }

    public String getName() {
        return name;
    }

    public Classes setName(String name) {
        this.name = name;
        return this;
    }

    public LocalDateTime getCreateTime() {
        return createTime;
    }

    public Classes setCreateTime(LocalDateTime createTime) {
        this.createTime = createTime;
        return this;
    }

    @Override
    protected Serializable pkVal() {
        return this.id;
    }

    @Override
    public String toString() {
        return "Classes{" + "id=" + id + ", name=" + name + ", createTime=" + createTime + "}";
    }
}
