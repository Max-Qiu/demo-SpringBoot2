package com.maxqiu.demo.common;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * 通用返回对象
 *
 * @author Max_Qiu
 */
@Data
@NoArgsConstructor
@Accessors(chain = true)
public class Result<T> {
    /**
     * 状态码
     */
    private Integer code;

    /**
     * 状态码对应提示信息
     */
    private String msg;

    /**
     * 数据
     */
    private T data;

    private Result(ResultEnum resultEnum) {
        this.code = resultEnum.getCode();
        this.msg = resultEnum.getMsg();
    }

    public static <K> Result<K> success() {
        return new Result<>(ResultEnum.SUCCESS);
    }

    public static <K> Result<K> success(K data) {
        Result<K> success = Result.success();
        return success.setData(data);
    }

    public static <K> Result<K> error() {
        return new Result<>(ResultEnum.SERVER_ERROR);
    }

    public static <K> Result<K> fail() {
        return new Result<>(ResultEnum.FAIL);
    }

    public static <K> Result<K> other(ResultEnum resultEnum) {
        return new Result<>(resultEnum);
    }

    public static <K> Result<K> other(ResultEnum resultEnum, K data) {
        Result<K> error = Result.other(resultEnum);
        return error.setData(data);
    }

    public static <K> Result<K> byFlag(boolean flag) {
        return new Result<>(flag ? ResultEnum.SUCCESS : ResultEnum.FAIL);
    }
}
