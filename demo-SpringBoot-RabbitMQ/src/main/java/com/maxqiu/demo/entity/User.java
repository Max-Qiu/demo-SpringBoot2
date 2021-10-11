package com.maxqiu.demo.entity;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * 实体对象
 *
 * 必须实现 Serializable 序列化接口
 *
 * @author Max_Qiu
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class User implements Serializable {
    private static final long serialVersionUID = -7312073258182989485L;
    private Integer id;
    private String name;
}
