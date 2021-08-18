package com.maxqiu.demo.vo;

import javax.validation.constraints.NotBlank;

import lombok.Data;

/**
 * 地址
 *
 * @author Max_Qiu
 */
@Data
public class AddressVO {
    @NotBlank
    private String province;
    @NotBlank
    private String city;
}
