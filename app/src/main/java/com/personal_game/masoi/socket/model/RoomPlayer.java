package com.personal_game.masoi.socket.model;

public class RoomPlayer {
    public void setFlute(String flute) {
        this.flute = flute;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public void setRoomId(String roomId) {
        this.roomId = roomId;
    }

    public void setGuard(String guard) {
        this.guard = guard;
    }

    public void setProphesy(String prophesy) {
        this.prophesy = prophesy;
    }

    public void setHunter(String hunter) {
        this.hunter = hunter;
    }

    public void setCupid(String cupid) {
        this.cupid = cupid;
    }

    private String flute;
    private String guard;
    private String prophesy;
    private String hunter;
    private String cupid;
    private String user;
    private boolean ready;
    private int func;
    private String roomId;
    private int code;
    private String roomName;
    private String userName;
    private String userImg;

    public RoomPlayer(String user, String roomId, String roomName, String userName, String userImg) {
        this.user = user;
        this.roomId = roomId;
        this.roomName = roomName;
        this.userName = userName;
        this.userImg = userImg;
    }

    public RoomPlayer(String user, String roomId, String roomName) {
        this.user = user;
        this.roomId = roomId;
        this.roomName = roomName;
    }

    public RoomPlayer(){

    }

    public RoomPlayer(String user, String roomId) {
        this.user = user;
        this.roomId = roomId;
    }

    public RoomPlayer(String user, String roomId, int code) {
        this.user = user;
        this.roomId = roomId;
        this.code = code;
    }

    public RoomPlayer(String user, boolean ready, String roomId) {
        this.user = user;
        this.ready = ready;
        this.roomId = roomId;
    }
}
