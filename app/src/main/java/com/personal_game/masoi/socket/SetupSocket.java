package com.personal_game.masoi.socket;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.gson.Gson;
import com.personal_game.masoi.socket.model.Advocate;
import com.personal_game.masoi.socket.model.PlayerBitten;
import com.personal_game.masoi.socket.model.PlayerPotion;
import com.personal_game.masoi.socket.model.Room;
import com.personal_game.masoi.socket.model.RoomPlayer;
import com.personal_game.masoi.socket.model.User;
import com.personal_game.masoi.socket.model.Vote;

import java.net.URISyntaxException;

import io.socket.client.IO;
import io.socket.client.Socket;

public class SetupSocket {
    //uriGlobal = "https://food-location.herokuapp.com/";
    //uriLocal = "http://192.168.1.4:3030"
    public static String uriLocal = "http://192.168.1.9:3030";
    public static String uriGlobal = "https://masoi-app.herokuapp.com/";
    public static String deviceId;
    public static Socket mSocket;
    {
        try {
            mSocket = IO.socket(SetupSocket.uriLocal);
        } catch (URISyntaxException e) {
            e.getMessage();
        }
    }

//    private void socket() {
//        mSocket.connect();
//    }

    //data: user || device || name
    public static void signIn(String user, String name){
        mSocket.connect();

        Gson gson = new Gson();
        User user1 = new User(user, "", name);
        mSocket.emit("login", gson.toJson(user1));
    }

    public static void signOut(){
        mSocket.emit("signout");
    }

    //data: user || roomId || roomName
    public static void createRoom(String user, String roomId, String roomName){
        Gson gson = new Gson();
        RoomPlayer player = new RoomPlayer(user, roomId, roomName);
        mSocket.emit("createRoom", gson.toJson(player));
    }

    //data: user || roomId || roomName || userName || userImg
    public static void joinRoom(String user, String roomId, String roomName, String userName, String userImg){
        Gson gson = new Gson();
        RoomPlayer player = new RoomPlayer(user, roomId, roomName, userName, userImg);
        mSocket.emit("joinRoom", gson.toJson(player));
    }

    //data: user || roomId || code -> 1: t??? tho??t & 2: b??? ??u???i & 3: ch??? ph??ng tho??t
    public static void exitRoom(String user, String roomId, int code){
        Gson gson = new Gson();
        RoomPlayer player = new RoomPlayer(user, roomId, code);
        mSocket.emit("exitRoom", gson.toJson(player));
    }

    //data: roomId || pass || voteTime || advocateTime
    public static void setting(String roomId, String pass, int voteTime, int advocateTime){
        Gson gson = new Gson();
        Room room = new Room(roomId, pass, voteTime, advocateTime);
        mSocket.emit("setting", gson.toJson(room));
    }

    //data: user || roomId || ready
    public static void ready(String user, String roomId, boolean ready){
        Gson gson = new Gson();
        RoomPlayer player = new RoomPlayer(user, ready, roomId);
        mSocket.emit("ready", gson.toJson(player));
    }

    //data: roomId || voteTime || advocateTime || historyId
    public static void start(String roomId, int voteTime, int advocateTime, String historyId){
        Gson gson = new Gson();
        Room room = new Room(roomId, voteTime, advocateTime, historyId);
        mSocket.emit("start", gson.toJson(room));
    }

    //data: user || roomId || cupid
    public static void cupid(String user, String roomId, String cupid){
        Gson gson = new Gson();
        RoomPlayer player = new RoomPlayer(user, roomId);
        player.setCupid(cupid);
        mSocket.emit("cupid", gson.toJson(player));
    }

    //data: user || roomId || guard
    public static void guard(String user, String roomId, String guard){
        Gson gson = new Gson();
        RoomPlayer player = new RoomPlayer(user, roomId);
        player.setGuard(guard);
        mSocket.emit("protected", gson.toJson(player));
    }

    //data: user || userBitten || roomId
    public static void wolf(String user, String userBitten, String roomId){
        Gson gson = new Gson();
        PlayerBitten player = new PlayerBitten(user, userBitten, roomId);
        mSocket.emit("wolf", gson.toJson(player));
    }

    //data: user || roomId || prophesy
    public static void prophesy(String user, String roomId, String prophesy){
        Gson gson = new Gson();
        RoomPlayer player = new RoomPlayer(user, roomId);
        player.setProphesy(prophesy);
        mSocket.emit("prophesy", gson.toJson(player));
    }

    //data: user || userPotion || code -> 1: help, 2: die || roomId
    public static void witch(String user, String userPotion, int code, String roomId){
        Gson gson = new Gson();
        PlayerPotion player = new PlayerPotion(user, userPotion, code, roomId);
        mSocket.emit("witch", gson.toJson(player));
    }

    //data: user || roomId || flute
    public static void flute(String user, String roomId, String flute){
        Gson gson = new Gson();
        RoomPlayer player = new RoomPlayer();
        player.setUser(user);
        player.setRoomId(roomId);
        player.setFlute(flute);
        mSocket.emit("flute", gson.toJson(player));
    }

    //data: user || roomId || hunter
    public static void hunter(String user, String roomId, String hunter){
        Gson gson = new Gson();
        RoomPlayer player = new RoomPlayer(user, roomId);
        player.setHunter(hunter);
        mSocket.emit("hunter", gson.toJson(player));
    }

    //data: user || userVote || roomId
    public static void vote(String user, String userVote, String roomId){
        Gson gson = new Gson();
        Vote player = new Vote(user, userVote, roomId);
        mSocket.emit("vote", gson.toJson(player));
    }

    //data user || roomId || agreeAdvocate -> 1: ?????ng ??, 2: kh??ng ?????ng ??
    public static void advocate(String user, String roomId, int agreeAdvocate){
        Gson gson = new Gson();
        Advocate player = new Advocate(user, roomId, agreeAdvocate);
        mSocket.emit("advocate", gson.toJson(player));
    }

    // get device token from FCM
//    public static void getToken(String user, Socket mSocket){
//        FirebaseMessaging.getInstance().getToken()
//                .addOnCompleteListener(new OnCompleteListener<String>() {
//                    @Override
//                    public void onComplete(@NonNull Task<String> task) {
//                        if (!task.isSuccessful()) {
//                            Log.e("notification_getToken", "Fetching FCM registration token failed", task.getException());
//                            return;
//                        }
//
//                        // Get new FCM registration token
//                        deviceId = task.getResult();
//
//                        Gson gson = new Gson();
//                        User user1 = new User(user, deviceId);
//                        mSocket.emit("login", gson.toJson(user1));
//                        // Log and toast
//                        Log.e("notification_getToken", deviceId);
//
//                    }
//                });
//    }
}