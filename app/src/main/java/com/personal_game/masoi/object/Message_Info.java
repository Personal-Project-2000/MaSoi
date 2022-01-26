package com.personal_game.masoi.object;

public class Message_Info {
    public Message_Info(int status1, String notification1, UserObject acc1) {
        this.status1 = status1;
        this.notification1 = notification1;
        this.acc1 = acc1;
    }

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

    public UserObject getAcc1() {
        return acc1;
    }

    public void setAcc1(UserObject acc1) {
        this.acc1 = acc1;
    }

    private int status1;
    private String notification1;
    private UserObject acc1;
}
