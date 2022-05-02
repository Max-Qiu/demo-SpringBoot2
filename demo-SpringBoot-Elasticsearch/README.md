> 官方教程：[Spring Data Elasticsearch - Reference Documentation](https://docs.spring.io/spring-data/elasticsearch/docs/current/reference/html/#reference)

PS：本文只是一篇极其简单的整合教程，不涉及复杂搜索示例，建议认真阅读官方文档

# 版本对应关系

Spring Boot | Spring Data Elasticsearch | Elasticsearch
---|---|---
2.4.x | 4.1.x | 7.9.3
2.5.x | 4.2.x | 7.12.1
2.6.x | 4.3.x | 7.15.2

- 本文以`Spring Boot 2.6.x`为例
- 本文不介绍`Reactive`模式的相关代码

# 准备

## pom.xml

```xml
<!--核心依赖-->
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-data-elasticsearch</artifactId>
</dependency>
<!--配置客户端需要的依赖-->
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-web</artifactId>
</dependency>
<!--若不需要Tomcat，则只需要依赖spring-web-->
<!--<dependency>
    <groupId>org.springframework</groupId>
    <artifactId>spring-web</artifactId>
</dependency>-->
```

## 客户端配置

官方文档：`5. Elasticsearch Clients`

> 官网未介绍yml方式配置，但是也是可以使用的，不做复杂的设置的话，`yml`即可

### application.yml

```yml
# yml配置elasticsearch客户端地址（可配置项有限）
spring:
  elasticsearch:
    uris: http://127.0.0.1:9200 # elasticsearch 连接地址
    #username: elastic # 用户名
    #password: 123456 # 密码
    connection-timeout: 10s # 连接超时时间（默认1s）
    socket-timeout: 30s # 数据读取超时时间（默认30s）
```

### Bean

```java
import java.time.Duration;

import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.elasticsearch.client.ClientConfiguration;
import org.springframework.data.elasticsearch.client.RestClients;
import org.springframework.data.elasticsearch.config.AbstractElasticsearchConfiguration;

/**
 * Java高级别REST客户端是Elasticsearch的默认客户端
 *
 * Bean方式配置
 *
 * @author Max_Qiu
 */
@Configuration
public class RestClientConfig extends AbstractElasticsearchConfiguration {
    @Bean
    @Override
    public RestHighLevelClient elasticsearchClient() {

        // 使用构建器来提供集群地址，设置默认值HttpHeaders或启用SSL。
        ClientConfiguration clientConfiguration = ClientConfiguration.builder()
            // 设置连接地址
            .connectedTo("127.0.0.1:9200")
            // 可以设置多个地址
            // .connectedTo("127.0.0.1:9200", "127.0.0.1:9201")
            // 是否启用ssl
            // .usingSsl()
            // 设置连接超时时间
            .withConnectTimeout(Duration.ofSeconds(10))
            // 设置
            .withSocketTimeout(Duration.ofSeconds(30))
            // 设置用户名密码
            // .withBasicAuth("elastic", "123456")
            // 创建连接信息
            .build();

        // 创建RestHighLevelClient。
        return RestClients.create(clientConfiguration).rest();
    }
}
```

## 日志输出

> 方便查询发送的`http请求`详细内容

在`application.yml`内添加如下内容

```
logging:
  file:
    name: ./log/log.log
  level:
    root: info
    org.springframework.data.elasticsearch.client.WIRE: trace
```

# 实体映射

官方文档：`6. Elasticsearch Object Mapping`

示例如下：

```
@Document(indexName = "user", shards = 3, replicas = 0)
public class User {
    @Id
    private Integer id;

    @Field(type = FieldType.Keyword)
    private String name;

    @Field(type = FieldType.Integer)
    private Integer age;

    @Field(type = FieldType.Text, analyzer = "ik_max_word")
    private String address;

    @Field(type = FieldType.Date, format = DateFormat.custom, pattern = "uuuu-MM-dd'T'HH:mm:ss.SSSX")
    private LocalDateTime createTime;
}
```

注解说明：

    在MappingElasticsearchConverter使用元数据驱动的对象的映射文件。元数据取自可以注释的实体属性。
    提供以下注释：
    
    @Document：在类级别应用，以指示该类是映射到数据库的候选对象。最重要的属性是：
        indexName：用于存储此实体的索引的名称。它可以包含SpEL模板表达式，例如 "log-#{T(java.time.LocalDate).now().toString()}"
        createIndex：标记是否在存储库引导中创建索引。默认值为true。请参见使用相应的映射自动创建索引
        versionType：版本管理的配置。默认值为EXTERNAL。
    
    @Id：在字段级别应用，以标记用于标识目的的字段。
    
    @Transient：默认情况下，存储或检索文档时，所有字段都映射到文档，此注释不包括该字段。
    
    @PersistenceConstructor：标记从数据库实例化对象时要使用的给定构造函数，甚至是受保护的程序包。构造函数参数按名称映射到检索到的Document中的键值。
    
    @Field：在字段级别应用并定义字段的属性，大多数属性映射到各自的Elasticsearch映射定义（以下列表不完整，请查看注释Javadoc以获得完整参考）：
        name：字段名称，它将在Elasticsearch文档中表示，如果未设置，则使用Java字段名称。
        type：字段类型，可以是Text, Keyword, Long, Integer, Short, Byte, Double, Float, Half_Float, Scaled_Float, Date, Date_Nanos, Boolean, Binary, Integer_Range, Float_Range, Long_Range, Double_Range, Date_Range, Ip_Range, Object, Nested, Ip, TokenCount, Percolator, Flattened, Search_As_You_Type。请参阅Elasticsearch映射类型
        format和日期类型的pattern定义。必须为日期类型定义format
        store：标记是否将原始字段值存储在Elasticsearch中，默认值为false。
        analyzer，searchAnalyzer，normalizer用于指定自定义分析和正规化。
    
    @GeoPoint：将字段标记为geo_point数据类型。如果字段是GeoPoint类的实例，则可以省略。
    
    @ValueConverter：定义用于转换给定属性的类。与注册的 Spring 不同，Converter这仅转换带注释的属性，而不是给定类型的每个属性。
    
    @Setting：注释定义不同的索引设置。以下参数可用：
        useServerConfiguration 不发送任何设置参数，因此 Elasticsearch 服务器配置确定它们。
        settingPath 是指定义必须在类路径中解析的设置的 JSON 文件
        shards要使用的分片数，默认为1
        replicas副本数，默认为1
        refreshIntervall, 默认为“1s”
        indexStoreType, 默认为"fs"

# CRUD

## Elasticsearch Operations

官方文档：`7. Elasticsearch Operations`

主要有以下4个类

- `IndexOperations` 在索引级别定义操作，例如创建或删除索引。
- `DocumentOperations` 定义根据其ID存储，更新和检索实体的操作。
- `SearchOperations` 定义使用查询搜索多个实体的动作
- `ElasticsearchOperations` 结合`DocumentOperations`和`SearchOperations`的接口
- `ElasticsearchRestTemplate` 是`ElasticsearchOperations`的实现类

> 日常使用时，使用`ElasticsearchRestTemplate`即可，代码如下

```java
@Autowired
private ElasticsearchRestTemplate template;
```

> `IndexOperations`需要从`ElasticsearchRestTemplate`中获取，例如：`template.indexOps(User.class)`

### 操作索引

```java
// 获取IndexOperations对象
IndexOperations indexOperations = template.indexOps(User.class);

// 查
boolean exists = indexOperations.exists();

// 删
boolean delete = indexOperations.delete();

// 增
boolean flag = indexOperations.create();

// 设置Mapping
boolean mapping = indexOperations.putMapping();
```

完整示例：`com.maxqiu.demo.IndexApi`

### 操作文档

```java
// 增/改
User user = new User();
user.set...
template.save(user);

// 批量 增/改
List<User> userList = new ArrayList<>();
userList.add(new User);
...
Iterable<User> users = template.save(userList);

// 查
User user = template.get("1", User.class);

// 删
String delete = template.delete("1", User.class);
```

完整示例：`com.maxqiu.demo.DocumentApi`

### 搜索


```java
// 新建一个QueryBuilder（该对象是Elasticsearch的，QueryBuilders可以构建各种各样的条件查询）
QueryBuilder queryBuilder = QueryBuilders.matchAllQuery();
// 新建一个Query（该对象是Spring Data Elasticsearch的）
Query query = new NativeSearchQueryBuilder().withQuery(queryBuilder).build();
// 使用template执行查询，SearchHits<User>就是执行后的返回结果的映射实体
SearchHits<User> search = template.search(query, User.class);
```

完整示例：`com.maxqiu.demo.SearchApi`

## ElasticsearchRepository

官方文档：`8. Elasticsearch Repositories`

一个类似于`Spring Data JPA`的文档操作方法

```java
// 新建一个User的数据持久层
@Repository
public interface UserRepository extends ElasticsearchRepository<User, Integer> {}
```

> 建立此持久层后，如果`User`实体`@Document`注解内的`createIndex`为`true`时（默认为`true`），则服务启动时会先检查索引是否存在，若不存在则会自动创建索引

### 默认的CRUD示例

```java
// 增/改
User user = new User(1, "张三", 18, "上海市闵行区", LocalDateTime.now());
User save = repository.save(user);

// 查
Optional<User> optionalUser = repository.findById(1);

// 删
repository.deleteById(1);
```

完整示例：`com.maxqiu.demo.repository.UserRepositoryTest`

### 自定义的条件查询

在`UserRepository`内新建抽象方法

```java
List<User> findUsersByNameAndAddress(String name, String address);
```

调用

```java
@Test
void test() {
    List<User> userList = repository.findUsersByNameAndAddress("张三", "上海");
    for (User user : userList) {
        System.out.println(user);
    }
}
```