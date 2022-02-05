package com.personal_game.masoi.backup;

import android.content.Context;
import android.content.SharedPreferences;

public class Shared_Preferences {
    private Context context;
    private SharedPreferences sharedPreferences;

    public Shared_Preferences(Context context) {
        this.context = context;
        sharedPreferences = context.getSharedPreferences("data", Context.MODE_PRIVATE);
    }

    public void saveUser(String Tk, String Pass){
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("Tk", Tk);
        editor.putString("Pass", Pass);
        editor.apply();
        editor.commit();
    }

    public void saveIp(String Ip){
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("Ip", Ip);
        editor.apply();
        editor.commit();
    }

    public String getTk(){
        return sharedPreferences.getString("Tk", "");
    }

    public String getPass(){
        return sharedPreferences.getString("Pass", "");
    }

    public String getIp(){
        return sharedPreferences.getString("Ip", "");
    }
}
