package com.maxqiu.demo;

import java.math.BigDecimal;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.maxqiu.demo.entity.TransactionFlow;
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
        TransactionFlow flow = new TransactionFlow();
        flow.setFromWalletId(1L);
        flow.setToWalletId(3L);
        flow.setMoney(new BigDecimal(50));
        boolean save = false;
        try {
            save = transactionFlowService.saveFlow(flow);
        } catch (TransactionalException e) {
            System.out.println(e.getMessage());
        }
        System.out.println("交易结果：" + save);
    }
}
