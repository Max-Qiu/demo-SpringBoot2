package com.maxqiu.demo.service;

import java.math.BigDecimal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.maxqiu.demo.entity.Wallet;
import com.maxqiu.demo.exception.TransactionalException;
import com.maxqiu.demo.mapper.WalletMapper;

/**
 * 钱包账户 服务类
 *
 * @author Max_Qiu
 */
@Service
public class WalletService extends ServiceImpl<WalletMapper, Wallet> {
    @Autowired
    private LogWalletService logWalletService;

    /**
     * 交易
     *
     *
     * @param flowId
     *            流水ID
     * @param fromId
     *            支出钱包ID
     * @param toId
     *            收入钱包ID
     * @param money
     *            交易金额
     * @return 交易是否成功
     * @throws TransactionalException
     *             交易异常
     */
    public void transaction(Long flowId, Long fromId, Long toId, BigDecimal money) throws TransactionalException {
        // 检查支出与收入账户
        Wallet expend = getById(fromId);
        Wallet income = getById(toId);
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
        if (expend.getMoney().compareTo(money) < 0) {
            throw new TransactionalException("支出账户余额不足");
        }
        // 交易
        if (baseMapper.expend(fromId, money)) {
            logWalletService.save(flowId, fromId, 1, money);
        } else {
            throw new TransactionalException("支出失败");
        }
        if (baseMapper.income(toId, money)) {
            logWalletService.save(flowId, toId, 2, money);
        } else {
            throw new TransactionalException("收入失败");
        }
    }
}
