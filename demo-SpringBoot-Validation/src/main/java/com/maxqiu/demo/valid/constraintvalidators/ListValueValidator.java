package com.maxqiu.demo.valid.constraintvalidators;

import java.util.HashSet;
import java.util.Set;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import com.maxqiu.demo.valid.constraints.ListValue;

/**
 * ListValue校验器
 *
 * @author Max_Qiu
 */
public class ListValueValidator implements ConstraintValidator<ListValue, Integer> {
    private Set<Integer> set = new HashSet<>();

    /**
     * 初始化
     */
    @Override
    public void initialize(ListValue constraintAnnotation) {
        // 将注解中的合法值取出，放在set中
        for (int val : constraintAnnotation.valueList()) {
            set.add(val);
        }
    }

    /**
     * 判断是否校验成功
     *
     * @param value
     *            需要校验的值
     */
    @Override
    public boolean isValid(Integer value, ConstraintValidatorContext context) {
        return set.contains(value);
    }
}
