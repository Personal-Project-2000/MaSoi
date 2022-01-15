package com.personal_game.masoi.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;

import com.personal_game.masoi.MainActivity;
import com.personal_game.masoi.adapter.KickAdapter;
import com.personal_game.masoi.databinding.LayoutExitBinding;
import com.personal_game.masoi.databinding.LayoutKickBinding;

import java.util.ArrayList;
import java.util.List;

public class ExitDialog extends Dialog{
    public Activity c;
    public Button save;
    private LayoutExitBinding layoutExitBinding;
    private final ExitListeners exitListeners;

    public ExitDialog(Activity a, ExitListeners exitListeners) {
        super(a);
        this.c = a;
        this.exitListeners = exitListeners;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        layoutExitBinding = LayoutExitBinding.inflate(getLayoutInflater());
        View view = layoutExitBinding.getRoot();
        setContentView(view);

        init();
    }

    private void init(){
        setListeners();
    }

    private void setListeners(){
        layoutExitBinding.btnNo.setOnClickListener(v -> {
            dismiss();
        });
        layoutExitBinding.btnYes.setOnClickListener(v -> {
            exitListeners.onClickYes();
        });
    }

    public interface ExitListeners {
        void onClickYes();
    }
}
