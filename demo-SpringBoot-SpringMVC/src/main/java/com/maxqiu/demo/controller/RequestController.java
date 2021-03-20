package com.maxqiu.demo.controller;

import org.springframework.stereotype.Controller;
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
     * post
     */
    @RequestMapping(value = "post", method = RequestMethod.POST)
    @ResponseBody
    public String post() {
        return "post";
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

    /**
     * ?：匹配一个字符（若多个字符需要多个?）
     */
    @RequestMapping("/ant/ab?")
    @ResponseBody
    public String antPath1() {
        return "/ant/ab?";
    }

    /**
     * *：匹配任意字符（字符中间不能有 / ）
     */
    @RequestMapping("/ant/*/abc")
    @ResponseBody
    public String antPath2() {
        return "/ant/*/abc";
    }

    /**
     * **：匹配多层路径（字符中间可以有 / 也可以没有 / ）
     */
    @RequestMapping("/ant/**/abc")
    @ResponseBody
    public String antPath3() {
        return "/ant/**/abc";
    }
}
