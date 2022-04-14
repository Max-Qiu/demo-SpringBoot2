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

    EXIST_LOG(1001, "已存在记录"),

    NOT_DIRECTORY(1002, "该目录不是文件夹"),

    ;

    private final Integer code;
    private final String msg;
}
