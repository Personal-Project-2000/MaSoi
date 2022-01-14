package com.personal_game.masoi;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.personal_game.masoi.Object.SpinnerObject;
import com.personal_game.masoi.adapter.SpinnerAdapter;

import java.util.ArrayList;

public class SettingActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        Spinner spinner;

        spinner = (Spinner) findViewById(R.id.spinner);

        SpinnerAdapter spinnerAdapter = new SpinnerAdapter(this, getSpinnerList());
        if(spinner != null) {
            spinner.setAdapter(spinnerAdapter);
            spinner.setOnItemSelectedListener(this);
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