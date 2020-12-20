package com.maxqiu.demo.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;

/**
 * 性别枚举
 * 
 * @author Max_Qiu
 */
public enum GenderEnum {
    MALE(1, "男"),

    FEMALE(2, "女"),

    OTHER(3, "其他");

    /**
     * 性别代码
     */
    @EnumValue
    private final int code;

    /**
     * 性别描述
     */
    private final String str;

    GenderEnum(int code, String str) {
        this.code = code;
        this.str = str;
    }

    public int getCode() {
        return code;
    }

    public String getStr() {
        return str;
    }
}
