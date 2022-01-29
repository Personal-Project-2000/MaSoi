package com.personal_game.masoi.socket.model;

public class PlayerPotion {
    private String user;
    private String userPotion;
    private int code;
    private String roomId;

    public PlayerPotion(String user, String userPotion, int code, String roomId) {
        this.user = user;
        this.userPotion = userPotion;
        this.code = code;
        this.roomId = roomId;
    }


}
