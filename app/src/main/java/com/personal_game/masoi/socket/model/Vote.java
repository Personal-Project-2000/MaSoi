package com.personal_game.masoi.socket.model;

public class Vote {
    private String user;
    private String userVote;
    private String roomId;

    public Vote(String user, String userVote, String roomId) {
        this.user = user;
        this.userVote = userVote;
        this.roomId = roomId;
    }
}
