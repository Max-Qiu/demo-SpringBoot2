package com.maxqiu.demo.system.entity;

import java.io.Serializable;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

/**
 * 数据源配置信息
 *
 * @author Max_Qiu
 */
@Getter
@Setter
@NoArgsConstructor
@Accessors(chain = true)
@TableName("db_info")
public class DbInfo extends Model<DbInfo> {
    private static final long serialVersionUID = 1L;

    /**
     * 主键id
     */
    @TableId("id")
    private Integer id;

    /**
     * 键
     */
    @TableField("`key`")
    private String key;

    /**
     * 连接池
     */
    @TableField("`type`")
    private String type;

    /**
     * 驱动
     */
    @TableField("`driver-class-name`")
    private String driverClassName;

    /**
     * 地址
     */
    @TableField("url")
    private String url;

    /**
     * 用户名
     */
    @TableField("username")
    private String username;

    /**
     * 密码
     */
    @TableField("`password`")
    private String password;

    @Override
    public Serializable pkVal() {
        return this.id;
    }
}
