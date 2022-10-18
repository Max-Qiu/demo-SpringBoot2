package com.maxqiu.demo.exception;

import org.apache.shiro.authz.AuthorizationException;
import org.apache.shiro.authz.UnauthorizedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 权限异常处理器
 *
 * @author Max_Qiu
 */
@ControllerAdvice("com.maxqiu.demo.controller")
public class PermissionsException {
    @ResponseBody
    @ExceptionHandler(UnauthorizedException.class)
    public String unauthorizedException(Exception e) {
        return "无权限";
    }

    @ResponseBody
    @ExceptionHandler(AuthorizationException.class)
    public String authorizationException(Exception e) {
        return "权限认证失败";
    }
}
