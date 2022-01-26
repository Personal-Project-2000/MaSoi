package com.personal_game.masoi.object;

public class PlayerObject {
    private String tk;
    private String name;
    private String img;
    private String baiName;
    private boolean win;

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

    public String getBaiName() {
        return baiName;
    }

    public void setBaiName(String baiName) {
        this.baiName = baiName;
    }

    public boolean isWin() {
        return win;
    }

    public void setWin(boolean win) {
        this.win = win;
    }

    public PlayerObject(String tk, String name, String img, String baiName, boolean win) {
        this.tk = tk;
        this.name = name;
        this.img = img;
        this.baiName = baiName;
        this.win = win;
    }


}
