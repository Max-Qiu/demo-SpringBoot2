# SpringMVC：接收数据 - @PathVariable、@RequestParam、@RequestBody、@RequestHeader、@CookieValue

> 本文档整理自视频教程：[尚硅谷_SpringMVC视频教程](http://www.atguigu.com/download_detail.shtml?v=23)，并对其扩充

环境介绍：本文直接使用`Maven`环境以及`SpringBoot 2.4.3`，不同于视频教程中的先建立web项目再拷贝jar包

建立SpringBoot项目添加如下核心依赖即可

```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-web</artifactId>
</dependency>
```

# 简介

- `Spring MVC`通过分析处理方法的签名，将`HTTP`请求信息绑定到处理方法的相应入参中。
- `Spring MVC`对控制器处理方法签名的限制是很宽松的，几乎可以按喜欢的任何方式对方法进行签名。
- 必要时可以对方法及方法入参标注相应的注解（`@PathVariable`、`@RequestParam`、`@RequestHeader`等）

一个请求主要有如下部分

    <请求协议> <请求地址>
    <请求头>
    <请求主体>

举例如下：

    POST http://127.0.0.1:8080/value/requestParam1
    Request Headers
        User-Agent: Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/89.0.4389.90 Safari/537.36
        Accept: */*
        Host: 127.0.0.1:8080
        Accept-Encoding: gzip, deflate, br
        Connection: keep-alive
        Content-Type: multipart/form-data;
        Content-Length: 164
    Request Body
        username: "1"

# 请求地址中的参数

一般情况下，URL中的参数为少量参数，例如：

- `RESTful`风格的URL请求数据
- 普通的`GET`请求传入页码参数获取分页数据（用`POST`协议也可以传）

## `@PathVariable`映射`URL`绑定的占位符

通过`@PathVariable`可以将`URL`中占位符参数绑定到控制器处理方法的入参中

> 关于restful的参考资料

- [理解RESTful架构](http://www.ruanyifeng.com/blog/2011/09/restful.html)
- [RESTful API 最佳实践](http://www.ruanyifeng.com/blog/2018/10/restful-api-best-practices.html)

> 示例

```java
@Controller
@RequestMapping("value")
public class ValueController {
    /**
     * @PathVariable 可以映射 URL 中的占位符到目标方法的参数中
     */
    @RequestMapping("/pathVariable/{id}")
    @ResponseBody
    public String pathVariable(@PathVariable("id") Integer id) {
        return "PathVariable：" + id;
    }
}
```

访问：`http://127.0.0.1:8080/value/pathVariable/1`<br>返回：`PathVariable：1`

## `@RequestParam`获取`URL` `?`后面的参数

在处理方法入参处使用`@RequestParam`可以把请求参数传递给请求方法

- value：参数名
- required：是否必须。默认为`true`，表示请求参数中必须包含对应的参数，若不存在，将抛出异常

> 示例

```java
@Controller
@RequestMapping("value")
public class ValueController {
    /**
     * 使用 @RequestParam 映射请求参数。其中：
     * value 值为请求参数的参数名
     * required 设置该参数是否必须。默认为 true
     * defaultValue 若不传时请求参数的默认值
     */
    @RequestMapping("urlParam1")
    @ResponseBody
    public String urlParam1(@RequestParam(value = "username") String username,
        @RequestParam(value = "age", defaultValue = "0") Integer age) {
        return "urlParam1：{username:" + username + ", age: " + age + "}";
    }

    /**
     * 若参数名和变量名相同，可以不写参数名
     */
    @RequestMapping("urlParam2")
    @ResponseBody
    public String urlParam2(@RequestParam String username,
        @RequestParam(required = false, defaultValue = "0") Integer age) {
        return "urlParam2：{username:" + username + ", age: " + age + "}";
    }

    /**
     * 若参数名和变量名相同，甚至可以不写 @RequestParam，且不写时不传值不会报错
     */
    @RequestMapping("urlParam3")
    @ResponseBody
    public String urlParam3(String username, Integer age) {
        return "urlParam3：{username:" + username + ", age: " + age + "}";
    }
}
```

PS：下方表格中的`http://127.0.0.1:8080/value/`已省略

请求 | 返回 | 说明
---|---|---
urlParam1?username=tom&age=18 | urlParam1：{username:tom, age: 18} |
urlParam1?username=tom | urlParam1：{username:tom, age: 0} | age设置了默认值为0
urlParam1 | 400 | 没有username参数
urlParam2?username=tom&age=18 | urlParam2：{username:tom, age: 18} | 未设置`value`，但是username自动映射到变量名
urlParam2?username=tom | urlParam2：{username:tom, age: 0} | 虽然设置了非必填，但是默认值仍在
urlParam3?username=tom&age=18 | urlParam3：{username:tom, age: 18} | 未设置`@RequestParam`，仍可以获取到值
urlParam3 | urlParam3：{username:null, age: null} | 不传值，不报错，值为null

# 请求主体中的参数

## `@RequestParam`获取`multipart/form-data`表单数据

`@RequestParam`使用规则同上

> 示例

```java
@Controller
@RequestMapping("value")
public class ValueController {
    /**
     * 获取 multipart/form-data 表单 body 内的参数，规则同上
     */
    @RequestMapping("bodyParam1")
    @ResponseBody
    public String bodyParam1(@RequestParam String username, Integer age) {
        return "bodyParam1：{username:" + username + ", age: " + age + "}";
    }
}
```

使用`Postman`测试如下

![](https://cdn.maxqiu.com/upload/7eb13d68e9b14890973812ea69955c22.jpg)

## `@RequestParam`获取`application/x-www-form-urlencoded`表单数据

`@RequestParam`使用规则同上。但是只支持`POST`协议

> 示例

```java
@Controller
@RequestMapping("value")
public class ValueController {
    /**
     * 获取 application/x-www-form-urlencoded 表单 body 内的参数，规则同上
     */
    @PostMapping("bodyParam2")
    @ResponseBody
    public String bodyParam2(@RequestParam String username, Integer age) {
        return "bodyParam2：{username:" + username + ", age: " + age + "}";
    }
}
```

使用`Postman`测试如下

![](https://cdn.maxqiu.com/upload/53aa02c3841f4760981705c1175b2af4.jpg)

## `@RequestBody`获取`raw`文本数据

当请求主体内传入一段文字时（Text、Json等），需要使用`@RequestBody`获取字符串数据

> 示例

```java
@Controller
@RequestMapping("value")
public class ValueController {
    /**
     * 获取 raw 字符串数据
     */
    @RequestMapping("bodyParam3")
    @ResponseBody
    public String bodyParam3(@RequestBody String text) {
        return "bodyParam3：{text:" + text + "}";
    }
}
```

使用`Postman`测试如下

![](https://cdn.maxqiu.com/upload/f34b17ad74964652a682a9ec6f117f1c.jpg)

# 获取`POJO`对象

通常的，当接收的字段较多是，可以将所有的参数封装成一个`POJO`对象，`Spring MVC`会按请求参数名和`POJO`属性名进行自动匹配，自动为该对象填充属性值。支持级联属性。如：address.province、address.city 等

## `@RequestParam`获取`POJO`对象

既可以获取`URL` `?`后面的数据，也可以获取两种表单数据

> 示例

```java
/**
 * 用户
 */
@Getter
@Setter
public class User {
    /**
     * 姓名
     */
    private String name;
    /**
     * 年龄
     */
    private Integer age;
    /**
     * 地址
     */
    private Address address;
}

/**
 * 地址
 */
@Getter
@Setter
public class Address {
    /**
     * 省
     */
    private String province;
    /**
     * 市
     */
    private String city;
}


@Controller
@RequestMapping("value")
public class ValueController {
    /**
     * 获取 URL 或 表单 数据，此处省略了 @RequestParam 注解
     */
    @RequestMapping("/pojo")
    @ResponseBody
    public User pojo(User user) {
        return user;
    }
}
```

- 方式1：`URL` `?`后面的参数<br>访问：`http://127.0.0.1:8080/value/pojo?name=tom&age=18&address.province=上海&address.city=闵行`<br>返回：`{"name":"tom","age":18,"address":{"province":"上海","city":"闵行"}}`
- 方式2：`multipart/form-data`表单数据<br>![](https://cdn.maxqiu.com/upload/151407c10b214e87922bb0301762873f.jpg)
- 方式3：`application/x-www-form-urlencoded`表单数据（仅支持`POST`）<br>![](https://cdn.maxqiu.com/upload/6bdb77d642f2490c9573bda9a5e89186.jpg)

## `@RequestBody`获取`json`文本数据自动转换为`POJO`对象

仅支持`json`格式的文本，即`Content-Type`为`application/json`

> 示例

```java
// 注：实体同上

@Controller
@RequestMapping("value")
public class ValueController {
    /**
     * 获取 JSON 数据
     */
    @RequestMapping("/pojo2")
    @ResponseBody
    public User pojo2(@RequestBody User user) {
        return user;
    }
}
```

使用`Postman`请求示例如下：

![](https://cdn.maxqiu.com/upload/56b30f5b9260451683ae0097a6460374.jpg)

# 其他参数

## `@RequestHeader`绑定请求报头的属性值

请求头包含了若干个属性，服务器可据此获知客户端的信息，通过`@RequestHeader`即可将请求头中的属性值绑定到处理方法的入参中

> 示例

```java
@Controller
@RequestMapping("value")
public class ValueController {
    /**
     * 使用 @RequestHeader 映射请求头信息，用法同 @RequestParam
     */
    @RequestMapping("/requestHeader")
    @ResponseBody
    public String requestHeader(@RequestHeader("Accept-Language") String al) {
        return "requestHeader：Accept-Language: " + al;
    }
}
```

访问：`http://127.0.0.1:8080/value/requestHeader`<br>返回：`requestHeader：Accept-Language: zh-CN,zh;q=0.9`

## `@CookieValue`绑定请求中的Cookie值

`@CookieValue`可让处理方法入参绑定某个`Cookie`值

```java
@Controller
@RequestMapping("value")
public class ValueController {
    /**
     * 使用 @CookieValue 映射一个 Cookie 值，用法同 @RequestParam
     */
    @RequestMapping("/cookieValue")
    @ResponseBody
    public String cookieValue(@CookieValue("test") String test) {
        return "cookieValue: test: " + test;
    }
}
```

首先：在浏览器控制台内存入一个cookie：`document.cookie="test=aaa;"`<br>访问：`http://127.0.0.1:8080/value/cookieValue`<br>返回：`cookieValue: test: aaa`

# Servlet API 作为入参

可以使用 Servlet 原生的 API 作为目标方法的参数，具体支持以下类型，扩展可以从 HttpServletRequest 或 HttpServletResponse 获取

- 常用：
  - javax.servlet.http.HttpServletRequest
  - javax.servlet.http.HttpServletResponse
- 扩展
  - javax.servlet.http.HttpSession
  - java.security.Principal
  - java.util.Locale;
  - javax.servlet.ServletInputStream;
  - javax.servlet.ServletOutputStream;
  - java.io.BufferedReader;
  - java.io.PrintWriter;

> 示例

```java
@Controller
@RequestMapping("value")
public class ValueController {
    @RequestMapping("/servletApi")
    public void servletApi(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.getWriter().write("servletApi:" + request + "," + response);
    }
}
```

访问：`http://127.0.0.1:8080/value/servletApi`<br>返回：`servletApi:org.apache.catalina.connector.RequestFacade@73b8a868,org.apache.catalina.connector.ResponseFacade@62044aea`
