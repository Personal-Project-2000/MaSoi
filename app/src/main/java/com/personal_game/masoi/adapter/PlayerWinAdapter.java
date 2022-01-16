package com.personal_game.masoi.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.personal_game.masoi.databinding.ItemPlayerBinding;
import com.personal_game.masoi.databinding.ItemPlayerwinBinding;

import java.util.List;

public class PlayerWinAdapter extends RecyclerView.Adapter<PlayerWinAdapter.ViewHolder>{
    private final List<String> playerList;
    private final Context context;

    public PlayerWinAdapter(List<String> playerList, Context context){
        this.playerList = playerList;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(ItemPlayerwinBinding.inflate(
                LayoutInflater.from(parent.getContext()),
                parent,
                false
        ));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.setData();
    }

    @Override
    public int getItemCount() {
        return playerList != null ? playerList.size() : 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        final ItemPlayerwinBinding binding;

        public ViewHolder(@NonNull ItemPlayerwinBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void setData() {
            binding.layoutMain.setOnClickListener(v -> {

            });
        }
    }
}
