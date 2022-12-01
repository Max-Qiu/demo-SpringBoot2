package com.maxqiu.demo.controller;

import javax.annotation.Resource;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.maxqiu.demo.service.Stock2Service;

/**
 * 库存（MySQL） 前端控制器
 *
 * @author Max_Qiu
 */
@RestController
@RequestMapping("stock2/deduct")
public class Stock2Controller {
    @Resource
    private Stock2Service stock2Service;

    @GetMapping("normal")
    public String normal() {
        this.stock2Service.normal();
        return "success";
    }

    @GetMapping("lock")
    public String lock() {
        this.stock2Service.lock();
        return "success";
    }

    @GetMapping("transactional")
    public String transactional() {
        this.stock2Service.transactional();
        return "success";
    }

    @GetMapping("oneSql")
    public String oneSql() {
        this.stock2Service.oneSql();
        return "success";
    }

    @GetMapping("selectForUpdate")
    public String selectForUpdate() {
        this.stock2Service.selectForUpdate();
        return "success";
    }

    @GetMapping("version")
    public String version() {
        this.stock2Service.version();
        return "success";
    }
}
