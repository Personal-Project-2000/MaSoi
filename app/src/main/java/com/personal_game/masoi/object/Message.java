package com.personal_game.masoi.object;

public class Message {
    public Message(int status1, String notification1, String id1) {
        this.status1 = status1;
        this.notification1 = notification1;
        this.id1 = id1;
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

    public String getId1() {
        return id1;
    }

    public void setId1(String id1) {
        this.id1 = id1;
    }

    private int status1;
    private String notification1;
    private String id1;
}
