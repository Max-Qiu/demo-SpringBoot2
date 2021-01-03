package com.maxqiu.demo;

import java.io.File;

import org.junit.jupiter.api.Test;

import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.generator.AutoGenerator;
import com.baomidou.mybatisplus.generator.InjectionConfig;
import com.baomidou.mybatisplus.generator.config.*;
import com.baomidou.mybatisplus.generator.config.rules.DateType;
import com.baomidou.mybatisplus.generator.config.rules.FileType;
import com.baomidou.mybatisplus.generator.config.rules.NamingStrategy;
import com.baomidou.mybatisplus.generator.keywords.MySqlKeyWordsHandler;

/**
 * 代码生成器
 * 
 * 以 MySQL 为例
 * 
 * 0. pom.xml 添加相关依赖
 * 
 * 1. 拷贝 CodeGenerator resources/mybatis
 * 
 * 2. 修改最重要的设置
 * 
 * 3. 运行 generator
 * 
 * 4. resources 目录下新建 mapper 文件夹，之后将生成 xml文件 移动到 resources/mapper 目录下
 * 
 * 5. 若新增表，则再次运行，xml文件 同样进行移动，重复的 xml文件夹 记得删除
 * 
 * PS 代码生成后推荐格式化一下，毕竟模板中可能有多余的空行或者空格或者import顺序不一样等等
 * 
 * @author Max_Qiu
 */
public class CodeGenerator {

    /**
     * 最重要的配置
     */
    // 数据库地址
    private static final String URL = "jdbc:mysql://localhost:3306/smp?useSSL=false&serverTimezone=GMT%2B8";
    // 用户名
    private static final String USERNAME = "root";
    // 密码
    private static final String PASSWORD = "123";
    // 父包名
    private static final String PARENT = "com.maxqiu.demo";
    // 去除表前缀（smp_user -> User）（若不需要去除，则设置为 "" ）
    private static final String TABLE_PREFIX = "smp_";
    // 作者（@author Max_Qiu）
    private static final String AUTHOR = "Max_Qiu";

    /**
     * 代码生成 示例代码
     */
    @Test
    void generator() {
        // 1. 数据源配置
        DataSourceConfig dataSourceConfig = new DataSourceConfig();
        // 数据库类型
        dataSourceConfig.setDbType(DbType.MYSQL);
        // 关键字处理器，自动在表字段为关键字时为实体属性添加 @TableField
        dataSourceConfig.setKeyWordsHandler(new MySqlKeyWordsHandler());
        // 数据库连接驱动
        dataSourceConfig.setDriverName("com.mysql.cj.jdbc.Driver");
        // 地址
        dataSourceConfig.setUrl(URL);
        // 用户名
        dataSourceConfig.setUsername(USERNAME);
        // 密码
        dataSourceConfig.setPassword(PASSWORD);

        // 2. 策略配置
        StrategyConfig strategyConfig = new StrategyConfig();
        // 是否大写命名（默认false，不知道干嘛用的）
        strategyConfig.setCapitalMode(false);
        // 是否跳过试图
        strategyConfig.setSkipView(true);
        // 数据库表映射到实体的命名策略（下划线转驼峰，默认不做改变）
        strategyConfig.setNaming(NamingStrategy.underline_to_camel);
        // 数据库表字段映射到实体的命名策略（下划线转驼峰，默认跟随naming）
        strategyConfig.setColumnNaming(NamingStrategy.underline_to_camel);
        // 自动去除表前缀（smp_user -> User）
        strategyConfig.setTablePrefix(TABLE_PREFIX);
        // 字段前缀
        // strategyConfig.setFieldPrefix("");
        // 排除的表，生成的表（二选一）
        strategyConfig.setExclude("smp_test_exclude");
        // strategyConfig.setInclude("xxx");
        // 是否生成serialVersionUID
        strategyConfig.setEntitySerialVersionUID(true);
        // 实体是否为链式模型，即实体可以连续set，例：.setXxx().setXxx();（需关闭lombok）
        strategyConfig.setChainModel(true);
        // 实体使用lombok
        strategyConfig.setEntityLombokModel(true);
        // 是否移除is前缀（is_deleted -> deleted）
        strategyConfig.setEntityBooleanColumnRemoveIsPrefix(true);
        // 生成Rest风格的Controller（@Controller -> @RestController 默认false）
        strategyConfig.setRestControllerStyle(true);
        // 是否生成实体的注解，即每个字段都设置 @TableId/@TableField
        strategyConfig.setEntityTableFieldAnnotationEnable(true);

        // 3. 包名策略配置
        PackageConfig packageConfig = new PackageConfig();
        // 父包名
        packageConfig.setParent(PARENT);
        // 子包名（以下为默认）
        packageConfig.setEntity("entity");
        packageConfig.setService("service");
        packageConfig.setServiceImpl("service.impl");
        packageConfig.setMapper("mapper");
        packageConfig.setXml("mapper.xml");
        packageConfig.setController("controller");

        // 4. 全局配置
        GlobalConfig globalConfig = new GlobalConfig();
        // 生成路径
        globalConfig.setOutputDir(System.getProperty("user.dir") + "/src/main/java");
        // 是否覆盖已有文件
        globalConfig.setFileOverride(false);
        // 生成后打开生成路径
        globalConfig.setOpen(false);
        // 是否在xml中开启耳机缓存配置
        globalConfig.setEnableCache(false);
        // 作者
        globalConfig.setAuthor(AUTHOR);
        // 是否支持AR模式
        globalConfig.setActiveRecord(true);
        // xml 中是否生成 通用查询映射结果
        globalConfig.setBaseResultMap(true);
        // 表中时间类型对应的实体属性类型
        globalConfig.setDateType(DateType.TIME_PACK);
        // xml 中是否生成 通用查询结果列
        globalConfig.setBaseColumnList(true);
        // 全局主键策略 自增、空、手动输入、雪花ID、UUID。。。等（不建议设置，如果是自增则会自动添加注解，其他情况手动指定）
        // globalConfig.setIdType(IdType.AUTO);

        // 5. 模板
        TemplateConfig templateConfig = new TemplateConfig();
        // 自定义entity模板路径
        templateConfig.setEntity("mybatis/entity.java");
        templateConfig.setService("mybatis/service.java");
        templateConfig.setServiceImpl("mybatis/serviceImpl.java");
        templateConfig.setMapper("mybatis/mapper.java");
        templateConfig.setXml("mybatis/mapper.xml");
        // 如果设置为null，则为关闭生成
        templateConfig.setController(null);

        // 6. 整合配置
        AutoGenerator autoGenerator = new AutoGenerator();
        autoGenerator.setDataSource(dataSourceConfig);
        autoGenerator.setStrategy(strategyConfig);
        autoGenerator.setPackageInfo(packageConfig);
        autoGenerator.setGlobalConfig(globalConfig);
        autoGenerator.setTemplate(templateConfig);
        // 自定义配置
        // setCfg(autoGenerator);

        // 7. 生成
        autoGenerator.execute();
    }

    /**
     * 自定义配置
     */
    private void setCfg(AutoGenerator autoGenerator) {
        // 示例：
        // 当“是否覆盖已有文件为false”时，配置Entity强制刷新，即表结构修改够，仅修改实体即可
        // 这种情况适用于：不手动修改实体（如添加一对多，Version注解，自定义的字段等），但是数据库经常修改，需要重新生成实体
        InjectionConfig injectionConfig = new InjectionConfig() {
            @Override
            public void initMap() {}
        };
        injectionConfig.setFileCreate((configBuilder, fileType, filePath) -> {
            // 如果是Entity则直接返回true表示写文件
            if (fileType == FileType.ENTITY) {
                return true;
            }
            // 否则先判断文件是否存在
            File file = new File(filePath);
            boolean exist = file.exists();
            // 文件不存在或者全局配置的fileOverride为true才写文件
            return !exist || configBuilder.getGlobalConfig().isFileOverride();
        });
        // 添加设置后
        autoGenerator.setCfg(injectionConfig);
    }
}
