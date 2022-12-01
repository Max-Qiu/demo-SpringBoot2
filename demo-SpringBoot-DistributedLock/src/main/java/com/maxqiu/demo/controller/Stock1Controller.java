package com.maxqiu.demo.controller;

import javax.annotation.Resource;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.maxqiu.demo.service.Stock1Service;

/**
 * 库存（变量） 前端控制器
 *
 * @author Max_Qiu
 */
@RestController
@RequestMapping("stock1/deduct")
public class Stock1Controller {
    @Resource
    private Stock1Service stock1Service;

    @GetMapping("normal")
    public String normal() {
        this.stock1Service.normal();
        return "success";
    }

    @GetMapping("sync")
    public String sync() {
        this.stock1Service.sync();
        return "success";
    }

    @GetMapping("lock")
    public String lock() {
        this.stock1Service.lock();
        return "success";
    }
}
