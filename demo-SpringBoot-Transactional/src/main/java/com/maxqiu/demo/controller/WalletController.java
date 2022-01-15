package com.maxqiu.demo.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.maxqiu.demo.common.Result;
import com.maxqiu.demo.entity.Wallet;
import com.maxqiu.demo.exception.TransactionalException;
import com.maxqiu.demo.request.TransactionFlowRequest;
import com.maxqiu.demo.service.LogWalletService;
import com.maxqiu.demo.service.TransactionFlowService;
import com.maxqiu.demo.service.WalletService;

/**
 * 钱包账户 前端控制器
 *
 * @author Max_Qiu
 */
@RestController
@RequestMapping("/wallet")
public class WalletController {
    @Autowired
    private WalletService walletService;

    @Autowired
    private TransactionFlowService transactionFlowService;

    @Autowired
    private LogWalletService logWalletService;

    /**
     * 交易
     */
    @PostMapping("transaction")
    @Transactional(rollbackFor = TransactionalException.class)
    public Result<Object> transaction(@Valid @RequestBody TransactionFlowRequest request) throws TransactionalException {
        // 检查支出与收入账户
        Wallet expend = walletService.getById(request.getFromWalletId());
        Wallet income = walletService.getById(request.getToWalletId());
        if (expend == null) {
            throw new TransactionalException("支出账户不存在");
        }
        if (income == null) {
            throw new TransactionalException("收入账户不存在");
        }
        if (expend.getLock()) {
            throw new TransactionalException("支出账户已锁定");
        }
        if (income.getLock()) {
            throw new TransactionalException("收入账户已锁定");
        }
        if (expend.getMoney().compareTo(request.getMoney()) < 0) {
            throw new TransactionalException("支出账户余额不足");
        }
        // 完成交易
        walletService.transaction(request.getFromWalletId(), request.getToWalletId(), request.getMoney());
        // 保存交易流水
        long flowId = transactionFlowService.saveFlow(request.getFromWalletId(), request.getToWalletId(), request.getMoney());
        logWalletService.save(flowId, request.getFromWalletId(), 1, request.getMoney());
        logWalletService.save(flowId, request.getToWalletId(), 2, request.getMoney());
        return Result.success();
    }
}
