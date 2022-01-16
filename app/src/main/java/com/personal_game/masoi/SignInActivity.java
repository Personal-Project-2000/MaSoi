package com.personal_game.masoi;

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

import com.google.android.material.navigation.NavigationView;
import com.personal_game.masoi.adapter.MainAdapter;
import com.personal_game.masoi.databinding.ActivitySignInBinding;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

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
        Picasso.Builder builder = new Picasso.Builder(getApplicationContext());
        builder.listener(new Picasso.Listener() {
            @Override
            public void onImageLoadFailed(Picasso picasso, Uri uri, Exception exception) {
                activitySignInBinding.imgLogo.setImageResource(R.drawable.logo);
            }
        });
        Picasso pic = builder.build();
        pic.load("https://ps.covid21tsp.space/Picture/hopot.png").into(activitySignInBinding.imgLogo);

        setListeners();
    }

    private void setListeners(){
        activitySignInBinding.btnSignIn.setOnClickListener(v ->{
            Intent intent = new Intent(SignInActivity.this, MainActivity.class);
            startActivity(intent);
        });

        activitySignInBinding.btnRegistration.setOnClickListener(v -> {
            Intent intent = new Intent(SignInActivity.this, RegistrationActivity.class);
            startActivity(intent);
        });
    }
}