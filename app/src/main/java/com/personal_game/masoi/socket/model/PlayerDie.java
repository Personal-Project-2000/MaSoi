package com.personal_game.masoi.socket.model;

public class PlayerDie {
    private String user;
    private String roomId;
    private String userName;
    private String img;

    public PlayerDie(String user, String roomId, String userName, String img) {
        this.user = user;
        this.roomId = roomId;
        this.userName = userName;
        this.img = img;
    }
}
