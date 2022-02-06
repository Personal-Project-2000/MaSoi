package com.personal_game.masoi.object;

import java.io.Serializable;

//object dành vào lúc khi vào activity Wait
public class PlayerObject1 implements Serializable {
    private String tk;
    private String name;
    private String img;
    private boolean boss;
    private boolean status;
    private String baiId;

    public String getBaiId() {
        return baiId;
    }

    public void setBaiId(String baiId) {
        this.baiId = baiId;
    }

    public String getTk() {
        return tk;
    }

    public void setTk(String tk) {
        this.tk = tk;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public boolean isBoss() {
        return boss;
    }

    public void setBoss(boolean boss) {
        this.boss = boss;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public PlayerObject1(String tk) {
        this.tk = tk;
    }

    public PlayerObject1(String tk, String name, String img, boolean boss, boolean status) {
        this.tk = tk;
        this.name = name;
        this.img = img;
        this.boss = boss;
        this.status = status;
    }
}
