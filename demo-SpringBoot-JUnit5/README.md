> 本文档整理自教程：
视频：[JUnit 5 用户手册](https://junit.org/junit5/docs/current/user-guide/#writing-tests)
文档：[尚硅谷_Spring Boot2](http://www.atguigu.com/download_detail.shtml?v=314)

---

> 文档：
[JUnit 5](https://junit.org/junit5/)
[尚硅谷/SpringBoot2核心技术与响应式编程/07、单元测试](https://www.yuque.com/atguigu/springboot/ksndgx)

# JUnit 5简介

> JUnit5与之前版本的Junit框架有很大的不同。由三个不同子项目的几个不同模块组成。

- `JUnit Platform`: Junit Platform是在JVM上启动测试框架的基础，不仅支持Junit自制的测试引擎，其他测试引擎也都可以接入。
- `JUnit Jupiter`: JUnit Jupiter提供了JUnit5的新的编程模型，是JUnit5新特性的核心。内部 包含了一个测试引擎，用于在Junit Platform上运行。
- `JUnit Vintage`: 由于JUint已经发展多年，为了照顾老的项目，JUnit Vintage提供了兼容JUnit4.x,Junit3.x的测试引擎。

`JUnit 5`在运行时需要`Java 8`（或更高版本）。

# 快速开始

## 普通Maven项目

> 第一步：添加依赖

```xml
<dependency>
    <groupId>org.junit.jupiter</groupId>
    <artifactId>junit-jupiter</artifactId>
    <version>5.7.1</version>
    <scope>test</scope>
</dependency>
```

> 第二步：使用

```java
import org.junit.jupiter.api.Test;

public class Demo {
    @Test
    public void demo() {
        System.out.println(123);
    }
}
```

## SpringBoot环境

注：`SpringBoot2.2.x`版本开始支持`JUnit5`：[Spring Boot 2.2 Release Notes - JUnit 5](https://github.com/spring-projects/spring-boot/wiki/Spring-Boot-2.2-Release-Notes#junit-5)

> 第一步：添加依赖

```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-test</artifactId>
    <scope>test</scope>
</dependency>
```

> 第二步：使用

```java
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class Junit5ApplicationTests {
    @Test
    void contextLoads() {
        System.out.println(123);
    }
}
```

添加`@SpringBootTest`注解后，`JUnit 5`可以使用`SpringBoot`的功能，如`@Autowired`注解

# 详细说明

## 常用注解

### `@Test`：表示方法是测试方法

与`JUnit4`的`@Test`不同，他的职责非常单一不能声明任何属性，拓展的测试将会由`Jupiter`提供额外测试

```java
@Test
void test() {
    System.out.println("test");
}
```

### `@Disabled`：表示测试类或测试方法不执行

`name`属性用于记录该测试方法停用的原因（非必须）

```java
@Test
@Disabled("不用了")
void disabled() {
    System.out.println("disabled");
}
```

效果如下：

![](https://cdn.maxqiu.com/upload/b072654eaae5439a86b7fe5fd7a4cf25.jpg)

### `@DisplayName`：为测试类或者测试方法设置展示名称

> 默认情况下，展示名称使用方法名或者类名

```java
@DisplayName("测试常用注解")
class Annotations {
    @Test
    @DisplayName("自定义名称")
    void displayName() {
        System.out.println("displayName");
    }
}
```

效果如下：

![](https://cdn.maxqiu.com/upload/695f768ab57a448ea2ad1998a614dd24.jpg)

#### `@DisplayNameGeneration`：声明测试类的自定义显示名称生成器。

> `JUnit Jupiter`支持自定义显示名称生成器，可以通过`@DisplayNameGeneration`注解进行配置。通过`@DisplayName`注解提供的值总是优先于由`DisplayNameGenerator`生成的显示名称。

可以通过实现`DisplayNameGenerator`来创建生成器。以下一些默认选项：

DisplayNameGenerator | 说明
---|---
Standard | 匹配JUnit Jupiter 5.0发布以来的标准显示名生成行为。
Simple | 移除不带参数的方法的末尾括号。
ReplaceUnderscores | 将下划线替换为空格。
IndicativeSentences | 通过连接测试和外围类的名称来生成完整的句子。

### `@RepeatedTest`：表示方法可重复执行

> `value`属性指定当前测试方法执行的次数

```
// @RepeatedTest(value = 3) // 不设置name时，value可以省略
@RepeatedTest(3)
void repeatedTest1() {
    System.out.println("repeatedTest1");
}
```

> `name`属性自定义显示内容

`name`支持如下占位符，默认使用`repetition {currentRepetition} of {totalRepetitions}`

- `{displayName}`：方法名称
- `{totalRepetitions}`：总次数
- `{currentRepetition}`：当前第几次

```java
@RepeatedTest(value = 3, name = "{displayName}方法：总计{totalRepetitions}次，当前第{currentRepetition}次")
void repeatedTest2() {
    System.out.println("repeatedTest2");
}
```

效果如下：

![](https://cdn.maxqiu.com/upload/cf3e8a8e3c274ec0a51cb217a7eb9a1b.jpg)

### `@BeforeEach`/`@AfterEach`/`@BeforeAll`/`@AfterAll`：测试前与测试后

- @BeforeEach :表示在每个单元测试之前执行
- @AfterEach :表示在每个单元测试之后执行
- @BeforeAll :表示在所有单元测试之前执行
- @AfterAll :表示在所有单元测试之后执行


```java
@BeforeEach
void beforeEach() {
    System.out.println("beforeEach");
}

@AfterEach
void afterEach() {
    System.out.println("afterEach");
}

@BeforeAll
static void beforeAll() {
    System.out.println("beforeAll");
}

@AfterAll
static void afterAll() {
    System.out.println("afterAll");
}
```

效果如下：

![](https://cdn.maxqiu.com/upload/aaf91f29ce884912a6e2e61504322a5b.jpg)

### `@Timeout`：表示测试方法运行如果超过了指定时间将会抛出异常

`value`指定数值；`unit`指定单位，默认`TimeUnit.SECONDS`

```java
@Test
@Timeout(value = 2000, unit = TimeUnit.MILLISECONDS)
void timeout() {
    System.out.println("timeout");
    try {
        Thread.sleep(3000);
    } catch (InterruptedException e) {
        e.printStackTrace();
    }
}
```

效果如下：

![](https://cdn.maxqiu.com/upload/0c5f2efdc6b74644a2116cbe853f3ad4.jpg)

> 根据运行效果得出结论：即使运行异常，`@AfterEach`和`@AfterAll`依旧执行

### `@Tag`：用于在类或方法级别上声明用于过滤测试的标签；

> 通过`@Tag`注解对测试类和方法进行标记。类似于TestNG中的测试组或`JUnit 4`中的类别。

```java
@Test
@Tag("taxes")
void testingTaxCalculation() {
    System.out.println("123");
}
```

### `@TestMethodOrder`/`@Order`：用于为带注解的测试类配置测试方法的执行顺序

> 默认情况下，测试方法将使用确定性的算法进行排序，但故意不明显。这确保了测试套件的后续运行以相同的顺序执行测试方法，从而允许可重复的构建。虽然真正的单元测试通常不应该依赖于它们执行的顺序，但有时需要强制执行特定的测试方法执行顺序

要控制测试方法执行的顺序，请使用`@TestMethodOrder`注解的测试类或测试接口，并指定所需的`MethodOrderer`实现。也可以实现自定义`MethodOrderer`，或者使用以下内置的`MethodOrderer`实现之一。

- `DisplayName`：根据测试方法的显示名称按字母和数字排序
- `MethodName`：根据方法名和正式参数列表按字母数字顺序对测试方法进行排序。
- `OrderAnnotation`：根据`@Order`注解指定的值对测试方法进行数字排序。
- `Random`：订单测试方法伪随机，并支持自定义种子的配置。
- `Alphanumeric`：根据测试方法的名称和形式参数列表按字母-数字排序。（不推荐使用会，在6.0中被移除）

示例：

```java
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

@TestMethodOrder(OrderAnnotation.class)
class OrderedTestsDemo {
    @Test
    @Order(1)
    void nullValues() {
        System.out.println("nullValues");
    }

    @Test
    @Order(2)
    void emptyValues() {
        System.out.println("emptyValues");
    }

    @Test
    @Order(3)
    void validValues() {
        System.out.println("validValues");
    }
}
```

效果如下：

![](https://cdn.maxqiu.com/upload/21ce061300714960bf77834a02021b24.jpg)

## 断言（Assertions）

`断言（assertions）`是测试方法中的核心部分，用来对测试需要满足的条件进行验证。这些断言方法都是`org.junit.jupiter.api.Assertions`的静态方法。

### 简单断言

> 用来对单个值进行简单的验证。方法如下：

方法 | 说明
--- | ---
assertEquals | 判断两个对象或两个原始类型是否相等
assertNotEquals | 判断两个对象或两个原始类型是否不相等
assertSame | 判断两个对象引用是否指向同一个对象
assertNotSame | 判断两个对象引用是否指向不同的对象
assertTrue | 判断给定的布尔值是否为 true
assertFalse | 判断给定的布尔值是否为 false
assertNull | 判断给定的对象引用是否为 null
assertNotNull | 判断给定的对象引用是否不为 null

示例：

```java
@Test
public void simple() {
    // 判断两个对象或两个原始类型是否相等
    assertEquals(3, 1 + 2);
    // 判断两个对象或两个原始类型是否不相等
    assertNotEquals(3, 1 + 1);
    // 判断两个对象引用是否指向同一个对象
    Object obj = new Object();
    assertSame(obj, obj);
    // 判断两个对象引用是否指向不同的对象
    assertNotSame(obj, new Object());
    // 判断给定的布尔值是否为 true
    assertTrue(1 < 2);
    // 判断给定的布尔值是否为 false
    assertFalse(1 > 2);
    // 判断给定的对象引用是否为 null
    assertNull(null);
    // 判断给定的对象引用是否不为 null
    assertNotNull(obj);
}
```

> 以上测试方法均可添加一个`String message`参数，当断言失败时，显示当前`message`

示例：

```java
@Test
public void simpleWithMessage() {
    // 断言失败时指定消息
    assertEquals(3, 2 + 2, "自定义断言失败消息");
}
```

运行：

![](https://cdn.maxqiu.com/upload/75385140545d4f05bafd34ed3feffc0f.jpg)

- Expected：期望值
- Actual：实际值

### `数组`/`迭代器`/`字符串`列表断言

```java
@Test
public void array() {
    // 判断数组是否相同
    assertArrayEquals(new int[] {1, 2}, new int[] {1, 2});
    // 判断Iterable是否相同
    assertIterableEquals(Arrays.asList(1, 2, 3), Arrays.asList(1, 2, 3));
    // 判断字符串列表
    List<String> list1 = Arrays.asList("1", "2", "3");
    List<String> list2 = Arrays.asList("1", "2", "3");
    assertLinesMatch(list1, list2);
}
```

### 组合断言

```java
@Test
public void all() {
    // 组合断言（可任意个Executable）
    assertAll(() -> assertEquals(2, 1 + 1), () -> assertTrue(1 > 0));
}
```

### 异常断言

```java
@Test
public void exceptionTest() {
    // 不抛出异常
    assertDoesNotThrow(() -> System.out.println(1 % 1), "抛出异常了");
    // 抛出指定异常
    assertThrows(ArithmeticException.class, () -> System.out.println(1 % 0));
}
```

### 超时断言

```java
@Test
public void timeout() {
    // 如果测试方法时间超过指定时间将会失败（需要等方法执行结束再判断）
    assertTimeout(Duration.ofMillis(1000), () -> Thread.sleep(2000));
    // 如果测试方法时间超过指定时间将会失败（超过指定时间时立即失败，不等待方法执行结束）
    assertTimeoutPreemptively(Duration.ofMillis(1000), () -> Thread.sleep(2000));
}
```

### 快速失败

```java
@Test
public void fastFail() {
    // 快速失败（直接调用，使当前测试方法失败）
    fail();
}
```

## 假设（Assumptions）

`假设`可以看成是`断言`执行的前提，当该`假设`不满足时，就停止执行`断言`等后续方法。所有`假设`方法都是`org.junit.jupiter.api.Assumptions`类中的静态方法。

> `断言`与`假设`的区别

- 断言：不满足的断言会使得测试方法失败
- 假设：不满足的假设条件会使得测试方法的执行终止

示例：

```java
// 环境
private final String environment = "DEV";

@Test
void trueOrFalse() {
    // 判断当前环境，应当是 DEV
    assumeTrue("DEV".equals(environment));
    // 判断当前环境，不能是 PRO
    assumeFalse("PRO".equals(environment));
}

@Test
void trueOrFalseWithMessage() {
    // 判断当前环境，应当是 DEV
    assumeTrue("DEV".equals(environment), "当前不是DEV环境");
    // 判断当前环境，不能是 PRO
    assumeFalse("DEV".equals(environment), "当前是PRO环境");
}

@Test
void that() {
    // 指定环境下执行的断言
    assumingThat("DEV".equals(environment), () -> {
        System.out.println("当前是DEV环境，可以执行如下断言");
        assertEquals(2, 4 - 2);
    });

    // 任何环境下都执行的断言
    assertEquals(42, 6 * 7);
    System.out.println("执行结束");
}
```

- `assumeTrue`和`assumFalse`确保给定的条件为`true`或``false`，不满足条件会使得测试执行终止。
- `assumingThat`的参数是表示条件的布尔值和对应的`Executable`接口的实现对象。
  - 条件满足时，`Executable`对象才会被执行；
  - 条件不满足时，跳过当前`Executable`，执行后续测试。

## 有条件的测试执行

`JUnit Jupiter`中的`ExecutionCondition`扩展`API`允许开发人员基于特定条件以编程方式启用或禁用容器或测试。

### 操作系统条件

> 通过`@EnabledOnOs`/`@DisabledOnOs`注解在特定操作系统上启用或禁用容器或测试 。

示例：

```java
import static org.junit.jupiter.api.condition.OS.LINUX;
import static org.junit.jupiter.api.condition.OS.MAC;
import static org.junit.jupiter.api.condition.OS.WINDOWS;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.DisabledOnOs;
import org.junit.jupiter.api.condition.EnabledOnOs;

class ConditionOS {
    @Test
    @EnabledOnOs(MAC)
    void onlyOnMacOs() {
        // ...
    }

    @Target(ElementType.METHOD)
    @Retention(RetentionPolicy.RUNTIME)
    @Test
    @EnabledOnOs(MAC)
    @interface TestOnMac {}

    @TestOnMac
    void testOnMac() {
        // ...
    }

    @Test
    @EnabledOnOs({LINUX, MAC})
    void onLinuxOrMac() {
        // ...
    }

    @Test
    @DisabledOnOs(WINDOWS)
    void notOnWindows() {
        // ...
    }
}
```

### Java运行时环境条件

> 可以通过`@EnabledOnJre`和`@DisabledOnJre`注解在特定版本的`JRE`上启用或禁用，或者通过`@EnabledForJreRange`和`@DisabledForJreRange`注解在特定范围的`JRE`版本上启用或禁用。范围默认为`JRE.java_8`作为下边界(min)和`JRE.OTHER`作为较高的边界(max)，允许使用半开放范围。


```java
import static org.junit.jupiter.api.condition.JRE.JAVA_10;
import static org.junit.jupiter.api.condition.JRE.JAVA_11;
import static org.junit.jupiter.api.condition.JRE.JAVA_8;
import static org.junit.jupiter.api.condition.JRE.JAVA_9;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.DisabledForJreRange;
import org.junit.jupiter.api.condition.DisabledOnJre;
import org.junit.jupiter.api.condition.EnabledForJreRange;
import org.junit.jupiter.api.condition.EnabledOnJre;

class ConditionJar {
    @Test
    @EnabledOnJre(JAVA_8)
    void onlyOnJava8() {
        // ...
    }

    @Test
    @EnabledOnJre({JAVA_9, JAVA_10})
    void onJava9Or10() {
        // ...
    }

    @Test
    @EnabledForJreRange(min = JAVA_9, max = JAVA_11)
    void fromJava9to11() {
        // ...
    }

    @Test
    @EnabledForJreRange(min = JAVA_9)
    void fromJava9toCurrentJavaFeatureNumber() {
        // ...
    }

    @Test
    @EnabledForJreRange(max = JAVA_11)
    void fromJava8To11() {
        // ...
    }

    @Test
    @DisabledOnJre(JAVA_9)
    void notOnJava9() {
        // ...
    }

    @Test
    @DisabledForJreRange(min = JAVA_9, max = JAVA_11)
    void notFromJava9to11() {
        // ...
    }

    @Test
    @DisabledForJreRange(min = JAVA_9)
    void notFromJava9toCurrentJavaFeatureNumber() {
        // ...
    }

    @Test
    @DisabledForJreRange(max = JAVA_11)
    void notFromJava8to11() {
        // ...
    }
}
```

### 系统属性条件

> 通过`@EnabledIfSystemProperty`和`@DisabledIfSystemProperty`注解根据命名的JVM系统属性的值来启用或禁用。`matches`属性提供的值将被解释为一个正则表达式。

```java
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.DisabledIfSystemProperty;
import org.junit.jupiter.api.condition.EnabledIfSystemProperty;

class ConditionSystem {
    @Test
    @EnabledIfSystemProperty(named = "os.arch", matches = ".*64.*")
    void onlyOn64BitArchitectures() {
        // ...
    }

    @Test
    @DisabledIfSystemProperty(named = "ci-server", matches = "true")
    void notOnCiServer() {
        // ...
    }
}
```

从`JUnit Jupiter 5.6`开始，`@EnabledIfSystemProperty`和`@DisabledIfSystemProperty`是可重复的注释。因此，这些注释可以在测试接口、测试类或测试方法上声明多次。具体来说，如果这些注释在给定元素上直接存在、间接存在或元存在，就可以找到它们。

### 环境变量条件

> 通过`@EnabledIfEnvironmentVariable`和`@DisabledIfEnvironmentVariable`注解，根据底层操作系统中命名的环境变量的值来启用或禁用。`matches`属性提供的值将被解释为一个正则表达式。

```java
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.DisabledIfEnvironmentVariable;
import org.junit.jupiter.api.condition.EnabledIfEnvironmentVariable;

class ConditionEnvironment {
    @Test
    @EnabledIfEnvironmentVariable(named = "ENV", matches = "staging-server")
    void onlyOnStagingServer() {
        // ...
    }

    @Test
    @DisabledIfEnvironmentVariable(named = "ENV", matches = ".*development.*")
    void notOnDeveloperWorkstation() {
        // ...
    }
}
```

从`JUnit Jupiter 5.6`开始，`@EnabledIfEnvironmentVariable`和`@DisabledIfEnvironmentVariable`是可重复的注释。因此，这些注释可以在测试接口、测试类或测试方法上声明多次。具体来说，如果这些注释在给定元素上直接存在、间接存在或元存在，就可以找到它们。

### 自定义条件

> 通过`@EnabledIf`和`@DisabledIf`注解基于方法的布尔返回来启用或禁用。方法通过注释的名称提供给注释，如果位于测试类之外，则通过注释的完全限定名称提供方法。如果需要，条件方法可以接受`ExtensionContext`类型的单个参数。

```java
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.DisabledIf;
import org.junit.jupiter.api.condition.EnabledIf;

class ConditionCustom {
    @Test
    @EnabledIf("customCondition")
    void enabled() {
        // ...
    }

    @Test
    @DisabledIf("customCondition")
    void disabled() {
        // ...
    }

    boolean customCondition() {
        return true;
    }
}
```

当`@EnabledIf`或`@DisabledIf`在类级别使用时，条件方法必须始终是`静态`的。位于外部类中的条件方法也必须是`静态`的。在任何其他情况下，可以同时使用静态方法或实例方法。

## 嵌套测试

官方示例：

```java
public class TestingAStackDemo {
    Stack<Object> stack;

    @Test
    @DisplayName("is instantiated with new Stack()")
    void isInstantiatedWithNew() {
        new Stack<>();
    }

    @Nested
    @DisplayName("when new")
    class WhenNew {
        @BeforeEach
        void createNewStack() {
            stack = new Stack<>();
        }

        @Test
        @DisplayName("is empty")
        void isEmpty() {
            assertTrue(stack.isEmpty());
        }

        @Test
        @DisplayName("throws EmptyStackException when popped")
        void throwsExceptionWhenPopped() {
            assertThrows(EmptyStackException.class, stack::pop);
        }

        @Test
        @DisplayName("throws EmptyStackException when peeked")
        void throwsExceptionWhenPeeked() {
            assertThrows(EmptyStackException.class, stack::peek);
        }

        @Nested
        @DisplayName("after pushing an element")
        class AfterPushing {
            String anElement = "an element";

            @BeforeEach
            void pushAnElement() {
                stack.push(anElement);
            }

            @Test
            @DisplayName("it is no longer empty")
            void isNotEmpty() {
                assertFalse(stack.isEmpty());
            }

            @Test
            @DisplayName("returns the element when popped and is empty")
            void returnElementWhenPopped() {
                assertEquals(anElement, stack.pop());
                assertTrue(stack.isEmpty());
            }

            @Test
            @DisplayName("returns the element when peeked but remains not empty")
            void returnElementWhenPeeked() {
                assertEquals(anElement, stack.peek());
                assertFalse(stack.isEmpty());
            }
        }
    }
}
```

运行结果：

![](https://cdn.maxqiu.com/upload/7de5792bf4454101b632fb153166d6fa.jpg)

在此示例中，通过为设置代码定义分层生命周期方法，内部测试中使用了外部测试的前提条件，例如：`createNewStack()`。在执行内部测试之前先运行外部测试的安装代码这一事实使您能够独立运行所有测试。您甚至可以单独运行内部测试，而无需外部测试，因为始终会执行外部测试中的设置代码。

## 参数化测试

### `@ParameterizedTest`：使用参数化测试注解

> 通过参数化测试，可以使用不同的参数多次运行测试。它的声明与常规`@Test`方法一样，但是使用`@ParameterizedTest`批注。另外，必须声明至少一个`参数源`，该源将为每次调用提供参数，然后在方法中使用这些参数。

示例：演示参数化测试，该测试使用`@ValueSource`注解将`String`数组指定为参数源。

```java
@ParameterizedTest
@ValueSource(ints = {1, 2, 3})
void valueSource(int argument) {
    assertTrue(argument > 0 && argument < 4);
}
```

> 具体示例来源见下文

### `@ValueSource`：简单的数据源

支持以下类型的文字值：

- short
- byte
- int
- long
- float
- double
- char
- boolean
- java.lang.String
- java.lang.Class

示例见上文

### null和空数据源

> 为了检查极端情况并验证软件的正确行为，需要为参数化测试提供null和空值。

- `@NullSource`：为带null注释的@ParameterizedTest 方法提供单个参数。`@NullSource`不能用于具有原始类型的参数。
- `@EmptySource`：提供了一个单一的空参数，注解`@ParameterizedTest`方法为以下类型的参数：
  - java.lang.String
  - java.util.List
  - java.util.Set
  - java.util.Map
  - 原始数组（例如，int[]，char[][]等）
  - 对象数组（例如，String[]，Integer[][]等等）。
- `@NullAndEmptySource`：组合了`@NullSource`和`@EmptySource`的功能的组合注解。

示例：

```java
@ParameterizedTest
@NullSource
@EmptySource
@ValueSource(strings = {" ", "   ", "\t", "\n"})
void nullEmptyAndBlankStrings1(String text) {
    assertTrue(text == null || text.trim().isEmpty());
}

@ParameterizedTest
@NullAndEmptySource
@ValueSource(strings = {" ", "   ", "\t", "\n"})
void nullEmptyAndBlankStrings2(String text) {
    assertTrue(text == null || text.trim().isEmpty());
}
```

### `@EnumSource`：枚举数据源

> 提供了使用Enum常量的便捷方法。

```java
@ParameterizedTest
@EnumSource(ChronoUnit.class)
void testWithEnumSource(TemporalUnit unit) {
    assertNotNull(unit);
}
```

### `@MethodSource`：方法返回值数据源

**使用测试类或外部类的一个或多个工厂方法的返回值作为数据源**

> 如果只需要一个参数，则可以返回一个`Stream`参数类型的实例。

示例：

```java
@ParameterizedTest
@MethodSource("stringProvider")
void testWithExplicitLocalMethodSource(String argument) {
    assertNotNull(argument);
}

static Stream<String> stringProvider() {
    return Stream.of("apple", "banana");
}
```

> 如果未设置`@MethodSource`注解的`value`属性，则`JUnit Jupiter`将按照约定搜索与当前`@ParameterizedTest`方法同名的工厂方法。

示例：

```java
@ParameterizedTest
@MethodSource
void testWithDefaultLocalMethodSource(String argument) {
    assertNotNull(argument);
}

static Stream<String> testWithDefaultLocalMethodSource() {
    return Stream.of("apple", "banana");
}
```

> 支持其他基本类型（DoubleStream，IntStream和LongStream）的流。

示例：

```java
@ParameterizedTest
@MethodSource("range")
void testWithRangeMethodSource(int argument) {
    assertNotEquals(9, argument);
}

static IntStream range() {
    return IntStream.range(0, 20).skip(10);
}
```

> 如果参数化测试方法声明了多个参数，则需要返回一个如下所示的Arguments实例对象或集合、流或数组，（`@MethodSource`有关支持的返回类型的更多详细信息，请参阅`Javadoc`）。

示例：

```java
@ParameterizedTest
@MethodSource("stringIntAndListProvider")
void testWithMultiArgMethodSource(String str, int num, List<String> list) {
    assertEquals(5, str.length());
    assertTrue(num >= 1 && num <= 2);
    assertEquals(2, list.size());
}

static Stream<Arguments> stringIntAndListProvider() {
    return Stream.of(arguments("apple", 1, Arrays.asList("a", "b")),
        arguments("lemon", 2, Arrays.asList("x", "y")));
}
```

> 可以通过提供完全限定的方法名来引用外部静态工厂方法

示例：

```java
package com.maxqiu.demo;

import java.util.stream.Stream;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

class Parameterized {
    @ParameterizedTest
    @MethodSource("com.maxqiu.demo.StringsProviders#tinyStrings")
    void testWithExternalMethodSource(String tinyString) {
        System.out.println(tinyString);
        // test with tiny string
    }
}

class StringsProviders {
    static Stream<String> tinyStrings() {
        return Stream.of(".", "oo", "OOO");
    }
}
```

### `@CsvSource`：CSV格式数据源

> 将参数列表表示为以逗号分隔的值

示例：

```java
@ParameterizedTest
@CsvSource({"apple,1", "banana,2", "'lemon,lime',0xF1"})
void testWithCsvSource(String fruit, int rank) {
    assertNotNull(fruit);
    assertNotEquals(0, rank);
}
```

> 默认的分隔符是逗号"`,`"，可以通过`delimiter`属性来使用另一个字符。

`@CsvSource`使用单引号`'`作为其引号字符。除非设置了属性，否则带引号的值`''`为空，除非设置了`emptyValue`属性。相反，将一个完全为空的值解释为`null`。通过指定一个或多个nullValues，可以将自定义值解释为null引用（请参见下表中的NIL示例）。如果一个的目标类型是一个基本类型，则抛出ArgumentConversionException

> 不带引号的空值将始终转换为空引用，而不管通过nullValues属性配置的任何自定义值。

输入示例 | 结果参数列表
---|---
@CsvSource({ "apple, banana" }) | "apple", "banana"
@CsvSource({ "apple, 'lemon, lime'" }) | "apple", "lemon, lime"
@CsvSource({ "apple, ''" }) | "apple", ""
@CsvSource({ "apple, " }) | "apple", null
@CsvSource(value = { "apple, banana, NIL" }, nullValues = "NIL") | "apple", "banana", null

### `@CsvFileSource`：CSV文件数据源

> 使用来自类路径或本地文件系统的CSV文件。CSV文件中的每一行都会导致对参数化测试的一次调用。

示例：

```java
@ParameterizedTest
@CsvFileSource(files = "src/test/resources/two-column.csv", numLinesToSkip = 1)
void testWithCsvFileSourceFromFile(String country, int reference) {
    assertNotNull(country);
    assertNotEquals(0, reference);
}
```

CSV文件内容：

```csv
Country, reference
Sweden, 1
Poland, 2
"United States of America", 3
```

> 默认的分隔符是逗号"`,`"，可以通过`delimiter`属性来使用另一个字符。任何以`#`符号开头的行都将被解释为注释，并且将被忽略。

与`@CsvSource`中使用的语法不同，`@CsvFileSource`使用双引号`""`作为引号字符。除非设置了属性，否则带引号的值`''`为空，除非设置了`emptyValue`属性。相反，将一个完全为空的值解释为`null`。通过指定一个或多个nullValues，可以将自定义值解释为null引用（请参见下表中的NIL示例）。如果一个的目标类型是一个基本类型，则抛出ArgumentConversionException

> 不带引号的空值将始终转换为空引用，而不管通过nullValues属性配置的任何自定义值。

### `@ArgumentsSource`：

> 定自定义的，可重用的ArgumentsProvider。请注意，ArgumentsProvider必须将的实现声明为顶级类或static嵌套类。

示例：

```java
class Parameterized {
    @ParameterizedTest
    @ArgumentsSource(MyArgumentsProvider.class)
    void testWithArgumentsSource(String argument) {
        assertNotNull(argument);
    }
}

class MyArgumentsProvider implements ArgumentsProvider {
    @Override
    public Stream<? extends Arguments> provideArguments(ExtensionContext context) {
        return Stream.of("apple", "banana").map(Arguments::of);
    }
}
```

# 补充说明

## 如何在打包时跳过测试

### 方式一：命令行

```bash
mvn clean package -DskipTests
# 或者
mvn clean package -Dmaven.test.skip=true
```

### 方式二：修改pom

```xml
<build>
    <plugins>
        <!-- maven 打包时跳过测试 -->
        <plugin>
            <groupId>org.apache.maven.plugins</groupId>
            <artifactId>maven-surefire-plugin</artifactId>
            <configuration>
                <skip>true</skip>
            </configuration>
        </plugin>
    </plugins>
</build>
```

### 方案三：`IntelliJ IDEA`开发工具配置

点击图中的按钮，之后`test`就会变成灰色，即跳过测试

![](https://cdn.maxqiu.com/upload/743ab8e18a6d40b7ae9b80e8130a1fd4.jpg)

## JUnit4迁移JUnit5

在将`JUnit 4`测试迁移到`JUnit5`（`JUnit Jupiter`）时，应该注意以下事项。

- 注解在`org.junit.jupiter.api`包中。
- 断言在`org.junit.jupiter.api.Assertions`包中。
- 假设在`org.junit.jupiter.api.Assumptions`包中。
- `@Before`和`@After`不再存在，使用`@BeforeEach`和`@AfterEach`代替。
- `@BeforeClass`和`@AfterClass`不再存在，用`@BeforeAll`和`@AfterAll`代替。
- `@Ignore`不再存在，使用`@Disabled`或其他内置执行条件之一代替
- `@Category`不再存在，使用`@Tag`代替。
- `@RunWith`不再存在，使用`@ExtendWith`代替。
- `@Rule`和`@ClassRule`不再存在，被`@ExtendWith`和`@RegisterExtension`取代
