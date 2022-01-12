package com.maxqiu.demo.service;

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

    @Transactional(rollbackFor = TransactionalException.class)
    public boolean saveFlow(TransactionFlow flow) throws TransactionalException {
        boolean insert = flow.insert();
        if (!insert) {
            throw new TransactionalException("交易流水保存异常！", flow.getFromWalletId(), flow.getToWalletId(),
                flow.getMoney());
        }
        walletService.transaction(flow.getId(), flow.getFromWalletId(), flow.getToWalletId(), flow.getMoney());
        return true;
    }
}
