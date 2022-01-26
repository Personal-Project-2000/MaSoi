package com.personal_game.masoi.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;

import com.personal_game.masoi.R;
import com.personal_game.masoi.adapter.SpinnerAdapter;
import com.personal_game.masoi.databinding.LayoutChangpassBinding;
import com.personal_game.masoi.databinding.LayoutPassroomBinding;
import com.personal_game.masoi.databinding.LayoutSettingBinding;
import com.personal_game.masoi.object.SpinnerObject;

import java.util.ArrayList;

public class PassRoomDialog extends Dialog {
    public Activity c;
    private LayoutPassroomBinding layoutPassroomBinding;
    private JoinListeners joinListeners;

    public PassRoomDialog(Activity a, JoinListeners joinListeners) {
        super(a);
        this.c = a;
        this.joinListeners = joinListeners;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        layoutPassroomBinding = LayoutPassroomBinding.inflate(getLayoutInflater());
        View view = layoutPassroomBinding.getRoot();
        setContentView(view);

        init();
    }

    private void init(){
       setListeners();
    }

    private void setListeners(){
        layoutPassroomBinding.btnJoin.setOnClickListener(v -> {
            joinListeners.onClick(layoutPassroomBinding.txt1.getText()+"");
        });
    }

    public interface JoinListeners{
        void onClick(String pass);
    }
}
