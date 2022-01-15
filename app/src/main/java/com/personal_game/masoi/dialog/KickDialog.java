package com.personal_game.masoi.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;

import com.personal_game.masoi.Object.SpinnerObject;
import com.personal_game.masoi.R;
import com.personal_game.masoi.adapter.KickAdapter;
import com.personal_game.masoi.adapter.PlayerAdapter;
import com.personal_game.masoi.adapter.SpinnerAdapter;
import com.personal_game.masoi.databinding.LayoutKickBinding;
import com.personal_game.masoi.databinding.LayoutSettingBinding;

import java.util.ArrayList;
import java.util.List;

public class KickDialog extends Dialog implements android.view.View.OnClickListener{
    public Activity c;
    public Button save;
    private LayoutKickBinding layoutKickBinding;
    private KickAdapter kickAdapter;

    public KickDialog(Activity a) {
        super(a);
        this.c = a;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        layoutKickBinding = LayoutKickBinding.inflate(getLayoutInflater());
        View view = layoutKickBinding.getRoot();
        setContentView(view);

        init();
    }

    private void init(){
        setPlayer();
    }

    private void setPlayer(){
        List<String> test = new ArrayList<>();

        for(int i = 0; i < 20; i++){
            test.add("t");
        }

        kickAdapter = new KickAdapter(test, c, new KickAdapter.KickListeners() {
            @Override
            public void onClick(String str, int position) {

            }
        });
        layoutKickBinding.rclPlayer.setAdapter(kickAdapter);
    }

    @Override
    public void onClick(View v) {
//        switch (v.getId()) {
//            case R.id.btnSave:
//                dismiss();
//                break;
//            default:
//                break;
//        }
        dismiss();
    }
}
