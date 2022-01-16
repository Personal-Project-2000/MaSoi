package com.personal_game.masoi;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.view.View;
import android.widget.Toast;

import com.personal_game.masoi.adapter.HistoryAdapter;
import com.personal_game.masoi.adapter.PlayerAdapter;
import com.personal_game.masoi.adapter.StoryAdapter;
import com.personal_game.masoi.databinding.ActivityHistoryBinding;
import com.personal_game.masoi.databinding.ActivityStoryBinding;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

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

        Uri uri = getIntent().getData();
        if (uri != null) {
            String path = uri.toString();
            String[] parameter = path.split("/");
            //GetReviewById(parameter[5]);
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

        activityStoryBinding.btnShare.setOnClickListener(v -> {
            try {
                Intent shareIntent = new Intent(Intent.ACTION_SEND);
                shareIntent.setType("text/plain");
                shareIntent.putExtra(Intent.EXTRA_SUBJECT, "My application name");
                String shareMessage= "\nLet me recommend you this application\n\n";
                shareMessage = shareMessage + "https://ps.covid21tsp.space/Share/Code/" + "Điền id restaurant vô đây";
                shareIntent.putExtra(Intent.EXTRA_TEXT, shareMessage);
                startActivity(Intent.createChooser(shareIntent, "choose one"));
            } catch(Exception e) {
                //e.toString();
            }
        });
    }

    private void setPlayer(){

        playerAdapter = new PlayerAdapter(test, getApplication(), 1, new PlayerAdapter.PlayerListeners() {
            @Override
            public void onClick() {

            }
        });

        RecyclerView rcl = findViewById(R.id.rclPlayer);
        rcl.setAdapter(playerAdapter);
    }

    private void setStory(){
        storyAdapter = new StoryAdapter(test, getApplication());

        RecyclerView rcl1 = findViewById(R.id.rclStory);
        rcl1.setAdapter(storyAdapter);
    }
}