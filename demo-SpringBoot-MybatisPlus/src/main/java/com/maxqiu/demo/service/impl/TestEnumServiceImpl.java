package com.maxqiu.demo.service.impl;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.maxqiu.demo.entity.TestEnum;
import com.maxqiu.demo.mapper.TestEnumMapper;
import com.maxqiu.demo.service.ITestEnumService;

/**
 * 枚举 服务实现类
 *
 * @author Max_Qiu
 */
@Service
public class TestEnumServiceImpl extends ServiceImpl<TestEnumMapper, TestEnum> implements ITestEnumService {

}
