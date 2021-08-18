package com.maxqiu.demo.vo;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import lombok.Data;

/**
 * 用户
 *
 * @author Max_Qiu
 */
@Data
public class UserVO {
    @NotNull
    private Integer id;
    @NotBlank
    private String name;
    @Valid
    @NotNull
    private AddressVO address;
}
