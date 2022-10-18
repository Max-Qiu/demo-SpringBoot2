package com.maxqiu.demo.controller;

import javax.servlet.http.HttpSession;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.apache.shiro.subject.Subject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author Max_Qiu
 */
@Controller
@RequestMapping("myController")
public class MyController {
    // 跳转登录页面
    @GetMapping("login")
    public String login() {
        return "login";
    }

    @GetMapping("userLogin")
    public String userLogin(String name, String pwd, @RequestParam(defaultValue = "false") boolean rememberMe, HttpSession session) {
        // 1获取subject对象
        Subject subject = SecurityUtils.getSubject();
        // 2封装请求数据到token
        AuthenticationToken token = new UsernamePasswordToken(name, pwd, rememberMe);
        // 3调用login方法进行登录认证
        try {
            subject.login(token);
            // return "登录成功";
            session.setAttribute("user", token.getPrincipal().toString());
            return "main";

        } catch (AuthenticationException e) {
            e.printStackTrace();
            System.out.println("登录失败");
            return "登录失败";
        }
    }

    // 登录认证验证rememberMe
    @GetMapping("userLoginRm")
    public String userLogin(HttpSession session) {
        session.setAttribute("user", "rememberMe");
        return "main";
    }

    // 登录认证验证角色
    @RequiresRoles("admin")
    @GetMapping("userLoginRoles")
    @ResponseBody
    public String userLoginRoles() {
        System.out.println("登录认证验证角色");
        return "验证角色成功";
    }

    // 登录认证验证权限
    @RequiresPermissions("user:delete")
    @GetMapping("userPermissions")
    @ResponseBody
    public String userLoginPermissions() {
        System.out.println("登录认证验证权限");
        return "验证权限成功";
    }
}
