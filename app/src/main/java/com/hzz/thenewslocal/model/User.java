package com.hzz.thenewslocal.model;

import java.io.Serializable;

public class User implements Serializable {
    private int id;
    private String name;
    private String sex;
    private String phone;
    private String password;
    private String photo;
    private String loginImg;
    private String selfinfo;


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

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getLoginImg() {
        return loginImg;
    }

    public void setLoginImg(String loginImg) {
        this.loginImg = loginImg;
    }

    public String getSelfinfo() {
        return selfinfo;
    }

    public void setSelfinfo(String selfinfo) {
        this.selfinfo = selfinfo;
    }
}
