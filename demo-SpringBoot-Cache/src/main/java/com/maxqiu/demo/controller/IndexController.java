package com.maxqiu.demo.controller;

import javax.annotation.Resource;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.maxqiu.demo.service.IndexService;

/**
 * @author Max_Qiu
 */
@RestController
public class IndexController {
    @Resource
    private IndexService indexService;

    @GetMapping("/")
    public String index() {
        return indexService.index();
    }
}
