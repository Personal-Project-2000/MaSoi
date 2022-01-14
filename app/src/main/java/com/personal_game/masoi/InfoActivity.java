package com.personal_game.masoi;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.personal_game.masoi.databinding.ActivityInfoBinding;
import com.personal_game.masoi.databinding.ActivityMainBinding;
import com.personal_game.masoi.databinding.LayoutChangpassBinding;
import com.personal_game.masoi.dialog.PassDialog;

public class InfoActivity extends AppCompatActivity {

    private ActivityInfoBinding activityInfoBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityInfoBinding = ActivityInfoBinding.inflate(getLayoutInflater());
        View view = activityInfoBinding.getRoot();
        setContentView(view);

        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        init();
    }

    private void init(){
        setListeners();
    }

    private void setListeners(){
        activityInfoBinding.btnBack.setOnClickListener(v -> {
            Intent intent = new Intent(getApplication(), MainActivity.class);
            startActivity(intent);
        });

        activityInfoBinding.btnChangePass.setOnClickListener(v -> {
            LayoutChangpassBinding layoutChangpassBinding;
            layoutChangpassBinding = LayoutChangpassBinding.inflate(getLayoutInflater());

            PassDialog dialog = new PassDialog(this);

            dialog.show();
            dialog.getWindow().setLayout(700, 550);
        });
    }
}