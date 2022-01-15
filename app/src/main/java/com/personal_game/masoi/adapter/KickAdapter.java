package com.personal_game.masoi.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.personal_game.masoi.databinding.ItemHistoryBinding;
import com.personal_game.masoi.databinding.ItemKickBinding;

import java.util.List;

public class KickAdapter extends RecyclerView.Adapter<KickAdapter.ViewHolder>{
    private final List<String> playerList;
    private final KickListeners kickListeners;
    private final Context context;

    public KickAdapter(List<String> playerList, Context context,KickListeners kickListeners){
        this.playerList = playerList;
        this.kickListeners = kickListeners;
        this.context = context;
    }

    @NonNull
    @Override
    public KickAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new KickAdapter.ViewHolder(ItemKickBinding.inflate(
                LayoutInflater.from(parent.getContext()),
                parent,
                false
        ));
    }

    @Override
    public void onBindViewHolder(@NonNull KickAdapter.ViewHolder holder, int position) {
        holder.setData();
    }

    @Override
    public int getItemCount() {
        return playerList != null ? playerList.size() : 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        final ItemKickBinding binding;

        public ViewHolder(@NonNull ItemKickBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void setData() {
            binding.btnKick.setOnClickListener(v -> {
                kickListeners.onClick("hihi", getAdapterPosition());
            });
        }
    }

    public interface KickListeners {
        void onClick(String str, int position);
    }
}
