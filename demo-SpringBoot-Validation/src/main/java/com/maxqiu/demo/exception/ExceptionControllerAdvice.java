package com.maxqiu.demo.exception;

import java.util.HashMap;
import java.util.Map;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Path;

import org.springframework.validation.BindException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.maxqiu.demo.common.Result;

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
     * 处理方法上的单个参数异常
     *
     * @param e
     * @return
     */
    @ExceptionHandler(value = ConstraintViolationException.class)
    public Result handleValidException(ConstraintViolationException e) {
        log.error("其他异常：{}\n异常类型：{}", e.getMessage(), e.getClass());
        Map<String, String> errorMap = new HashMap<>();
        for (ConstraintViolation<?> constraintViolation : e.getConstraintViolations()) {
            String field = "";
            for (Path.Node node : constraintViolation.getPropertyPath()) {
                field = node.getName();
            }
            errorMap.put(field, constraintViolation.getMessage());
        }
        return Result.error(500, errorMap);
    }

    /**
     * 处理方法的实体参数异常
     */
    @ExceptionHandler(value = BindException.class)
    public Result handleValidException(BindException e) {
        log.error("其他异常：{}\n异常类型：{}", e.getMessage(), e.getClass());
        Map<String, String> errorMap = new HashMap<>();
        e.getBindingResult().getFieldErrors().forEach(r -> errorMap.put(r.getField(), r.getDefaultMessage()));
        return Result.error(500, errorMap);
    }

    @ExceptionHandler(value = Throwable.class)
    public Result handleException(Throwable throwable) {
        log.error("其他异常：{}\n异常类型：{}", throwable.getMessage(), throwable.getClass());
        return Result.error();
    }
}
