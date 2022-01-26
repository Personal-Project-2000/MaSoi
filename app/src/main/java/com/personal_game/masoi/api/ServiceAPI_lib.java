package com.personal_game.masoi.api;

import com.personal_game.masoi.object.Message;
import com.personal_game.masoi.object.Message_History;
import com.personal_game.masoi.object.Message_Info;
import com.personal_game.masoi.object.Message_RoomDetail;
import com.personal_game.masoi.object.Message_RoomList;
import com.personal_game.masoi.object.Message_Story;
import com.personal_game.masoi.object.Request_ChangPass;
import com.personal_game.masoi.object.Request_JoinRoom;
import com.personal_game.masoi.object.RoomObject;
import com.personal_game.masoi.object.UserObject;

import java.util.List;

import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.Query;

public interface ServiceAPI_lib {
    //-----------------------------------------------User--------------------------------------------------------

    @POST("Registration_Post")
    Call<Message> Registration(@Body UserObject user);

    @GET("SignIn_Get")
    Call<Message> SignIn(@Query("Tk") String Tk, @Query("Pass") String Pass);

    @GET("GetInfo_Get")
    Call<Message_Info> Info(@Query("Tk") String Tk);

    @POST("ChangePass_Post")
    Call<Message> ChangePass(@Body Request_ChangPass changPass);

    @Multipart
    @POST("UpdateImg_Post")
    Call<Message> UpImgMain(@Query("Tk") String Tk, @Query("domain") String domain, @Part MultipartBody.Part photo);

    @Multipart
    @POST("UpdateImgBack_Post")
    Call<Message> UpImgBack(@Part MultipartBody.Part photo, @Query("Tk") String Tk, @Query("domain") String domain);

    @POST("UpdateInfo_Post")
    Call<Message> UpInfo(@Query("Tk") String Tk, @Query("Name") String Name);

    //-----------------------------------------------History--------------------------------------------------------

    @GET("HistoryList_Get")
    Call<Message_History> GetHistory(@Query("Tk") String Tk);

    @GET("History_Get")
    Call<Message_Story> GetStory(@Query("HistoryId") String HistoryId);

    //-----------------------------------------------Room--------------------------------------------------------

    @GET("RoomList_Get")
    Call<Message_RoomList> RoomList();

    @GET("Room_Get")
    Call<Message_RoomDetail> GetRoom(@Query("RoomId") String RoomId);

    @POST("CreateRoom_Post")
    Call<Message> CreateRoom(@Query("Tk") String Tk);

    @POST("ExitRoom_Post")
    Call<Message> ExitRoom(@Query("RoomId") String RoomId, @Query("Tk") String Tk);

    @POST("JoinRoom_Post")
    Call<Message> JoinRoom(@Body Request_JoinRoom joinRoom);

    @POST("Ready_Post")
    Call<Message> Ready(@Query("Tk") String Tk, @Query("RoomId") String RoomId);

    @POST("Start_Post")
    Call<Message> Start(@Query("RoomId") String RoomId, @Query("Amount") int Amount);

    @POST("UpdateRoom_Post")
    Call<Message> Setting(@Body RoomObject room);
}
