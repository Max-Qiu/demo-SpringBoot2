package com.maxqiu.demo.exception;

import java.util.HashMap;
import java.util.Map;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Path;

import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.multipart.support.MissingServletRequestPartException;

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
    @ResponseStatus(code = HttpStatus.BAD_REQUEST)
    public Result<Map<String, String>> handleValidException(ConstraintViolationException e) {
        Map<String, String> errorMap = new HashMap<>();
        for (ConstraintViolation<?> constraintViolation : e.getConstraintViolations()) {
            String field = "";
            for (Path.Node node : constraintViolation.getPropertyPath()) {
                field = node.getName();
            }
            errorMap.put(field, constraintViolation.getMessage());
        }
        return Result.other(ResultEnum.PARAMETER_ERROR, errorMap);
    }

    /**
     * 处理方法的实体参数异常
     */
    @ExceptionHandler(value = BindException.class)
    @ResponseStatus(code = HttpStatus.BAD_REQUEST)
    public Result<Map<String, String>> handleValidException(BindException e) {
        Map<String, String> errorMap = new HashMap<>();
        e.getBindingResult().getFieldErrors().forEach(r -> errorMap.put(r.getField(), r.getDefaultMessage()));
        return Result.other(ResultEnum.PARAMETER_ERROR, errorMap);
    }

    /**
     * 处理方法的参数格式异常
     */
    @ExceptionHandler(value = HttpMessageNotReadableException.class)
    @ResponseStatus(code = HttpStatus.BAD_REQUEST)
    public Result<String> handleException(HttpMessageNotReadableException e) {
        return Result.other(ResultEnum.PARAMETER_ERROR, e.getMessage());
    }

    /**
     * 处理方法的参数缺失
     */
    @ExceptionHandler(value = MissingServletRequestPartException.class)
    @ResponseStatus(code = HttpStatus.BAD_REQUEST)
    public Result<String> handleException(MissingServletRequestPartException e) {
        return Result.other(ResultEnum.PARAMETER_ERROR, e.getMessage());
    }

    /**
     * 处理方法的参数缺失
     */
    @ExceptionHandler(value = MissingServletRequestParameterException.class)
    @ResponseStatus(code = HttpStatus.BAD_REQUEST)
    public Result<String> handleException(MissingServletRequestParameterException e) {
        return Result.other(ResultEnum.PARAMETER_ERROR, e.getMessage());
    }

    /**
     * 处理方法的参数类型不匹配异常
     */
    @ExceptionHandler(value = MethodArgumentTypeMismatchException.class)
    @ResponseStatus(code = HttpStatus.BAD_REQUEST)
    public Result<String> handleException(MethodArgumentTypeMismatchException e) {
        return Result.other(ResultEnum.PARAMETER_ERROR, e.getMessage());
    }

    /**
     * 全局异常捕获
     */
    @ExceptionHandler(value = Throwable.class)
    @ResponseStatus(code = HttpStatus.INTERNAL_SERVER_ERROR)
    public Result<String> handleException(Throwable throwable) {
        log.error("其他异常：{}", throwable.toString());
        throwable.printStackTrace();
        return Result.error();
    }
}
