package com.maxqiu.demo.mapper;

import java.math.BigDecimal;

import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.maxqiu.demo.entity.Wallet;

/**
 * 钱包账户 Mapper 接口
 *
 * @author Max_Qiu
 */
public interface WalletMapper extends BaseMapper<Wallet> {
    /**
     * 支出
     *
     * @param id
     *            ID
     * @param money
     *            金额
     */
    boolean expend(@Param("id") Long id, @Param("money") BigDecimal money);

    /**
     * 收入
     *
     * @param id
     *            ID
     * @param money
     *            金额
     */
    boolean income(@Param("id") Long id, @Param("money") BigDecimal money);
}
