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
 * @author Max_Qiu
 */
public class CodeGenerator {

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
        dataSourceConfig.setUrl(
            "jdbc:mysql://localhost:3306/smp?characterEncoding=utf8&zeroDateTimeBehavior=convertToNull&useSSL=false&serverTimezone=Asia/Shanghai");
        // 用户名
        dataSourceConfig.setUsername("root");
        // 密码
        dataSourceConfig.setPassword("123");

        // 2. 策略配置
        StrategyConfig strategyConfig = new StrategyConfig();
        // 是否大写命名
        // strategyConfig.setCapitalMode(false);
        // 是否跳过试图
        strategyConfig.setSkipView(true);
        // 数据库表映射到实体的命名策略
        strategyConfig.setNaming(NamingStrategy.underline_to_camel);
        // 数据库表字段映射到实体的命名策略
        strategyConfig.setColumnNaming(NamingStrategy.underline_to_camel);
        // 表前缀
        strategyConfig.setTablePrefix("smp_");
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
        strategyConfig.setEntityLombokModel(false);
        // 是否移除is前缀
        strategyConfig.setEntityBooleanColumnRemoveIsPrefix(true);
        // 生成Rest风格的Controller
        strategyConfig.setRestControllerStyle(true);
        // 是否生成实体的注解，即每个字段都设置 @TableId/@TableField
        strategyConfig.setEntityTableFieldAnnotationEnable(true);

        // 3. 包名策略配置
        PackageConfig packageConfig = new PackageConfig();
        // 父包名
        packageConfig.setParent("com.maxqiu.demo");
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
        // 是否文件覆盖
        globalConfig.setFileOverride(false);
        // 生成后打开生成路径
        globalConfig.setOpen(false);
        // 是否在xml中开启耳机缓存配置
        globalConfig.setEnableCache(false);
        // 作者
        globalConfig.setAuthor("Max_Qiu");
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
        templateConfig.setController("mybatis/controller.java");
        // 如果设置为null，则为关闭生成
        // templateConfig.setController(null);

        // 6. 自定义配置
        // 示例：当是否文件覆盖为 false 时，配置Entity强制刷新
        InjectionConfig cfg = new InjectionConfig() {
            @Override
            public void initMap() {}
        };
        cfg.setFileCreate((configBuilder, fileType, filePath) -> {
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

        // 6. 整合配置
        AutoGenerator autoGenerator = new AutoGenerator();
        autoGenerator.setDataSource(dataSourceConfig);
        autoGenerator.setStrategy(strategyConfig);
        autoGenerator.setPackageInfo(packageConfig);
        autoGenerator.setGlobalConfig(globalConfig);
        autoGenerator.setTemplate(templateConfig);
        autoGenerator.setCfg(cfg);

        // 7. 生成
        autoGenerator.execute();
    }
}
