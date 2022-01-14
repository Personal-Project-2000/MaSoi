package com.personal_game.masoi;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.personal_game.masoi.adapter.HistoryAdapter;
import com.personal_game.masoi.adapter.PlayerAdapter;
import com.personal_game.masoi.adapter.StoryAdapter;
import com.personal_game.masoi.databinding.ActivityHistoryBinding;
import com.personal_game.masoi.databinding.ActivityStoryBinding;

import java.util.ArrayList;
import java.util.List;

public class StoryActivity extends AppCompatActivity {

    private PlayerAdapter playerAdapter;
    private StoryAdapter storyAdapter;
    private ActivityStoryBinding activityStoryBinding;
    private List<String> test;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityStoryBinding = ActivityStoryBinding.inflate(getLayoutInflater());
        View view = activityStoryBinding.getRoot();
        setContentView(view);

        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        init();
    }

    private void init(){
        test = new ArrayList<>();

        for(int i = 0; i < 20; i++){
            test.add("t");
        }

        setListeners();
        setPlayer();
        setStory();
    }

    private void setListeners(){
        activityStoryBinding.btnBack.setOnClickListener(v -> {
            Intent intent = new Intent(getApplication(), HistoryActivity.class);
            startActivity(intent);
        });
    }

    private void setPlayer(){

        playerAdapter = new PlayerAdapter(test, getApplication());

        RecyclerView rcl = findViewById(R.id.rclPlayer);
        rcl.setAdapter(playerAdapter);
    }

    private void setStory(){
        storyAdapter = new StoryAdapter(test, getApplication());

        RecyclerView rcl1 = findViewById(R.id.rclStory);
        rcl1.setAdapter(storyAdapter);
    }
}