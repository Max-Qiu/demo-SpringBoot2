package com.maxqiu.demo.config;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

/**
 * 生成密码
 *
 * @author Max_Qiu
 */
public class GeneratePassword {
    /**
     * 使用该方法可以生成密码
     */
    public static void main(String[] args) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String result = encoder.encode("123");
        System.out.println(result);
    }
}
