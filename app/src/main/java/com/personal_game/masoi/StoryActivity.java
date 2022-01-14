package com.personal_game.masoi;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;

import com.personal_game.masoi.adapter.HistoryAdapter;
import com.personal_game.masoi.adapter.PlayerAdapter;
import com.personal_game.masoi.adapter.StoryAdapter;

import java.util.ArrayList;
import java.util.List;

public class StoryActivity extends AppCompatActivity {

    private PlayerAdapter playerAdapter;
    private StoryAdapter storyAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_story);

        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        List<String> test = new ArrayList<>();

        for(int i = 0; i < 20; i++){
            test.add("t");
        }

        playerAdapter = new PlayerAdapter(test, getApplication());

        RecyclerView rcl = findViewById(R.id.rclPlayer);
        rcl.setAdapter(playerAdapter);

        rcl.setItemViewCacheSize(3);
        rcl.setDrawingCacheEnabled(true);


        storyAdapter = new StoryAdapter(test, getApplication());

        RecyclerView rcl1 = findViewById(R.id.rclStory);
        rcl1.setAdapter(storyAdapter);
    }
}