package com.maxqiu.demo.controller;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.maxqiu.demo.vo.NormalVO;
import com.maxqiu.demo.vo.UserVO;

/**
 * @author Max_Qiu
 */
@RestController
@Validated
public class IndexController {
    /**
     * 例：校验邮箱与验证码
     */
    @GetMapping("code")
    public String code(@Email @NotBlank String email,
        @Size(min = 6, max = 6, message = "验证码为6位") @NotBlank String code) {
        // 邮箱和验证码正确性校验：略
        return email + "\t" + code;
    }

    /**
     * 实体校验
     */
    @PostMapping("normal")
    public NormalVO normal(@Validated NormalVO vo) {
        return vo;
    }

    /**
     * 嵌套实体验证
     */
    @PostMapping("nest")
    public UserVO nest(@Validated @RequestBody UserVO user) {
        return user;
    }
}
