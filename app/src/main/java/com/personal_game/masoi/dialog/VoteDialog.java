package com.personal_game.masoi.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.view.View;
import android.view.Window;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;

import com.personal_game.masoi.adapter.PlayerAdapter;
import com.personal_game.masoi.adapter.PlayerAdapter1;
import com.personal_game.masoi.databinding.LayoutVoteBinding;
import com.personal_game.masoi.object.PlayerObject;
import com.personal_game.masoi.object.PlayerObject1;
import com.personal_game.masoi.socket.SetupSocket;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class VoteDialog extends Dialog{
    public Activity c;
    private int code; //1: Bầu cử tộc trưởng || 2: Chỉ định sói || 3: bài phù thủy
    private LayoutVoteBinding layoutVoteBinding;
    private PlayerAdapter1 playerAdapter;
    private String title;
    private List<PlayerObject1> playerList;
    private VoteListeners voteListeners;

    public VoteDialog(Activity a, int code, String title, List<PlayerObject1> playerList, VoteListeners voteListeners) {
        super(a);
        this.c = a;
        this.code = code;
        this.title = title;
        this.playerList = playerList;
        this.voteListeners = voteListeners;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        layoutVoteBinding = LayoutVoteBinding.inflate(getLayoutInflater());
        View view = layoutVoteBinding.getRoot();
        setContentView(view);

        init();
    }

    private void init(){
        if(code == 1) {
            layoutVoteBinding.txtName.setText("Bầu cử tộc trưởng");
        }else{
            layoutVoteBinding.txtName.setText("Vote con sói");
        }

        setPlayer();
    }

    private void setPlayer(){
        playerAdapter = new PlayerAdapter1(playerList, c, 1, new PlayerAdapter1.PlayerListeners() {
            @Override
            public void onClick(PlayerObject1 player) {
                if(code == 2) {
                    ConfirmDialog dialog = new ConfirmDialog(c, new ConfirmDialog.ExitListeners() {
                        @Override
                        public void onClickYes() {
                            voteListeners.onClickYes(player);
                            dismiss();
                        }
                    }, "Bạn nghi " + player.getName() + " là sói phải không?");

                    dialog.show();
                    dialog.getWindow().setLayout(600, 300);
                }
            }
        });
        layoutVoteBinding.rclPlayer.setAdapter(playerAdapter);
    }

    public interface VoteListeners{
        void onClickYes(PlayerObject1 player);
    }
}
