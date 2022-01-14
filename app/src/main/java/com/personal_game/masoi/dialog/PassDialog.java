package com.personal_game.masoi.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.personal_game.masoi.R;

public class PassDialog extends Dialog implements android.view.View.OnClickListener{
    public Activity c;
    public Dialog d;
    public Button exit, change;

    public PassDialog(Activity a) {
        super(a);
        this.c = a;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.layout_changpass);

        exit = (Button) findViewById(R.id.btnExit);
        change = (Button) findViewById(R.id.btnChangePass);
        exit.setOnClickListener(this);
        change.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnExit:
                dismiss();
                break;
            case R.id.btnChangePass:

                break;
            default:
                break;
        }
        dismiss();
    }
}
