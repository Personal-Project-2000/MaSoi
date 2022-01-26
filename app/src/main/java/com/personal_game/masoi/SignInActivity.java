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
import com.personal_game.masoi.databinding.ActivitySignInBinding;
import com.personal_game.masoi.object.Message;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SignInActivity extends AppCompatActivity {

    private ActivitySignInBinding activitySignInBinding;

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
        //kiểm tra ảnh có tồn tại không
//        Picasso.Builder builder = new Picasso.Builder(getApplicationContext());
//        builder.listener(new Picasso.Listener() {
//            @Override
//            public void onImageLoadFailed(Picasso picasso, Uri uri, Exception exception) {
//                activitySignInBinding.imgLogo.setImageResource(R.drawable.logo);
//            }
//        });
//        Picasso pic = builder.build();
//        pic.load("https://ps.covid21tsp.space/Picture/hopot.png").into(activitySignInBinding.imgLogo);

        setListeners();
    }

    private void setListeners(){
        activitySignInBinding.btnSignIn.setOnClickListener(v ->{
            SignIn(activitySignInBinding.inputTK.getText()+"", activitySignInBinding.inputPass.getText()+"");
        });

        activitySignInBinding.btnRegistration.setOnClickListener(v -> {
            Intent intent = new Intent(SignInActivity.this, RegistrationActivity.class);
            startActivity(intent);
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
                loading(false);
                Toast.makeText(getApplication(), response.body().getNotification1(), Toast.LENGTH_SHORT).show();

                if(response.body().getStatus1() == 1){
                    Intent intent = new Intent(SignInActivity.this, MainActivity.class);
                    intent.putExtra("Tk", Tk);
                    startActivity(intent);
                }
            }

            @Override
            public void onFailure(Call<Message> call, Throwable t) {
                loading(false);
                Toast.makeText(getApplication(), "Đăng nhập thất bại", Toast.LENGTH_SHORT).show();
            }
        });
    }
}