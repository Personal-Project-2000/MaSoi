package com.personal_game.masoi.object;

public class Request_JoinRoom {
    private String RoomId;
    private String Tk;
    private String Pass;

    public String getRoomId() {
        return RoomId;
    }

    public void setRoomId(String roomId) {
        RoomId = roomId;
    }

    public String getTk() {
        return Tk;
    }

    public void setTk(String tk) {
        Tk = tk;
    }

    public String getPass() {
        return Pass;
    }

    public void setPass(String pass) {
        Pass = pass;
    }

    public Request_JoinRoom(String roomId, String tk, String pass) {
        RoomId = roomId;
        Tk = tk;
        Pass = pass;
    }
}
