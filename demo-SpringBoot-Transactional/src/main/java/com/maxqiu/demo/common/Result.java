package com.maxqiu.demo.common;

import com.maxqiu.demo.enums.ResultEnum;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * 通用返回对象
 *
 * @author Max_Qiu
 */
@Getter
@Setter
@NoArgsConstructor
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
        success.setData(data);
        return success;
    }

    public static <K> Result<K> fail() {
        return new Result<>(ResultEnum.FAIL);
    }

    public static <K> Result<K> fail(K data) {
        return new Result<>(ResultEnum.FAIL);
    }

    public static <K> Result<K> error() {
        return new Result<>(ResultEnum.ERROR);
    }

    public static <K> Result<K> other(ResultEnum resultEnum) {
        return new Result<>(resultEnum);
    }

    public static <K> Result<K> other(ResultEnum resultEnum, K data) {
        Result<K> error = Result.other(resultEnum);
        error.setData(data);
        return error;
    }

    public static <K> Result<K> byFlag(boolean flag) {
        return new Result<>(flag ? ResultEnum.SUCCESS : ResultEnum.FAIL);
    }
}
