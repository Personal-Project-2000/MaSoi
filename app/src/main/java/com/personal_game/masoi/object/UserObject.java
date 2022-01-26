package com.personal_game.masoi.object;

import java.io.Serializable;

public class UserObject implements Serializable {
    public UserObject() {
    }

    public UserObject(String tk, String fullName, String pass, String img, String imgBack, String language, Boolean background) {
        this.tk = tk;
        this.fullName = fullName;
        this.pass = pass;
        this.img = img;
        this.imgBack = imgBack;
        this.language = language;
        this.background = background;
    }

    public UserObject(String tk, String fullName, String pass) {
        this.tk = tk;
        this.fullName = fullName;
        this.pass = pass;
    }

    public String getTk() {
        return tk;
    }

    public void setTk(String tk) {
        this.tk = tk;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getPass() {
        return pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getImgBack() {
        return imgBack;
    }

    public void setImgBack(String imgBack) {
        this.imgBack = imgBack;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public Boolean getBackground() {
        return background;
    }

    public void setBackground(Boolean background) {
        this.background = background;
    }

    private String tk ;
    private String fullName ;
    private String pass ;
    private String img ;
    private String imgBack ;
    private String language ;
    private Boolean background ;
}
