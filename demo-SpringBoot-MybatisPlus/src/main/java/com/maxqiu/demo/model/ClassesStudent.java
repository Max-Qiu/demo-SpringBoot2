package com.maxqiu.demo.model;

import java.util.List;

import com.maxqiu.demo.entity.Classes;
import com.maxqiu.demo.entity.Student;

/**
 * 自定义班级
 * 
 * @author Max_Qiu
 */
public class ClassesStudent extends Classes {
    private static final long serialVersionUID = -4328989516223829865L;
    private List<Student> studentList;

    public List<Student> getStudentList() {
        return studentList;
    }

    public void setStudentList(List<Student> studentList) {
        this.studentList = studentList;
    }

    @Override
    public String toString() {
        return "ClassesStudent{" + "studentList=" + studentList + "} " + super.toString();
    }
}
