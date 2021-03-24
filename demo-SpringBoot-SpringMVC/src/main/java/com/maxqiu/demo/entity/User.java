package com.maxqiu.demo.entity;

import lombok.Getter;
import lombok.Setter;

/**
 * 用户
 * 
 * @author Max_Qiu
 */
@Getter
@Setter
public class User {
    /**
     * 姓名
     */
    private String name;
    /**
     * 年龄
     */
    private Integer age;
    /**
     * 地址
     */
    private Address address;
}
