package com.personal_game.masoi.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;

import androidx.recyclerview.widget.RecyclerView;

import com.personal_game.masoi.R;
import com.personal_game.masoi.StoryActivity;
import com.personal_game.masoi.adapter.HistoryAdapter;
import com.personal_game.masoi.adapter.StoryAdapter;
import com.personal_game.masoi.databinding.LayoutConfirmBinding;
import com.personal_game.masoi.databinding.LayoutStoryBinding;

import java.util.ArrayList;
import java.util.List;

public class StoryDialog extends Dialog {
    public Activity c;
    private LayoutStoryBinding layoutStoryBinding;
    private StoryListeners storyListeners;
    private StoryAdapter storyAdapter;

    public StoryDialog(Activity a, StoryListeners storyListeners) {
        super(a);
        this.c = a;
        this.storyListeners = storyListeners;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        layoutStoryBinding = LayoutStoryBinding.inflate(getLayoutInflater());
        View view = layoutStoryBinding.getRoot();
        setContentView(view);

        init();
    }

    private void init(){
        setListeners();
        setStory();
    }

    private void setListeners(){
        layoutStoryBinding.btnContinue.setOnClickListener(v -> {
            dismiss();
            storyListeners.onClickContinue();
        });
    }

    private void setStory(){
        List<String> test = new ArrayList<>();

        for(int i = 0; i < 20; i++){
            test.add("t");
        }

        storyAdapter = new StoryAdapter(test, c);
        layoutStoryBinding.rclStory.setAdapter(storyAdapter);
    }

    public interface StoryListeners{
        void onClickContinue();
    }
}
