package com.maxqiu.demo.service;

import java.math.BigDecimal;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.maxqiu.demo.entity.TransactionFlow;
import com.maxqiu.demo.mapper.TransactionFlowMapper;

/**
 * 交易流水 服务类
 *
 * @author Max_Qiu
 */
@Service
public class TransactionFlowService extends ServiceImpl<TransactionFlowMapper, TransactionFlow> {
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
     */
    public Long saveFlow(Long fromWalletId, Long toWalletId, BigDecimal money) {
        TransactionFlow flow = new TransactionFlow();
        flow.setFromWalletId(fromWalletId);
        flow.setToWalletId(toWalletId);
        flow.setMoney(money);
        flow.insert();
        return flow.getId();
    }
}
