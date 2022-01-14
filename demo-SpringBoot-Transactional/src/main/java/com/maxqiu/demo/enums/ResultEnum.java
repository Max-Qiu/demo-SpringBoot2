package com.maxqiu.demo.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 返回值枚举
 *
 * @author Max_Qiu
 */
@AllArgsConstructor
@Getter
public enum ResultEnum {
    // 成功状态码
    SUCCESS(0, "成功"),
    // 失败状态码
    FAIL(1, "失败"),
    // 参数异常
    PARAMETER_ERROR(2, "参数异常"),

    ERROR(99, "系统异常"),

    TRANSACTION_ERROR(1001, "交易异常"),

    ;

    private final Integer code;
    private final String msg;
}
