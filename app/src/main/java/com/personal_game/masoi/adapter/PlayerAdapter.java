package com.personal_game.masoi.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.personal_game.masoi.databinding.ItemMainBinding;
import com.personal_game.masoi.databinding.ItemPlayerBinding;

import java.util.List;

public class PlayerAdapter extends RecyclerView.Adapter<PlayerAdapter.ViewHolder>{
    private final List<String> playerList;
    private final Context context;
    private int code; // 1: storyActivity || 2: waitActivity

    public PlayerAdapter(List<String> playerList, Context context, int code){
        this.playerList = playerList;
        this.context = context;
        this.code = code;
    }

    @NonNull
    @Override
    public PlayerAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new PlayerAdapter.ViewHolder(ItemPlayerBinding.inflate(
                LayoutInflater.from(parent.getContext()),
                parent,
                false
        ));
    }

    @Override
    public void onBindViewHolder(@NonNull PlayerAdapter.ViewHolder holder, int position) {
        holder.setData();
    }

    @Override
    public int getItemCount() {
        return playerList != null ? playerList.size() : 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        final ItemPlayerBinding binding;

        public ViewHolder(@NonNull ItemPlayerBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void setData() {
            if(code == 2){
                binding.layoutMain.getLayoutParams().height = 70;

                binding.imgMain.getLayoutParams().height = 45;
                binding.imgMain.getLayoutParams().width = 45;
                binding.imgMain.requestLayout();

                binding.txtName.setTextSize(11);
            }
        }
    }
}
