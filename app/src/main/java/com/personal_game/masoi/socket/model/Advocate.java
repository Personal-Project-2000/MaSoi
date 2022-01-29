package com.personal_game.masoi.socket.model;

public class Advocate {
    private String user;
    private String roomId;
    private int agreeAdvocate;

    public Advocate(String user, String roomId, int agreeAdvocate) {
        this.user = user;
        this.roomId = roomId;
        this.agreeAdvocate = agreeAdvocate;
    }
}
