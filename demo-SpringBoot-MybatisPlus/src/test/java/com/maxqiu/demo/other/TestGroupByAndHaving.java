package com.maxqiu.demo.other;

import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.maxqiu.demo.entity.Student;
import com.maxqiu.demo.mapper.StudentMapper;

/**
 * @author Max_Qiu
 */
@SpringBootTest
public class TestGroupByAndHaving {

    @Autowired
    private StudentMapper studentMapper;

    /**
     * 分组
     */
    @Test
    public void testGroup() {
        // SELECT classes_id, count(*) FROM smp_student GROUP BY classes_id
        QueryWrapper<Student> wrapper = new QueryWrapper<>();
        wrapper.select("classes_id, count(*)").groupBy("classes_id");
        List<Map<String, Object>> maplist = studentMapper.selectMaps(wrapper);
        for (Map<String, Object> mp : maplist) {
            System.out.println(mp);
        }
        // lambda语法可能不太方便获取count(*)这种
        // lambdaQueryWrapper groupBy
        LambdaQueryWrapper<Student> lambdaQueryWrapper =
            new QueryWrapper<Student>().lambda().select(Student::getClassesId).groupBy(Student::getClassesId);
        for (Student user : studentMapper.selectList(lambdaQueryWrapper)) {
            System.out.println(user);
        }
    }

    /**
     * 分组
     * 
     * 并将分组后的数据放入实体字段
     * 
     * 实体中该字段需要设置 @TableField(exist = false)
     */
    @Test
    public void testTableFieldExistFalse() {
        // SELECT classes_id, count(classes_id) as count FROM smp_student GROUP BY classes_id
        QueryWrapper<Student> wrapper = new QueryWrapper<>();
        wrapper.select("classes_id, count(classes_id) as count").groupBy("classes_id");
        List<Student> list = studentMapper.selectList(wrapper);
        list.forEach(System.out::println);
    }

    /**
     * 分组
     *
     * 并将分组后的数据放入实体字段
     *
     * 实体中该字段需要设置 @TableField(exist = false)
     */
    @Test
    public void testGroupByHaving() {
        // SELECT classes_id, count(classes_id) as count FROM smp_student
        // GROUP BY classes_id HAVING count(classes_id) > ?
        QueryWrapper<Student> wrapper = new QueryWrapper<>();
        wrapper.select("classes_id, count(classes_id) as count");
        wrapper.groupBy("classes_id").having("count(classes_id) > {0}", "3");
        List<Student> list = studentMapper.selectList(wrapper);
        list.forEach(System.out::println);
    }
}
