package com.personal_game.masoi;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import com.google.android.material.navigation.NavigationView;
import com.personal_game.masoi.adapter.MainAdapter;
import com.personal_game.masoi.adapter.PlayerAdapter;
import com.personal_game.masoi.databinding.ActivityMainBinding;
import com.personal_game.masoi.databinding.ActivityWaitBinding;
import com.personal_game.masoi.dialog.ExitDialog;
import com.personal_game.masoi.dialog.KickDialog;
import com.personal_game.masoi.dialog.SettingDialog;

import java.util.ArrayList;
import java.util.List;

public class WaitActivity extends AppCompatActivity {

    private ActivityWaitBinding activityWaitBinding;
    private PlayerAdapter playerAdapter;

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
                        SettingDialog dialog = new SettingDialog(WaitActivity.this);

                        dialog.show();
                        dialog.getWindow().setLayout(700, 500);
                        break;
                    }
                    case R.id.nav_kick: {
                        KickDialog dialog = new KickDialog(WaitActivity.this);

                        dialog.show();
                        dialog.getWindow().setLayout(700, 1050);
                        break;
                    }
                    case R.id.nav_signout: {
                        ExitDialog dialog = new ExitDialog(WaitActivity.this, new ExitDialog.ExitListeners() {
                            @Override
                            public void onClickYes() {
                                Intent intent = new Intent(WaitActivity.this, MainActivity.class);
                                startActivity(intent);
                            }
                        });

                        dialog.show();
                        dialog.getWindow().setLayout(600, 250);
                        break;
                    }
                }
                return false;
            }
        });
    }

    private void setRoom(){
        List<String> test = new ArrayList<>();

        for(int i = 0; i < 20; i++){
            test.add("t");
        }

        playerAdapter = new PlayerAdapter(test, getApplication(), 2);
        activityWaitBinding.rclPlayer.setAdapter(playerAdapter);
    }
}