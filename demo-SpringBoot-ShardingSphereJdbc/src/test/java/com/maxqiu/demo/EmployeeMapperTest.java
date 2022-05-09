package com.maxqiu.demo;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.maxqiu.demo.entity.Employee;
import com.maxqiu.demo.mapper.EmployeeMapper;

/**
 * @author Max_Qiu
 */
@SpringBootTest
public class EmployeeMapperTest {
    @Autowired
    private EmployeeMapper employeeMapper;

    @Test
    void addEmployee() {
        Employee employee = new Employee();
        employee.setName("TOM");
        employeeMapper.insert(employee);
    }

    @Test
    void getEmployee() {
        Employee employee = employeeMapper.selectById(1522880054993555457L);
        System.out.println(employee);
    }
}
