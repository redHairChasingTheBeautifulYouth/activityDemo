package com.processVariable;
/*
 *
 * @author LiuBing
 * @date 2018/6/6 20:13
 */

import java.io.Serializable;

public class Person implements Serializable{

    private Integer id;
    private String name;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
