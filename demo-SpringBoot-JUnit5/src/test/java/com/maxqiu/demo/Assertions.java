package com.maxqiu.demo;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertIterableEquals;
import static org.junit.jupiter.api.Assertions.assertLinesMatch;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTimeout;
import static org.junit.jupiter.api.Assertions.assertTimeoutPreemptively;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import java.time.Duration;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;

/**
 * 测试断言
 * 
 * @author Max_Qiu
 */
class Assertions {
    @Test
    public void simple() {
        // 判断两个对象或两个原始类型是否相等
        assertEquals(3, 1 + 2);
        // 判断两个对象或两个原始类型是否不相等
        assertNotEquals(3, 1 + 1);
        // 判断两个对象引用是否指向同一个对象
        Object obj = new Object();
        assertSame(obj, obj);
        // 判断两个对象引用是否指向不同的对象
        assertNotSame(obj, new Object());
        // 判断给定的布尔值是否为 true
        assertTrue(1 < 2);
        // 判断给定的布尔值是否为 false
        assertFalse(1 > 2);
        // 判断给定的对象引用是否为 null
        assertNull(null);
        // 判断给定的对象引用是否不为 null
        assertNotNull(obj);
    }

    @Test
    public void simpleWithMessage() {
        // 断言失败时指定消息
        assertEquals(3, 2 + 2, "自定义断言失败消息");
    }

    @Test
    public void array() {
        // 判断数组是否相同
        assertArrayEquals(new int[] {1, 2}, new int[] {1, 2});
        // 判断Iterable是否相同
        assertIterableEquals(Arrays.asList(1, 2, 3), Arrays.asList(1, 2, 3));
        // 判断字符串列表
        List<String> list1 = Arrays.asList("1", "2", "3");
        List<String> list2 = Arrays.asList("1", "2", "3");
        assertLinesMatch(list1, list2);
    }

    @Test
    public void all() {
        // 组合断言（可任意个Executable）
        assertAll(() -> assertEquals(2, 1 + 1), () -> assertTrue(1 > 0));
    }

    @Test
    public void exceptionTest() {
        // 不抛出异常
        assertDoesNotThrow(() -> System.out.println(1 % 1), "抛出异常了");
        // 抛出指定异常
        assertThrows(ArithmeticException.class, () -> System.out.println(1 % 0));
    }

    @Test
    public void timeout() {
        // 如果测试方法时间超过指定时间将会失败（需要等方法执行结束再判断）
        assertTimeout(Duration.ofMillis(1000), () -> Thread.sleep(2000));
        // 如果测试方法时间超过指定时间将会失败（超过指定时间时立即失败，不等待方法执行结束）
        assertTimeoutPreemptively(Duration.ofMillis(1000), () -> Thread.sleep(2000));
    }

    @Test
    public void fastFail() {
        // 快速失败（直接调用，使当前测试方法失败）
        fail();
    }
}
