package com.personal_game.masoi.object;

import java.io.Serializable;

public class RoomObject implements Serializable {
    private String id;
    private String number;
    private String pass;
    private int voteTime;
    private int advocateTime;
    private int sl;
    private boolean format;

    public RoomObject(String id, String pass, int voteTime, int advocateTime, boolean format) {
        this.id = id;
        this.pass = pass;
        this.voteTime = voteTime;
        this.advocateTime = advocateTime;
        this.format = format;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getPass() {
        return pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }

    public int getVoteTime() {
        return voteTime;
    }

    public void setVoteTime(int voteTime) {
        this.voteTime = voteTime;
    }

    public int getAdvocateTime() {
        return advocateTime;
    }

    public void setAdvocateTime(int advocateTime) {
        this.advocateTime = advocateTime;
    }

    public int getSl() {
        return sl;
    }

    public void setSl(int sl) {
        this.sl = sl;
    }

    public boolean isFormat() {
        return format;
    }

    public void setFormat(boolean format) {
        this.format = format;
    }

    public RoomObject(String id, String number, String pass, int voteTime, int advocateTime, int sl, boolean format) {
        this.id = id;
        this.number = number;
        this.pass = pass;
        this.voteTime = voteTime;
        this.advocateTime = advocateTime;
        this.sl = sl;
        this.format = format;
    }
}
