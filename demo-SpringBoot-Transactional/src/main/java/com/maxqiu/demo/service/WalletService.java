package com.maxqiu.demo.service;

import java.math.BigDecimal;

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
    /**
     * 交易
     *
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
    public void transaction(Long fromId, Long toId, BigDecimal money) throws TransactionalException {
        if (!baseMapper.expend(fromId, money)) {
            throw new TransactionalException("支出失败");
        }
        if (!baseMapper.income(toId, money)) {
            throw new TransactionalException("收入失败");
        }
    }
}
