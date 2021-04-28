package com.maxqiu.demo;

import java.util.concurrent.TimeUnit;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;

/**
 * 测试常用注解
 */
@DisplayName("测试常用注解")
class Annotations {
    @Test
    void test() {
        System.out.println("test");
    }

    @Test
    @Disabled("不用了")
    void disabled() {
        System.out.println("disabled");
    }

    @Test
    @DisplayName("自定义名称")
    void displayName() {
        System.out.println("displayName");
    }

    // @RepeatedTest(value = 3) // 不设置name时，value可以省略
    @RepeatedTest(3)
    void repeatedTest1() {
        System.out.println("repeatedTest1");
    }

    @RepeatedTest(value = 3, name = "{displayName}：总计{totalRepetitions}次，当前第{currentRepetition}次")
    @DisplayName("测试RepeatedTest")
    void repeatedTest2() {
        System.out.println("repeatedTest2");
    }

    @BeforeEach
    void beforeEach() {
        System.out.println("beforeEach");
    }

    @AfterEach
    void afterEach() {
        System.out.println("afterEach");
    }

    @BeforeAll
    static void beforeAll() {
        System.out.println("beforeAll");
    }

    @AfterAll
    static void afterAll() {
        System.out.println("afterAll");
    }

    @Test
    @Timeout(value = 2000, unit = TimeUnit.MILLISECONDS)
    void timeout() {
        System.out.println("timeout");
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Test
    @Tag("taxes")
    void testingTaxCalculation() {
        System.out.println("123");
    }
}
