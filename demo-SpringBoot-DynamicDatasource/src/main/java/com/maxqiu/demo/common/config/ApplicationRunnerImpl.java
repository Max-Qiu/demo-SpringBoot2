package com.maxqiu.demo.common.config;

import java.util.List;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import com.baomidou.dynamic.datasource.DynamicRoutingDataSource;
import com.baomidou.dynamic.datasource.creator.HikariDataSourceCreator;
import com.baomidou.dynamic.datasource.spring.boot.autoconfigure.DataSourceProperty;
import com.maxqiu.demo.system.entity.DbInfo;
import com.maxqiu.demo.system.service.DbInfoService;

import lombok.extern.slf4j.Slf4j;

/**
 * 启动后初始化操作
 *
 * @author Max_Qiu
 */
@Slf4j
@Component
public class ApplicationRunnerImpl implements ApplicationRunner {
    @Autowired
    private DataSource dataSource;

    @Autowired
    private HikariDataSourceCreator hikariDataSourceCreator;

    @Autowired
    private DbInfoService dbInfoService;

    @Override
    public void run(ApplicationArguments args) {
        List<DbInfo> list = dbInfoService.list();
        for (DbInfo dbInfo : list) {
            DataSourceProperty dataSourceProperty = new DataSourceProperty();
            dataSourceProperty.setDriverClassName(dbInfo.getDriverClassName());
            dataSourceProperty.setUrl(dbInfo.getUrl());
            dataSourceProperty.setUsername(dbInfo.getUsername());
            dataSourceProperty.setPassword(dbInfo.getPassword());
            dataSourceProperty.setLazy(true);
            DynamicRoutingDataSource ds = (DynamicRoutingDataSource)dataSource;
            DataSource dataSource = hikariDataSourceCreator.createDataSource(dataSourceProperty);
            ds.addDataSource(dbInfo.getKey(), dataSource);
        }
        DynamicRoutingDataSource ds = (DynamicRoutingDataSource)dataSource;
        System.out.println(ds.getDataSources().keySet());
    }
}
