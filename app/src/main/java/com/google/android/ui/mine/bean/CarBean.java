package com.google.android.ui.mine.bean;

import androidx.annotation.NonNull;

public class CarBean {
    private String car_id;
    private String car_name;
    private String car_info;
    private String car_lat;
    private String car_lng;

    @NonNull
    @Override
    public String toString() {
        return "CarBean{" +
                "car_id='" + car_id + '\'' +
                ", car_name='" + car_name + '\'' +
                ", car_info='" + car_info + '\'' +
                ", car_lat='" + car_lat + '\'' +
                ", car_lng='" + car_lng + '\'' +
                '}';
    }

    public String getCar_lat() {
        return car_lat;
    }

    public void setCar_lat(String car_lat) {
        this.car_lat = car_lat;
    }

    public String getCar_lng() {
        return car_lng;
    }

    public void setCar_lng(String car_lng) {
        this.car_lng = car_lng;
    }

    public String getCar_id() {
        return car_id;
    }

    public void setCar_id(String car_id) {
        this.car_id = car_id;
    }

    public String getCar_name() {
        return car_name;
    }

    public void setCar_name(String car_name) {
        this.car_name = car_name;
    }

    public String getCar_info() {
        return car_info;
    }

    public void setCar_info(String car_info) {
        this.car_info = car_info;
    }
}
