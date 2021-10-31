package com.maxqiu.demo.controller;

import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.maxqiu.demo.common.Result;
import com.maxqiu.demo.vo.NormalVO;
import com.maxqiu.demo.vo.UserVO;

/**
 * 实体校验
 *
 * @author Max_Qiu
 */
@RestController
@RequestMapping("vo")
public class VoController {
    /**
     * 实体校验
     */
    @PostMapping("normal")
    public Result<NormalVO> normal(@Validated NormalVO vo) {
        return Result.success(vo);
    }

    /**
     * 嵌套实体验证
     */
    @PostMapping("nest")
    public Result<UserVO> nest(@Validated @RequestBody UserVO user) {
        return Result.success(user);
    }
}
