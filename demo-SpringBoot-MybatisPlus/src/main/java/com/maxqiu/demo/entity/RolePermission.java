package com.maxqiu.demo.entity;

import java.io.Serializable;
import java.time.LocalDateTime;

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
 * 角色权限关联表
 *
 * @author Max_Qiu
 */
@Getter
@Setter
@NoArgsConstructor
@ToString
@Accessors(chain = true)
@TableName("smp_role_permission")
public class RolePermission extends Model<RolePermission> {
    private static final long serialVersionUID = 1L;

    /**
     * 角色id
     */
    @TableId("role_id")
    private Long roleId;

    /**
     * 角色id
     */
    @TableField("permission_id")
    private Long permissionId;

    /**
     * 创建时间
     */
    @TableField("create_time")
    private LocalDateTime createTime;

    @Override
    public Serializable pkVal() {
        return this.roleId;
    }
}
