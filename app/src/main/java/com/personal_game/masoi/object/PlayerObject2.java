package com.personal_game.masoi.object;

//object dành vào lúc khi vào activity Play
public class PlayerObject2 {
    private String tk;
    private String name;
    private String img;
    private boolean boss;
    private boolean patriarch;
    private boolean hypnosis;
    private boolean love;
    private boolean die;
    private boolean dieNew;

    public PlayerObject2(String tk, String name, String img, boolean boss, boolean patriarch, boolean hypnosis, boolean love, boolean die) {
        this.tk = tk;
        this.name = name;
        this.img = img;
        this.boss = boss;
        this.patriarch = patriarch;
        this.hypnosis = hypnosis;
        this.love = love;
        this.die = die;
    }

    public PlayerObject2(String tk, String name, String img, boolean boss) {
        this.tk = tk;
        this.name = name;
        this.img = img;
        this.boss = boss;
        this.die = false;
        this.love = false;
        this.hypnosis = false;
        this.patriarch = false;
        this.dieNew = false;
    }

    public boolean isDieNew() {
        return dieNew;
    }

    public void setDieNew(boolean dieNew) {
        this.dieNew = dieNew;
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
