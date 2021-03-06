package com.personal_game.masoi;

import static com.personal_game.masoi.api.RetrofitServer.getRetrofit_lib;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;
import com.personal_game.masoi.adapter.MainAdapter;
import com.personal_game.masoi.api.ServiceAPI_lib;
import com.personal_game.masoi.backup.Shared_Preferences;
import com.personal_game.masoi.databinding.ActivitySignInBinding;
import com.personal_game.masoi.object.Message;
import com.personal_game.masoi.object.Message_Adress;
import com.personal_game.masoi.object.Message_BaiInfo;
import com.personal_game.masoi.object.Message_RoomDetail;
import com.personal_game.masoi.object.PlayerObject1;
import com.personal_game.masoi.object.RoomObject;
import com.personal_game.masoi.socket.SetupSocket;
import com.personal_game.masoi.socket.model.Room;
import com.squareup.picasso.Picasso;

import org.json.JSONObject;

import java.io.Serializable;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SignInActivity extends AppCompatActivity {

    private ActivitySignInBinding activitySignInBinding;

    private Socket mSocket;
    {
        try {
            mSocket = IO.socket(SetupSocket.uriLocal);
        } catch (URISyntaxException e) {
            e.getMessage();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activitySignInBinding = ActivitySignInBinding.inflate(getLayoutInflater());
        View view = activitySignInBinding.getRoot();
        setContentView(view);

        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        init();
    }

    private void init(){
        //ki???m tra ???nh c?? t???n t???i kh??ng
//        Picasso.Builder builder = new Picasso.Builder(getApplicationContext());
//        builder.listener(new Picasso.Listener() {
//            @Override
//            public void onImageLoadFailed(Picasso picasso, Uri uri, Exception exception) {
//                activitySignInBinding.imgLogo.setImageResource(R.drawable.logo);
//            }
//        });
//        Picasso pic = builder.build();
//        pic.load("https://ps.covid21tsp.space/Picture/hopot.png").into(activitySignInBinding.imgLogo);

        SetupSocket.mSocket = mSocket;
        //SetupSocket.mSocket.connect();

        Shared_Preferences shared_preferences = new Shared_Preferences(getApplication());
        if(!shared_preferences.getTk().equals("")){
            activitySignInBinding.checkSignIn.setChecked(true);
            activitySignInBinding.inputTK.setText(shared_preferences.getTk());
            activitySignInBinding.inputPass.setText(shared_preferences.getPass());
        }

        setListeners();
        socket();
    }

    private void setListeners(){
        activitySignInBinding.btnSignIn.setOnClickListener(v ->{
            Shared_Preferences shared_preferences = new Shared_Preferences(getApplication());
            if(activitySignInBinding.checkSignIn.isChecked()){
                shared_preferences.saveUser(activitySignInBinding.inputTK.getText()+"", activitySignInBinding.inputPass.getText()+"");
            }else{
                shared_preferences.saveUser("", "");
            }
            SignIn(activitySignInBinding.inputTK.getText()+"", activitySignInBinding.inputPass.getText()+"");
        });

        activitySignInBinding.btnRegistration.setOnClickListener(v -> {
            Intent intent = new Intent(SignInActivity.this, RegistrationActivity.class);
            startActivity(intent);
        });

        activitySignInBinding.checkSignIn.setOnClickListener(v -> {
            Shared_Preferences shared_preferences = new Shared_Preferences(getApplication());
            if(activitySignInBinding.checkSignIn.isChecked()){
                shared_preferences.saveUser(activitySignInBinding.inputTK.getText()+"", activitySignInBinding.inputPass.getText()+"");
            }else{
                shared_preferences.saveUser("", "");
            }
        });

        activitySignInBinding.btnFB.setOnClickListener(v -> {
            Toast.makeText(getApplication(), "Ch???c n??ng n??y ??ang b???o tr??", Toast.LENGTH_SHORT).show();
        });

        activitySignInBinding.btnGG.setOnClickListener(v -> {
            Toast.makeText(getApplication(), "Ch???c n??ng n??y ??ang b???o tr??", Toast.LENGTH_SHORT).show();
        });
    }

    private void loading(boolean Loading) {
        if (Loading) {
            activitySignInBinding.progressBar.setVisibility(View.VISIBLE);
            activitySignInBinding.btnSignIn.setVisibility(View.GONE);
        } else {
            activitySignInBinding.progressBar.setVisibility(View.GONE);
            activitySignInBinding.btnSignIn.setVisibility(View.VISIBLE);
        }
    }

    private void SignIn(String Tk, String Pass){
        loading(true);
        ServiceAPI_lib serviceAPI_lib = getRetrofit_lib().create(ServiceAPI_lib.class);
        Call<Message> signIn = serviceAPI_lib.SignIn(Tk, Pass);
        signIn.enqueue(new Callback<Message>() {
            @Override
            public void onResponse(Call<Message> call, Response<Message> response) {
                Toast.makeText(getApplication(), response.body().getNotification1(), Toast.LENGTH_SHORT).show();

                if(response.body().getStatus1() == 1){
                    SetupSocket.signIn(Tk, response.body().getId1());
                }
            }

            @Override
            public void onFailure(Call<Message> call, Throwable t) {
                Toast.makeText(getApplication(), "????ng nh???p th???t b???i", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void CheckRoom(String Tk){
        ServiceAPI_lib serviceAPI_lib = getRetrofit_lib().create(ServiceAPI_lib.class);
        Call<Message_RoomDetail> call = serviceAPI_lib.CheckRoom(Tk);
        call.enqueue(new Callback<Message_RoomDetail>() {
            @Override
            public void onResponse(Call<Message_RoomDetail> call, Response<Message_RoomDetail> response) {
                loading(false);
                //n???u tr???ng tr??i b???ng 1 l?? do t??i kho???n ??ang ch??i m?? l???i b??? out ph??ng n??n ????ng nh???p s??? ??c v?? l???i ph??ng ????
                if(response.body().getStatus1() == 1){
                    List<PlayerObject1> playerList = response.body().getPlayers1();
                    //ki???m tra tr???ng th??i ph??ng ??ang ph??ng ch??? hay ???? ch??i => true c?? ngh??a ??ang ch???
                    if(response.body().getRoomInfo1().isStatus()){

                        int code;
                        if(playerList.get(takePosition(playerList, Tk)).isBoss()){
                            code = 1; //create room
                        }else{
                            code = 2; // join room
                        }

                        Intent intent = new Intent(getApplication(), WaitActivity.class);
                        intent.putExtra("code", code);
                        intent.putExtra("playerList", (Serializable) playerList);
                        intent.putExtra("room", response.body().getRoomInfo1());
                        intent.putExtra("mine", playerList.get(takePosition(playerList, Tk)));
                        intent.putExtra("Tk", Tk);
                        startActivity(intent);
                    }else{
                        int posotion = takePosition(playerList, Tk);
                        String baiId = playerList.get(posotion).getBaiId();

                        GetBai(baiId, playerList, response.body().getRoomInfo1(), Tk, playerList.get(posotion));
                    }
                }else{
                    Intent intent = new Intent(SignInActivity.this, MainActivity.class);
                    intent.putExtra("Tk", Tk);
                    startActivity(intent);
                }
            }

            @Override
            public void onFailure(Call<Message_RoomDetail> call, Throwable t) {
                loading(false);
            }
        });
    }

    private void GetBai(String baiId, List<PlayerObject1> playerList, RoomObject room, String Tk, PlayerObject1 mine){
        loading(true);
        ServiceAPI_lib serviceAPI_lib = getRetrofit_lib().create(ServiceAPI_lib.class);
        Call<Message_BaiInfo> call = serviceAPI_lib.BaiInfo(baiId, "", "", "");
        call.enqueue(new Callback<Message_BaiInfo>() {
            @Override
            public void onResponse(Call<Message_BaiInfo> call, Response<Message_BaiInfo> response) {
                if(response.body().getStatus1() == 1 && response.body().getBaiInfo1() != null){
                    Intent intent = new Intent(SignInActivity.this, PlayActivity.class);
                    intent.putExtra("playerList", (Serializable) playerList);
                    intent.putExtra("room", room);
                    intent.putExtra("bai", response.body().getBaiInfo1());
                    intent.putExtra("mine", mine);
                    intent.putExtra("Tk", Tk);
                    startActivity(intent);
                }else{
                    Toast.makeText(getApplication(), "H??y th??? ????ng nh???p l???i", Toast.LENGTH_SHORT).show();
                }
                loading(false);
            }

            @Override
            public void onFailure(Call<Message_BaiInfo> call, Throwable t) {
                Toast.makeText(getApplication(), "H??y th??? ????ng nh???p l???i", Toast.LENGTH_SHORT).show();
                loading(false);
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

    private void socket(){
        SetupSocket.mSocket.on("noti_login", checkLoginRealTime);
    }

    private final Emitter.Listener checkLoginRealTime = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    JSONObject data = (JSONObject) args[0];
                    int message = data.optInt("message");

                    if(message == 1){
                        CheckRoom(activitySignInBinding.inputTK.getText()+"");
                    }
                }
            });
        }
    };
}