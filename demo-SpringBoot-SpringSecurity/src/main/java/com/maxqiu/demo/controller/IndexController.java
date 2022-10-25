package com.maxqiu.demo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author Max_Qiu
 */
@Controller
@RequestMapping("")
public class IndexController {
    /**
     * 首页
     */
    @GetMapping("")
    @ResponseBody
    public String index() {
        return "Index! 这是首页！";
    }

    /**
     * 测试页
     */
    @GetMapping("hello")
    @ResponseBody
    public String hello() {
        return "Hello! 这是测试页！";
    }

    /**
     * 自定义的登录页面（GET方式）
     */
    @GetMapping("login")
    public String login() {
        return "login";
    }

    @PostMapping("/success")
    @ResponseBody
    public String success() {
        return "success";
    }

    @PostMapping("/fail")
    @ResponseBody
    public String fail() {
        return "fail";
    }
}
