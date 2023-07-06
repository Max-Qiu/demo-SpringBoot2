package com.maxqiu.demo.model;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

/**
 * 自定义分页实体
 *
 * @author Max_Qiu
 * @param <T>
 */
public class MyPage<T> extends Page<T> {
    private static final long serialVersionUID = 6952602466495827040L;

    private Integer selectInt;
    private String selectStr;
    private String username;

    public MyPage(long current, long size) {
        super(current, size);
    }

    public Integer getSelectInt() {
        return selectInt;
    }

    public void setSelectInt(Integer selectInt) {
        this.selectInt = selectInt;
    }

    public String getSelectStr() {
        return selectStr;
    }

    public void setSelectStr(String selectStr) {
        this.selectStr = selectStr;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
