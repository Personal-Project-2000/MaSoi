package com.personal_game.masoi.object;

import java.util.List;

public class Message_RoomList {
    private int status1;
    private String notification1;
    private List<RoomObject> roomInfo1;

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

    public List<RoomObject> getRoomInfo1() {
        return roomInfo1;
    }

    public void setRoomInfo1(List<RoomObject> roomInfo1) {
        this.roomInfo1 = roomInfo1;
    }

    public Message_RoomList(int status1, String notification1, List<RoomObject> roomInfo1) {
        this.status1 = status1;
        this.notification1 = notification1;
        this.roomInfo1 = roomInfo1;
    }
}
