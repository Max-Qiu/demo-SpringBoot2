package com.maxqiu.demo.service.impl;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.maxqiu.demo.entity.AutoFill;
import com.maxqiu.demo.mapper.AutoFillMapper;
import com.maxqiu.demo.service.IAutoFillService;

/**
 * 自动插入 服务实现类
 *
 * @author Max_Qiu
 */
@Service
public class AutoFillServiceImpl extends ServiceImpl<AutoFillMapper, AutoFill> implements IAutoFillService {

}
