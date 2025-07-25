package com.one.vo;

import lombok.Data;

@Data
public class StudentVO {
    private String name;
    private Integer age;

    public StudentVO(String name, Integer age) {
        this.name = name;
        this.age = age;
    }

    public String setName(String name) {
        this.name = name;
        return "my name is:" + name;
    }

}
