package com.personal_game.masoi;

import static com.personal_game.masoi.api.RetrofitServer.getRetrofit_lib;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;
import com.personal_game.masoi.adapter.HistoryAdapter;
import com.personal_game.masoi.adapter.MainAdapter;
import com.personal_game.masoi.adapter.StoryAdapter;
import com.personal_game.masoi.api.ServiceAPI_lib;
import com.personal_game.masoi.databinding.ActivityHistoryBinding;
import com.personal_game.masoi.databinding.ActivityMainBinding;
import com.personal_game.masoi.object.HistoryObject;
import com.personal_game.masoi.object.Message_History;
import com.personal_game.masoi.object.UserObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HistoryActivity extends AppCompatActivity {

    private HistoryAdapter historyAdapter;
    private ActivityHistoryBinding activityHistoryBinding;
    private UserObject user;
    private List<HistoryObject> historyList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityHistoryBinding = ActivityHistoryBinding.inflate(getLayoutInflater());
        View view = activityHistoryBinding.getRoot();
        setContentView(view);

        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        init();
    }

    private void init(){
        user = (UserObject) getIntent().getSerializableExtra("user");

        setListeners();
        setHistory();
    }

    private void setListeners(){
        activityHistoryBinding.btnBack.setOnClickListener(v -> {
            finish();
        });

        activityHistoryBinding.imageViewMic.setOnClickListener(v -> {
            Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
            intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
            intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
            intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "hãy nói gì đi");
            try {
                activityResultLauncher.launch(intent);
            } catch (Exception e) {
                Toast.makeText(getApplication(), "Error",Toast.LENGTH_SHORT).show();
            }
        });
    }

    ActivityResultLauncher<Intent> activityResultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
        @Override
        public void onActivityResult(ActivityResult result) {
            if (result.getResultCode() == Activity.RESULT_OK) {
                if (result.getData() != null) {
                    ArrayList<String> data = result.getData().getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    activityHistoryBinding.inputSearch.setText(Objects.requireNonNull(data).get(0));
                } else {
                    Toast.makeText(getApplication(),"Vui lòng thử lại.", Toast.LENGTH_SHORT).show();
                }
            }
        }
    });

    private void setHistory(){
        ServiceAPI_lib serviceAPI_lib = getRetrofit_lib().create(ServiceAPI_lib.class);
        Call<Message_History> call = serviceAPI_lib.GetHistory(user.getTk());
        call.enqueue(new Callback<Message_History>() {
            @Override
            public void onResponse(Call<Message_History> call, Response<Message_History> response) {
                if(response.body().getStatus1() == 1 && response.body().getHistoryLists1() != null){
                    historyList = response.body().getHistoryLists1();

                    historyAdapter = new HistoryAdapter(historyList, getApplication(), new HistoryAdapter.HistoryListeners() {
                        @Override
                        public void onClick(HistoryObject historyObject) {
                            Intent intent = new Intent(getApplication(), StoryActivity.class);
                            intent.putExtra("history", historyObject);
                            startActivityForResult(intent, 1);
                        }
                    });

                    activityHistoryBinding.rclHistory.setAdapter(historyAdapter);
                }
            }

            @Override
            public void onFailure(Call<Message_History> call, Throwable t) {

            }
        });
    }
}