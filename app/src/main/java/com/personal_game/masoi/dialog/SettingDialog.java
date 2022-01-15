package com.personal_game.masoi.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;

import com.personal_game.masoi.Object.SpinnerObject;
import com.personal_game.masoi.R;
import com.personal_game.masoi.adapter.SpinnerAdapter;
import com.personal_game.masoi.databinding.ActivitySettingBinding;
import com.personal_game.masoi.databinding.LayoutSettingBinding;

import java.util.ArrayList;

public class SettingDialog extends Dialog implements android.view.View.OnClickListener, AdapterView.OnItemSelectedListener{
    public Activity c;
    public Button save;
    private LayoutSettingBinding layoutSettingBinding;
    private int code; //1: createRoom || 2: joinRoom

    public SettingDialog(Activity a, int code) {
        super(a);
        this.c = a;
        this.code = code;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        layoutSettingBinding = LayoutSettingBinding.inflate(getLayoutInflater());
        View view = layoutSettingBinding.getRoot();
        setContentView(view);

        save = (Button) findViewById(R.id.btnSave);
        save.setOnClickListener(this);

        init();
    }

    private void init(){
        if(code == 2){
            layoutSettingBinding.inputPass.setVisibility(View.INVISIBLE);
            layoutSettingBinding.spinnerAdvocateTime.setVisibility(View.INVISIBLE);
            layoutSettingBinding.spinnerVoteTime.setVisibility(View.INVISIBLE);
            layoutSettingBinding.btnSave.setVisibility(View.INVISIBLE);

            layoutSettingBinding.txtPass.setVisibility(View.VISIBLE);
            layoutSettingBinding.txtAdvocateTime.setVisibility(View.VISIBLE);
            layoutSettingBinding.txtVoteTime.setVisibility(View.VISIBLE);
        }else{
            layoutSettingBinding.txtPass.setVisibility(View.INVISIBLE);
            layoutSettingBinding.txtAdvocateTime.setVisibility(View.INVISIBLE);
            layoutSettingBinding.txtVoteTime.setVisibility(View.INVISIBLE);

            setVoteTime();;
            setAdvocateTime();
        }
    }

    private void setVoteTime(){
        SpinnerAdapter spinnerAdapter = new SpinnerAdapter(c, getSpinnerList());
        if(layoutSettingBinding.spinnerVoteTime != null) {
            layoutSettingBinding.spinnerVoteTime.setAdapter(spinnerAdapter);
            layoutSettingBinding.spinnerVoteTime.setOnItemSelectedListener(this);
        }
    }

    private void setAdvocateTime(){
        SpinnerAdapter spinnerAdapter = new SpinnerAdapter(c, getSpinnerList());
        if(layoutSettingBinding.spinnerAdvocateTime != null) {
            layoutSettingBinding.spinnerAdvocateTime.setAdapter(spinnerAdapter);
            layoutSettingBinding.spinnerAdvocateTime.setOnItemSelectedListener(this);
        }
    }

    private ArrayList<SpinnerObject> getSpinnerList(){
        ArrayList<SpinnerObject> list = new ArrayList<>();
        list.add(new SpinnerObject("5p"));
        list.add(new SpinnerObject("10p"));
        list.add(new SpinnerObject("15p"));

        return list;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnSave:
                dismiss();
                break;
            default:
                break;
        }
        dismiss();
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
