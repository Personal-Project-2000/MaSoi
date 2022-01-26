package com.personal_game.masoi.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.view.Window;

import com.personal_game.masoi.adapter.PlayerAdapter;
import com.personal_game.masoi.databinding.LayoutAdvocateBinding;
import com.personal_game.masoi.databinding.LayoutVoteBinding;
import com.personal_game.masoi.object.PlayerObject;

import java.util.ArrayList;
import java.util.List;

public class AdvocateDialog extends Dialog {
    public Activity c;
    private LayoutAdvocateBinding layoutAdvocateBinding;
    private PlayerAdapter playerAdapter;
    private AdvocateListeners advocateListeners;

    public AdvocateDialog(Activity a, AdvocateListeners advocateListeners) {
        super(a);
        this.c = a;
        this.advocateListeners = advocateListeners;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        layoutAdvocateBinding = LayoutAdvocateBinding.inflate(getLayoutInflater());
        View view = layoutAdvocateBinding.getRoot();
        setContentView(view);

        init();
    }

    private void init(){
        setPlayer();
        setListeners();
    }

    private void setPlayer(){
        List<PlayerObject> playerList = new ArrayList<>();

        playerAdapter = new PlayerAdapter(playerList, c, 1, new PlayerAdapter.PlayerListeners() {
            @Override
            public void onClick() {

            }
        });
        layoutAdvocateBinding.rclPlayer.setAdapter(playerAdapter);
    }

    private void setListeners(){
        layoutAdvocateBinding.imgYes.setOnClickListener(v -> {
            advocateListeners.onClickYes();
            dismiss();
        });
        layoutAdvocateBinding.imgNo.setOnClickListener(v -> {
            advocateListeners.onClickNo();
            dismiss();
        });
    }

    public interface AdvocateListeners{
        void onClickYes();
        void onClickNo();
    }
}
