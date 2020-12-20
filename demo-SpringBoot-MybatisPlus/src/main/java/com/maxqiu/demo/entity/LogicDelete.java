package com.maxqiu.demo.entity;

import java.io.Serializable;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;

/**
 * 逻辑删除
 *
 * @author Max_Qiu
 */
@TableName("smp_logic_delete")
public class LogicDelete extends Model<LogicDelete> {

    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 名称
     */
    @TableField("username")
    private String username;

    /**
     * 逻辑删除
     */
    @TableField("deleted")
    private Boolean deleted;

    public Long getId() {
        return id;
    }

    public LogicDelete setId(Long id) {
        this.id = id;
        return this;
    }

    public String getUsername() {
        return username;
    }

    public LogicDelete setUsername(String username) {
        this.username = username;
        return this;
    }

    public Boolean getDeleted() {
        return deleted;
    }

    public LogicDelete setDeleted(Boolean deleted) {
        this.deleted = deleted;
        return this;
    }

    @Override
    protected Serializable pkVal() {
        return this.id;
    }

    @Override
    public String toString() {
        return JSON.toJSONString(this);
    }
}
