package com.maxqiu.demo.controller;

import javax.annotation.Resource;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.maxqiu.demo.service.Stock3Service;

/**
 * 库存（Redis） 前端控制器
 *
 * @author Max_Qiu
 */
@RestController
@RequestMapping("stock3/deduct")
public class Stock3Controller {
    @Resource
    private Stock3Service stock3Service;

    @GetMapping("normal")
    public String normal() {
        this.stock3Service.normal();
        return "success";
    }

    @GetMapping("watch")
    public String watch() {
        this.stock3Service.watch();
        return "success";
    }

    @GetMapping("lock")
    public String lock() {
        this.stock3Service.lock();
        return "success";
    }
}
