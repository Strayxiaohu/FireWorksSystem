package com.xiaohu.fireworkssystem.model.bean;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.io.Serializable;

/**
 * Created by Administrator on 2016/7/28.
 */
@DatabaseTable(tableName = "user")
public class UserBean implements Serializable {
    //表示id为主键且自动生成
    @DatabaseField(generatedId = true)
    private int id;
    //数据库的字段名称
    @DatabaseField(columnName = "name")
    private String name;
    @DatabaseField
    private int age;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }
}
