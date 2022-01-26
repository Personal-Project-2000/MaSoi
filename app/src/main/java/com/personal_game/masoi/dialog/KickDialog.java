package com.personal_game.masoi.dialog;

import static com.personal_game.masoi.api.RetrofitServer.getRetrofit_lib;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.Toast;

import com.personal_game.masoi.adapter.KickAdapter;
import com.personal_game.masoi.api.ServiceAPI_lib;
import com.personal_game.masoi.databinding.LayoutKickBinding;
import com.personal_game.masoi.object.Message;
import com.personal_game.masoi.object.PlayerObject;
import com.personal_game.masoi.object.PlayerObject1;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class KickDialog extends Dialog{
    public Activity c;
    public Button save;
    private LayoutKickBinding layoutKickBinding;
    private KickAdapter kickAdapter;
    private List<PlayerObject1> playerList;
    private String roomId;
    private KickListeners kickListeners;

    public KickDialog(Activity a, List<PlayerObject1> playerList, KickListeners kickListeners, String roomId) {
        super(a);
        this.c = a;
        this.playerList = playerList;
        this.kickListeners = kickListeners;
        this.roomId = roomId;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        layoutKickBinding = LayoutKickBinding.inflate(getLayoutInflater());
        View view = layoutKickBinding.getRoot();
        setContentView(view);

        init();
    }

    private void init(){
        setPlayer();
    }

    private void setPlayer(){
        kickAdapter = new KickAdapter(playerList, c, new KickAdapter.KickListeners() {
            @Override
            public void onClick(PlayerObject1 player, int position) {
                KickPlayer(roomId, player.getTk(), position);
            }
        });
        layoutKickBinding.rclPlayer.setAdapter(kickAdapter);
    }

    public interface KickListeners{
        void OnClick(int position);
    }

    private void KickPlayer(String roomId, String Tk, int position){
        ServiceAPI_lib serviceAPI_lib = getRetrofit_lib().create(ServiceAPI_lib.class);
        Call<Message> call = serviceAPI_lib.ExitRoom(roomId, Tk);
        call.enqueue(new Callback<Message>() {
            @Override
            public void onResponse(Call<Message> call, Response<Message> response) {
                if(response.body().getStatus1() == 1){
                    kickListeners.OnClick(position);
                    playerList.remove(position);
                    kickAdapter.notifyItemRemoved(position);
                }
            }

            @Override
            public void onFailure(Call<Message> call, Throwable t) {
                Toast.makeText(c, "Đuổi thất bại", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
