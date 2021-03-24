# SpringMVC：路由映射 - @RequestMapping 注解详解

> 本文档整理自视频教程：[尚硅谷_SpringMVC视频教程](http://www.atguigu.com/download_detail.shtml?v=23)，并对其扩充

环境介绍：本文直接使用`Maven`环境以及`SpringBoot 2.4.3`，不同于视频教程中的先建立web项目再拷贝jar包

建立SpringBoot项目添加如下核心依赖即可

```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-web</artifactId>
</dependency>
```

> 示例代码：
GitHub：[https://github.com/Max-Qiu/demo/tree/main/demo-SpringBoot-SpringMVC](https://github.com/Max-Qiu/demo/tree/main/demo-SpringBoot-SpringMVC)
Gitee：[https://gitee.com/Max-Qiu/demo/tree/main/demo-SpringBoot-SpringMVC](https://gitee.com/Max-Qiu/demo/tree/main/demo-SpringBoot-SpringMVC)

# 基本介绍

- `Spring MVC`使用`@RequestMapping`注解为控制器指定可以处理哪些`URL`请求
- `@RequestMapping`注解在控制器的类定义及方法定义处都可标注

# 匹配规则

- 修饰Controller类：<br>提供初步的请求映射信息。相对于 WEB 应用的根目录
- 修饰Controller类内的方法：<br>提供进一步的细分映射信息。相对于类定义处的`URL`。<br>若类定义处未标注`@RequestMapping`，则方法处标记的`URL`相对于 WEB 应用的根目录

即最终访问的URL大致为：`http://[IP]:[端口]/[类上的URL]/[方法上的URL]`

特殊的，若`@RequestMapping()`填写含有`/`的字符串，例如：`/`、`abc/`、`/abc`或`/abc/`等，具体下文示例说明

PS：示例中的`@ResponseBody`定义返回体，此处不做详细介绍

> 示例1：

```java
@Controller
@RequestMapping("")
public class IndexController {
    @RequestMapping("")
    @ResponseBody
    public String index() {
        return "index";
    }
}
```

请求：`http://127.0.0.1:8080`，若请求`http://127.0.0.1:8080/`会自动去掉最后的`/`

- 示例中类上的`@RequestMapping`可以省去，也可以写成`@RequestMapping("/")`，且访问时URL会自动去掉`/`
- 示例中方法上的`@RequestMapping`也可以写成`@RequestMapping("/")`，且访问时URL会自动去掉`/`

> 示例2：

```java
@Controller
@RequestMapping("")
public class IndexController {
    @RequestMapping("abc")
    @ResponseBody
    public String index() {
        return "index";
    }
}
```

请求：`http://127.0.0.1:8080/abc`或`http://127.0.0.1:8080/abc/`均可以访问，但URL不会自动去掉最后一个`/`

- 若类的修改为`/`，方法的不变，对请求地址均不会产生影响
- 若类的不变，方法修改为`"/abc"`，对请求地址均不会产生影响
- 若类的不变，方法修改为`"/abc/"`，则只有`http://127.0.0.1:8080/abc/`能够访问

> 示例3：

```java
@Controller
@RequestMapping("abc")
public class IndexController {
    @RequestMapping("")
    @ResponseBody
    public String index() {
        return "index";
    }
}
```

请求：`http://127.0.0.1:8080/abc`或`http://127.0.0.1:8080/abc/`均可以访问，但URL不会自动去掉最后一个`/`

- 示例中若类的修改为`"abc/"`或`"/abc/"`，方法的不变，则只有`http://127.0.0.1:8080/abc/`能够访问
- 同示例2：方法修改为`"/"`，类的不变，也只有`http://127.0.0.1:8080/abc/`能够访问

> 示例4：

```java
@Controller
@RequestMapping("abc")
public class IndexController {
    @RequestMapping("def")
    @ResponseBody
    public String index() {
        return "index";
    }
}
```

请求：`http://127.0.0.1:8080/abc/def`或`http://127.0.0.1:8080/abc/def/`均可以访问，但URL不会自动去掉最后一个`/`

- 中若方法的不变，类的修改为`"/abc"`、`"abc/"`、`"/abc/"`对请求地址均不会产生影响
- 若方法的修改为`"/def"`对请求地址也不会产生影响
- 同示例2：方法修改为`"def/"`或`"/def/"`，则只有`http://127.0.0.1:8080/abc/def/`可以访问

> 示例5：

```java
@Controller
@RequestMapping("a/b")
public class IndexController {
    @RequestMapping("c/d")
    @ResponseBody
    public String index() {
        return "index";
    }
}
```

请求：`http://127.0.0.1:8080/a/b/c/d`或`http://127.0.0.1:8080/a/b/c/d/`均可以访问，但URL不会自动去掉最后一个`/`

- 若中方法的不变，类的修改为`"/a/b"`、`"a/b/"`、`"/a/b/"`对请求地址均不会产生影响
- 若方法的修改为`"/c/d"`对请求地址也不会产生影响
- 同示例2：方法修改为`"c/d/"`或`"/c/d/"`，则只有`http://127.0.0.1:8080/a/b/c/d/`可以访问

## 关于`/`的总结

- 根路径若为空，则端口号后面的`/`会自动去除
- 定义前的`/`若不写，则会自动补全
- 方法定义最后的`/`若不写，则UML结尾有无`/`均可以访问
- 方法定义最后的`/`若写了，则UML结尾必须加`/`才能访问
- 若定义的URL中间有`/`则访问也要加`/`
- 若类定义的结尾有`/`且方法定义的开头有`/`，重复的`/`会自动。

# 请求方式

用于区分请求方式，在`@RequestMapping`中使用`method = RequestMethod.???`配置，一般配合restful风格的URL一起使用，具体协议种类如下：


方式 | 简写
---|---
GET | @GetMapping
POST | @PostMapping
PUT | @PutMapping
DELETE | @DeleteMapping
PATCH | @PatchMapping
OPTIONS |
HEAD |
TRACE |

> 示例1

```java
@Controller
@RequestMapping("request")
public class RequestController {
    /**
     * get
     */
    @RequestMapping(value = "get", method = RequestMethod.GET)
    @ResponseBody
    public String get() {
        return "get";
    }

    /**
     * GetMapping
     */
    @GetMapping(value = "get2")
    @ResponseBody
    public String get2() {
        return "get2";
    }

    /**
     * post
     */
    @RequestMapping(value = "post", method = RequestMethod.POST)
    @ResponseBody
    public String post() {
        return "post";
    }

    /**
     * PostMapping
     */
    @PostMapping("post2")
    @ResponseBody
    public String post2() {
        return "post2";
    }
}
```

- 访问`http://127.0.0.1:8080/request/get`可正常返回
- 访问`http://127.0.0.1:8080/request/post`需要使用POST方式，可以借助`Postman`工具，如下图

![](https://cdn.maxqiu.com/upload/2307b712bee349c19bfda36041558c80.jpg)

# 请求参数和请求头

在`@RequestMapping`可以添加`params`和`headers`进行更加精确的映射请求

> `params`和`headers`支持简单达式，如下：

- `param1`：请求必包含名为`param1`请求参数
- `!param1`：请求不包含名为`param1`请求参数
- `param1 != value1`：请求包含名为`param1`请求参数但其值不为`value1`
- `{"param1=value1", "param2"}`：请求必包含名为`param1`和`param2`两个请求参数且`param1`参数值必为`value1`

> 示例

```java
@Controller
@RequestMapping("request")
public class RequestController {
    /**
     * 使用 params 更加精确的映射请求
     */
    @RequestMapping(value = "params", params = {"username", "age!=10"})
    @ResponseBody
    public String params() {
        return "params";
    }

    /**
     * 使用 headers 更加精确的映射请求
     */
    @RequestMapping(value = "headers", headers = {"Accept-Language=zh-CN,zh;q=0.9"})
    @ResponseBody
    public String headers() {
        return "headers";
    }
}
```

- params：
  - 必须携带`username`才可以正常访问，例如：`http://127.0.0.1:8080/request/params?username=1`
  - 当携带`age=10`参数时无法访问，例如：`http://127.0.0.1:8080/request/params?username=1&age=10`
- headers：如下图，使用不同的浏览器访问`http://127.0.0.1:8080/request/headers`，当请求头完全匹配才能正常访问

![](https://cdn.maxqiu.com/upload/0c72c5fa8337455d84d0ebfcc6b6c4c6.jpg)

# Ant 风格资源地址

> Ant 风格资源地址支持三种匹配符：

- ?：匹配文件名中的一个字符
- *：匹配文件名中的任意字符
- **：匹配多层路径

> 示例

```java
@Controller
@RequestMapping("request")
public class RequestController {
    /**
     * ?：匹配一个字符（若多个字符需要多个?）
     */
    @RequestMapping("/ant/ab?")
    @ResponseBody
    public String antPath1() {
        return "/ant/ab?";
    }

    /**
     * *：匹配任意字符（字符中间不能有 / ）
     */
    @RequestMapping("/ant/*/abc")
    @ResponseBody
    public String antPath2() {
        return "/ant/*/abc";
    }

    /**
     * **：匹配多层路径（字符中间可以有 / 也可以没有 / ）
     */
    @RequestMapping("/ant/**/abc")
    @ResponseBody
    public String antPath3() {
        return "/ant/**/abc";
    }
}
```

- `/ant/ab?`
  - `http://127.0.0.1:8080/request/ant/aba`
  - `http://127.0.0.1:8080/request/ant/abb`
  - ……
- `/ant/*/abc`
  - `http://127.0.0.1:8080/request/ant/a/abc`
  - `http://127.0.0.1:8080/request/ant/ab/abc`
  - ……
- `/ant/**/abc`
  - `http://127.0.0.1:8080/request/ant/a/c/abc`
  - `http://127.0.0.1:8080/request/ant/ab/c/abc`
  - `http://127.0.0.1:8080/request/ant/a/abc`（若没有单个`*`的匹配规则，则也可以匹配到）
  - ……
