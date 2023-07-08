package com.maxqiu.demo.vo;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * 用户
 *
 * @author Max_Qiu
 */
@Getter
@Setter
@NoArgsConstructor
@ToString
public class UserVO {
    @NotNull
    private Integer id;
    @NotBlank
    private String name;
    @Valid
    @NotNull
    private AddressVO address;
}
