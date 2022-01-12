package com.maxqiu.demo.service;

import java.math.BigDecimal;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.maxqiu.demo.entity.LogWallet;
import com.maxqiu.demo.mapper.LogWalletMapper;

/**
 * 钱包日志 服务类
 *
 * @author Max_Qiu
 */
@Service
public class LogWalletService extends ServiceImpl<LogWalletMapper, LogWallet> {
    /**
     * 保存日志
     *
     * @param flowId
     *            流水ID
     * @param walletId
     *            钱包ID
     * @param type
     *            交易类型 1支出 2收入
     * @param money
     *            金额
     */
    public void save(Long flowId, Long walletId, int type, BigDecimal money) {
        new LogWallet().setFlowId(flowId).setWalletId(walletId).setType(type).setMoney(money).insert();
    }
}
