package com.maxqiu.demo.exception;

import lombok.Getter;

/**
 * 交易异常
 *
 * @author Max_Qiu
 */
@Getter
public class TransactionalException extends Exception {
    public TransactionalException(String message) {
        super(message);
    }
}
