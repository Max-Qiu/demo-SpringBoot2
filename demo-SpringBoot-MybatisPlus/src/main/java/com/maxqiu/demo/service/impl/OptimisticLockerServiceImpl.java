package com.maxqiu.demo.service.impl;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.maxqiu.demo.entity.OptimisticLocker;
import com.maxqiu.demo.mapper.OptimisticLockerMapper;
import com.maxqiu.demo.service.IOptimisticLockerService;

/**
 * 乐观锁 服务实现类
 *
 * @author Max_Qiu
 */
@Service
public class OptimisticLockerServiceImpl extends ServiceImpl<OptimisticLockerMapper, OptimisticLocker> implements IOptimisticLockerService {

}
