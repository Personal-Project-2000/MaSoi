package com.personal_game.masoi.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.personal_game.masoi.databinding.ItemPlayerBinding;
import com.personal_game.masoi.databinding.ItemStoryBinding;
import com.personal_game.masoi.object.StoryObject;

import java.util.List;

public class StoryAdapter extends RecyclerView.Adapter<StoryAdapter.ViewHolder>{
    private final List<StoryObject> storyList;
    private final Context context;

    public StoryAdapter(List<StoryObject> storyList, Context context){
        this.storyList = storyList;
        this.context = context;
    }

    @NonNull
    @Override
    public StoryAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new StoryAdapter.ViewHolder(ItemStoryBinding.inflate(
                LayoutInflater.from(parent.getContext()),
                parent,
                false
        ));
    }

    @Override
    public void onBindViewHolder(@NonNull StoryAdapter.ViewHolder holder, int position) {
        holder.setData(storyList.get(position));
    }

    @Override
    public int getItemCount() {
        return storyList != null ? storyList.size() : 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        final ItemStoryBinding binding;

        public ViewHolder(@NonNull ItemStoryBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void setData(StoryObject storyObject) {
            binding.txtContent.setText(storyObject.getContent());
        }
    }
}
