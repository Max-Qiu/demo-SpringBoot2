package com.maxqiu.demo;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.maxqiu.demo.repository.UserRepository;

@SpringBootTest
class JpaApplicationTests {
    @Autowired
    private UserRepository userRepository;

    @Test
    void contextLoads() {
        System.out.println(userRepository.listByName("hello"));
    }
}
