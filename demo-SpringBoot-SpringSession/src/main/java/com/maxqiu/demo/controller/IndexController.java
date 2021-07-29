package com.maxqiu.demo.controller;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Session读写
 *
 * @author Max_Qiu
 */
@RestController
public class IndexController {
    @Value("${server.port}")
    private String port;

    @RequestMapping("get")
    public String get(HttpSession session) {
        String name = (String)session.getAttribute("name");
        return "port:" + port + "\tname:" + (name != null ? name : "null");
    }

    @RequestMapping("set")
    public String set(HttpSession session) {
        session.setAttribute("name", "max");
        return "ok";
    }
}
