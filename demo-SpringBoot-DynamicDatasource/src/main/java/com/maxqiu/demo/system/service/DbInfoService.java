package com.maxqiu.demo.system.service;

import org.springframework.stereotype.Service;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.maxqiu.demo.system.entity.DbInfo;
import com.maxqiu.demo.system.mapper.DbInfoMapper;

/**
 * 数据源配置信息 服务类
 *
 * @author Max_Qiu
 */
@Service
@DS("system")
public class DbInfoService extends ServiceImpl<DbInfoMapper, DbInfo> {

}
