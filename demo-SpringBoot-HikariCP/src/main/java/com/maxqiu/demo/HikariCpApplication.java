package com.maxqiu.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Max_Qiu
 */
@SpringBootApplication
@RestController
public class HikariCpApplication {
    public static void main(String[] args) {
        SpringApplication.run(HikariCpApplication.class, args);
    }

    @Autowired
    JdbcTemplate jdbcTemplate;

    @GetMapping("sql")
    public String sql() {
        jdbcTemplate.execute("select * from `user` where id = 1");
        jdbcTemplate.execute("select * from `user` where id = 2");
        jdbcTemplate.execute("select * from `user` where id = 3");
        return "123";
    }
}
