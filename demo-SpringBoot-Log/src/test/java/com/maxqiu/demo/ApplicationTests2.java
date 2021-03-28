package com.maxqiu.demo;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import lombok.extern.slf4j.Slf4j;

@SpringBootTest
@Slf4j
class ApplicationTests2 {
    @Test
    public void contextLoads() {
        log.trace("这是trace日志...");
        log.debug("这是debug日志...");
        log.info("这是info日志...");
        log.warn("这是warn日志...");
        log.error("这是error日志...");
    }
}
