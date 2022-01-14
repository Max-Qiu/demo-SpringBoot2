package com.maxqiu.demo.request;

import java.math.BigDecimal;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * 交易流水
 *
 * @author Max_Qiu
 */
@Getter
@Setter
@NoArgsConstructor
public class TransactionFlowRequest {
    /**
     * 支出账户ID
     */
    @NotNull
    private Long fromWalletId;

    /**
     * 收入账户ID
     */
    @NotNull
    private Long toWalletId;

    /**
     * 交易金额
     */
    @NotNull
    @Positive
    private BigDecimal money;
}
