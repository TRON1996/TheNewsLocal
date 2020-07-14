package com.hzz.thenewslocal.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class News implements Serializable {
    private int id;
    private String title;
    private String content;
    private List<String> imgName =new ArrayList<>();
    private String type;
    private String time;
    private User user;
    private  String stringimg;

    public String getStringimg() {
        return stringimg;
    }

    public void setStringimg(String stringimg) {
        this.stringimg = stringimg;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public List<String> getImgName() {
        return imgName;
    }

    public void setImgName(List<String> imgName) {
        this.imgName = imgName;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
