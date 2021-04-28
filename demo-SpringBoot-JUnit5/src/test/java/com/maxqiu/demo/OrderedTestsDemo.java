package com.maxqiu.demo;

import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

/**
 * @author Max_Qiu
 */
@TestMethodOrder(OrderAnnotation.class)
class OrderedTestsDemo {
    @Test
    @Order(1)
    void nullValues() {
        System.out.println("nullValues");
    }

    @Test
    @Order(2)
    void emptyValues() {
        System.out.println("emptyValues");
    }

    @Test
    @Order(3)
    void validValues() {
        System.out.println("validValues");
    }
}