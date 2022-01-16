package com.personal_game.masoi;

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
import com.personal_game.masoi.databinding.ActivityWaitBinding;
import com.personal_game.masoi.dialog.ConfirmDialog;
import com.personal_game.masoi.dialog.KickDialog;
import com.personal_game.masoi.dialog.SettingDialog;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

public class WaitActivity extends AppCompatActivity {

    private ActivityWaitBinding activityWaitBinding;
    private PlayerAdapter playerAdapter;
    private int code; //1: createRoom || 2: joinRoom

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
                        SettingDialog dialog = new SettingDialog(WaitActivity.this, code);

                        dialog.show();
                        if(code == 1) {
                            dialog.getWindow().setLayout(700, 500);
                        }else{
                            dialog.getWindow().setLayout(450, 330);
                        }
                        break;
                    }
                    case R.id.nav_kick: {
                        if(code == 1) {
                            KickDialog dialog = new KickDialog(WaitActivity.this);

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
                                Intent intent = new Intent(WaitActivity.this, MainActivity.class);
                                startActivity(intent);
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
            Intent intent = new Intent(WaitActivity.this, PlayActivity.class);
            startActivity(intent);
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

    private void setRoom(){
        List<String> test = new ArrayList<>();

        for(int i = 0; i < 20; i++){
            test.add("t");
        }

        playerAdapter = new PlayerAdapter(test, getApplication(), 2, new PlayerAdapter.PlayerListeners() {
            @Override
            public void onClick() {

            }
        });
        activityWaitBinding.rclPlayer.setAdapter(playerAdapter);
    }
}