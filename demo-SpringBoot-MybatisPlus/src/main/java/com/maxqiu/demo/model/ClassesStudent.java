package com.maxqiu.demo.model;

import java.util.List;

import com.maxqiu.demo.entity.Classes;
import com.maxqiu.demo.entity.Student;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * 自定义班级
 * 
 * @author Max_Qiu
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@NoArgsConstructor
public class ClassesStudent extends Classes {
    private static final long serialVersionUID = -4328989516223829865L;
    private List<Student> studentList;
}
