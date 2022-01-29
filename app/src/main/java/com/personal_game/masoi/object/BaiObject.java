package com.personal_game.masoi.object;

import java.io.Serializable;

public class BaiObject implements Serializable {
    private String id;
    private String name;
    private String description;
    private String img;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public BaiObject(String id, String name, String description, String img) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.img = img;
    }
}
