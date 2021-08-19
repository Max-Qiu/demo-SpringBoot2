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
`org.springframework.validation.annotation.Validated` | `JSR-303`的变体。支持`Spring`的`JSR-303`

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

1. 以上标注`可以为null`的（即`null`是合法值），若必须有值，需要再添加`@NotNull`注解
2. 以上注解均可添加`message`属性用于自定义错误消息

# 使用校验

## 一般校验

### 普通参数校验

1. 在字段上添加校验规则
2. 在类上添加`@Validated`或`@Valid`
3. 在验证注解内使用`message`字段添加自定义的校验语句

```java

@RestController
@Validated
public class IndexController {
    /**
     * 例：校验邮箱与验证码
     */
    @GetMapping("code")
    public String code(@Email @NotBlank(message = "邮箱不能为空！") String email,
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
@RequestMapping("vo")
public class VoController {
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
@RequestMapping("vo")
public class VoController {
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

有时，需要对不同场景添加不同的校验规则（例如：新增和修改），此时可以使用`Spring`的`JSR-303`分组功能

1. 新增分组校验接口（空的接口，无需任何方法）
2. 在方法的参数添加`@Validated`，并在注解内添加分组校验接口（可添加多个）
3. 在实体的对应的字段的校验注解中添加`groups`属性并指定分组校验接口（可添加多个）
4. 注：未添加分组校验接口的校验注解不会生效

> 示例如下：

分组接口

```java
public interface AddValidGroup {}

public interface UpdateValidGroup {}
```

指定校验分组

```java
@RestController
@RequestMapping("group")
public class GroupController {
    @PostMapping("add")
    public EmployeeVO add(@Validated(AddValidGroup.class) EmployeeVO vo) {
        return vo;
    }

    @PostMapping("update")
    public EmployeeVO update(@Validated(UpdateValidGroup.class) EmployeeVO vo) {
        return vo;
    }
}
```

校验注解添加校验分组属性

```java

@Data
public class EmployeeVO {
    /**
     * id
     */
    @Null(groups = AddValidGroup.class, message = "新增时ID必须为null")
    @NotNull(groups = UpdateValidGroup.class, message = "修改时员工ID不能为空")
    private Integer id;

    /**
     * 姓名
     */
    @NotBlank(groups = {AddValidGroup.class, UpdateValidGroup.class}, message = "姓名不能为空")
    private String name;

    /**
     * 手机号
     *
     * 例：若不加分组，则不进行校验
     */
    @NotBlank
    private String phone;
}
```

## 自定义校验

有时，系统自带的校验器不能满足使用需求，此时可以自定义校验规则，完成校验

1. 参考系统自带的校验注解，编写自定义的校验注解
2. 编写校验注解对应的校验器
3. 编写校验失败默认的提示语句
4. 使用校验注解

> 以校验状态字段为例，示例如下：

自定义校验注解，可以参考`JSR-303`的校验注解

```java
/**
 * 值在指定的List中
 */
@Documented
@Constraint(validatedBy = {ListValueValidator.class})
@Target({METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER, TYPE_USE})
@Retention(RUNTIME)
public @interface ListValue {
    String message() default "{com.maxqiu.demo.valid.constraints.ListValue.message}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    int[] valueList() default {};
}
```

自定义校验注解对应的校验器，可以参考`ConstraintValidator`接口的实现类

```java
/**
 * ListValue校验器
 */
public class ListValueValidator implements ConstraintValidator<ListValue, Integer> {
    private Set<Integer> set = new HashSet<>();

    /**
     * 初始化
     */
    @Override
    public void initialize(ListValue constraintAnnotation) {
        // 将注解中的合法值取出，放在set中
        for (int val : constraintAnnotation.valueList()) {
            set.add(val);
        }
    }

    /**
     * 判断是否校验成功
     *
     * @param value
     *            需要校验的值
     */
    @Override
    public boolean isValid(Integer value, ConstraintValidatorContext context) {
        return set.contains(value);
    }
}
```

编写默认的校验失败提示语句

1. 新建`ValidationMessages.properties`文件，放在项目的`resources`目录下
2. 添加内容`com.maxqiu.demo.valid.constraints.ListValue.message=\u5FC5\u987B\u63D0\u4EA4\u6307\u5B9A\u7684\u503C`
    1. 内容的键就是自定义校验注解的`message`值
    2. 内容的值就是提示的内容，直接写中文也可以，建议进行`Unicode编码转换`

使用注解（支持使用分组）

```java

@Data
public class EmployeeVO {
    /**
     * id
     */
    @NotNull(groups = ChangeStatusValidGroup.class, message = "修改时员工ID不能为空")
    private Integer id;

    /**
     * 状态
     */
    @ListValue(valueList = {0, 1}, groups = ChangeStatusValidGroup.class)
    private Integer status;
}
```

```java
@RestController
@RequestMapping("custom")
public class CustomController {
    /**
     * 修改状态
     */
    @PostMapping("status")
    public EmployeeVO status(@Validated(ChangeStatusValidGroup.class) EmployeeVO vo) {
        return vo;
    }
}
```

# 校验异常处理

默认情况下，校验出错后的返回结果不能符合业务需求，所以需要自定义返回结果

## 局部异常处理

`Spring`提供了`BindingResult`用于接收校验异常结果，只需要在被校验的实体后面紧跟着一个`BindingResult`即可获取校验失败结果。示例如下：L

```
@GetMapping("exception")
public Object exception(@Validated NormalVO vo, BindingResult result) {
    if (result.hasErrors()) {
        Map<String, String> map = new HashMap<>();
        // 获取校验的错误结果并遍历
        result.getFieldErrors().forEach((item) -> {
            // 获取错误的属性的名字和错误提示
            map.put(item.getField(), item.getDefaultMessage());
        });
        return Result.error(500, map);
    }
    return vo;
}
```

## 全局异常处理

- `Spring`提供了`ControllerAdvice`和`ExceptionHandler`注解用于捕获`Controller`抛出的异常
- 普通参数和实体参数校验异常不一样，需要分开处理
- 局部异常处理覆盖全局异常处理（局部处理完成，全局这边捕获不到异常）

> 示例：处理全局异常，并返回`json`

```
/**
 * 集中处理所有异常
 *
 * @author Max_Qiu
 */
@Slf4j
// @ResponseBody
// @ControllerAdvice(basePackages = "com.maxqiu.demo.controller")
@RestControllerAdvice(basePackages = "com.maxqiu.demo.controller")
public class ExceptionControllerAdvice {
    /**
     * 处理方法的普通参数校验异常
     */
    @ExceptionHandler(value = ConstraintViolationException.class)
    public Result handleValidException(ConstraintViolationException e) {
        Map<String, String> errorMap = new HashMap<>();
        for (ConstraintViolation<?> constraintViolation : e.getConstraintViolations()) {
            String field = "";
            for (Path.Node node : constraintViolation.getPropertyPath()) {
                field = node.getName();
            }
            errorMap.put(field, constraintViolation.getMessage());
        }
        return Result.error(500, errorMap);
    }

    /**
     * 处理方法的实体参数校验异常
     */
    @ExceptionHandler(value = BindException.class)
    public Result handleValidException(BindException e) {
        Map<String, String> errorMap = new HashMap<>();
        e.getBindingResult().getFieldErrors().forEach(r -> errorMap.put(r.getField(), r.getDefaultMessage()));
        return Result.error(500, errorMap);
    }

    @ExceptionHandler(value = Throwable.class)
    public Result handleException(Throwable throwable) {
        log.error("其他异常：{}\n异常类型：{}", throwable.getMessage(), throwable.getClass());
        return Result.error();
    }
}
```
