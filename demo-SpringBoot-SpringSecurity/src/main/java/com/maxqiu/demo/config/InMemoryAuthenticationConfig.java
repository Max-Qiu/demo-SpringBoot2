package com.maxqiu.demo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;

/**
 * 将用户存储在内存中（不推荐）
 *
 * @author Max_Qiu
 */
// @Configuration
public class InMemoryAuthenticationConfig {
    @Bean
    public UserDetailsService users() {
        // 新建用户：用户名为user，密码为password，角色为user，注：{bcrypt}代表使用BCryptPasswordEncoder加密解密
        UserDetails user =
            User.builder().username("user").password("{bcrypt}$2a$10$nKcHTA5eFei3leL/lPlfDuxxwej3kfco8gSptsnNXcRObJfI8lmA6").roles("user").build();
        // 新建用户：用户名为admin，密码为password，角色为admin，注：{bcrypt}代表使用BCryptPasswordEncoder加密解密
        UserDetails admin =
            User.builder().username("admin").password("{bcrypt}$2a$10$mqubM5XE8X42dpPMR1eMiO38IjKvzGiE62zCFjbA0XHI2e90en8Zq").roles("admin").build();
        return new InMemoryUserDetailsManager(user, admin);
    }
}
