package com.personal_game.masoi.adapter;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.personal_game.masoi.R;
import com.personal_game.masoi.databinding.ItemHistoryBinding;
import com.personal_game.masoi.databinding.ItemKickBinding;
import com.personal_game.masoi.object.PlayerObject;
import com.personal_game.masoi.object.PlayerObject1;
import com.squareup.picasso.Picasso;

import java.util.List;

public class KickAdapter extends RecyclerView.Adapter<KickAdapter.ViewHolder>{
    private final List<PlayerObject1> playerList;
    private final KickListeners kickListeners;
    private final Context context;

    public KickAdapter(List<PlayerObject1> playerList, Context context,KickListeners kickListeners){
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
        holder.setData(playerList.get(position));
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

        public void setData(PlayerObject1 player) {
            if(player.getImg() != null){
                Picasso.Builder builder = new Picasso.Builder(context);
                builder.listener(new Picasso.Listener() {
                    @Override
                    public void onImageLoadFailed(Picasso picasso, Uri uri, Exception exception) {
                        binding.imgMain.setImageResource(R.drawable.logo);
                    }
                });
                Picasso pic = builder.build();
                pic.load(player.getImg()).into(binding.imgMain);
            }

            binding.txtName.setText(player.getName());

            binding.btnKick.setOnClickListener(v -> {
                kickListeners.onClick(player, getAdapterPosition());
            });
        }
    }

    public interface KickListeners {
        void onClick(PlayerObject1 player, int position);
    }
}
