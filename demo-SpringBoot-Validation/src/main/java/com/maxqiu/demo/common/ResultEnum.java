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
    // 操作成功
    SUCCESS(0, "成功"),
    // 操作失败
    FAIL(1, "失败"),
    // 参数异常
    PARAMETER_ERROR(400, "参数校验异常"),
    // 服务器异常
    SERVER_ERROR(500, "未知异常"),

    ;

    private final Integer code;
    private final String msg;
}
