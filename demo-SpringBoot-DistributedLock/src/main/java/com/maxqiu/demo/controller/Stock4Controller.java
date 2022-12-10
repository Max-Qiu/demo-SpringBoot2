package com.maxqiu.demo.controller;

import javax.annotation.Resource;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.maxqiu.demo.service.Stock4Service;

/**
 * 库存（Redisson） 前端控制器
 *
 * @author Max_Qiu
 */
@RestController
@RequestMapping("stock4/deduct")
public class Stock4Controller {
    @Resource
    private Stock4Service stock4Service;

    @GetMapping("lock")
    public String lock() {
        this.stock4Service.lock();
        return "success";
    }
}
