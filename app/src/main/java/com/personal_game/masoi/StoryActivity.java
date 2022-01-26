package com.personal_game.masoi;

import static com.personal_game.masoi.api.RetrofitServer.getRetrofit_lib;

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
import com.personal_game.masoi.api.ServiceAPI_lib;
import com.personal_game.masoi.databinding.ActivityHistoryBinding;
import com.personal_game.masoi.databinding.ActivityStoryBinding;
import com.personal_game.masoi.object.HistoryObject;
import com.personal_game.masoi.object.Message_Story;
import com.personal_game.masoi.object.PlayerObject;
import com.personal_game.masoi.object.StoryObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class StoryActivity extends AppCompatActivity {

    private PlayerAdapter playerAdapter;
    private StoryAdapter storyAdapter;
    private ActivityStoryBinding activityStoryBinding;
    private List<PlayerObject> playerList;
    private List<StoryObject> storyList;
    private HistoryObject history;

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
        history = (HistoryObject) getIntent().getSerializableExtra("history");

        Uri uri = getIntent().getData();
        if (uri != null) {
            String path = uri.toString();
            String[] parameter = path.split("/");
            //GetReviewById(parameter[5]);
        }

        loadStory();
        setListeners();
    }

    private void loadStory(){
        ServiceAPI_lib serviceAPI_lib = getRetrofit_lib().create(ServiceAPI_lib.class);
        Call<Message_Story> call = serviceAPI_lib.GetStory(history.getHistoryId());
        call.enqueue(new Callback<Message_Story>() {
            @Override
            public void onResponse(Call<Message_Story> call, Response<Message_Story> response) {
                if(response.body().getStatus1() == 1 && response.body().getStories1() != null){
                    storyList = response.body().getStories1();
                    playerList = response.body().getPlayers1();

                    activityStoryBinding.txtTime.setText("Thời gian: "+history.getStartTime());
                    activityStoryBinding.txtQuantity.setText("Số lượng: "+ history.getSl());
                    if(history.isWin())
                        activityStoryBinding.txtResult.setText("Chiến Thắng");
                    else
                        activityStoryBinding.txtResult.setText("Thua cuộc");
                    activityStoryBinding.txtRangeTime.setText("Thời gian diễn ra: "+history.getTime()+"p");

                    setPlayer();
                    setStory();
                }
            }

            @Override
            public void onFailure(Call<Message_Story> call, Throwable t) {

            }
        });
    }

    private void setListeners(){
        activityStoryBinding.btnBack.setOnClickListener(v -> {
           finish();
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
        if(playerList != null) {
            playerAdapter = new PlayerAdapter(playerList, getApplication(), 1, new PlayerAdapter.PlayerListeners() {
                @Override
                public void onClick() {

                }
            });

            RecyclerView rcl = findViewById(R.id.rclPlayer);
            rcl.setAdapter(playerAdapter);
        }
    }

    private void setStory(){
        if(storyList != null) {
            storyAdapter = new StoryAdapter(storyList, getApplication());

            RecyclerView rcl1 = findViewById(R.id.rclStory);
            rcl1.setAdapter(storyAdapter);
        }
    }
}