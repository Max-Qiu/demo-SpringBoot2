package com.maxqiu.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.maxqiu.demo.service.CacheService;

/**
 * @author Max_Qiu
 */
@RestController
public class CacheController {
    @Autowired
    private CacheService cacheableService;

    @GetMapping("/cache1")
    public String cache1() {
        return cacheableService.cache1();
    }

    @GetMapping("/cache2")
    public String cache2() {
        return cacheableService.cache2();
    }

    @GetMapping("/cache3")
    public String cache3() {
        return cacheableService.cache3();
    }

    @GetMapping("/key1")
    public String key1(String key) {
        return cacheableService.key1(key);
    }

    @GetMapping("/key2")
    public String key2(String key) {
        return cacheableService.key2(key);
    }

    @GetMapping("/key3")
    public String key3(String username, String password) {
        return cacheableService.key3(username, password);
    }

    @GetMapping("/sync")
    public String sync() {
        return cacheableService.sync();
    }

    @GetMapping("/condition")
    public String condition(Integer id) {
        return cacheableService.condition(id);
    }

    @GetMapping("/unless")
    public Integer unless() {
        return cacheableService.unless();
    }

    @GetMapping("/cachePut")
    public String cachePut() {
        return cacheableService.cachePut();
    }

    @GetMapping("/cacheEvict1")
    public String cacheEvict1() {
        cacheableService.cacheEvict1();
        return "1";
    }

    @GetMapping("/cacheEvict2")
    public String cacheEvict2() {
        cacheableService.cacheEvict2();
        return "1";
    }

    @GetMapping("/cacheEvict3")
    public String cacheEvict3(Integer key) {
        cacheableService.cacheEvict3(key);
        return "1";
    }

    @GetMapping("/cacheManager")
    public String cacheManager() {
        return cacheableService.cacheManager();
    }
}
