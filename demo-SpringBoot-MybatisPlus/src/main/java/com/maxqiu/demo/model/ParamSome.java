package com.maxqiu.demo.model;

/**
 * @author miemie
 * @since 2018-09-20
 */
public class ParamSome {
    private Integer yihao;
    private String erhao;

    public Integer getYihao() {
        return yihao;
    }

    public void setYihao(Integer yihao) {
        this.yihao = yihao;
    }

    public String getErhao() {
        return erhao;
    }

    public void setErhao(String erhao) {
        this.erhao = erhao;
    }

    public ParamSome() {}

    public ParamSome(Integer yihao, String erhao) {
        this.yihao = yihao;
        this.erhao = erhao;
    }
}
