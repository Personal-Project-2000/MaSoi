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
import com.personal_game.masoi.databinding.LayoutConfirmBinding;
import com.personal_game.masoi.databinding.LayoutKickBinding;

import java.util.ArrayList;
import java.util.List;

public class ConfirmDialog extends Dialog{
    public Activity c;
    public String title;
    private LayoutConfirmBinding layoutConfirmBinding;
    private final ExitListeners exitListeners;

    public ConfirmDialog(Activity a, ExitListeners exitListeners, String title) {
        super(a);
        this.c = a;
        this.exitListeners = exitListeners;
        this.title = title;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        layoutConfirmBinding = LayoutConfirmBinding.inflate(getLayoutInflater());
        View view = layoutConfirmBinding.getRoot();
        setContentView(view);

        init();
    }

    private void init(){
        layoutConfirmBinding.txt1.setText(title);

        setListeners();
    }

    private void setListeners(){
        layoutConfirmBinding.btnNo.setOnClickListener(v -> {
            dismiss();
        });
        layoutConfirmBinding.btnYes.setOnClickListener(v -> {
            exitListeners.onClickYes();
            dismiss();
        });
    }

    public interface ExitListeners {
        void onClickYes();
    }
}
