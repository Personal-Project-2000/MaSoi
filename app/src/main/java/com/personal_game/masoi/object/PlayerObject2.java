package com.personal_game.masoi.object;

//object dành vào lúc khi vào activity Play
public class PlayerObject2 {
    public PlayerObject2(String tk, String name, String img, boolean patriarch, boolean hypnosis, boolean love, boolean die) {
        this.tk = tk;
        this.name = name;
        this.img = img;
        this.patriarch = patriarch;
        this.hypnosis = hypnosis;
        this.love = love;
        this.die = die;
    }

    private String tk;
    private String name;
    private String img;
    private boolean patriarch;
    private boolean hypnosis;
    private boolean love;
    private boolean die;

    public PlayerObject2(String tk, String name, String img) {
        this.tk = tk;
        this.name = name;
        this.img = img;
        this.die = false;
        this.love = false;
        this.hypnosis = false;
        this.patriarch = false;
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

    public boolean isPatriarch() {
        return patriarch;
    }

    public void setPatriarch(boolean patriarch) {
        this.patriarch = patriarch;
    }

    public boolean isHypnosis() {
        return hypnosis;
    }

    public void setHypnosis(boolean hypnosis) {
        this.hypnosis = hypnosis;
    }

    public boolean isLove() {
        return love;
    }

    public void setLove(boolean love) {
        this.love = love;
    }

    public boolean isDie() {
        return die;
    }

    public void setDie(boolean die) {
        this.die = die;
    }
}
