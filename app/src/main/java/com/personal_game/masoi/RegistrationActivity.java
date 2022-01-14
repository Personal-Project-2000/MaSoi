package com.personal_game.masoi;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.personal_game.masoi.databinding.ActivityRegistrationBinding;
import com.personal_game.masoi.databinding.ActivitySignInBinding;

public class RegistrationActivity extends AppCompatActivity {

    private ActivityRegistrationBinding activityRegistrationBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityRegistrationBinding = ActivityRegistrationBinding.inflate(getLayoutInflater());
        View view = activityRegistrationBinding.getRoot();
        setContentView(view);

        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        init();
    }

    private void init(){
        setListeners();
    }

    private void setListeners(){
        activityRegistrationBinding.btnSignIn.setOnClickListener(v ->{
            Intent intent = new Intent(getApplication(), SignInActivity.class);
            startActivity(intent);
        });

        activityRegistrationBinding.btnRegistration.setOnClickListener(v -> {

        });
    }
}