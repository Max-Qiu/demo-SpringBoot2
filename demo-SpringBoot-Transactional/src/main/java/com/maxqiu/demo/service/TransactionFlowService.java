package com.maxqiu.demo.service;

import java.math.BigDecimal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.maxqiu.demo.entity.TransactionFlow;
import com.maxqiu.demo.exception.TransactionalException;
import com.maxqiu.demo.mapper.TransactionFlowMapper;

/**
 * 交易流水 服务类
 *
 * @author Max_Qiu
 */
@Service
public class TransactionFlowService extends ServiceImpl<TransactionFlowMapper, TransactionFlow> {
    @Autowired
    private WalletService walletService;

    /**
     * 保存流水
     *
     * @param fromWalletId
     *            支出钱包ID
     * @param toWalletId
     *            收入钱包ID
     * @param money
     *            交易金额
     * @return 是否交易成功
     * @throws TransactionalException
     *             交易异常
     */
    @Transactional(rollbackFor = TransactionalException.class)
    public boolean saveFlow(Long fromWalletId, Long toWalletId, BigDecimal money) throws TransactionalException {
        TransactionFlow flow = new TransactionFlow();
        flow.setFromWalletId(fromWalletId);
        flow.setToWalletId(toWalletId);
        flow.setMoney(money);
        boolean insert = flow.insert();
        if (!insert) {
            throw new TransactionalException("交易流水保存异常！");
        }
        walletService.transaction(flow.getId(), fromWalletId, toWalletId, money);
        return true;
    }
}
