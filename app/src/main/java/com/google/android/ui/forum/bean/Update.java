package com.google.android.ui.forum.bean;

import androidx.annotation.NonNull;

public class Update {
    private Integer update_id;
    private Integer driver_id;
    private String driver_name;
    private String update_title;
    private String update_content;
    private String update_time;
    private String update_image;

    @NonNull
    @Override
    public String toString() {
        return "Update{" +
                "update_id=" + update_id +
                ", driver_id=" + driver_id +
                ", driver_name='" + driver_name + '\'' +
                ", update_title='" + update_title + '\'' +
                ", update_content='" + update_content + '\'' +
                ", update_time='" + update_time + '\'' +
                ", update_image='" + update_image + '\'' +
                '}';
    }

    public Integer getUpdate_id() {
        return update_id;
    }

    public void setUpdate_id(Integer update_id) {
        this.update_id = update_id;
    }

    public Integer getDriver_id() {
        return driver_id;
    }

    public void setDriver_id(Integer driver_id) {
        this.driver_id = driver_id;
    }

    public String getDriver_name() {
        return driver_name;
    }

    public void setDriver_name(String driver_name) {
        this.driver_name = driver_name;
    }

    public String getUpdate_title() {
        return update_title;
    }

    public void setUpdate_title(String update_title) {
        this.update_title = update_title;
    }

    public String getUpdate_content() {
        return update_content;
    }

    public void setUpdate_content(String update_content) {
        this.update_content = update_content;
    }

    public String getUpdate_time() {
        return update_time;
    }

    public void setUpdate_time(String update_time) {
        this.update_time = update_time;
    }

    public String getUpdate_image() {
        return update_image;
    }

    public void setUpdate_image(String update_image) {
        this.update_image = update_image;
    }
}
