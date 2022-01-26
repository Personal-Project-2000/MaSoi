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
import com.personal_game.masoi.api.ServiceAPI_lib;
import com.personal_game.masoi.databinding.ActivityMainBinding;
import com.personal_game.masoi.databinding.ActivitySignInBinding;
import com.personal_game.masoi.dialog.ConfirmDialog;
import com.personal_game.masoi.dialog.PassRoomDialog;
import com.personal_game.masoi.object.Message;
import com.personal_game.masoi.object.Message_Info;
import com.personal_game.masoi.object.Message_RoomList;
import com.personal_game.masoi.object.Request_JoinRoom;
import com.personal_game.masoi.object.RoomObject;
import com.personal_game.masoi.object.UserObject;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

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
                                    JoinRoom(roomObject.getId(), pass);
                                }
                            });

                            dialog.show();
                            dialog.getWindow().setLayout(650, 270);
                        }

                        @Override
                        public void onClickUnLock(RoomObject roomObject) {
                            JoinRoom(roomObject.getId(), "");
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
                    Intent intent = new Intent(getApplication(), WaitActivity.class);
                    intent.putExtra("code", 1);
                    intent.putExtra("roomId", response.body().getId1());
                    intent.putExtra("Tk", Tk);
                    startActivityForResult(intent, 5);
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

    private void JoinRoom(String roomId, String pass){
        ServiceAPI_lib serviceAPI_lib = getRetrofit_lib().create(ServiceAPI_lib.class);
        Call<Message> call = serviceAPI_lib.JoinRoom(new Request_JoinRoom(roomId, Tk, pass));
        call.enqueue(new Callback<Message>() {
            @Override
            public void onResponse(Call<Message> call, Response<Message> response) {
                if(response.body().getStatus1() == 1){
                    Intent intent = new Intent(getApplication(), WaitActivity.class);
                    intent.putExtra("code", 2);
                    intent.putExtra("roomId", roomId);
                    intent.putExtra("Tk", Tk);
                    startActivityForResult(intent, 6);
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
}