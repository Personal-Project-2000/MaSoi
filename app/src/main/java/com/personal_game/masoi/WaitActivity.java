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
import android.util.Log;
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
import com.personal_game.masoi.object.Message_BaiInfo;
import com.personal_game.masoi.object.Message_RoomDetail;
import com.personal_game.masoi.object.PlayerObject;
import com.personal_game.masoi.object.PlayerObject1;
import com.personal_game.masoi.object.RoomObject;
import com.personal_game.masoi.socket.SetupSocket;

import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

import io.socket.emitter.Emitter;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class WaitActivity extends AppCompatActivity {

    private ActivityWaitBinding activityWaitBinding;
    private PlayerAdapter1 playerAdapter;
    private int code; //1: createRoom || 2: joinRoom
    private List<PlayerObject1> playerList;
    private PlayerObject1 mine;
    private String Tk;
    private RoomObject room;
    private int tang = 0;

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
        room = (RoomObject) intent.getSerializableExtra("room");
        playerList = (List<PlayerObject1>) intent.getSerializableExtra("playerList");
        mine = (PlayerObject1) intent.getSerializableExtra("mine");
        Tk = intent.getStringExtra("Tk");

        if(code == 2) {
            activityWaitBinding.btnReady.setText("Sẵn sàng");
        }

        setListeners();
        setRoom();
        socket();
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
                            }, room.getId());

                            dialog.show();
                            //dialog.getWindow().setLayout(700, 1050);
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
                                if(mine.isBoss())
                                    exitRoom(3);
                                else
                                    exitRoom(1);
                            }
                        }, "Bạn muốn rời khỏi phòng?");

                        dialog.show();
                        //dialog.getWindow().setLayout(600, 250);
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
        playerAdapter = new PlayerAdapter1(playerList, getApplication(), 2, new PlayerAdapter1.PlayerListeners() {
            @Override
            public void onClick(PlayerObject1 player) {

            }
        });
        activityWaitBinding.rclPlayer.setAdapter(playerAdapter);

        activityWaitBinding.txtName.setText("Phòng: "+room.getNumber());
    }

    private void exitRoom(int code){
        ServiceAPI_lib serviceAPI_lib = getRetrofit_lib().create(ServiceAPI_lib.class);
        Call<Message> call = serviceAPI_lib.ExitRoom(room.getId(), Tk);
        call.enqueue(new Callback<Message>() {
            @Override
            public void onResponse(Call<Message> call, Response<Message> response) {
                if(response.body().getStatus1() == 1){
                    SetupSocket.exitRoom(Tk, room.getId(), code);

                    Intent intent = new Intent(WaitActivity.this, MainActivity.class);
                    intent.putExtra("Tk", Tk);
                    startActivity(intent);
                }
            }

            @Override
            public void onFailure(Call<Message> call, Throwable t) {
                Toast.makeText(getApplication(), "Đuổi thất bại", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private int takePosition(String name){
        for (int i = 0; i < playerList.size(); i ++) {
            if(playerList.get(i).getTk().equals(name) ){
                return i;
            }
        }
        return 0;
    }

    private void Ready() {
        loading(true);
        ServiceAPI_lib serviceAPI_lib = getRetrofit_lib().create(ServiceAPI_lib.class);
        Call<Message> call = serviceAPI_lib.Ready(Tk, room.getId());
        call.enqueue(new Callback<Message>() {
            @Override
            public void onResponse(Call<Message> call, Response<Message> response) {
                loading(false);
                if(response.body().getStatus1() == 1){
                    int position = takePosition(Tk);
                    if(playerList.get(position).isStatus()){
                        SetupSocket.ready(Tk, room.getId(), false);

                        playerList.get(position).setStatus(false);
                        activityWaitBinding.btnReady.setBackgroundResource(R.color.black);
                    }else{
                        SetupSocket.ready(Tk, room.getId(), true);

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
        Call<Message> call = serviceAPI_lib.Start(room.getId(), 1);
        call.enqueue(new Callback<Message>() {
            @Override
            public void onResponse(Call<Message> call, Response<Message> response) {
                loading(false);
                if(response.body().getStatus1() == 1){
                    SetupSocket.start(room.getId(), room.getVoteTime(), room.getAdvocateTime(), response.body().getId1());
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

    private void GetBai(String baiId, String historyId){
        ServiceAPI_lib serviceAPI_lib = getRetrofit_lib().create(ServiceAPI_lib.class);
        Call<Message_BaiInfo> call = serviceAPI_lib.BaiInfo(baiId, mine.getTk(), room.getId(), historyId);
        call.enqueue(new Callback<Message_BaiInfo>() {
            @Override
            public void onResponse(Call<Message_BaiInfo> call, Response<Message_BaiInfo> response) {
                if(response.body().getStatus1() == 1 && response.body().getBaiInfo1() != null){
                    Intent intent = new Intent(WaitActivity.this, PlayActivity.class);
                    intent.putExtra("playerList", (Serializable) playerList);
                    intent.putExtra("room", room);
                    intent.putExtra("bai", response.body().getBaiInfo1());
                    intent.putExtra("mine", mine);
                    intent.putExtra("Tk", Tk);
                    startActivity(intent);
                }else{
                    Toast.makeText(getApplication(), "Hãy thử đăng nhập lại", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(WaitActivity.this, SignInActivity.class);
                    startActivity(intent);
                }
                loading(false);
            }

            @Override
            public void onFailure(Call<Message_BaiInfo> call, Throwable t) {
                Toast.makeText(getApplication(), "Hãy thử đăng nhập lại", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(WaitActivity.this, SignInActivity.class);
                startActivity(intent);
                loading(false);
            }
        });
    }

    private void socket(){
        SetupSocket.mSocket.on("S_startRoom", startRoom);
        SetupSocket.mSocket.on("S_startRoomBoss", startBoss);
        SetupSocket.mSocket.on("S_joinroom", joinRoom);
        SetupSocket.mSocket.on("S_exitroom", exitRoom);
        SetupSocket.mSocket.on("S_kickroom", kickRoom);
        SetupSocket.mSocket.on("S_settingroom", settingRoom);
        SetupSocket.mSocket.on("S_bossout", bossOut);
        SetupSocket.mSocket.on("S_readyroom", ready);
    }

    private final Emitter.Listener joinRoom = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Log.e("lỗi tàng phá", tang+"");
                    tang++;
                    JSONObject data = (JSONObject) args[0];
                    String user = data.optString("user");
                    String userName = data.optString("userName");
                    String userImg = data.optString("userImg");

                    playerList.add(new PlayerObject1(user, userName, userImg, false, false));
                    playerAdapter.notifyItemInserted(playerList.size()-1);
                }
            });
        }
    };

    private final Emitter.Listener exitRoom = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    JSONObject data = (JSONObject) args[0];
                    String user = data.optString("user");

                    int position = takePosition(user);
                    playerList.remove(position);
                    playerAdapter.notifyItemRemoved(position);
                }
            });
        }
    };

    private final Emitter.Listener kickRoom = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Intent intent = new Intent(WaitActivity.this, MainActivity.class);
                    intent.putExtra("Tk", Tk);
                    startActivity(intent);
                }
            });
        }
    };

    private final Emitter.Listener settingRoom = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    JSONObject data = (JSONObject) args[0];
                    String pass = data.optString("pass");
                    int voteTime = data.optInt("voteTime");
                    int advocateTime = data.optInt("advocateTime");

                    if(room != null){
                        room.setPass(pass);
                        room.setVoteTime(Integer.parseInt(voteTime+""));
                        room.setAdvocateTime(Integer.parseInt(advocateTime+""));
                    }
                }
            });
        }
    };

    private final Emitter.Listener bossOut = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    code = 1;

                    int position = takePosition(Tk);

                    if(playerList.size() > 0){
                        playerList.get(position).setStatus(true);
                        playerAdapter.notifyItemChanged(position);
                    }

                    activityWaitBinding.btnReady.setText("Bắt Đầu");
                }
            });
        }
    };

    private final Emitter.Listener ready = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    JSONObject data = (JSONObject) args[0];
                    String user = data.optString("user");
                    Boolean ready = data.optBoolean("ready");

                    int position = takePosition(user);

                    //đôi lúc sinh lỗi ngoài phạm vi nên kiểm tra cho an toàn
                    if(playerList.size() > 0){
                        playerList.get(position).setStatus(ready);
                        playerAdapter.notifyItemChanged(position);
                    }
                }
            });
        }
    };

    private final Emitter.Listener startRoom = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    JSONObject data = (JSONObject) args[0];
                    String bai = data.optString("bai");
                    String historyId = data.optString("historyId");
                    loading(true);
                    GetBai(bai, historyId);
                }
            });
        }
    };

    private final Emitter.Listener startBoss = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    JSONObject data = (JSONObject) args[0];
                    String bai = data.optString("bai");
                    String historyId = data.optString("historyId");
                    loading(true);
                    GetBai(bai, historyId);
                }
            });
        }
    };
}