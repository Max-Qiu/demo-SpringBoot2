package com.maxqiu.demo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 映射请求
 *
 * @author Max_Qiu
 */
@Controller
@RequestMapping("request")
public class RequestController {
    /**
     * get
     */
    @RequestMapping(value = "get", method = RequestMethod.GET)
    @ResponseBody
    public String get() {
        return "get";
    }

    /**
     * GetMapping
     */
    @GetMapping(value = "get2")
    @ResponseBody
    public String get2() {
        return "get2";
    }

    /**
     * post
     */
    @RequestMapping(value = "post", method = RequestMethod.POST)
    @ResponseBody
    public String post() {
        return "post";
    }

    /**
     * PostMapping
     */
    @PostMapping("post2")
    @ResponseBody
    public String post2() {
        return "post2";
    }

    /**
     * 使用 params 更加精确的映射请求
     */
    @RequestMapping(value = "params", params = {"username", "age!=10"})
    @ResponseBody
    public String params() {
        return "params";
    }

    /**
     * 使用 headers 更加精确的映射请求
     */
    @RequestMapping(value = "headers", headers = {"Accept-Language=zh-CN,zh;q=0.9"})
    @ResponseBody
    public String headers() {
        return "headers";
    }
}
