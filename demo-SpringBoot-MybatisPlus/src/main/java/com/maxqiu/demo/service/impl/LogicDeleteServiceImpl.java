package com.maxqiu.demo.service.impl;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.maxqiu.demo.entity.LogicDelete;
import com.maxqiu.demo.mapper.LogicDeleteMapper;
import com.maxqiu.demo.service.ILogicDeleteService;

/**
 * 逻辑删除 服务实现类
 *
 * @author Max_Qiu
 */
@Service
public class LogicDeleteServiceImpl extends ServiceImpl<LogicDeleteMapper, LogicDelete> implements ILogicDeleteService {

}
