参考链接：[HandlerMethodArgumentResolver(四)：自定参数解析器处理特定场景需求，介绍PropertyNamingStrategy的使用【享学Spring MVC】](https://fangshixiang.blog.csdn.net/article/details/100183979)

# 前言

日常在写后端接口时，经常会写如下代码，用于获取当前用户

```java
@Autowired
private UserService userService;

@GetMapping("xxx")
public String test(@RequestHeader("Authorization") String token){
    // 根据token获取当前用户
    User user = userService.getUserByToken(token);
    return "xxx";
}
```

实际上，`SpringMVC`提供了`HandlerMethodArgumentResolver`接口，用于添加自定义参数解析器，方便在方法的参数中获取当前用户

# 示例代码

> 注解

```java
/**
 * 自定义接口，用户标记当前方法的参数
 */
@Documented
@Target({ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface CurrUser {}
```

> 实体

```java
/**
 * 用户信息实体
 */
@Getter
@Setter
public class CurrUserVo {
    private Long id;
    private String name;
}
```

> 自定义解析器

```java
/**
 * 自定义解析器，用于处理类型是CurrUserVo的参数
 */
public class CurrUserArgumentResolver implements HandlerMethodArgumentResolver {
    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        // 只有标注有CurrUser注解，并且数据类型是CurrUserVo的才给与处理
        CurrUser ann = parameter.getParameterAnnotation(CurrUser.class);
        Class<?> parameterType = parameter.getParameterType();
        return ann != null && CurrUserVo.class.isAssignableFrom(parameterType);
    }

    @Override
    public CurrUserVo resolveArgument(MethodParameter parameter, ModelAndViewContainer container,
        NativeWebRequest webRequest, WebDataBinderFactory binderFactory) {
        HttpServletRequest request = webRequest.getNativeRequest(HttpServletRequest.class);
        // 从请求头中拿到token
        String token = request.getHeader("Authorization");
        if (ObjectUtils.isEmpty(token)) {
            // 此处不做异常处理，校验token因放在拦截器中处理
            return null;
        }
        // 此处作为测试写死一个用户，实际使用时调用service获取当前用户
        CurrUserVo userVo = new CurrUserVo();
        userVo.setId(1L);
        userVo.setName("tom");
        return userVo;
    }
}
```

> `WebMvc`配置

```java
/**
 * MVC配置类
 */
@Configuration
@EnableWebMvc
public class WebMvcConfig implements WebMvcConfigurer {
    /**
     * 将自定义解析器注入bean
     */
    @Bean
    public CurrUserArgumentResolver currUserArgumentResolver() {
        return new CurrUserArgumentResolver();
    }

    /**
     * 添加自定义解析器
     */
    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> argumentResolvers) {
        argumentResolvers.add(currUserArgumentResolver());
    }
}
```

# 测试

```java
@RestController
public class IndexController {
    @GetMapping("currUser")
    public CurrUserVo currUser(@CurrUser CurrUserVo currUser) {
        return currUser;
    }
}
```

![](https://cdn.maxqiu.com/upload/8386b58f5dc64a2face3077959812aae.jpg)


> 补充

如果实际使用时只需要获取用户ID，则（以`Long`类型为例）

- `CurrUserArgumentResolver`
    - `supportsParameter`中的`CurrUserVo.class`改为`Long.class`
    - `resolveArgument`的返回参数修改为`Long`，且**获取用户**的代码直接返回用户ID
- `Controller`
    - 方法的入参改为`@CurrUser Long currUserId`
