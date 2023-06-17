package com.google.android.ui.mine.bean;

import org.litepal.crud.LitePalSupport;

public class UserBean extends LitePalSupport {
    private int id;
    private String username;
    private String sex;
    private int age;
    private String head;

    @Override
    public String toString() {
        return "UserBean{" +
                "id='" + id + '\'' +
                ", username='" + username + '\'' +
                ", sex='" + sex + '\'' +
                ", age='" + age + '\'' +
                ", head='" + head + '\'' +
                '}';
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getHead() {
        return head;
    }

    public void setHead(String head) {
        this.head = head;
    }
}
