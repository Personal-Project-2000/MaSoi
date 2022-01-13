package com.personal_game.masoi.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.personal_game.masoi.databinding.ItemMainBinding;

import java.util.List;

public class MainAdapter extends RecyclerView.Adapter<MainAdapter.ViewHolder>{

    private final List<String> roomList;
    private final Context context;

    public MainAdapter(List<String> roomList, Context context){
        this.roomList = roomList;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(ItemMainBinding.inflate(
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
        return roomList != null ? roomList.size() : 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        final ItemMainBinding binding;

        public ViewHolder(@NonNull ItemMainBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void setData() {

        }
    }
}
