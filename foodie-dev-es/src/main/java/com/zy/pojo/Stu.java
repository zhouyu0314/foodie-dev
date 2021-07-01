package com.zy.pojo;

import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.io.Serializable;

/**
 * 配合Test使用
 */
@Document(indexName = "stu" ,type = "_doc") //es7 type可以不写
public class Stu implements Serializable {
    @Id //此注解的作用是会自动创建一个和stuId相同的文档id
    private Long stuId;

    @Field(store = true)//默认情况下不存储原文
    private String name;

    @Field(store = true)
    private Integer age;

    @Field(store = true)
    private Double money;

    @Field(store = true,type = FieldType.Keyword)
    private String sign;

    @Field(store = true)
    private String desc;

    public Long getStuId() {
        return stuId;
    }

    public void setStuId(Long stuId) {
        this.stuId = stuId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public Double getMoney() {
        return money;
    }

    public void setMoney(Double money) {
        this.money = money;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    @Override
    public String toString() {
        return "Stu{" +
                "stuId=" + stuId +
                ", name='" + name + '\'' +
                ", age=" + age +
                ", money=" + money +
                ", sign='" + sign + '\'' +
                ", desc='" + desc + '\'' +
                '}';
    }
}
