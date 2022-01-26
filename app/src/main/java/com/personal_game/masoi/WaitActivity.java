package com.personal_game.masoi;

import static com.personal_game.masoi.api.RetrofitServer.getRetrofit_lib;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;
import com.personal_game.masoi.adapter.PlayerAdapter;
import com.personal_game.masoi.adapter.PlayerAdapter1;
import com.personal_game.masoi.api.ServiceAPI_lib;
import com.personal_game.masoi.databinding.ActivityWaitBinding;
import com.personal_game.masoi.dialog.ConfirmDialog;
import com.personal_game.masoi.dialog.KickDialog;
import com.personal_game.masoi.dialog.SettingDialog;
import com.personal_game.masoi.object.Message;
import com.personal_game.masoi.object.Message_RoomDetail;
import com.personal_game.masoi.object.PlayerObject;
import com.personal_game.masoi.object.PlayerObject1;
import com.personal_game.masoi.object.RoomObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class WaitActivity extends AppCompatActivity {

    private ActivityWaitBinding activityWaitBinding;
    private PlayerAdapter1 playerAdapter;
    private int code; //1: createRoom || 2: joinRoom
    private String roomId;
    private List<PlayerObject1> playerList;
    private String Tk;
    private RoomObject room;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityWaitBinding = ActivityWaitBinding.inflate(getLayoutInflater());
        View view = activityWaitBinding.getRoot();
        setContentView(view);

        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        init();
    }

    private void init(){
        Intent intent = getIntent();
        code = intent.getIntExtra("code", 2);
        roomId = intent.getStringExtra("roomId");
        Tk = intent.getStringExtra("Tk");

        if(code == 2) {
            activityWaitBinding.btnReady.setText("Sẵn sàng");
        }

        setListeners();
        setRoom();
    }

    private void setListeners(){
        activityWaitBinding.btnMenu.setOnClickListener(v -> {
            DrawerLayout d =  findViewById(R.id.drawerLayout);
            d.openDrawer(GravityCompat.START);
        });

        activityWaitBinding.navigationView.setItemIconTintList(null);

        activityWaitBinding.navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.nav_setting: {
                        SettingDialog dialog = new SettingDialog(WaitActivity.this, code, new SettingDialog.SettingListeners() {
                            @Override
                            public void onClickSave() {

                            }
                        }, room);

                        dialog.show();
                        if(code == 1) {
                            dialog.getWindow().setLayout(700, 500);
                        }else{
                            dialog.getWindow().setLayout(450, 380);
                        }
                        break;
                    }
                    case R.id.nav_kick: {
                        if(code == 1) {
                            KickDialog dialog = new KickDialog(WaitActivity.this, playerList, new KickDialog.KickListeners() {
                                @Override
                                public void OnClick(int position) {
                                    playerAdapter.notifyItemRemoved(position);
                                }
                            }, roomId);

                            dialog.show();
                            dialog.getWindow().setLayout(700, 1050);
                        }
                        else{
                            Toast.makeText(getApplication(), "Chủ phòng mới sử dụng được chức năng này", Toast.LENGTH_SHORT).show();
                        }
                        break;
                    }
                    case R.id.nav_signout: {
                        ConfirmDialog dialog = new ConfirmDialog(WaitActivity.this, new ConfirmDialog.ExitListeners() {
                            @Override
                            public void onClickYes() {
                                exitRoom();
                            }
                        }, "Bạn muốn rời khỏi phòng?");

                        dialog.show();
                        dialog.getWindow().setLayout(600, 250);
                        break;
                    }
                }
                return false;
            }
        });

        activityWaitBinding.btnReady.setOnClickListener(v -> {
            if(code == 2){
                Ready();
            }else{
                Start();
            }
        });

        activityWaitBinding.imageViewMic.setOnClickListener(v -> {
            Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
            intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
            intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
            intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "hãy nói gì đi");
            try {
                activityResultLauncher.launch(intent);
            } catch (Exception e) {
                Toast.makeText(getApplication(), "Error",Toast.LENGTH_SHORT).show();
            }
        });
    }

    ActivityResultLauncher<Intent> activityResultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
        @Override
        public void onActivityResult(ActivityResult result) {
            if (result.getResultCode() == Activity.RESULT_OK) {
                if (result.getData() != null) {
                    ArrayList<String> data = result.getData().getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    activityWaitBinding.inputSearch.setText(Objects.requireNonNull(data).get(0));
                } else {
                    Toast.makeText(getApplication(),"Vui lòng thử lại.", Toast.LENGTH_SHORT).show();
                }
            }
        }
    });

    private void loading(boolean Loading) {
        if (Loading) {
            activityWaitBinding.progressBar.setVisibility(View.VISIBLE);
            activityWaitBinding.btnReady.setVisibility(View.GONE);
        } else {
            activityWaitBinding.progressBar.setVisibility(View.GONE);
            activityWaitBinding.btnReady.setVisibility(View.VISIBLE);
        }
    }

    private void setRoom(){
        ServiceAPI_lib serviceAPI_lib = getRetrofit_lib().create(ServiceAPI_lib.class);
        Call<Message_RoomDetail> call = serviceAPI_lib.GetRoom(roomId);
        call.enqueue(new Callback<Message_RoomDetail>() {
            @Override
            public void onResponse(Call<Message_RoomDetail> call, Response<Message_RoomDetail> response) {
                if(response.body().getStatus1() == 1 && response.body().getPlayers1() != null){
                    playerList = response.body().getPlayers1();
                    room = response.body().getRoomInfo1();

                    playerAdapter = new PlayerAdapter1(playerList, getApplication(), 2, new PlayerAdapter1.PlayerListeners() {
                        @Override
                        public void onClick() {

                        }
                    });
                    activityWaitBinding.rclPlayer.setAdapter(playerAdapter);

                    activityWaitBinding.txtName.setText("Phòng: "+response.body().getRoomInfo1().getNumber());
                }
            }

            @Override
            public void onFailure(Call<Message_RoomDetail> call, Throwable t) {

            }
        });
    }

    private void exitRoom(){
        ServiceAPI_lib serviceAPI_lib = getRetrofit_lib().create(ServiceAPI_lib.class);
        Call<Message> call = serviceAPI_lib.ExitRoom(roomId, Tk);
        call.enqueue(new Callback<Message>() {
            @Override
            public void onResponse(Call<Message> call, Response<Message> response) {
                if(response.body().getStatus1() == 1){
                    finish();
                }
            }

            @Override
            public void onFailure(Call<Message> call, Throwable t) {
                Toast.makeText(getApplication(), "Đuổi thất bại", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private int takePosition(){
        for (int i = 0; i < playerList.size(); i ++) {
            if(playerList.get(i).getTk().equals(Tk) ){
                return i;
            }
        }
        return 0;
    }

    private void Ready(){
        loading(true);
        ServiceAPI_lib serviceAPI_lib = getRetrofit_lib().create(ServiceAPI_lib.class);
        Call<Message> call = serviceAPI_lib.Ready(Tk, roomId);
        call.enqueue(new Callback<Message>() {
            @Override
            public void onResponse(Call<Message> call, Response<Message> response) {
                loading(false);
                if(response.body().getStatus1() == 1){
                    int position = takePosition();
                    if(playerList.get(position).isStatus()){
                        playerList.get(position).setStatus(false);
                        activityWaitBinding.btnReady.setBackgroundResource(R.color.black);
                    }else{
                        playerList.get(position).setStatus(true);
                        activityWaitBinding.btnReady.setBackgroundResource(R.color.yellow);
                    }

                    playerAdapter.notifyItemChanged(position);
                }else{
                    Toast.makeText(getApplication(), response.body().getNotification1(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Message> call, Throwable t) {
                loading(false);
                Toast.makeText(getApplication(), "Tạo phòng thất bại", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void Start(){
        loading(true);
        ServiceAPI_lib serviceAPI_lib = getRetrofit_lib().create(ServiceAPI_lib.class);
        Call<Message> call = serviceAPI_lib.Start(roomId, 1);
        call.enqueue(new Callback<Message>() {
            @Override
            public void onResponse(Call<Message> call, Response<Message> response) {
                loading(false);
                if(response.body().getStatus1() == 1){
                    Intent intent = new Intent(WaitActivity.this, PlayActivity.class);
                    intent.putExtra("playerList", (Serializable) playerList);
                    intent.putExtra("room", room);
                    intent.putExtra("Tk", Tk);
                    startActivity(intent);
                }else{
                    Toast.makeText(getApplication(), response.body().getNotification1(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Message> call, Throwable t) {
                loading(false);
                Toast.makeText(getApplication(), "Tạo phòng thất bại", Toast.LENGTH_SHORT).show();
            }
        });
    }
}