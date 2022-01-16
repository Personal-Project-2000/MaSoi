package com.personal_game.masoi;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.personal_game.masoi.Object.SpinnerObject;
import com.personal_game.masoi.adapter.SpinnerAdapter;
import com.personal_game.masoi.databinding.ActivityHistoryBinding;
import com.personal_game.masoi.databinding.ActivitySettingBinding;

import java.util.ArrayList;

public class SettingActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener{

    private ActivitySettingBinding activitySettingBinding;
    private boolean checkBack = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activitySettingBinding = ActivitySettingBinding.inflate(getLayoutInflater());
        View view = activitySettingBinding.getRoot();
        setContentView(view);

        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        init();
    }

    private void init(){
        setListeners();
        setSpinner();
    }

    private void setListeners(){
        activitySettingBinding.btnBack.setOnClickListener(v -> {
            Intent intent = new Intent(getApplication(), MainActivity.class);
            startActivity(intent);
        });

        activitySettingBinding.checkBack.setOnClickListener(v -> {
            if(checkBack) {
                checkBack = false;
                activitySettingBinding.checkBack.setImageResource(R.drawable.off);
            }else{
                checkBack = true;
                activitySettingBinding.checkBack.setImageResource(R.drawable.on);
            }
        });
    }

    private void setSpinner(){
        SpinnerAdapter spinnerAdapter = new SpinnerAdapter(this, getSpinnerList());
        if(activitySettingBinding.spinner != null) {
            activitySettingBinding.spinner.setAdapter(spinnerAdapter);
            activitySettingBinding.spinner.setOnItemSelectedListener(this);
        }
    }

    private ArrayList<SpinnerObject> getSpinnerList(){
        ArrayList<SpinnerObject> list = new ArrayList<>();
        list.add(new SpinnerObject("Việt Nam", R.mipmap.ic_flagvn));
        list.add(new SpinnerObject("Tiếng anh", R.mipmap.ic_flagen));

        return list;
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        SpinnerObject item = (SpinnerObject) parent.getSelectedItem();
        Toast.makeText(this, item.getSpinnerItemName(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}