package com.maxqiu.demo;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assumptions.assumeFalse;
import static org.junit.jupiter.api.Assumptions.assumeTrue;
import static org.junit.jupiter.api.Assumptions.assumingThat;

import org.junit.jupiter.api.Test;

/**
 * 测试假设条件
 * 
 * @author Max_Qiu
 */
class Assumptions {
    // 环境
    private final String environment = "DEV";

    @Test
    void trueOrFalse() {
        // 判断当前环境，应当是 DEV
        assumeTrue("DEV".equals(environment));
        // 判断当前环境，不能是 PRO
        assumeFalse("PRO".equals(environment));
    }

    @Test
    void trueOrFalseWithMessage() {
        // 判断当前环境，应当是 DEV
        assumeTrue("DEV".equals(environment), "当前不是DEV环境");
        // 判断当前环境，不能是 PRO
        assumeFalse("DEV".equals(environment), "当前是PRO环境");
    }

    @Test
    void that() {
        // 指定环境下执行的断言
        assumingThat("DEV".equals(environment), () -> {
            System.out.println("当前是DEV环境，可以执行如下断言");
            assertEquals(2, 4 - 2);
        });

        // 任何环境下都执行的断言
        assertEquals(42, 6 * 7);
        System.out.println("执行结束");
    }
}
