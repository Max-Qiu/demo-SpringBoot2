package com.maxqiu.demo.controller;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.maxqiu.demo.entity.User;

/**
 * 获取参数
 *
 * @author Max_Qiu
 */
@Controller
@RequestMapping("value")
public class ValueController {
    /**
     * `@PathVariable` 可以映射 URL 中的占位符到目标方法的参数中
     */
    @RequestMapping("/pathVariable/{id}")
    @ResponseBody
    public String pathVariable(@PathVariable("id") Integer id) {
        return "PathVariable：" + id;
    }

    /**
     * 使用 @RequestParam 映射请求参数。其中：<br>
     * value 值为请求参数的参数名<br>
     * required 设置该参数是否必须。默认为 true<br>
     * defaultValue 若不传时请求参数的默认值<br>
     */
    @RequestMapping("urlParam1")
    @ResponseBody
    public String urlParam1(@RequestParam(value = "username") String username, @RequestParam(value = "age", defaultValue = "0") Integer age) {
        return "urlParam1：{username:" + username + ", age: " + age + "}";
    }

    /**
     * 若参数名和变量名相同，可以不写参数名
     */
    @RequestMapping("urlParam2")
    @ResponseBody
    public String urlParam2(@RequestParam String username, @RequestParam(required = false, defaultValue = "0") Integer age) {
        return "urlParam2：{username:" + username + ", age: " + age + "}";
    }

    /**
     * 若参数名和变量名相同，甚至可以不写 @RequestParam，且不写时不传值不会报错
     */
    @RequestMapping("urlParam3")
    @ResponseBody
    public String urlParam3(String username, Integer age) {
        return "urlParam3：{username:" + username + ", age: " + age + "}";
    }

    /**
     * 获取 multipart/form-data 表单 body 内的参数，规则同上
     */
    @RequestMapping("bodyParam1")
    @ResponseBody
    public String bodyParam1(@RequestParam String username, Integer age) {
        return "bodyParam1：{username:" + username + ", age: " + age + "}";
    }

    /**
     * 获取 application/x-www-form-urlencoded 表单 body 内的参数，规则同上
     */
    @PostMapping("bodyParam2")
    @ResponseBody
    public String bodyParam2(@RequestParam String username, Integer age) {
        return "bodyParam2：{username:" + username + ", age: " + age + "}";
    }

    /**
     * 获取 raw 字符串数据
     */
    @RequestMapping("bodyParam3")
    @ResponseBody
    public String bodyParam3(@RequestBody String text) {
        return "bodyParam3：{text:" + text + "}";
    }

    /**
     * 获取 URL 或 表单 数据，此处省略了 @RequestParam 注解
     */
    @RequestMapping("/pojo")
    @ResponseBody
    public User pojo(User user) {
        return user;
    }

    /**
     * 获取 JSON 数据
     */
    @RequestMapping("/pojo2")
    @ResponseBody
    public User pojo2(@RequestBody User user) {
        return user;
    }

    /**
     * 使用 @RequestHeader 映射请求头信息，用法同 @RequestParam
     */
    @RequestMapping("/requestHeader")
    @ResponseBody
    public String requestHeader(@RequestHeader("Accept-Language") String al) {
        return "requestHeader：Accept-Language: " + al;
    }

    /**
     * 使用 @CookieValue 映射一个 Cookie 值，用法同 @RequestParam
     */
    @RequestMapping("/cookieValue")
    @ResponseBody
    public String cookieValue(@CookieValue("test") String test) {
        return "cookieValue: test: " + test;
    }

    /**
     * 可以使用 Servlet 原生的 API 作为目标方法的参数，具体支持以下类型，扩展可以从 HttpServletRequest 或 HttpServletResponse 获取
     *
     * - 常用：<br>
     * - javax.servlet.http.HttpServletRequest<br>
     * - javax.servlet.http.HttpServletResponse<br>
     *
     * - 扩展<br>
     * - javax.servlet.http.HttpSession<br>
     * - java.security.Principal<br>
     * - java.util.Locale<br>
     * - javax.servlet.ServletInputStream<br>
     * - javax.servlet.ServletOutputStream<br>
     * - java.io.BufferedReader<br>
     * - java.io.PrintWriter<br>
     */
    @RequestMapping("/servletApi")
    public void servletApi(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.getWriter().write("servletApi:" + request + "," + response);
    }
}
