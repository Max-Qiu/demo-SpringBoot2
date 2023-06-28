package com.maxqiu.demo.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.maxqiu.demo.common.CurrUser;
import com.maxqiu.demo.common.CurrUserVO;

/**
 * 首页
 *
 * @author Max_Qiu
 */
@RestController
public class IndexController {
    @RequestMapping("")
    public String index() {
        return "index";
    }

    @GetMapping("currUser")
    public CurrUserVO currUser(@CurrUser CurrUserVO userVO) {
        return userVO;
    }
}
