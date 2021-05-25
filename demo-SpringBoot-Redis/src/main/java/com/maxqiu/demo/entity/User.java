package com.maxqiu.demo.entity;

import java.io.Serializable;
import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * @author Max_Qiu
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors
public class User implements Serializable {
    private static final long serialVersionUID = 8782039579040438137L;
    private Integer id;
    private String name;
    private BigDecimal height;
}
