package com.personal_game.masoi.object;

public class Message_Adress {
    private int status1;
    private String notification1;
    private String port1;
    private String ip1;

    public Message_Adress(int status1, String notification1, String port1, String ip1) {
        this.status1 = status1;
        this.notification1 = notification1;
        this.port1 = port1;
        this.ip1 = ip1;
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

    public String getPort1() {
        return port1;
    }

    public void setPort1(String port1) {
        this.port1 = port1;
    }

    public String getIp1() {
        return ip1;
    }

    public void setIp1(String ip1) {
        this.ip1 = ip1;
    }
}
