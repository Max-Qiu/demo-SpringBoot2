# 依赖

```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-validation</artifactId>
</dependency>
```

# 注解

## 启用验证注解

注解 | 说明
---|---
`javax.validation.Valid` | `JSR303`标准的注解
`org.springframework.validation.annotation.Validated` | `JSR-303`的变体。为支持`Spring`的`JSR-303`支持

参考文档：[@Validated和@Valid的区别？教你使用它完成Controller参数校验（含级联属性校验）以及原理分析【享学Spring】](https://blog.csdn.net/f641385712/article/details/97621783)

## 常用约束条件注解

以下注解都在`javax.validation.constraints`包（`JSR303`）中，此外还有`org.hibernate.validator.constraints`包（`Hibernate`）中的`@URL`比较常用

注解 | 说明 | 被约束类型 | 是否可以为`null`
---|---|---|---
`@Null` | 必须为`null` | 所有类型 | 必须为`null`
`@NotNull` | 不能为`null` | 所有类型 | 不能为`null`
`@NotEmpty` | 不能为`null`或`空` | CharSequence（字符串长度）<br>Collection（集合大小）<br>Map（Map大小）<br>Array（数组大小） | 不能为`null`
`@Size` | 大小 | 同上 | 可以为`null`
`@NotBlank` | 至少包含一个非空白字符 | CharSequence（字符串） | 不能为`null`
`@AssertTrue`、`@AssertFalse` | 只能为`true`或`false` |  boolean、Boolean | 可以为`null`
`@Max`、`@Min` | 最大最小值<br>注解参数是`long` | BigDecimal<br>BigInteger<br>byte、short、int、long、和各自的包装类<br>由于舍入错误，double和float不受支持 | 可以为`null`
`@DecimalMax`、`@DecimalMin` | 最大最小值<br>注解参数是`String`<br>额外`inclusive`属性判断是否包含当前值 | 同上 | 可以为`null`
`@Positive` | 正数 | 同上 | 可以为`null`
`@PositiveOrZero` | 正数或0 | 同上 | 可以为`null`
`@Negative` | 负数 | 同上 | 可以为`null`
`@NegativeOrZero` | 负数或0 | 同上 | 可以为`null`
`@Digits` | 数值整数与小数的最大**位数** | 同上 | 可以为`null`
`@Future` | 将来的时间 | java.util.Date<br>java.util.Calendar<br>java.time.Instant<br>java.time.LocalDate<br>java.time.LocalDateTime<br>java.time.LocalTime<br>java.time.MonthDay<br>java.time.OffsetDateTime<br>java.time.OffsetTime<br>java.time.Year<br>java.time.YearMonth<br>java.time.ZonedDateTime<br>java.time.chrono.HijrahDate<br>java.time.chrono.JapaneseDate<br>java.time.chrono.MinguoDate<br>java.time.chrono.ThaiBuddhistDate | 可以为`null`
`@FutureOrPresent` | 将来或现在的时间 | 同上 | 可以为`null`
`@Past` | 过去的时间 | 同上 | 可以为`null`
`@PastOrPresent` | 过去或现在的时间 | 同上 | 可以为`null`
`@Email` | 邮箱（不太好用，建议正则） | String | 可以为`null`
`@Pattern` | 正则 | CharSequence（字符串） | 不能为`null`
`@URL` | URL地址（不太好用，建议正则） | String | 可以为`null`

> 注：以上标注`可以为null`的（即`null`值是合法的），若必须有值，需要再添加`@NotNull`注解

# 使用校验

## 一般校验

### 普通参数校验

1. 在字段上添加校验规则
2. 在类上添加`@Validated`或`@Valid`

```java
@RestController
@Validated
public class IndexController {
    /**
     * 例：校验邮箱与验证码
     */
    @GetMapping("code")
    public String code(@Email @NotBlank String email,
        @Size(min = 6, max = 6, message = "验证码为6位") @NotBlank String code) {
        // 邮箱和验证码正确性校验：略
        return email + "\t" + code;
    }
}
```

### 实体对象校验

1. 在实体中添加校验规则
2. 在方法的参数添加`@Validated`或`@Valid`

```java
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
```

```java
@RestController
public class IndexController {
    /**
     * 实体校验
     */
    @PostMapping("normal")
    public NormalVO normal(@Validated NormalVO vo) {
        return vo;
    }
}
```

### 嵌套实体校验

1. 在实体中添加校验规则
2. 在方法的参数添加`@Validated`或`@Valid`
3. 在父实体的子实体属性上添加`@Valid`注解

```java
@RestController
public class IndexController {
    /**
     * 嵌套实体验证
     */
    @PostMapping("nest")
    public UserVO nest(@Validated @RequestBody UserVO user) {
        return user;
    }
}
```

```java
@Data
public class UserVO {
    @NotNull
    private Integer id;
    @NotBlank
    private String name;
    @Valid
    @NotNull
    private AddressVO address;
}
```

```java
@Data
public class AddressVO {
    @NotBlank
    private String province;
    @NotBlank
    private String city;
}
```

## 分组校验

## 自定义校验

# 异常处理

## 方法级异常处理

## 全局异常处理