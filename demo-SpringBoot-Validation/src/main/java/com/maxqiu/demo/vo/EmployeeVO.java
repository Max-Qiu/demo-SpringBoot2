package com.maxqiu.demo.vo;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;

import com.maxqiu.demo.valid.constraints.ListValue;
import com.maxqiu.demo.valid.group.AddValidGroup;
import com.maxqiu.demo.valid.group.ChangeStatusValidGroup;
import com.maxqiu.demo.valid.group.UpdateValidGroup;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * 员工
 *
 * @author Max_Qiu
 */
@Getter
@Setter
@NoArgsConstructor
@ToString
public class EmployeeVO {
    /**
     * id
     */
    @Null(groups = AddValidGroup.class, message = "新增时ID必须为null")
    @NotNull(groups = {UpdateValidGroup.class, ChangeStatusValidGroup.class}, message = "修改时员工ID不能为空")
    private Integer id;

    /**
     * 姓名
     */
    @NotBlank(groups = {AddValidGroup.class, UpdateValidGroup.class}, message = "姓名不能为空")
    private String name;

    /**
     * 手机号
     *
     * 例：若不加分组，则不进行校验
     */
    @NotBlank
    private String phone;

    /**
     * 状态
     */
    @ListValue(valueList = {0, 1}, groups = ChangeStatusValidGroup.class)
    private Integer status;
}
