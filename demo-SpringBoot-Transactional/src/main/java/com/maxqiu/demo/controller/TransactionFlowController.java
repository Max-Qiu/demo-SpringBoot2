package com.maxqiu.demo.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.maxqiu.demo.exception.TransactionalException;
import com.maxqiu.demo.request.TransactionFlowRequest;
import com.maxqiu.demo.service.TransactionFlowService;

/**
 * 交易流水 前端控制器
 *
 * @author Max_Qiu
 */
@RestController
@RequestMapping("/transaction-flow")
public class TransactionFlowController {
    @Autowired
    private TransactionFlowService transactionFlowService;

    @PostMapping("create")
    public boolean create(@Valid @RequestBody TransactionFlowRequest request) throws TransactionalException {
        return transactionFlowService.saveFlow(request.getFromWalletId(), request.getToWalletId(), request.getMoney());
    }
}
