package com.maxqiu.demo.entity;

import java.io.Serializable;
import java.time.LocalDateTime;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;

/**
 * 
 *
 * @author Max_Qiu
 */
@TableName("smp_auto_fill")
public class AutoFill extends Model<AutoFill> {

    private static final long serialVersionUID = 1L;

    /**
     * 主键id
     */
    @TableId("id")
    private Long id;

    /**
     * 测试字段
     */
    @TableField("`name`")
    private String name;

    /**
     * 创建时间
     */
    @TableField(value = "create_time", fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    /**
     * 修改时间
     */
    @TableField(value = "update_time", fill = FieldFill.UPDATE)
    private LocalDateTime updateTime;

    public Long getId() {
        return id;
    }

    public AutoFill setId(Long id) {
        this.id = id;
        return this;
    }

    public String getName() {
        return name;
    }

    public AutoFill setName(String name) {
        this.name = name;
        return this;
    }

    public LocalDateTime getCreateTime() {
        return createTime;
    }

    public AutoFill setCreateTime(LocalDateTime createTime) {
        this.createTime = createTime;
        return this;
    }

    public LocalDateTime getUpdateTime() {
        return updateTime;
    }

    public AutoFill setUpdateTime(LocalDateTime updateTime) {
        this.updateTime = updateTime;
        return this;
    }

    @Override
    protected Serializable pkVal() {
        return this.id;
    }

    @Override
    public String toString() {
        return "AutoFill{" + "id=" + id + ", name=" + name + ", createTime=" + createTime + ", updateTime=" + updateTime
            + "}";
    }
}
