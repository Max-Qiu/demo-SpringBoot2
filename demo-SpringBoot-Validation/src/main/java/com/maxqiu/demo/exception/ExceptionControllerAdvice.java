package com.maxqiu.demo.exception;

import java.util.HashMap;
import java.util.Map;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Path;

import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.maxqiu.demo.common.Result;
import com.maxqiu.demo.common.ResultEnum;

import lombok.extern.slf4j.Slf4j;

/**
 * 集中处理所有异常
 *
 * @author Max_Qiu
 */
@Slf4j
// @ResponseBody
// @ControllerAdvice(basePackages = "com.maxqiu.demo.controller")
@RestControllerAdvice(basePackages = "com.maxqiu.demo.controller")
public class ExceptionControllerAdvice {
    /**
     * 处理方法的普通参数异常
     */
    @ExceptionHandler(value = ConstraintViolationException.class)
    public Result<Map<String, String>> handleValidException(ConstraintViolationException e) {
        Map<String, String> errorMap = new HashMap<>();
        for (ConstraintViolation<?> constraintViolation : e.getConstraintViolations()) {
            String field = "";
            for (Path.Node node : constraintViolation.getPropertyPath()) {
                field = node.getName();
            }
            errorMap.put(field, constraintViolation.getMessage());
        }
        return Result.other(ResultEnum.PARAMETER_VERIFY_ERROR, errorMap);
    }

    /**
     * 处理方法的实体参数异常
     */
    @ExceptionHandler(value = BindException.class)
    public Result<Map<String, String>> handleValidException(BindException e) {
        Map<String, String> errorMap = new HashMap<>();
        e.getBindingResult().getFieldErrors().forEach(r -> errorMap.put(r.getField(), r.getDefaultMessage()));
        return Result.other(ResultEnum.PARAMETER_VERIFY_ERROR, errorMap);
    }

    /**
     * 处理参数格式异常
     */
    @ExceptionHandler(value = HttpMessageNotReadableException.class)
    public Result<String> handleException() {
        return Result.other(ResultEnum.PARAMETER_FORMAT_ERROR);
    }

    @ExceptionHandler(value = Throwable.class)
    public Result<String> handleException(Throwable throwable) {
        log.error("其他异常：{}\n异常类型：{}", throwable.getMessage(), throwable.getClass());
        return Result.error();
    }
}
