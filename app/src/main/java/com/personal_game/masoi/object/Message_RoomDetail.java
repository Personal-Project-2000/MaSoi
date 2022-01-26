package com.personal_game.masoi.object;

import java.util.List;

public class Message_RoomDetail {
    private int status1;
    private String notification1;
    private RoomObject roomInfo1;
    private List<PlayerObject1> players1;

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

    public RoomObject getRoomInfo1() {
        return roomInfo1;
    }

    public void setRoomInfo1(RoomObject roomInfo1) {
        this.roomInfo1 = roomInfo1;
    }

    public List<PlayerObject1> getPlayers1() {
        return players1;
    }

    public void setPlayers1(List<PlayerObject1> players1) {
        this.players1 = players1;
    }

    public Message_RoomDetail(int status1, String notification1, RoomObject roomInfo1, List<PlayerObject1> players1) {
        this.status1 = status1;
        this.notification1 = notification1;
        this.roomInfo1 = roomInfo1;
        this.players1 = players1;
    }
}
