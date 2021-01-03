package com.maxqiu.demo.enums;

import com.baomidou.mybatisplus.annotation.IEnum;

/**
 * 状态枚举
 * 
 * @author Max_Qiu
 */
public enum StateEnum implements IEnum<Integer> {
    NORMAL(1, "正常"),

    LOCK(2, "锁定"),

    ;

    /**
     * 值
     */
    private final Integer value;

    /**
     * 描述
     */
    private final String desc;

    StateEnum(Integer value, String desc) {
        this.value = value;
        this.desc = desc;
    }

    @Override
    public Integer getValue() {
        return value;
    }

    public String getDesc() {
        return desc;
    }
}
