package com.maxqiu.demo.vo;

import javax.validation.constraints.NotBlank;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * 地址
 *
 * @author Max_Qiu
 */
@Getter
@Setter
@NoArgsConstructor
@ToString
public class AddressVO {
    @NotBlank
    private String province;
    @NotBlank
    private String city;
}
