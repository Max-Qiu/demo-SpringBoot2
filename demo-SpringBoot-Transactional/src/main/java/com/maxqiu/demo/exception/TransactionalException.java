package com.maxqiu.demo.exception;

import java.math.BigDecimal;

import lombok.Getter;

/**
 * 交易异常
 *
 * @author Max_Qiu
 */
@Getter
public class TransactionalException extends Exception {
    public TransactionalException(String message, Long fromId, Long toId, BigDecimal money) {
        super(message + "\t支出ID：" + fromId + "\t收入ID：" + toId + "\t金额：" + money);
    }
}
