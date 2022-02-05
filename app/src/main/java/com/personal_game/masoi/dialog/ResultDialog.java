package com.personal_game.masoi.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.view.Window;

import androidx.recyclerview.widget.GridLayoutManager;

import com.personal_game.masoi.adapter.PlayerAdapter;
import com.personal_game.masoi.adapter.PlayerWinAdapter;
import com.personal_game.masoi.databinding.LayoutResultBinding;
import com.personal_game.masoi.databinding.LayoutVoteBinding;

import java.util.ArrayList;
import java.util.List;

public class ResultDialog extends Dialog {
    public Activity c;
    private LayoutResultBinding layoutResultBinding;
    private PlayerWinAdapter playerWinAdapter;
    private final PlayerWinListeners playerWinListeners;
    private String result;

    public ResultDialog(Activity a, PlayerWinListeners playerWinListeners, String result) {
        super(a);
        this.c = a;
        this.playerWinListeners = playerWinListeners;
        this.result = result;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        layoutResultBinding = LayoutResultBinding.inflate(getLayoutInflater());
        View view = layoutResultBinding.getRoot();
        setContentView(view);

        init();
    }

    private void init(){
        layoutResultBinding.txtResult.setText(result);

        setPlayer();
        setListeners();
    }

    private void setPlayer(){
        List<String> test = new ArrayList<>();

        for(int i = 0; i < 20; i++){
            test.add("t");
        }

        playerWinAdapter = new PlayerWinAdapter(test, c);

        GridLayoutManager gridLayoutManager = new GridLayoutManager(c, 4);
        gridLayoutManager.setOrientation(GridLayoutManager.VERTICAL);

        layoutResultBinding.rclPlayer.setLayoutManager(gridLayoutManager);
        layoutResultBinding.rclPlayer.setAdapter(playerWinAdapter);
    }

    private void setListeners(){
        layoutResultBinding.btnContinue.setOnClickListener(v -> {
            playerWinListeners.onClickContinue();
            dismiss();
        });
    }

    public interface PlayerWinListeners{
        void onClickContinue();
    }
}
