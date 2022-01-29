package com.personal_game.masoi.socket.model;

public class PlayerBitten {
    private String user;
    private String userBitten;
    private String roomId;

    public PlayerBitten(String user, String userBitten, String roomId) {
        this.user = user;
        this.userBitten = userBitten;
        this.roomId = roomId;
    }
}
