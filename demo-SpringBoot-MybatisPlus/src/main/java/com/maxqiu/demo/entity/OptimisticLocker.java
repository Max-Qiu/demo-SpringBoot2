package com.maxqiu.demo.entity;

import java.io.Serializable;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.Version;
import com.baomidou.mybatisplus.extension.activerecord.Model;

/**
 * 
 *
 * @author Max_Qiu
 */
@TableName("smp_optimistic_locker")
public class OptimisticLocker extends Model<OptimisticLocker> {

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
     * 版本
     */
    @Version
    @TableField("version")
    private Long version;

    public Long getId() {
        return id;
    }

    public OptimisticLocker setId(Long id) {
        this.id = id;
        return this;
    }

    public String getName() {
        return name;
    }

    public OptimisticLocker setName(String name) {
        this.name = name;
        return this;
    }

    public Long getVersion() {
        return version;
    }

    public OptimisticLocker setVersion(Long version) {
        this.version = version;
        return this;
    }

    @Override
    protected Serializable pkVal() {
        return this.id;
    }

    @Override
    public String toString() {
        return "OptimisticLocker{" + "id=" + id + ", name=" + name + ", version=" + version + "}";
    }
}
