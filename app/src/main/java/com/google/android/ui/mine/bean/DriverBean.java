package com.google.android.ui.mine.bean;

import androidx.annotation.NonNull;

import org.litepal.crud.LitePalSupport;

public class DriverBean extends LitePalSupport {
    private int driver_id;
    private String car_id;
    private String username;
    private String sex;
    private int age;
    private String head;

    @NonNull
    @Override
    public String toString() {
        return "DriverBean{" +
                "driver_id='" + driver_id + '\'' +
                ", car_id='" + car_id + '\'' +
                ", username='" + username + '\'' +
                ", sex='" + sex + '\'' +
                ", age='" + age + '\'' +
                ", head='" + head + '\'' +
                '}';
    }

    public int getDriver_id() {
        return driver_id;
    }

    public void setDriver_id(int driver_id) {
        this.driver_id = driver_id;
    }

    public String getCar_id() {
        return car_id;
    }

    public void setCar_id(String car_id) {
        this.car_id = car_id;
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
