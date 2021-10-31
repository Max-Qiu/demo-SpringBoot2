package com.maxqiu.demo.common;

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
    //
    FAIL(1, "失败"),
    // 参数校验异常
    PARAMETER_VERIFY_ERROR(2, "参数校验异常"),
    // 参数格式异常
    PARAMETER_FORMAT_ERROR(3, "参数格式异常"),

    // 未知异常
    ERROR(99, "未知异常"),

    ;

    private Integer code;
    private String msg;
}
