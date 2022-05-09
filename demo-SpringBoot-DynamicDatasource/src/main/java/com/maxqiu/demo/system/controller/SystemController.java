package com.maxqiu.demo.system.controller;

import java.util.Set;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.dynamic.datasource.DynamicRoutingDataSource;

/**
 * @author Max_Qiu
 */
@RestController
@RequestMapping("system")
public class SystemController {
    @Autowired
    private DataSource dataSource;

    @GetMapping("data-source/list")
    public Set<String> dataSourceList() {
        DynamicRoutingDataSource ds = (DynamicRoutingDataSource)dataSource;
        return ds.getDataSources().keySet();
    }
}
