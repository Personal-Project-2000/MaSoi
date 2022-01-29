package com.personal_game.masoi;

import static com.personal_game.masoi.api.RetrofitServer.getRetrofit_lib;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;
import com.personal_game.masoi.adapter.HistoryAdapter;
import com.personal_game.masoi.adapter.MainAdapter;
import com.personal_game.masoi.adapter.PlayerAdapter1;
import com.personal_game.masoi.api.ServiceAPI_lib;
import com.personal_game.masoi.databinding.ActivityMainBinding;
import com.personal_game.masoi.databinding.ActivitySignInBinding;
import com.personal_game.masoi.dialog.ConfirmDialog;
import com.personal_game.masoi.dialog.PassRoomDialog;
import com.personal_game.masoi.object.Message;
import com.personal_game.masoi.object.Message_Info;
import com.personal_game.masoi.object.Message_RoomDetail;
import com.personal_game.masoi.object.Message_RoomList;
import com.personal_game.masoi.object.PlayerObject1;
import com.personal_game.masoi.object.Request_JoinRoom;
import com.personal_game.masoi.object.RoomObject;
import com.personal_game.masoi.object.UserObject;
import com.personal_game.masoi.socket.SetupSocket;
import com.squareup.picasso.Picasso;

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

public class MainActivity extends AppCompatActivity {

    private MainAdapter mainAdapter;
    private ActivityMainBinding activityMainBinding;
    private String Tk;
    private ImageView img;
    private ImageView imgBack;
    private TextView name;
    private UserObject user;
    private List<RoomObject> roomList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityMainBinding = ActivityMainBinding.inflate(getLayoutInflater());
        View view = activityMainBinding.getRoot();
        setContentView(view);

        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        init();
    }

    private void init(){
        img = activityMainBinding.navigationView.getHeaderView(0).findViewById(R.id.imgMain);
        imgBack = activityMainBinding.navigationView.getHeaderView(0).findViewById(R.id.imgBack);
        name = activityMainBinding.navigationView.getHeaderView(0).findViewById(R.id.txtName);

        Tk = getIntent().getStringExtra("Tk");

        Info();
        setListeners();
        setRoom();
        socket();
    }

    private void setListeners(){
        activityMainBinding.btnMenu.setOnClickListener(v -> {
            DrawerLayout d =  findViewById(R.id.drawerLayout);
            d.openDrawer(GravityCompat.START);
        });

        activityMainBinding.navigationView.setItemIconTintList(null);

        activityMainBinding.navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.nav_info: {
                        Intent intent = new Intent(getApplication(), InfoActivity.class);
                        intent.putExtra("user", user);
                        startActivityForResult(intent, 1);
                        break;
                    }
                    case R.id.nav_history: {
                        Intent intent = new Intent(getApplication(), HistoryActivity.class);
                        intent.putExtra("user", user);
                        startActivityForResult(intent, 2);
                        break;
                    }
                    case R.id.nav_setting: {
                        Intent intent = new Intent(getApplication(), SettingActivity.class);
                        intent.putExtra("user", user);
                        startActivityForResult(intent, 3);
                        break;
                    }
                    case R.id.nav_create: {
                        CreateRoom();
                        break;
                    }
                    case R.id.nav_signout: {
                        SetupSocket.signOut();

                        Intent intent = new Intent(getApplication(), SignInActivity.class);
                        startActivity(intent);
                        break;
                    }
                }
                return false;
            }
        });

        activityMainBinding.imageViewMic.setOnClickListener(v -> {
            Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
            intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
            intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
            intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "hãy nói gì đi");
            try {
                activityResultLauncher.launch(intent);
            } catch (Exception e) {
                Toast.makeText(MainActivity.this, "Error",Toast.LENGTH_SHORT).show();
            }
        });
    }

    ActivityResultLauncher<Intent> activityResultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
        @Override
        public void onActivityResult(ActivityResult result) {
            if (result.getResultCode() == Activity.RESULT_OK) {
                if (result.getData() != null) {
                    ArrayList<String> data = result.getData().getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    activityMainBinding.inputSearch.setText(Objects.requireNonNull(data).get(0));
                } else {
                    Toast.makeText(getApplication(),"Vui lòng thử lại.", Toast.LENGTH_SHORT).show();
                }
            }
        }
    });

    private void setRoom(){
        ServiceAPI_lib serviceAPI_lib = getRetrofit_lib().create(ServiceAPI_lib.class);
        Call<Message_RoomList> getRoomList = serviceAPI_lib.RoomList();
        getRoomList.enqueue(new Callback<Message_RoomList>() {
            @Override
            public void onResponse(Call<Message_RoomList> call, Response<Message_RoomList> response) {
                if(response.body().getStatus1() == 1 && response.body().getRoomInfo1() != null){
                    roomList = response.body().getRoomInfo1();

                    mainAdapter = new MainAdapter(roomList, getApplication(), new MainAdapter.MainListeners() {
                        @Override
                        public void onClickLock(RoomObject roomObject) {
                            PassRoomDialog dialog = new PassRoomDialog(MainActivity.this, new PassRoomDialog.JoinListeners() {
                                @Override
                                public void onClick(String pass) {
                                    JoinRoom(roomObject, pass);
                                }
                            });

                            dialog.show();
                            dialog.getWindow().setLayout(650, 270);
                        }

                        @Override
                        public void onClickUnLock(RoomObject roomObject) {
                            JoinRoom(roomObject, "");
                        }
                    });

                    GridLayoutManager gridLayoutManager = new GridLayoutManager(getApplicationContext(), 2);
                    gridLayoutManager.setOrientation(GridLayoutManager.VERTICAL);

                    RecyclerView rcl = findViewById(R.id.rclRoom);
                    rcl.setLayoutManager(gridLayoutManager);
                    rcl.setAdapter(mainAdapter);
                }
            }

            @Override
            public void onFailure(Call<Message_RoomList> call, Throwable t) {

            }
        });
    }

    private void Info(){
        ServiceAPI_lib serviceAPI_lib = getRetrofit_lib().create(ServiceAPI_lib.class);
        Call<Message_Info> info = serviceAPI_lib.Info(Tk);
        info.enqueue(new Callback<Message_Info>() {
            @Override
            public void onResponse(Call<Message_Info> call, Response<Message_Info> response) {
                if(response.body().getStatus1() == 1 && response.body().getAcc1() != null){
                    user = response.body().getAcc1();
                    if(user.getImg() != null){
                        Picasso.Builder builder = new Picasso.Builder(getApplicationContext());
                        builder.listener(new Picasso.Listener() {
                            @Override
                            public void onImageLoadFailed(Picasso picasso, Uri uri, Exception exception) {
                                img.setImageResource(R.drawable.logo);
                            }
                        });
                        Picasso pic = builder.build();
                        pic.load(user.getImg()).into(img);
                    }

                    if(user.getImgBack() != null){
                        Picasso.Builder builder1 = new Picasso.Builder(getApplicationContext());
                        builder1.listener(new Picasso.Listener() {
                            @Override
                            public void onImageLoadFailed(Picasso picasso, Uri uri, Exception exception) {
                                imgBack.setImageResource(R.drawable.background);
                            }
                        });
                        Picasso pic1 = builder1.build();
                        pic1.load(user.getImgBack()).into(imgBack);
                    }

                    name.setText(user.getFullName());
                }
            }

            @Override
            public void onFailure(Call<Message_Info> call, Throwable t) {

            }
        });
    }

    private void CreateRoom(){
        ServiceAPI_lib serviceAPI_lib = getRetrofit_lib().create(ServiceAPI_lib.class);
        Call<Message> call = serviceAPI_lib.CreateRoom(Tk);
        call.enqueue(new Callback<Message>() {
            @Override
            public void onResponse(Call<Message> call, Response<Message> response) {
                if(response.body().getStatus1() == 1){
                    GetRoom(response.body().getId1(), 1, 5);
                }else{
                    Toast.makeText(getApplication(), response.body().getNotification1(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Message> call, Throwable t) {
                Toast.makeText(getApplication(), "Tạo phòng thất bại", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void JoinRoom(RoomObject room, String pass){
        ServiceAPI_lib serviceAPI_lib = getRetrofit_lib().create(ServiceAPI_lib.class);
        Call<Message> call = serviceAPI_lib.JoinRoom(new Request_JoinRoom(room.getId(), Tk, pass));
        call.enqueue(new Callback<Message>() {
            @Override
            public void onResponse(Call<Message> call, Response<Message> response) {
                if(response.body().getStatus1() == 1){
                    SetupSocket.joinRoom(Tk, room.getId(), room.getNumber(), user.getFullName(), user.getImg());

                    GetRoom(room.getId(), 2, 6);
                }else{
                    Toast.makeText(getApplication(), response.body().getNotification1(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Message> call, Throwable t) {
                Toast.makeText(getApplication(), "Tạo phòng thất bại", Toast.LENGTH_SHORT).show();
            }
        });
    }

    //requestCode => 5: gửi thông điệp tạo phòng
    private void GetRoom(String roomId, int code, int requestCode){
        ServiceAPI_lib serviceAPI_lib = getRetrofit_lib().create(ServiceAPI_lib.class);
        Call<Message_RoomDetail> call = serviceAPI_lib.GetRoom(roomId);
        call.enqueue(new Callback<Message_RoomDetail>() {
            @Override
            public void onResponse(Call<Message_RoomDetail> call, Response<Message_RoomDetail> response) {
                if(response.body().getStatus1() == 1 && response.body().getPlayers1() != null){
                    if(requestCode == 5){
                        SetupSocket.createRoom(Tk, response.body().getRoomInfo1().getId(), response.body().getRoomInfo1().getNumber());
                    }

                    List<PlayerObject1> playerList = response.body().getPlayers1();
                    PlayerObject1 player =playerList.get(takePosition(playerList, Tk));

                    Intent intent = new Intent(getApplication(), WaitActivity.class);
                    intent.putExtra("code", code);
                    intent.putExtra("playerList", (Serializable) response.body().getPlayers1());
                    intent.putExtra("room", response.body().getRoomInfo1());
                    intent.putExtra("mine", player);
                    intent.putExtra("Tk", Tk);
                    startActivity(intent);
                }
            }

            @Override
            public void onFailure(Call<Message_RoomDetail> call, Throwable t) {

            }
        });
    }

    private int takePosition(List<PlayerObject1> playerList, String Tk){
        for (int i = 0; i < playerList.size(); i ++) {
            if(playerList.get(i).getTk().equals(Tk) ){
                return i;
            }
        }
        return 0;
    }

    private int takePositionRoom(String roomId){
        for (int i = 0; i < roomList.size(); i ++) {
            if(roomList.get(i).getId().equals(roomId) ){
                return i;
            }
        }
        return 0;
    }

    private void socket(){
        SetupSocket.mSocket.on("S_joinroom1", joinRoom);
        SetupSocket.mSocket.on("S_exitroom1", exitRoom);
        SetupSocket.mSocket.on("S_createroom", createRoom);
        SetupSocket.mSocket.on("S_settingroom1", settingRoom);
        SetupSocket.mSocket.on("S_startRoom1", start);
    }

    private final Emitter.Listener joinRoom = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    JSONObject data = (JSONObject) args[0];
                    String roomId = data.optString("roomId");
                    int quantity = data.optInt("quantity");

                    int position = takePositionRoom(roomId);

                    if(roomList.size() > 0) {
                        roomList.get(position).setSl(quantity);
                        mainAdapter.notifyItemChanged(position);
                    }
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
                    String roomId = data.optString("roomId");
                    int quantity = data.optInt("quantity");

                    int position = takePositionRoom(roomId);

                    if(roomList.size() > 0) {
                        roomList.get(position).setSl(quantity);
                        mainAdapter.notifyItemChanged(position);

                        if(quantity == 0){
                            roomList.remove(position);
                            mainAdapter.notifyItemRemoved(position);
                        }
                    }
                }
            });
        }
    };

    private final Emitter.Listener createRoom = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    JSONObject data = (JSONObject) args[0];
                    String roomId = data.optString("roomId");
                    String roomName = data.optString("roomName");

                    roomList.add(new RoomObject(roomId, roomName, "", 1));
                    mainAdapter.notifyItemInserted(roomList.size()-1);
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
                    String roomId = data.optString("roomId");
                    String pass = data.optString("pass");

                    int position = takePositionRoom(roomId);

                    if(roomList.size() > 0){
                        roomList.get(position).setPass(pass);
                        mainAdapter.notifyItemChanged(position);
                    }
                }
            });
        }
    };

    private final Emitter.Listener start = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    JSONObject data = (JSONObject) args[0];
                    String roomId = data.optString("roomId");

                    int position = takePositionRoom(roomId);

                    if(roomList.size() > 0){
                        roomList.remove(position);
                        mainAdapter.notifyItemRemoved(position);
                    }
                }
            });
        }
    };
}