package com.maxqiu.demo;

import java.math.BigDecimal;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.maxqiu.demo.exception.TransactionalException;
import com.maxqiu.demo.service.TransactionFlowService;

/**
 * @author Max_Qiu
 */
@SpringBootTest
public class TransactionalTest {
    @Autowired
    private TransactionFlowService transactionFlowService;

    @Test
    void test() {
        boolean save = false;
        try {
            save = transactionFlowService.saveFlow(1L, 3L, new BigDecimal(50));
        } catch (TransactionalException e) {
            System.out.println(e.getMessage());
        }
        System.out.println("交易结果：" + save);
    }
}
