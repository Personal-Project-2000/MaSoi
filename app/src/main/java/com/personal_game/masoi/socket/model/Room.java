package com.personal_game.masoi.socket.model;

public class Room {
    private String roomId;
    private String pass;
    private int voteTime;
    private int advocateTime;
    private String historyId;

    public Room(String roomId, String pass, int voteTime, int advocateTime) {
        this.roomId = roomId;
        this.pass = pass;
        this.voteTime = voteTime;
        this.advocateTime = advocateTime;
    }

    public Room(String roomId) {
        this.roomId = roomId;
    }

    public Room(String roomId, int voteTime, int advocateTime, String historyId) {
        this.roomId = roomId;
        this.voteTime = voteTime;
        this.advocateTime = advocateTime;
        this.historyId = historyId;
    }


}
