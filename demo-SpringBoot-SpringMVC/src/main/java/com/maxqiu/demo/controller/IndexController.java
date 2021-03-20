package com.maxqiu.demo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 首页
 * 
 * @author Max_Qiu
 */
@Controller
@RequestMapping("")
public class IndexController {
    @RequestMapping("")
    @ResponseBody
    public String index() {
        return "index";
    }
}
