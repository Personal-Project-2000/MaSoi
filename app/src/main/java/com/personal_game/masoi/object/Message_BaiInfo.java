package com.personal_game.masoi.object;

public class Message_BaiInfo {
    private int status1;
    private String notification1;
    private BaiObject baiInfo1;

    public int getStatus1() {
        return status1;
    }

    public void setStatus1(int status1) {
        this.status1 = status1;
    }

    public String getNotification1() {
        return notification1;
    }

    public void setNotification1(String notification1) {
        this.notification1 = notification1;
    }

    public BaiObject getBaiInfo1() {
        return baiInfo1;
    }

    public void setBaiInfo1(BaiObject baiInfo1) {
        this.baiInfo1 = baiInfo1;
    }

    public Message_BaiInfo(int status1, String notification1, BaiObject baiInfo1) {
        this.status1 = status1;
        this.notification1 = notification1;
        this.baiInfo1 = baiInfo1;
    }
}
