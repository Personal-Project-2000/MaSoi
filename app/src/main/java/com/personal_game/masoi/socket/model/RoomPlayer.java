package com.personal_game.masoi.socket.model;

public class RoomPlayer {
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
