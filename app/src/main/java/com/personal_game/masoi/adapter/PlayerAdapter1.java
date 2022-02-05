package com.personal_game.masoi.adapter;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.personal_game.masoi.R;
import com.personal_game.masoi.databinding.ItemPlayerBinding;
import com.personal_game.masoi.object.PlayerObject1;
import com.squareup.picasso.Picasso;

import java.util.List;

public class PlayerAdapter1 extends RecyclerView.Adapter<PlayerAdapter1.ViewHolder>{
    private final List<PlayerObject1> playerList;
    private final Context context;
    private PlayerListeners playerListeners;
    private int code; // 1: storyActivity || 2: waitActivity

    public PlayerAdapter1(List<PlayerObject1> playerList, Context context, int code, PlayerListeners playerListeners){
        this.playerList = playerList;
        this.context = context;
        this.code = code;
        this.playerListeners = playerListeners;
    }

    @NonNull
    @Override
    public PlayerAdapter1.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new PlayerAdapter1.ViewHolder(ItemPlayerBinding.inflate(
                LayoutInflater.from(parent.getContext()),
                parent,
                false
        ));
    }

    @Override
    public void onBindViewHolder(@NonNull PlayerAdapter1.ViewHolder holder, int position) {
        holder.setData(playerList.get(position));
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

        public void setData(PlayerObject1 playerObject) {
            if(playerObject.getImg() != null){
                Picasso.Builder builder = new Picasso.Builder(context);
                builder.listener(new Picasso.Listener() {
                    @Override
                    public void onImageLoadFailed(Picasso picasso, Uri uri, Exception exception) {
                        binding.imgMain.setImageResource(R.drawable.logo);
                    }
                });
                Picasso pic = builder.build();
                pic.load(playerObject.getImg()).into(binding.imgMain);
            }

            binding.txtName.setText(playerObject.getName());

            if(code == 2){
                binding.layoutMain.getLayoutParams().height = 70;

                binding.imgMain.getLayoutParams().height = 40;
                binding.imgMain.getLayoutParams().width = 40;
                binding.imgMain.requestLayout();

                binding.txtName.setTextSize(11);
            }

            if(playerObject.isStatus()){
                binding.layoutMain.setBackgroundResource(R.drawable.vien_yellow);
            }else{
                binding.layoutMain.setBackgroundResource(R.drawable.vien);
            }

            binding.layoutMain.setOnClickListener(v -> {
                playerListeners.onClick(playerObject);
            });
        }
    }

    public interface PlayerListeners {
        void onClick(PlayerObject1 player);
    }
}
