package com.personal_game.masoi;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.personal_game.masoi.adapter.HistoryAdapter;
import com.personal_game.masoi.adapter.MainAdapter;

import java.util.ArrayList;
import java.util.List;

public class HistoryActivity extends AppCompatActivity {

    private HistoryAdapter historyAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        List<String> test = new ArrayList<>();

        for(int i = 0; i < 20; i++){
            test.add("t");
        }

        historyAdapter = new HistoryAdapter(test, getApplication());

        RecyclerView rcl = findViewById(R.id.rclHistory);
        rcl.setAdapter(historyAdapter);
    }
}