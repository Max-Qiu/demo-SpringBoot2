package com.maxqiu.demo.service.impl;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.maxqiu.demo.entity.Student;
import com.maxqiu.demo.mapper.StudentMapper;
import com.maxqiu.demo.service.IStudentService;

/**
 * 学生表 服务实现类
 *
 * @author Max_Qiu
 */
@Service
public class StudentServiceImpl extends ServiceImpl<StudentMapper, Student> implements IStudentService {

}
