package com.maxqiu.demo.controller;

import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.maxqiu.demo.common.Result;
import com.maxqiu.demo.valid.group.AddValidGroup;
import com.maxqiu.demo.valid.group.UpdateValidGroup;
import com.maxqiu.demo.vo.EmployeeVO;

/**
 * 分组校验
 *
 * @author Max_Qiu
 */
@RestController
@RequestMapping("group")
public class GroupController {
    @PostMapping("add")
    public Result<EmployeeVO> add(@Validated(AddValidGroup.class) EmployeeVO vo) {
        return Result.success(vo);
    }

    @PostMapping("update")
    public Result<EmployeeVO> update(@Validated(UpdateValidGroup.class) EmployeeVO vo) {
        return Result.success(vo);
    }
}
