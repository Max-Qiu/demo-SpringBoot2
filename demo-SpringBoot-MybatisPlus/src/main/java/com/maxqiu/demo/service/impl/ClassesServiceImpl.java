package com.maxqiu.demo.service.impl;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.maxqiu.demo.entity.Classes;
import com.maxqiu.demo.mapper.ClassesMapper;
import com.maxqiu.demo.service.IClassesService;

/**
 * 班级表 服务实现类
 *
 * @author Max_Qiu
 */
@Service
public class ClassesServiceImpl extends ServiceImpl<ClassesMapper, Classes> implements IClassesService {

}
