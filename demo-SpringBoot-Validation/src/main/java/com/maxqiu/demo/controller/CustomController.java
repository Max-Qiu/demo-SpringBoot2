package com.maxqiu.demo.controller;

import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.maxqiu.demo.common.Result;
import com.maxqiu.demo.valid.group.ChangeStatusValidGroup;
import com.maxqiu.demo.vo.EmployeeVO;

/**
 * @author Max_Qiu
 */
@RestController
@RequestMapping("custom")
public class CustomController {
    /**
     * 修改状态
     */
    @PostMapping("status")
    public Result<EmployeeVO> status(@Validated(ChangeStatusValidGroup.class) EmployeeVO vo) {
        return Result.success(vo);
    }
}
