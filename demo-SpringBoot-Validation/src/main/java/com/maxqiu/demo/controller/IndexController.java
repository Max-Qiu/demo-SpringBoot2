package com.maxqiu.demo.controller;

import java.util.HashMap;
import java.util.Map;

import javax.validation.Valid;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.maxqiu.demo.common.Result;
import com.maxqiu.demo.common.ResultEnum;
import com.maxqiu.demo.vo.NormalVO;

/**
 * 普通参数校验
 *
 * @author Max_Qiu
 */
@RestController
@Validated
public class IndexController {
    /**
     * 例：校验邮箱与验证码
     */
    @GetMapping("code")
    public Result<String> code(@Email @NotBlank(message = "邮箱不能为空！") String email,
        @Size(min = 6, max = 6, message = "验证码为6位") @NotBlank String code) {
        // 邮箱和验证码正确性校验：略
        return Result.success(email + "\t" + code);
    }

    /**
     * 方法级处理校验异常
     */
    @GetMapping("exception")
    public Result<?> exception(@Valid NormalVO vo, BindingResult result) {
        if (result.hasErrors()) {
            Map<String, String> map = new HashMap<>();
            // 获取校验的错误结果并遍历
            result.getFieldErrors().forEach((item) -> {
                // 获取错误的属性的名字和错误提示
                map.put(item.getField(), item.getDefaultMessage());
            });
            return Result.other(ResultEnum.PARAMETER_ERROR, map);
        }
        return Result.success(vo);
    }
}
