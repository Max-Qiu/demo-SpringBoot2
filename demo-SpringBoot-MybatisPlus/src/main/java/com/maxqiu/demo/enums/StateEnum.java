package com.maxqiu.demo.enums;

import com.baomidou.mybatisplus.annotation.IEnum;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 状态枚举
 *
 * @author Max_Qiu
 */
@Getter
@AllArgsConstructor
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
}
