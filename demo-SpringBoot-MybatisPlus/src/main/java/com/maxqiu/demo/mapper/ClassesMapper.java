package com.maxqiu.demo.mapper;

import java.util.Map;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.maxqiu.demo.entity.Classes;
import com.maxqiu.demo.model.ClassesStudent;

/**
 * 班级表 Mapper 接口
 *
 * @author Max_Qiu
 */
public interface ClassesMapper extends BaseMapper<Classes> {
    Page<ClassesStudent> classesStudentPage(Page<ClassesStudent> page, Map<String, Object> map);
}
