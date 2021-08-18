package com.maxqiu.demo.vo;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import javax.validation.constraints.AssertTrue;
import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Digits;
import javax.validation.constraints.Email;
import javax.validation.constraints.Future;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.Negative;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.URL;
import org.springframework.format.annotation.DateTimeFormat;

import lombok.Data;

/**
 * @author Max_Qiu
 */
@Data
public class NormalVO {
    /**
     * 不能为null
     */
    @NotNull
    private Integer id;

    /**
     * 不为null或者空
     */
    @NotEmpty
    private String notEmpty;

    /**
     * 大小
     */
    @Size(min = 6, max = 6)
    private String size;

    /**
     * 至少有一个非空白字符串
     */
    @NotBlank
    private String notBlank;

    /**
     * 判断标识符
     */
    @AssertTrue
    // @AssertFalse
    private Boolean flag;

    /**
     * 最大和最小值
     */
    @Max(100)
    @Min(10)
    private Integer number;

    /**
     * 最大和最小值（inclusive可设置是否包含边界值）
     */
    @DecimalMax(value = "100", inclusive = false)
    @DecimalMin(value = "10", inclusive = true)
    private Integer decimal;

    /**
     * 正数
     */
    @Positive
    private Integer positive;

    /**
     * 负数
     */
    @Negative
    private Integer negative;

    /**
     * 整数与小数的最大长度
     */
    @Digits(integer = 5, fraction = 3)
    private BigDecimal digits;

    /**
     * 将来的时间
     */
    @Future
    // 使用指定格式接收数据
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime future;

    /**
     * 过去的时间
     */
    @Past
    // 使用指定格式接收数据
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime past;

    /**
     * 邮箱
     */
    @Email
    private String email;

    /**
     * 正则
     *
     * 例：只能是数字和字母
     */
    @Pattern(regexp = "^[A-Za-z0-9]+$")
    private String pattern;

    /**
     * 是一个URL连接
     */
    @URL
    private String url;
}
