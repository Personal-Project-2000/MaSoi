package com.personal_game.masoi;

import static com.personal_game.masoi.api.RetrofitServer.getRetrofit_lib;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.personal_game.masoi.adapter.HistoryAdapter;
import com.personal_game.masoi.adapter.PLayerWithPlayAdapter;
import com.personal_game.masoi.adapter.PlayerAdapter1;
import com.personal_game.masoi.api.ServiceAPI_lib;
import com.personal_game.masoi.databinding.ActivityHistoryBinding;
import com.personal_game.masoi.databinding.ActivityPlayBinding;
import com.personal_game.masoi.dialog.AdvocateDialog;
import com.personal_game.masoi.dialog.ConfirmDialog;
import com.personal_game.masoi.dialog.KickDialog;
import com.personal_game.masoi.dialog.ResultDialog;
import com.personal_game.masoi.dialog.StoryDialog;
import com.personal_game.masoi.dialog.VoteDialog;
import com.personal_game.masoi.object.Message;
import com.personal_game.masoi.object.PlayerObject1;
import com.personal_game.masoi.object.RoomObject;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PlayActivity extends AppCompatActivity {

    private ActivityPlayBinding activityPlayBinding;
    private PLayerWithPlayAdapter pLayerWithPlayAdapter;
    private RoomObject room;
    private List<PlayerObject1> playerList;
    private String Tk;

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
        Intent intent = getIntent();
        room = (RoomObject) intent.getSerializableExtra("room");
        playerList = (List<PlayerObject1>) intent.getSerializableExtra("playerList");
        Tk = intent.getStringExtra("Tk");

        VoteDialog dialog = new VoteDialog(PlayActivity.this, 1, "Bạn muốn chọn A làm tộc trưởng?", playerList);

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
                    exitRoom();
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
        pLayerWithPlayAdapter = new PLayerWithPlayAdapter(playerList, getApplication());

        activityPlayBinding.rclPlayer.setAdapter(pLayerWithPlayAdapter);
    }

    private void exitRoom(){
        ServiceAPI_lib serviceAPI_lib = getRetrofit_lib().create(ServiceAPI_lib.class);
        Call<Message> call = serviceAPI_lib.ExitRoom(room.getId(), Tk);
        call.enqueue(new Callback<Message>() {
            @Override
            public void onResponse(Call<Message> call, Response<Message> response) {
                if(response.body().getStatus1() == 1){
                    Intent intent = new Intent(PlayActivity.this, MainActivity.class);
                    intent.putExtra("Tk", Tk);
                    startActivity(intent);
                }
            }

            @Override
            public void onFailure(Call<Message> call, Throwable t) {
                Toast.makeText(getApplication(), "Đuổi thất bại", Toast.LENGTH_SHORT).show();
            }
        });
    }
}