package com.maxqiu.demo.config;

import java.io.IOException;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.CsrfConfigurer;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.rememberme.JdbcTokenRepositoryImpl;

import com.maxqiu.demo.entity.User;
import com.maxqiu.demo.service.UserService;

/**
 * @author Max_Qiu
 */
@Configuration
public class SecurityConfig {
    @Resource
    private UserService userService;

    @Resource
    private DataSource dataSource;

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
            List<GrantedAuthority> auths = AuthorityUtils.createAuthorityList("admin", "user");
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
        // 开启基于表单的身份验证
        http.formLogin(formLoginConfigurer -> {
            /*
            自定义登录页面，以 /login 为例，有如下四种请求
            /login GET 请求用于展示页面
            /login POST 请求用于页面提交表单验证
            /login?error GET 登录失败后回到登录页面
            /login?logout GET 退出登录后回到登录页面
             */
            formLoginConfigurer.loginPage("/login");

            /*
            配置登录表单用户名和密码的字段名，如果相同则无需修改
            用户名默认为：username
            密码默认为：password
             */
            formLoginConfigurer.usernameParameter(UsernamePasswordAuthenticationFilter.SPRING_SECURITY_FORM_USERNAME_KEY);
            formLoginConfigurer.passwordParameter(UsernamePasswordAuthenticationFilter.SPRING_SECURITY_FORM_PASSWORD_KEY);

            // 不建议使用 ForwardUrl ，因为 URL 地址不会变
            // formLoginConfigurer.successForwardUrl("/success");
            // formLoginConfigurer.failureForwardUrl("/fail");

            /*
            登录成功后跳转的页面
            alwaysUse 是否总是使用该登录成功页 默认为 false
            false ： /a 当前页面没权限跳转登录页 -> /login 执行登录 -> /a 登录成功回到当前页面
            true ： /a 当前页面没权限跳转登录页 -> /login 执行登录 -> / 登录成功回到指定的登录成功页面
             */
            formLoginConfigurer.defaultSuccessUrl("/", false);

            // 登录成功后，根据上文 defaultSuccessUrl 中的源码，默认会走 SavedRequestAwareAuthenticationSuccessHandler 处理器
            // 如果想要在登录成功后实现一些功能，比如记录登录日志等，可以重写 SavedRequestAwareAuthenticationSuccessHandler 的 onAuthenticationSuccess 方法，如下文
            SavedRequestAwareAuthenticationSuccessHandler successHandler = new SavedRequestAwareAuthenticationSuccessHandler() {
                @Override
                public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication)
                    throws ServletException, IOException {
                    // 此处可以做一些登录成功后的操作
                    // 完成操作后一定要调用父类的 onAuthenticationSuccess 方法，否则 url 重定向等操作会失效
                    super.onAuthenticationSuccess(request, response, authentication);
                }
            };
            successHandler.setDefaultTargetUrl("/");
            successHandler.setAlwaysUseDefaultTargetUrl(false);
            // 注意：successHandler 和 defaultSuccessUrl 互斥（看一下 defaultSuccessUrl 源码就知道了），根据实际情况选择合适的方法
            formLoginConfigurer.successHandler(successHandler);
        });

        // 配置退出
        http.logout(logoutConfigurer -> {
            // 指定退出后的页面
            logoutConfigurer.logoutUrl("/logout");
        });

        // 启用记住我
        http.rememberMe(rememberMeConfigurer -> {
            // 设置token时间（单位：秒）
            rememberMeConfigurer.tokenValiditySeconds(7 * 24 * 60 * 60);

            /*
            启用数据库存储登录持久化数据
            数据库表结构在 JdbcTokenRepositoryImpl.CREATE_TABLE_SQL ，需要手动执行
            表创建完成后最好再对 username 字段创建索引，因为删除语句的条件是 username 字段
             */
            JdbcTokenRepositoryImpl jdbcTokenRepository = new JdbcTokenRepositoryImpl();
            // 设置数据源
            jdbcTokenRepository.setDataSource(dataSource);
            rememberMeConfigurer.tokenRepository(jdbcTokenRepository);
        });

        // 配置认证
        http.authorizeRequests(urlRegistry -> {
            // 指定登录页面无需权限校验
            urlRegistry.antMatchers("/login").permitAll();
            // 所有的其他请求都需要认证
            urlRegistry.anyRequest().authenticated();
        });

        // 配置 csrf （直接关闭配置）
        http.csrf(CsrfConfigurer::disable);

        return http.build();
    }
}
