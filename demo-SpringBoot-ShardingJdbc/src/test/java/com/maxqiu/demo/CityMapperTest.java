package com.maxqiu.demo;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.maxqiu.demo.entity.City;
import com.maxqiu.demo.mapper.CityMapper;

/**
 * @author Max_Qiu
 */
@SpringBootTest
public class CityMapperTest {
    @Autowired
    private CityMapper cityMapper;

    @Test
    void addEmployee() {
        City city = new City();
        city.setName("江苏");
        // 观察控制台输出，在每个库里面都执行了插入语句
        cityMapper.insert(city);
    }

    @Test
    void getEmployee() {
        // 取值时，从任意一个库中取出数据
        City city = cityMapper.selectById(1522883012481159170L);
        System.out.println(city);
    }
}
