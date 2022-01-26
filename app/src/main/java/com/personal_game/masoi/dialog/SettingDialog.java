package com.personal_game.masoi.dialog;

import static com.personal_game.masoi.api.RetrofitServer.getRetrofit_lib;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.Toast;

import com.personal_game.masoi.api.ServiceAPI_lib;
import com.personal_game.masoi.object.Message;
import com.personal_game.masoi.object.RoomObject;
import com.personal_game.masoi.object.SpinnerObject;
import com.personal_game.masoi.R;
import com.personal_game.masoi.adapter.SpinnerAdapter;
import com.personal_game.masoi.databinding.LayoutSettingBinding;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SettingDialog extends Dialog{
    public Activity c;
    private LayoutSettingBinding layoutSettingBinding;
    private int code; //1: createRoom || 2: joinRoom
    private SettingListeners settingListeners;
    private RoomObject room;

    public SettingDialog(Activity a, int code, SettingListeners settingListeners, RoomObject room) {
        super(a);
        this.c = a;
        this.code = code;
        this.settingListeners = settingListeners;
        this.room = room;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        layoutSettingBinding = LayoutSettingBinding.inflate(getLayoutInflater());
        View view = layoutSettingBinding.getRoot();
        setContentView(view);

        init();
    }

    private void init(){
        if(code == 2){
            layoutSettingBinding.inputPass.setVisibility(View.INVISIBLE);
            layoutSettingBinding.spinnerAdvocateTime.setVisibility(View.INVISIBLE);
            layoutSettingBinding.spinnerVoteTime.setVisibility(View.INVISIBLE);
            layoutSettingBinding.btnSave.setVisibility(View.INVISIBLE);
            layoutSettingBinding.txt5.setVisibility(View.INVISIBLE);
            layoutSettingBinding.txt6.setVisibility(View.INVISIBLE);

            layoutSettingBinding.txtPass.setVisibility(View.VISIBLE);
            layoutSettingBinding.txtAdvocateTime.setVisibility(View.VISIBLE);
            layoutSettingBinding.txtVoteTime.setVisibility(View.VISIBLE);

            layoutSettingBinding.txtPass.setText(room.getPass());
            layoutSettingBinding.txtAdvocateTime.setText(room.getAdvocateTime()+" phút");
            layoutSettingBinding.txtVoteTime.setText(room.getVoteTime()+" phút");
        }else{
            layoutSettingBinding.txtPass.setVisibility(View.INVISIBLE);
            layoutSettingBinding.txtAdvocateTime.setVisibility(View.INVISIBLE);
            layoutSettingBinding.txtVoteTime.setVisibility(View.INVISIBLE);

            layoutSettingBinding.inputPass.setText(room.getPass());
            layoutSettingBinding.spinnerVoteTime.setText(room.getVoteTime()+"");
            layoutSettingBinding.spinnerAdvocateTime.setText(room.getAdvocateTime()+"");
        }

        setListeners();
    }

    private void setListeners(){
        layoutSettingBinding.btnSave.setOnClickListener(v -> {
            Setting();
            settingListeners.onClickSave();
        });
    }

    public interface SettingListeners{
        void onClickSave();
    }

    private void Setting(){
        ServiceAPI_lib serviceAPI_lib = getRetrofit_lib().create(ServiceAPI_lib.class);
        Call<Message> call = serviceAPI_lib.Setting(new RoomObject(room.getId(), layoutSettingBinding.inputPass.getText()+"", Integer.parseInt(layoutSettingBinding.spinnerVoteTime.getText()+""), Integer.parseInt(layoutSettingBinding.spinnerAdvocateTime.getText()+""), true));
        call.enqueue(new Callback<Message>() {
            @Override
            public void onResponse(Call<Message> call, Response<Message> response) {
                if(response.body().getStatus1() == 1){
                    room.setPass(layoutSettingBinding.inputPass.getText()+"");
                    room.setAdvocateTime(Integer.parseInt(layoutSettingBinding.spinnerAdvocateTime.getText()+""));
                    room.setVoteTime(Integer.parseInt(layoutSettingBinding.spinnerVoteTime.getText()+""));

                    Toast.makeText(c, response.body().getNotification1(), Toast.LENGTH_SHORT).show();
                    dismiss();
                }
            }

            @Override
            public void onFailure(Call<Message> call, Throwable t) {
                Toast.makeText(c, "Cài đặt phòng thất bại", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
