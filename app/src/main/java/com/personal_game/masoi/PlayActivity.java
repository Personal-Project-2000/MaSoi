package com.personal_game.masoi;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.personal_game.masoi.adapter.HistoryAdapter;
import com.personal_game.masoi.adapter.PLayerWithPlayAdapter;
import com.personal_game.masoi.databinding.ActivityHistoryBinding;
import com.personal_game.masoi.databinding.ActivityPlayBinding;
import com.personal_game.masoi.dialog.AdvocateDialog;
import com.personal_game.masoi.dialog.ConfirmDialog;
import com.personal_game.masoi.dialog.KickDialog;
import com.personal_game.masoi.dialog.ResultDialog;
import com.personal_game.masoi.dialog.StoryDialog;
import com.personal_game.masoi.dialog.VoteDialog;

import java.util.ArrayList;
import java.util.List;

public class PlayActivity extends AppCompatActivity {

    private ActivityPlayBinding activityPlayBinding;
    private PLayerWithPlayAdapter pLayerWithPlayAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityPlayBinding = ActivityPlayBinding.inflate(getLayoutInflater());
        View view = activityPlayBinding.getRoot();
        setContentView(view);

        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        init();
    }

    private void init(){
        VoteDialog dialog = new VoteDialog(PlayActivity.this, 1, "Bạn muốn chọn A làm tộc trưởng?");

        dialog.show();
        dialog.getWindow().setLayout(700, 1200);
        dialog.setCanceledOnTouchOutside(false); //Bấm ngoài hộp thoại không dismiss

        setListeners();
        setPlayer();
    }

    private void setListeners(){
        activityPlayBinding.btnBack.setOnClickListener(v -> {
            ConfirmDialog dialog = new ConfirmDialog(PlayActivity.this, new ConfirmDialog.ExitListeners() {
                @Override
                public void onClickYes() {
                    Intent intent = new Intent(PlayActivity.this, MainActivity.class);
                    startActivity(intent);
                }
            }, "Bạn muốn rời khỏi phòng?");

            dialog.show();
            dialog.getWindow().setLayout(600, 250);
        });

        activityPlayBinding.imgGia.setOnClickListener(v -> {
            AdvocateDialog dialog = new AdvocateDialog(this, new AdvocateDialog.AdvocateListeners() {
                @Override
                public void onClickYes() {

                }

                @Override
                public void onClickNo() {

                }
            });

            dialog.show();
            dialog.getWindow().setLayout(700, 1200);
            dialog.setCanceledOnTouchOutside(false); //Bấm ngoài hộp thoại không dismiss
        });

        activityPlayBinding.imgLove.setOnClickListener(v -> {

            ResultDialog dialog = new ResultDialog(this, new ResultDialog.PlayerWinListeners() {
                @Override
                public void onClickContinue() {
                    StoryDialog dialog1 = new StoryDialog(PlayActivity.this, new StoryDialog.StoryListeners() {
                        @Override
                        public void onClickContinue() {
                            Intent intent = new Intent(PlayActivity.this, WaitActivity.class);
                            startActivity(intent);
                        }
                    });

                    dialog1.show();
                    dialog1.getWindow().setLayout(700, 1150);
                    dialog1.setCanceledOnTouchOutside(false); //Bấm ngoài hộp thoại không dismiss
                }
            });

            dialog.show();
            dialog.getWindow().setLayout(650, 1100);
            dialog.setCanceledOnTouchOutside(false); //Bấm ngoài hộp thoại không dismiss
        });
    }

    private void setPlayer(){
        List<String> test = new ArrayList<>();

        for(int i = 0; i < 20; i++){
            test.add("t");
        }

        pLayerWithPlayAdapter = new PLayerWithPlayAdapter(test, getApplication());

        activityPlayBinding.rclPlayer.setAdapter(pLayerWithPlayAdapter);
    }
}