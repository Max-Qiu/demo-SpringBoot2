package com.maxqiu.demo.config;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import com.maxqiu.demo.entity.User;
import com.maxqiu.demo.service.UserService;

/**
 * @author Max_Qiu
 */
@Configuration
public class SecurityConfig {
    @Resource
    private UserService userService;

    /**
     * 用户详细信息
     */
    @Bean
    public UserDetailsService userDetailsService() {
        return username -> {
            User user = userService.getByUsername(username);
            if (user == null) {
                throw new UsernameNotFoundException("用户名不存在！");
            }
            List<GrantedAuthority> auths = AuthorityUtils.commaSeparatedStringToAuthorityList("admin,role");
            // 这里创建的用户是org.springframework.security.core.userdetails.User
            return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(), auths);
        };
    }

    /**
     * 指定密码的加密方式，这样在密码的前面就不需要添加{bcrypt}
     */
    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * 自定义安全配置
     */
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        // 指定支持基于表单的身份验证
        http.formLogin(e -> {
            // 自定义登录页面（包含GET和POST）
            e.loginPage("/login");
        });

        // 启用记住我
        http.rememberMe(e -> {
            // 设置token时间（单位：秒）
            e.tokenValiditySeconds(7 * 24 * 60 * 60);
        });

        // 配置认证
        http.authorizeRequests(e -> {
            // 指定登录页面无需权限校验
            e.antMatchers("/login").permitAll();
            // 所有的其他请求都需要认证
            e.anyRequest().authenticated();
        });

        // 关闭 csrf
        http.csrf().disable();
        return http.build();
    }
}
