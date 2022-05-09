package com.maxqiu.demo.system.job;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.baomidou.dynamic.datasource.toolkit.DynamicDataSourceContextHolder;
import com.maxqiu.demo.normal.entity.User;
import com.maxqiu.demo.normal.service.UserService;
import com.maxqiu.demo.system.entity.DbInfo;
import com.maxqiu.demo.system.service.DbInfoService;

/**
 * 测试定时任务
 *
 * @author Max_Qiu
 */
@Component
public class TestJob {
    @Autowired
    private DbInfoService dbInfoService;

    @Autowired
    private UserService userService;

    @Scheduled(cron = "* * * * * ?")
    void test() {
        for (DbInfo dbInfo : dbInfoService.list()) {
            DynamicDataSourceContextHolder.push(dbInfo.getKey());
            for (User user : userService.list()) {
                if (user == null || !dbInfo.getKey().equals(user.getUsername())) {
                    System.out.println("用户查询不匹配");
                }
            }
        }
        DynamicDataSourceContextHolder.clear();
    }
}
